package org.example.module_dangnhap.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.request.AccountReqDTO;
import org.example.module_dangnhap.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InforUserDto {
     Long id;
     String name;
     String email;
     String phone;
     String address;
     Long accountId;
     Set<Role> roleName;

    public InforUserDto(String name, String email, String phone, String address) {
    }
}
