package org.example.module_dangnhap.repo;

import jakarta.transaction.Transactional;
import org.example.module_dangnhap.dto.response.InforUserDto;
import org.example.module_dangnhap.entity.InforUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InforUserRepo extends JpaRepository<InforUser, Long> {

                @Query(value =
                        "SELECT id,name, email, phone,address,account_id FROM infor_user",nativeQuery = true)
        List<InforUser> findAllInforUser();

        @Transactional
        @Modifying
        @Query(value = "DELETE FROM infor_user WHERE id = :id", nativeQuery = true)
        void deleteByIdNative(@Param("id") Long id);

        @Transactional
        @Modifying // Đánh dấu đây là một thao tác thay đổi
        @Query(value = "INSERT INTO infor_user (name, email, phone) VALUES (:name, :email, :phone)", nativeQuery = true)
        void addNative(@Param("name") String name, @Param("email") String email, @Param("phone") String phone);


        @Transactional
        @Modifying // Đánh dấu đây là một thao tác thay đổi
        @Query(value = "UPDATE infor_user SET name = :name, email = :email, phone = :phone WHERE id = :id", nativeQuery = true)
        void updateNative(@Param("id") Long id, @Param("name") String name, @Param("email") String email, @Param("phone") String phone);
}
