package org.example.module_dangnhap.service.Iteface;

import org.example.module_dangnhap.dto.response.InforUserDto;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface InforUserRepoService {
    List<InforUserDto> findAll();
    void delete(Long id);
    void add(String name, String email, String phone);

    void update(Long id, String name, String email, String phone);
}
