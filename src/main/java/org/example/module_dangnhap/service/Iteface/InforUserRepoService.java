package org.example.module_dangnhap.service.Iteface;

import org.example.module_dangnhap.dto.request.InforUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface InforUserRepoService {
     Page<InforUserDto> findAll(Pageable pageable);
    void delete(Long id);
    void add(Long accountId,String name, String email, String phone,String address);


    void update(Long id, String name, String email, String phone, String address);
}
