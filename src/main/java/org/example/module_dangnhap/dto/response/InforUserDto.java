package org.example.module_dangnhap.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.example.module_dangnhap.dto.request.AccountReqDTO;

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
}
