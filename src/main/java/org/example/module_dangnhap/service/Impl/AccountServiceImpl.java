package org.example.module_dangnhap.service.Impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.repo.IAccountRepository;
import org.example.module_dangnhap.service.Iteface.IAccountService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements IAccountService {
    
    IAccountRepository iAccountRepository;
}
