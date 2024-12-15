package org.example.module_dangnhap.service.Iteface;


import org.example.module_dangnhap.dto.request.AccountReqDTO;
import org.example.module_dangnhap.dto.request.ChangePasswordRequest;
import org.example.module_dangnhap.dto.response.ChangePasswordResponse;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.dto.response.UserDtoResponse;
import org.example.module_dangnhap.entity.Account;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface IAccountService extends UserDetailsService {
     String createAccountAndCustomerInfo(AccountReqDTO accountReqDTO, InforUserDto inforUserDto,String roleName);


    Account getCurrentAccount();


    ChangePasswordResponse changePassword(ChangePasswordRequest changePasswordRequest);
    String updateAccountAndCustomerInfo(AccountReqDTO accountReqDTO, UserDtoResponse inforUserDto,String roleName);

}
