package org.example.module_dangnhap.repo;


import org.example.module_dangnhap.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IAccountRepository extends JpaRepository<Account, Long> {

    @Procedure(procedureName = "create_employee_account")
    Integer createEmployeeAccount(
            @Param("p_username") String username,
            @Param("p_password") String password,
            @Param("p_employee_id") Long employeeId);

    @Query(value = "SELECT id, username, password, is_active FROM account WHERE username = :username", nativeQuery = true)
    Optional<Account> findByUsername(String username);


    @Modifying
    @Query(value = "UPDATE Account SET password = :newPassword WHERE username = :username", nativeQuery = true)
    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);

}
