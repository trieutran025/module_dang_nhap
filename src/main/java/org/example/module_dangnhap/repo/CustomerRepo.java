package org.example.module_dangnhap.repo;

import org.example.module_dangnhap.entity.InforUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<InforUser,Long> {

    @Query(value = "SELECT * FROM htks.infor_user WHERE account_id = :accountId", nativeQuery = true)
    InforUser findUserByAccountId(@Param("accountId") Long accountId);
}
