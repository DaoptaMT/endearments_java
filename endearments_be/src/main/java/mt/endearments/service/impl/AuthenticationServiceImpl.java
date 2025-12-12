package mt.endearments.service.impl;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.dto.request.AuthenticationRequestDTO;
import mt.endearments.dto.response.AuthenticationResponseDTO;
import mt.endearments.enums.ApiException;
import mt.endearments.enums.ErrorCode;
import mt.endearments.enums.TokenType;
import mt.endearments.model.User;
import mt.endearments.repository.user.UserRepository;
import mt.endearments.service.AuthenticationService;
import mt.endearments.service.JwtService;
import mt.endearments.service.RedisTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    RedisTokenService redisTokenService;
    AuthenticationManager authenticationManager;
    JwtService jwtService;

    @Override
    public AuthenticationResponseDTO login(AuthenticationRequestDTO authenticationRequestDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequestDTO.getEmail(),
                        authenticationRequestDTO.getPassword()
                )
        );

        var user = userRepository.findByName(authenticationRequestDTO.getName())
                .orElseThrow(() -> new UsernameNotFoundException("Email or Password is incorrect"));

        String accessToken = jwtService.generateToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        return buildAuthenticationResponse(user, accessToken, refreshToken, "Login success");
    }

    @Override
    public AuthenticationResponseDTO refreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("x-token");
        if (StringUtils.isBlank(refreshToken)) {
            throw new ApiException(ErrorCode.TOKEN_NOT_BLANK);
        }

        final String email = jwtService.extractUsername(refreshToken, TokenType.REFRESH_TOKEN);

        User user = userRepository.findByName(email)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        if (!jwtService.isTokenValid(refreshToken, user, TokenType.REFRESH_TOKEN)) {
            throw new ApiException(ErrorCode.INVALID_TOKEN);
        }

        String accessToken = jwtService.generateToken(user);

        return buildAuthenticationResponse(user, accessToken, refreshToken,
                "Refresh token success");
    }

    private AuthenticationResponseDTO buildAuthenticationResponse(User user, String accessToken,
                                                                  String refreshToken, String message) {
        redisTokenService.revokeAllUserTokens(user.getId());
        redisTokenService.saveToken(user, accessToken, 15);
        redisTokenService.saveToken(user, refreshToken, 10080);

        return AuthenticationResponseDTO.builder()
                .tokenType(TokenType.BEARER)
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .message(message)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
