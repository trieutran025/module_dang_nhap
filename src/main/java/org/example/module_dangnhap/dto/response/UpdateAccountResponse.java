package org.example.module_dangnhap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.request.AccountReqDTO;
import org.example.module_dangnhap.dto.request.InforUserDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountResponse {
    AccountReqDTO accountReqDTO;
    UserDtoResponse userDto;
    private String roleName;
}
