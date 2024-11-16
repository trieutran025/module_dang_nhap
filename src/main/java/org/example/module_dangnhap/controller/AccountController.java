package org.example.module_dangnhap.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.request.AccountReqDTO;
import org.example.module_dangnhap.dto.request.ChangePasswordRequest;
import org.example.module_dangnhap.dto.response.ChangePasswordResponse;
import org.example.module_dangnhap.service.Iteface.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin("*")
public class AccountController {
    @Autowired
    private IAccountService accountService;
    @PostMapping("/account/register")
    public ResponseEntity<String> createAccountAndCustomerInfo(
            @RequestBody AccountReqDTO accountReqDTO) {
        try {
            accountService.createAccountAndCustomerInfo(accountReqDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest){
        return ResponseEntity.ok(accountService.changePassword(changePasswordRequest));
    }
}
