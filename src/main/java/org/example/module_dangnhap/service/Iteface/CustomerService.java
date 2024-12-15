package org.example.module_dangnhap.service.Iteface;

import org.example.module_dangnhap.dto.request.InforUserDto;

public interface CustomerService {
    InforUserDto getUserInfoByAccountId(Long accountId);
}