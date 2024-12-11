package org.example.module_dangnhap.repo;

import jakarta.transaction.Transactional;
import org.example.module_dangnhap.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String roleName);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO account_role (account_id, role_id) VALUES (:accountId, :roleId)", nativeQuery = true)
    int assignRoleToAccount(@Param("accountId") Long accountId, @Param("roleId") Long roleId);
}