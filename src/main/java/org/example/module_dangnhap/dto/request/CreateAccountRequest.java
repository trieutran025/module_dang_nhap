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
public class CreateAccountRequest {
    private AccountReqDTO accountReqDTO;
    private InforUserDto inforUserDto;
    private String roleName;
}
