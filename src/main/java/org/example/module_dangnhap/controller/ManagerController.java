package org.example.module_dangnhap.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.service.Impl.ManagerServiceImpl;
import org.example.module_dangnhap.service.Iteface.IAccountService;
import org.example.module_dangnhap.service.Iteface.IManagerService;
import org.example.module_dangnhap.service.Iteface.InforUserRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/receptionist/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Validated
public class ManagerController {

    private final ManagerServiceImpl iManagerService;
    @GetMapping("")
    public ResponseEntity<Page<InforUserDto>> getAllReceptionist(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        if (page.orElse(0) < 0||size <= 0) {
//            log.error("Invalid page or size parameters: page={}, size={}", page, size);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page.orElse(0), size);
        Page<InforUserDto> inforUserDtos = iManagerService.findAll(pageable);

        if (inforUserDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(inforUserDtos, HttpStatus.OK);
    }
}
