package mt.endearments.service;

import mt.endearments.enums.TokenType;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String generateToken(UserDetails userDetails);
    String extractUsername(String token, TokenType tokenType);
    Boolean isTokenValid(String token, UserDetails userDetails,  TokenType tokenType);
    String generateRefreshToken(UserDetails userDetails);
    String generateResetToken(String email);
    boolean validateResetToken(String token, String email);
}
