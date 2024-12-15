package org.example.module_dangnhap.service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.repo.CustomerRepo;
import org.example.module_dangnhap.service.Iteface.CustomerService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomerServiceImpl implements CustomerService {
    CustomerRepo customerRepo;
    @Override
    public InforUserDto getUserInfoByAccountId(Long accountId) {
        InforUser user = customerRepo.findUserByAccountId(accountId);
        if (user != null) {
            return new InforUserDto(user.getName(), user.getEmail(), user.getPhone(), user.getAddress());
        }
        throw new RuntimeException("User not found");
    }
}
