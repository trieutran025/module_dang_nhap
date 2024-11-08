package org.example.module_dangnhap.service.Impl.authencation;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.example.module_dangnhap.entity.Account;
import org.example.module_dangnhap.entity.authentication.Token;
import org.example.module_dangnhap.repo.authentication.ITokenRepository;
import org.example.module_dangnhap.service.Iteface.authentication.JWTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.Function;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class JwtServiceImpl implements JWTService {

    @NonFinal
    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @NonFinal
    @Value("${application.security.jwt.access-token-expiration}")
    long accessTokenExpire;

    @NonFinal
    @Value("${application.security.jwt.refresh-token-expiration}")
    long refreshTokenExpire;

    ITokenRepository iTokenRepository;

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);

        boolean validToken = iTokenRepository
                .findByAccessToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token) && validToken;
    }

    @Override
    public boolean isValidRefreshToken(String token, Account account) {
        String username = extractUsername(token);

        boolean validRefreshToken = iTokenRepository
                .findByRefreshToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(account.getUsername())) && !isTokenExpired(token) && validRefreshToken;
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .setSigningKey(getSignerKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public String generateAccessToken(Account account) {
        return generateToken(account, accessTokenExpire);
    }

    @Override
    public String generateRefreshToken(Account account) {
        return generateToken(account, refreshTokenExpire);
    }

    private String generateToken(Account account, long expireTime) {
        return Jwts.builder()
                .subject(account.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expireTime))
                .claim("scope", buildScope(account))
                .signWith(getSignerKey())
                .compact();
    }

    private SecretKey getSignerKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String buildScope(Account user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getRoleName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }

        return stringJoiner.toString();
    }

    @Override
    public String[] getRolesFromToken(String token) {
        Claims claims = extractAllClaims(token);
        String scope = (String) claims.get("scope");
        return scope.split(" ");
    }


}
