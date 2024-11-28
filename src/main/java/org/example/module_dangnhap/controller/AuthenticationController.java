package org.example.module_dangnhap.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.request.authentication.AuthenticationRequest;
import org.example.module_dangnhap.repo.authentication.AuthenticationResponse;
import org.example.module_dangnhap.service.Impl.authencation.AuthenticationServiceImpl;
import org.example.module_dangnhap.exception.authentication.InvalidPasswordException;
import org.example.module_dangnhap.exception.authentication.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:3000")
public class AuthenticationController {

    AuthenticationServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        try {
            AuthenticationResponse response = authService.authenticate(request);
            if (response != null && response.isAuthenticated()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(AuthenticationResponse.builder()
                                .message("Mật khẩu không đúng.")
                                .isAuthenticated(false)
                                .build());
            }
        } catch (InvalidPasswordException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(AuthenticationResponse.builder()
                            .message("Mật khẩu không đúng.")
                            .isAuthenticated(false)
                            .build());
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(AuthenticationResponse.builder()
                            .message("Tài khoản không tồn tại.")
                            .isAuthenticated(false)
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(AuthenticationResponse.builder()
                            .message(e.getMessage())
                            .isAuthenticated(false)
                            .build());
        }
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Không cần xử lý gì trên server nếu chỉ sử dụng JWT
        // Tuy nhiên, bạn có thể xóa session hoặc hủy token nếu sử dụng các kỹ thuật khác
        return ResponseEntity.ok("Đăng xuất thành công");
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        return authService.refreshToken(request, response);
    }

    // Exception handler for any other unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Lỗi không xác định: " + e.getMessage());
    }
}   
