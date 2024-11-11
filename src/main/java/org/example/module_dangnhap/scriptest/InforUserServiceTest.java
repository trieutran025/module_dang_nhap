package org.example.module_dangnhap.scriptest;

import org.example.module_dangnhap.dto.response.InforUserDto;
import org.example.module_dangnhap.entity.Account;
import org.example.module_dangnhap.entity.InforUser;
import org.example.module_dangnhap.entity.Role;
import org.example.module_dangnhap.repo.InforUserRepo;
import org.example.module_dangnhap.service.Impl.InforUserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class InforUserServiceTest {

    @InjectMocks
    private InforUserService inforUserService;

    @Mock
    private InforUserRepo inforUserRepo;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Khởi tạo các vai trò
        Role adminRole = new Role(1L, "ADMIN", null);
        Role userRole = new Role(2L, "USER", null);

        // Tạo Set<Role> để lưu trữ các vai trò
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);

        // Tạo nhiều đối tượng Account và InforUser
        Account account1 = new Account(1L, "Trieutran025", "123", roles, true);
        InforUser user1 = new InforUser(1L, "John Doe", "john@example.com", "123456789", "123 Address", account1);
        InforUserDto userDto1 = new InforUserDto(1L, "John Doe", "john@example.com", "123456789","Ngo Quyen",1L);

        Account account2 = new Account(2L, "JaneSmith", "456", roles, true);
        InforUser user2 = new InforUser(2L, "Jane Smith", "jane@example.com", "987654321", "456 Address", account2);
        InforUserDto userDto2 = new InforUserDto(2L, "Jane Smith", "jane@example.com", "987654321","456 Address",2L);

        Account account3 = new Account(3L, "MarkJohnson", "789", roles, false);
        InforUser user3 = new InforUser(3L, "Mark Johnson", "mark@example.com", "555555555", "789 Address", account3);
        InforUserDto userDto3 = new InforUserDto(3L, "Mark Johnson", "mark@example.com", "555555555","789 Address",3L);

        // Danh sách người dùng mong đợi
        List<InforUser> users = List.of(user1, user2, user3);
        List<InforUserDto> expectedDtos = List.of(userDto1, userDto2, userDto3);

        // Giả lập phương thức findAllInforUser và ánh xạ model
        when(inforUserRepo.findAllInforUser()).thenReturn(users);
        when(modelMapper.map(user1, InforUserDto.class)).thenReturn(userDto1);
        when(modelMapper.map(user2, InforUserDto.class)).thenReturn(userDto2);
        when(modelMapper.map(user3, InforUserDto.class)).thenReturn(userDto3);

        // Gọi phương thức findAll của service
        List<InforUserDto> result = inforUserService.findAll();

        // Kiểm tra kết quả với vòng lặp
        assertEquals(expectedDtos.size(), result.size(), "Số lượng user không khớp");

        for (int i = 0; i < expectedDtos.size(); i++) {
            InforUserDto expectedDto = expectedDtos.get(i);
            InforUserDto actualDto = result.get(i);

            // So sánh các thuộc tính của từng đối tượng
            assertEquals(expectedDto.getId(), actualDto.getId(), "ID không khớp cho user tại vị trí " + i);
            assertEquals(expectedDto.getName(), actualDto.getName(), "Tên không khớp cho user tại vị trí " + i);
            assertEquals(expectedDto.getEmail(), actualDto.getEmail(), "Email không khớp cho user tại vị trí " + i);
            assertEquals(expectedDto.getPhone(), actualDto.getPhone(), "Số điện thoại không khớp cho user tại vị trí " + i);
        }

        // In ra thông báo nếu kiểm tra thành công
        System.out.println("Kiểm tra thành công: Tất cả thông tin user trong danh sách đều đúng!");
    }
}
