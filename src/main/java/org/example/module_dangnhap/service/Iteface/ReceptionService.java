package org.example.module_dangnhap.service.Iteface;

import org.example.module_dangnhap.dto.request.InforUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface ReceptionService {
    Page<InforUserDto> findAll(Pageable pageable);
}
