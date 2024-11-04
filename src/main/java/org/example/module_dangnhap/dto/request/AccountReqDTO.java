package org.example.module_dangnhap.dto.request;


import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.processing.Pattern;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountReqDTO {
//    @NotBlank(message = "Username is mandatory")
//    @Size(max = 30, message = "Username must not exceed 30 characters")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Username must contain only letters and numbers")
    String username;
//    @NotBlank(message = "Password is mandatory")
//    @Size(max = 30, message = "Password must not exceed 30 characters")
//    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "Password must contain only letters and numbers")
    String password; // Mật khẩu nên được mã hóa trước khi lưu
}
