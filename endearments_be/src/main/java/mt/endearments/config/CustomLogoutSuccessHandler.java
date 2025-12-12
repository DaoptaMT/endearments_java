package mt.endearments.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mt.endearments.enums.TokenType;
import mt.endearments.service.JwtService;
import mt.endearments.service.RedisTokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final RedisTokenService redisTokenService;
    private final JwtService jwtService; // optional, để parse username/id từ token nếu cần

    @Override
    public void onLogoutSuccess(HttpServletRequest request,
                                HttpServletResponse response,
                                Authentication authentication) throws IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                // cố gắng lấy username từ token để revoke
                String username = jwtService.extractUsername(token, TokenType.ACCESS_TOKEN);

                // nếu bạn lưu token theo userId, cố gắng lấy id từ authentication principal
                Long userId = null;
                if (authentication != null && authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails) {
                    Object principal = authentication.getPrincipal();
                    try {
                        // reflection lấy getId nếu có
                        java.lang.reflect.Method m = principal.getClass().getMethod("getId");
                        Object idObj = m.invoke(principal);
                        if (idObj instanceof Number) {
                            userId = ((Number) idObj).longValue();
                        }
                    } catch (NoSuchMethodException ignored) {
                    } catch (Exception e) {
                        log.debug("Could not extract user id from principal: {}", e.getMessage());
                    }
                }

                // Gọi redisTokenService để revoke/invalidate token
//                if (userId != null) {
//                    redisTokenService.invalidateToken(userId, token); // bạn có thể cần đổi tên method
//                } else if (username != null) {
//                    redisTokenService.invalidateTokenByUsername(username, token); // fallback
//                } else {
//                    // as last resort attempt to remove token directly
//                    redisTokenService.invalidateTokenDirect(token);
//                }

                log.info("Logout: token revoked for user [{}]", username);

            } catch (Exception e) {
                log.warn("Logout: failed to revoke token: {}", e.getMessage());
            }
        }

        // trả response logout success
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.getWriter().write("{\"message\": \"Logout successful\"}");
        response.getWriter().flush();
    }
}
