package org.example.module_dangnhap.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountUserDto {
    AccountReqDTO accountReqDTO;
    InforUserDto userDto;
    private String roleName;
}
