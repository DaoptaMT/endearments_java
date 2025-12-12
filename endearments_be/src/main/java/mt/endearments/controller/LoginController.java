package mt.endearments.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import mt.endearments.dto.request.AuthRequestDTO;
import mt.endearments.dto.request.ForgotPasswordRequest;
import mt.endearments.dto.request.ResetPasswordRequestDTO;
import mt.endearments.dto.request.UserRequestDTO;
import mt.endearments.dto.response.UserResponseDTO;
import mt.endearments.enums.TokenType;
import mt.endearments.service.JwtService;
import mt.endearments.service.MailService;
import mt.endearments.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {
    UserService userService;
    AuthenticationManager authenticationManager;
    JwtService jwtService;
    MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO request) {
        UserResponseDTO user = userService.register(request);
        mailService.sendMail(
                user.getEmail(),
                "Đăng ký thành công",
                "Chào " + user.getEmail() + ", bạn đã đăng ký thành công tài khoản!"
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateToken(userDetails);
            String refreshToken = jwtService.generateRefreshToken(userDetails);

            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Email hoặc mật khẩu không đúng");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        String resetToken = jwtService.generateResetToken(request.getEmail());

        String resetLink = "http://localhost:5173/reset-password?token=" + resetToken;
        mailService.sendMail(
                request.getEmail(),
                "Quên mật khẩu",
                "Nhấn vào link sau để đặt lại mật khẩu: " + resetLink
        );

        return ResponseEntity.ok("Đã gửi mail reset password");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        String email = jwtService.extractUsername(request.getToken(), TokenType.ACCESS_TOKEN);
        userService.updatePassword(email, request.getNewPassword());

        mailService.sendMail(
                email,
                "Đổi mật khẩu thành công",
                "Bạn đã đổi mật khẩu thành công!"
        );

        return ResponseEntity.ok("Mật khẩu đã được đặt lại");
    }
}
