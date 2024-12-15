package org.example.module_dangnhap.repo;


import jakarta.transaction.Transactional;
import org.example.module_dangnhap.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {
    @Modifying
    @Query(value = "INSERT INTO account (username, password, is_active) VALUES (:username, :password, true)", nativeQuery = true)
    void createAccount(@Param("username") String username, @Param("password") String password);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO infor_user (account_id) " +
            "VALUES (LAST_INSERT_ID())", nativeQuery = true)
    Integer linkAccountToInforUser();

    @Modifying
    @Transactional
    @Query(value = "UPDATE account SET username = :username, password = :password WHERE account_id = :accountId", nativeQuery = true)
    void updateAccount(@Param("accountId") Long accountId, @Param("username") String username, @Param("password") String password);


    @Modifying
    @Transactional
    @Query(value = "UPDATE infor_user SET account_id = :accountId WHERE id = :id", nativeQuery = true)
    Integer linkAccountToInforUser(@Param("id") Long id, @Param("accountId") Long accountId);



    @Query(value = "SELECT account_id, username, password, is_active FROM account WHERE username = :username", nativeQuery = true)
    Optional<Account> findByUsername(String username);

    @Query(value = "SELECT a.account_id FROM account a WHERE a.username = :username",nativeQuery = true)
    Long findAccountIdByUsername(@Param("username") String username);
    @Modifying
    @Query(value = "UPDATE account SET password = :newPassword WHERE username = :username", nativeQuery = true)
    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
}
