package org.example.module_dangnhap.service.Impl;

import org.example.module_dangnhap.dto.response.InforUserDto;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.repo.InforUserRepo;
import org.example.module_dangnhap.service.Iteface.InforUserRepoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InforUserService implements InforUserRepoService {
    @Autowired
    InforUserRepo inforUserRepo;
    @Autowired
    ModelMapper modelMapper;

    public InforUserService(InforUserRepo userRepository, PasswordEncoder passwordEncoder) {
    }

    @Override
    public List<InforUserDto> findAll() {
        List<InforUser> inforUsers = inforUserRepo.findAllInforUser();
        List<InforUserDto> inforUserDtos = inforUsers.stream()
                .map(inforUser -> modelMapper.map(inforUser, InforUserDto.class))
                .collect(Collectors.toList());
        return inforUserDtos;
    }


    @Override
    public void delete(Long id) {
        inforUserRepo.deleteByIdNative(id);
    }


    @Override
    public void add(String name, String email, String phone) {
        inforUserRepo.addNative(name, email, phone);
    }

    @Override
    public void update(Long id, String name, String email, String phone) {
        inforUserRepo.updateNative(id, name, email, phone);
    }
}
