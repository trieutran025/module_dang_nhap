package org.example.module_dangnhap.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.response.InforUserDto;
import org.example.module_dangnhap.service.Iteface.InforUserRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    @Autowired
    private InforUserRepoService inforUserRepoService;

    @GetMapping("")
    public ResponseEntity<List<InforUserDto>> getAllInforUser() {
        return new ResponseEntity<>(inforUserRepoService.findAll(), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Void> addInforUser(@RequestBody InforUserDto userDto) {
        inforUserRepoService.add(userDto.getName(), userDto.getEmail(), userDto.getPhone(), userDto.getAddress());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateInforUser(@PathVariable Long id, @RequestBody InforUserDto userDto) {
        inforUserRepoService.update(id, userDto.getName(), userDto.getEmail(), userDto.getPhone());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInforUser(@PathVariable Long id) {
        inforUserRepoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

