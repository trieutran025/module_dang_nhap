package org.example.module_dangnhap.repo.authentication;

import org.example.module_dangnhap.entity.authentication.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token,Long> {
    @Query(value = """

            SELECT token.id, token.access_token, token.refresh_token, token.logged_out, token.account_id
                    FROM token
                    JOIN account ON token.account_id = account.account_id
                    WHERE account.account_id = ? AND token.logged_out = false
                    """, nativeQuery = true)
    List<Token> findAllAccessTokensByUser(Long userId);
    @Query(value = "SELECT id, access_token, refresh_token, logged_out, account_id FROM token WHERE access_token = :token", nativeQuery = true)
    Optional<Token> findByAccessToken(String token);

    @Query(value = "SELECT id, access_token, refresh_token, logged_out, account_id FROM token WHERE refresh_token = :token", nativeQuery = true)
    Optional<Token> findByRefreshToken(String token);

    @Modifying
    @Transactional
    @Query(
            value = "INSERT INTO token (access_token, refresh_token, logged_out, account_id) VALUES (:accessToken, :refreshToken, :loggedOut, :accountId)",
            nativeQuery = true
    )
    void saveToken(
            @Param("accessToken") String accessToken,
            @Param("refreshToken") String refreshToken,
            @Param("loggedOut") boolean loggedOut,
            @Param("accountId") Long accountId
    );

    @Transactional
    @Modifying
    @Query(value = "UPDATE token SET logged_out = true WHERE id IN (:tokenIds)", nativeQuery = true)
    void updateTokensToLoggedOut(List<Long> tokenIds);

    @Modifying
    @Transactional
    @Query(value = "UPDATE token SET logged_out = true WHERE id = :id", nativeQuery = true)
    void logOutTokenById(Long id);
}
