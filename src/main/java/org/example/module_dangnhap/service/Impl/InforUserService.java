package org.example.module_dangnhap.service.Impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.repo.IAccountRepository;
import org.example.module_dangnhap.repo.InforUserRepo;
import org.example.module_dangnhap.service.Iteface.InforUserRepoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class InforUserService implements InforUserRepoService {
    @Autowired
    InforUserRepo inforUserRepo;
    @Autowired
    IAccountRepository accountRepo;
    @Autowired
    ModelMapper modelMapper;



    @Override
    public Page<InforUserDto> findAll(Pageable pageable) {
        // Lấy account_id của người dùng hiện tại từ SecurityContextHolder
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentAccountId = null;

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            currentAccountId = accountRepo.findAccountIdByUsername(username);
        }

        // Gọi repository để lấy danh sách nhân viên, bỏ qua người dùng hiện tại và phân trang
        Page<InforUser> inforUsersPage = inforUserRepo.findAllEmployee(currentAccountId, pageable);

        // Ánh xạ danh sách InforUser thành InforUserDto
        Page<InforUserDto> inforUserDtosPage = inforUsersPage.map(inforUser -> {
            InforUserDto inforUserDto = modelMapper.map(inforUser, InforUserDto.class);
            // Thêm thông tin role_name vào trong DTO nếu cần
            inforUserDto.setRoleName(inforUser.getAccount().getRoles());
            return inforUserDto;
        });

        return inforUserDtosPage;
    }



    @Override
    public void delete(Long id) {
        inforUserRepo.deleteByIdNative(id);
    }


    @Override
    public void add(Long accountId,String name, String email, String phone,String address) {
        inforUserRepo.addNative(accountId, name, email, phone,address);
    }

    @Override
    public void update(Long id, String name, String email, String phone,String address) {
        inforUserRepo.updateNative(id, name, email, phone, address);
    }
}
