package org.example.module_dangnhap.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.response.InforUserDto;
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
}
