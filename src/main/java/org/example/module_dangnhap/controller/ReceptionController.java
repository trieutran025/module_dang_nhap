package org.example.module_dangnhap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.module_dangnhap.dto.request.InforUserDto;
import org.example.module_dangnhap.service.Impl.ReceptionServiceImpl;
import org.example.module_dangnhap.service.Iteface.ReceptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/customer/")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
@Validated
public class ReceptionController {
    private final ReceptionServiceImpl receptionService;
    @GetMapping("")
    public ResponseEntity<Page<InforUserDto>> getAllCustomer(
            @RequestParam("page") Optional<Integer> page,
            @RequestParam(value = "size", defaultValue = "5") int size) {

        if (page.orElse(0) < 0||size <= 0) {
//            log.error("Invalid page or size parameters: page={}, size={}", page, size);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable pageable = PageRequest.of(page.orElse(0), size);
        Page<InforUserDto> inforUserDtos = receptionService.findAll(pageable);

        if (inforUserDtos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(inforUserDtos, HttpStatus.OK);
    }
}
