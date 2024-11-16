package org.example.module_dangnhap.service.Iteface.authentication;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.module_dangnhap.dto.request.authentication.AuthenticationRequest;
import org.example.module_dangnhap.repo.authentication.AuthenticationResponse;
import org.springframework.http.ResponseEntity;

import javax.security.auth.login.AccountNotFoundException;

public interface IAuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request) throws AccountNotFoundException;
    ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response);

}
