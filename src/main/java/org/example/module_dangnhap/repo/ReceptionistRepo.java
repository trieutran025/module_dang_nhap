package org.example.module_dangnhap.repo;

import org.example.module_dangnhap.entity.InforUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceptionistRepo extends JpaRepository<InforUser,Long> {
    @Query(value = "SELECT u.id, u.name, u.email, u.phone, u.address, u.account_id " +
            "FROM infor_user u " +
            "JOIN account a ON u.account_id = a.account_id " +
            "JOIN account_role ar ON a.account_id = ar.account_id " +
            "JOIN role r ON ar.role_id = r.role_id " +
            "WHERE r.role_name IN ( 'CUSTOMER') " +
            "AND u.account_id != :currentAccountId",
            countQuery = "SELECT COUNT(*) FROM infor_user u " +
                    "JOIN account a ON u.account_id = a.account_id " +
                    "JOIN account_role ar ON a.account_id = ar.account_id " +
                    "JOIN role r ON ar.role_id = r.role_id " +
                    "WHERE r.role_name IN ('CUSTOMER') " +
                    "AND u.account_id != :currentAccountId",
            nativeQuery = true)
    Page<InforUser> findAllCustomer(@Param("currentAccountId") Long currentAccountId, Pageable pageable);
}
