package mt.endearments.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import mt.endearments.enums.TokenType;
import mt.endearments.model.User;
import mt.endearments.service.JwtService;
import mt.endearments.service.RedisTokenService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    JwtService jwtService;
    // bean UserDetailsService của bạn
    org.springframework.security.core.userdetails.UserDetailsService userDetailsService;
    RedisTokenService redisTokenService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        // nếu không có header hoặc không bắt đầu bằng "Bearer " -> bỏ qua filter này
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwtToken = authorizationHeader.substring(7);
        try {
            final String username = jwtService.extractUsername(jwtToken, TokenType.ACCESS_TOKEN);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // lấy userId nếu có (tránh cast trực tiếp để không bị ClassCastException)
                Optional<Long> maybeUserId = extractUserId(userDetails);

                boolean isValidInRedis = false;
                if (maybeUserId.isPresent()) {
                    // nếu Redis token service lưu bằng userId
                    isValidInRedis = redisTokenService.isTokenValid(maybeUserId.get(), jwtToken);
                }
//                else {
                    // fallback: kiểm tra theo username (nếu redis lưu theo username)
//                    isValidInRedis = redisTokenService.isTokenValidByUsername(username, jwtToken);
//                }

                boolean tokenValid = jwtService.isTokenValid(jwtToken, userDetails, TokenType.ACCESS_TOKEN);

                if (tokenValid && isValidInRedis) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } else {
                    // không hợp lệ: đảm bảo không set authentication
                    log.debug("Token invalid or not present in redis for user [{}]. tokenValid={}, inRedis={}",
                            username, tokenValid, isValidInRedis);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            // ghi log chi tiết, trả 401 cho client (chỉ khi bạn muốn block request có token bị lỗi)
            log.warn("JWT Authentication failed for request [{} {}]: {}",
                    request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);

            if (!response.isCommitted()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                // trả JSON ngắn gọn
                response.getWriter().write("{\"error\": \"Authentication failed. Token invalid or expired.\"}");
                response.getWriter().flush();
            }
        }
    }

    /**
     * Cố gắng lấy user id từ UserDetails nếu entity của bạn expose getId().
     * Tránh cast thẳng vào entity để không phụ thuộc package.
     */
    private Optional<Long> extractUserId(UserDetails userDetails) {
        if (userDetails == null) {
            return Optional.empty();
        }

        // nếu userDetails là instance của class domain User (ví dụ mt.endearments.model.User),
        // bạn có thể check và cast; ở đây dùng reflection để an toàn hơn.
        try {
            Method m = userDetails.getClass().getMethod("getId");
            Object idObj = m.invoke(userDetails);
            if (idObj instanceof Number) {
                return Optional.of(((Number) idObj).longValue());
            }
        } catch (NoSuchMethodException nsme) {
            // không có getId(), ignore
        } catch (Exception e) {
            log.debug("Failed to extract id from UserDetails by reflection: {}", e.getMessage());
        }

        return Optional.empty();
    }
}
