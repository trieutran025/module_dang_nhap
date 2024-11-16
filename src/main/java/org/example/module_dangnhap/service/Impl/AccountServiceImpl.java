package org.example.module_dangnhap.service.Impl;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.AccountReqDTO;
import org.example.module_dangnhap.dto.request.ChangePasswordRequest;
import org.example.module_dangnhap.dto.response.ChangePasswordResponse;
import org.example.module_dangnhap.entity.Account;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.entity.Role;
import org.example.module_dangnhap.repo.IAccountRepository;
import org.example.module_dangnhap.repo.InforUserRepo;
import org.example.module_dangnhap.repo.RoleRepository;
import org.example.module_dangnhap.service.Iteface.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    public String createAccountAndCustomerInfo(AccountReqDTO accountReqDTO) {
        // Step_1: Check if account exists
        Optional<Account> existingAccount = iAccountRepository.findByUsername(accountReqDTO.getUsername());
        if (existingAccount.isPresent()) {
            return "Account with username: " + accountReqDTO.getUsername() + " already exists";
        }
        // Step_3: Encrypt password
        String encryptedPassword = passwordEncoder.encode(accountReqDTO.getPassword());
        // Step_4: Create account
        int status = iAccountRepository.createAccount(accountReqDTO.getUsername(), encryptedPassword);
        iAccountRepository.linkAccountToInforUser();
        if (status == 1) {
            return "Employee account created successfully";
        } else {
            return "Employee account created failed";
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
