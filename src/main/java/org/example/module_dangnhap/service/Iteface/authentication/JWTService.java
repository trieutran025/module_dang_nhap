package org.example.module_dangnhap.service.Iteface.authentication;

import io.jsonwebtoken.Claims;
import org.example.module_dangnhap.entity.Account;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;

public interface JWTService {
    String extractUsername(String token);
    boolean isValid(String token, UserDetails userDetails);
    boolean isValidRefreshToken(String token, Account account);
    <T> T extractClaim(String token, Function<Claims,T> resolver);
    String generateAccessToken(Account account);
    String generateRefreshToken(Account account);
    String[] getRolesFromToken(String token);
}
