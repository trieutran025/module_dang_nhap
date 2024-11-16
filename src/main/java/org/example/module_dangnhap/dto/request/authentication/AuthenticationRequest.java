package org.example.module_dangnhap.dto.request.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.entity.Role;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    String username;
    String password;
}
