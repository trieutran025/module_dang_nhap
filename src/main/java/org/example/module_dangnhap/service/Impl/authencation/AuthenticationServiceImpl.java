package org.example.module_dangnhap.service.Impl.authencation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.authentication.AuthenticationRequest;
import org.example.module_dangnhap.entity.Account;
import org.example.module_dangnhap.entity.Role;
import org.example.module_dangnhap.entity.authentication.Token;
import org.example.module_dangnhap.exception.authentication.InvalidPasswordException;
import org.example.module_dangnhap.repo.IAccountRepository;
import org.example.module_dangnhap.repo.authentication.AuthenticationResponse;
import org.example.module_dangnhap.repo.authentication.ITokenRepository;
import org.example.module_dangnhap.service.Iteface.authentication.IAuthenticationService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationServiceImpl implements IAuthenticationService {

    IAccountRepository iAccountRepository;
    JwtServiceImpl jwtServiceImpl;
    ITokenRepository iTokenRepository;
    AuthenticationManager authenticationManager;

    /*====================================== AUTHENTICATION METHODS ======================================*/

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws AccountNotFoundException {
        try {
            Account user = iAccountRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new AccountNotFoundException("Tài khoản KHÔNG tồn tại."));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            // Log để kiểm tra thông tin user
            System.out.println("User found: " + user.getUsername());

            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                    .flatMap(role -> Stream.concat(
                            Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName())),
                            role.getPermissions().stream().map(permission -> new SimpleGrantedAuthority(permission.getName()))
                    ))
                    .collect(Collectors.toList());

            // Log để kiểm tra thông tin authorities
            System.out.println("Authorities: " + authorities);

            String accessToken = jwtServiceImpl.generateAccessToken(user);
            String refreshToken = jwtServiceImpl.generateRefreshToken(user);

            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);


            // Log thông tin user và authorities trong SecurityContextHolder
            System.out.println("Authorities in SecurityContextHolder: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

            List<String> roles = authorities.stream()
                    .map(SimpleGrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // Log thông tin roles
            System.out.println("User: " + user.getUsername() + " - Authorities: " + roles);

            return AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .isAuthenticated(true)
                    .roles(roles)
                    .message("Đăng nhập thành công.")
                    .build();

        } catch (BadCredentialsException e) {
            throw new InvalidPasswordException("Mật khẩu KHÔNG trùng khớp.");
        } catch (AccountNotFoundException e) {
            throw e;
        }
    }

    /*====================================== REFRESH TOKEN METHODS ======================================*/
    public ResponseEntity refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Token không hợp lệ", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        // Extract username from the token
        String username = jwtServiceImpl.extractUsername(token);

        // Check if the user exists in the database
        Account user = iAccountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        // Check if the refresh token is valid
        if (jwtServiceImpl.isValidRefreshToken(token, user)) {
            // Generate new access and refresh tokens
            String accessToken = jwtServiceImpl.generateAccessToken(user);
            String refreshToken = jwtServiceImpl.generateRefreshToken(user);

            // Revoke old tokens and save new tokens
            revokeAllTokenByUser(user);
            saveUserToken(accessToken, refreshToken, user);

            return new ResponseEntity<>(AuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .message("Token mới đã được tạo")
                    .build(), HttpStatus.OK);
        }

        return new ResponseEntity<>("Token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED);
    }

    /*====================================== TOKEN MANAGEMENT METHODS ======================================*/
    private void revokeAllTokenByUser(Account user) {
        // Retrieve all valid tokens for the user from the token repository
        List<Token> validTokens = iTokenRepository.findAllAccessTokensByUser(user.getAccountId());

        // If there are no valid tokens, return early
        if (validTokens.isEmpty()) {
            return;
        }

        // Get a list of token IDs to revoke
        List<Long> tokenIds = validTokens.stream()
                .map(Token::getId)
                .toList();

        // Update tokens to "logged out" in the repository
        iTokenRepository.updateTokensToLoggedOut(tokenIds);
    }

    private void saveUserToken(String accessToken, String refreshToken, Account user) {
        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setLoggedOut(false);
        token.setAccount(user);

        iTokenRepository.saveToken(token.getAccessToken(),
                token.getRefreshToken(),
                token.isLoggedOut(),
                token.getAccount().getAccountId());
    }
}
