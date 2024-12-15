package org.example.module_dangnhap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.AccountUserDto;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.dto.response.UpdateAccountResponse;
import org.example.module_dangnhap.service.Iteface.IAccountService;
import org.example.module_dangnhap.service.Iteface.InforUserRepoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/employees/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Validated
public class AdminController {

    private final InforUserRepoService inforUserRepoService;
    private final IAccountService iAccountService;

    @GetMapping("")
    public ResponseEntity<Page<InforUserDto>> getAllInforUser(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        if (page.orElse(0) < 0||size <= 0) {
            log.error("Invalid page or size parameters: page={}, size={}", page, size);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page.orElse(0), size);
        Page<InforUserDto> inforUserDtos = inforUserRepoService.findAll(pageable);

        if (inforUserDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(inforUserDtos, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> addInforUser(@RequestBody AccountUserDto accountUserDto) {
      iAccountService.createAccountAndCustomerInfo(accountUserDto.getAccountReqDTO(),accountUserDto.getUserDto(),accountUserDto.getRoleName() );
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateInforUser(@PathVariable Long id, @RequestBody UpdateAccountResponse accountUserDto) {
        // Đảm bảo ID từ @PathVariable được truyền vào phương thức
//        accountUserDto.getUserDto().setId(id);
//        accountUserDto.getUserDto().setAccountId(id); // Đảm bảo accountId được thiết lập

        System.out.println("Setting ID: " + id);
        System.out.println("UserDto ID: " + accountUserDto.getUserDto().getId());
        System.out.println("AccountReqDTO: " + accountUserDto.getAccountReqDTO());
        System.out.println("UserDto: " + accountUserDto.getUserDto());

        iAccountService.updateAccountAndCustomerInfo(accountUserDto.getAccountReqDTO(), accountUserDto.getUserDto(), accountUserDto.getRoleName());

        return new ResponseEntity<>(HttpStatus.CREATED);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInforUser(@PathVariable Long id) {
        inforUserRepoService.delete(id);
        log.info("Deleted user with ID {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
