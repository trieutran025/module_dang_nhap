package org.example.module_dangnhap.config.authentation;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.entity.authentication.Token;
import org.example.module_dangnhap.repo.authentication.ITokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;


@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CustomLogoutHandler implements LogoutHandler {
    ITokenRepository iTokenRepository;
    @Override
    public void logout(HttpServletRequest request,
                       HttpServletResponse response,
                       Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if(authHeader == null || !authHeader.startsWith("Bearer")){
            return;
        }
        String token = authHeader.substring(7);
        Token storedToken = iTokenRepository.findByAccessToken(token).orElse(null);

        if(storedToken != null){
            storedToken.setLoggedOut(true);
            iTokenRepository.logOutTokenById(storedToken.getId());
        }
    }
}
