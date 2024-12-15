package org.example.module_dangnhap.service.Impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.AccountReqDTO;
import org.example.module_dangnhap.dto.request.ChangePasswordRequest;
import org.example.module_dangnhap.dto.response.ChangePasswordResponse;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.dto.response.UserDtoResponse;
import org.example.module_dangnhap.entity.Account;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.entity.Role;
import org.example.module_dangnhap.repo.IAccountRepository;
import org.example.module_dangnhap.repo.InforUserRepo;
import org.example.module_dangnhap.repo.RoleRepository;
import org.example.module_dangnhap.service.Iteface.IAccountService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceImpl implements IAccountService {

    IAccountRepository iAccountRepository;
    private PasswordEncoder passwordEncoder;

    InforUserRepo inforUserRepo;
    RoleRepository roleRepository;



    @Transactional
    public String createAccountAndCustomerInfo(AccountReqDTO accountReqDTO, InforUserDto inforUserDto, String roleName) {
        try {
            // Bước 1: Kiểm tra tài khoản đã tồn tại
            Optional<Account> existingAccount = iAccountRepository.findByUsername(accountReqDTO.getUsername());
            if (existingAccount.isPresent()) {
                return "Tài khoản với tên người dùng: " + accountReqDTO.getUsername() + " đã tồn tại";
            }

            // Bước 2: Mã hóa mật khẩu
            String encryptedPassword = passwordEncoder.encode(accountReqDTO.getPassword());

            // Bước 3: Tạo tài khoản
            iAccountRepository.createAccount(accountReqDTO.getUsername(), encryptedPassword);

            // Bước 4: Truy vấn tài khoản đã tạo
            Account createdAccount = iAccountRepository.findByUsername(accountReqDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("Không thể tìm thấy tài khoản đã tạo"));

            // Bước 5: Tạo InforUser và liên kết với tài khoản
            inforUserDto.setAccountId(createdAccount.getAccountId()); // Đảm bảo ID tài khoản được thiết lập
            int inforUserCreated = inforUserRepo.addNative(
                    inforUserDto.getAccountId(),
                    inforUserDto.getName(),
                    inforUserDto.getEmail(),
                    inforUserDto.getPhone(),
                    inforUserDto.getAddress()
            );
            if (inforUserCreated <= 0) {
                throw new RuntimeException("Không thể tạo thông tin InforUser");
            }

            // Bước 6: Tạo vai trò cho tài khoản (nếu chưa có vai trò)
            Optional<Role> role = roleRepository.findByRoleName(roleName);
            if (role.isPresent()) {
                // Thêm vai trò vào tài khoản
                roleRepository.assignRoleToAccount(createdAccount.getAccountId(), role.get().getRoleId());
            } else {
                throw new RuntimeException("Vai trò không tồn tại");
            }

            return "Tạo tài khoản, thông tin khách hàng và gán vai trò thành công!";
        } catch (Exception e) {
            // Ghi log lỗi để dễ dàng debug
            e.printStackTrace();
            // Rollback giao dịch và trả thông báo lỗi
            throw new RuntimeException("Không thể tạo tài khoản và thông tin khách hàng: " + e.getMessage());
        }
    }
    @Transactional
    public String updateAccountAndCustomerInfo(AccountReqDTO accountReqDTO, UserDtoResponse inforUserDto, String roleName) {
        try {
            // Bước 1: Lấy accountId từ infor_userId (ID của thông tin người dùng)
            Optional<InforUser> inforUser = inforUserRepo.findById(inforUserDto.getId());
            if (inforUser.isEmpty()) {
                return "Không tìm thấy thông tin người dùng";
            }

            Long accountId = inforUser.get().getAccount().getAccountId(); // Lấy accountId từ thông tin người dùng

            // Bước 2: Cập nhật tài khoản
            Optional<Account> existingAccount = iAccountRepository.findById(accountId);
            if (existingAccount.isEmpty()) {
                return "Tài khoản không tồn tại";
            }

            // Kiểm tra mật khẩu và mã hóa nếu có sự thay đổi
            String encryptedPassword = accountReqDTO.getPassword();
            if (encryptedPassword != null && !encryptedPassword.isEmpty()) {
                encryptedPassword = passwordEncoder.encode(encryptedPassword);  // Mã hóa mật khẩu
            }

            // Cập nhật tài khoản
            iAccountRepository.updateAccount(accountId, accountReqDTO.getUsername(), encryptedPassword);

            // Bước 3: Cập nhật thông tin người dùng (InforUser)
            inforUserDto.setAccountId(accountId); // Đảm bảo accountId được thiết lập
            int inforUserUpdated = inforUserRepo.updateNative(
                    inforUserDto.getId(),
                    inforUserDto.getName(),
                    inforUserDto.getEmail(),
                    inforUserDto.getPhone(),
                    inforUserDto.getAddress()
            );
            if (inforUserUpdated <= 0) {
                throw new RuntimeException("Không thể cập nhật thông tin người dùng");
            }

            // Bước 4: Cập nhật vai trò cho tài khoản
            Optional<Role> role = roleRepository.findByRoleName(roleName);
            if (role.isPresent()) {
                int rowsAffected = roleRepository.updateRoleForAccount(accountId, role.get().getRoleId());
                if (rowsAffected <= 0) {
                    throw new RuntimeException("Không thể cập nhật vai trò cho tài khoản");
                }
            } else {
                throw new RuntimeException("Vai trò không tồn tại");
            }

            return "Cập nhật tài khoản, thông tin người dùng và vai trò thành công!";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể cập nhật tài khoản và thông tin người dùng: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = iAccountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Access roles to initialize them within the transaction

        account.getRoles().size();
        return account;
    }



    @Override
    public Account getCurrentAccount() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        return iAccountRepository.findByUsername(name).orElseThrow();
    }

    @Override
    @Transactional
    public ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest) {
        Account account = getCurrentAccount();

        if (passwordEncoder.matches(changePasswordRequest.getOldPassword(), account.getPassword())) {

            String newPasswordEncoded = passwordEncoder.encode(changePasswordRequest.getNewPassword());
            int updateCount = iAccountRepository.updatePasswordByUsername(account.getUsername(), newPasswordEncoded);

            if (updateCount > 0) {
                return ChangePasswordResponse.builder()
                        .message("Đổi mật khẩu thành công.")
                        .build();
            }
        }
        return ChangePasswordResponse.builder()
                .message("Đổi mật khẩu thất bại.")
                .build();
    }
}
