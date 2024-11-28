package org.example.module_dangnhap.service.Impl;

import org.example.module_dangnhap.dto.response.InforUserDto;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.repo.IAccountRepository;
import org.example.module_dangnhap.repo.InforUserRepo;
import org.example.module_dangnhap.service.Iteface.InforUserRepoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InforUserService implements InforUserRepoService {
    @Autowired
    InforUserRepo inforUserRepo;
    @Autowired
    IAccountRepository accountRepo;
    @Autowired
    ModelMapper modelMapper;



    @Override
    public List<InforUserDto> findAll() {
        // Lấy account_id của người dùng hiện tại từ SecurityContextHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentAccountId = null;

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            currentAccountId = accountRepo.findAccountIdByUsername(username);
        }

        // Gọi repository để lấy danh sách nhân viên, bỏ qua người dùng hiện tại
        List<InforUser> inforUsers = inforUserRepo.findAllEmployee(currentAccountId);

        // Ánh xạ danh sách InforUser thành InforUserDto
        return inforUsers.stream()
                .map(inforUser -> {
                    InforUserDto inforUserDto = modelMapper.map(inforUser, InforUserDto.class);
                    // Thêm thông tin role_name vào trong DTO nếu cần
                    inforUserDto.setRoleName(inforUser.getAccount().getRoles());
                    return inforUserDto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public void delete(Long id) {
        inforUserRepo.deleteByIdNative(id);
    }


    @Override
    public void add(String name, String email, String phone,String address) {
        inforUserRepo.addNative(name, email, phone,address);
    }

    @Override
    public void update(Long id, String name, String email, String phone) {
        inforUserRepo.updateNative(id, name, email, phone);
    }
}
