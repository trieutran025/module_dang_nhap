package org.example.module_dangnhap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDtoResponse {
    Long id;
    String name;
    String email;
    String phone;
    String address;
    Long accountId;
}
