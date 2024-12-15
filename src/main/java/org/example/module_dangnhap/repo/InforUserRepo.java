package org.example.module_dangnhap.repo;

import jakarta.transaction.Transactional;
import org.example.module_dangnhap.entity.InforUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InforUserRepo extends JpaRepository<InforUser, Long> {

        @Query(value = "SELECT u.id, u.name, u.email, u.phone, u.address, u.account_id " +
                "FROM infor_user u " +
                "JOIN account a ON u.account_id = a.account_id " +
                "JOIN account_role ar ON a.account_id = ar.account_id " +
                "JOIN role r ON ar.role_id = r.role_id " +
                "WHERE r.role_name IN ('MANAGER', 'RECEPTIONIST') " +
                "AND u.account_id != :currentAccountId",
                countQuery = "SELECT COUNT(*) FROM infor_user u " +
                        "JOIN account a ON u.account_id = a.account_id " +
                        "JOIN account_role ar ON a.account_id = ar.account_id " +
                        "JOIN role r ON ar.role_id = r.role_id " +
                        "WHERE r.role_name IN ('MANAGER', 'RECEPTIONIST') " +
                        "AND u.account_id != :currentAccountId",
                nativeQuery = true)
        Page<InforUser> findAllEmployee(@Param("currentAccountId") Long currentAccountId, Pageable pageable);

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM infor_user WHERE id = :id", nativeQuery = true)
        void deleteByIdNative(@Param("id") Long id);
        @Query(value = "SELECT id, name, email, phone, address, account_id FROM infor_user WHERE id = :id", nativeQuery = true)
        Optional<InforUser> findInforUserById(@Param("id") Long id);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO infor_user (account_id, name, email, phone, address) VALUES (:accountId, :name, :email, :phone, :address)",nativeQuery = true)
        int addNative(@Param("accountId") Long accountId,
                      @Param("name") String name,
                      @Param("email") String email,
                      @Param("phone") String phone,
                      @Param("address") String address);



                @Modifying // Đánh dấu đây là một thao tác thay đổi
                @Transactional
                @Query(value = "UPDATE infor_user SET name = :name, email = :email, phone = :phone ,address = :address WHERE id = :id", nativeQuery = true)
                int updateNative(@Param("id") Long id, @Param("name") String name, @Param("email") String email, @Param("phone") String phone, @Param("address") String address);
}
