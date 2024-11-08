package org.example.module_dangnhap.service.Iteface.authentication;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.module_dangnhap.dto.request.authentication.AuthenticationRequest;
import org.example.module_dangnhap.repo.authentication.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response);

}
