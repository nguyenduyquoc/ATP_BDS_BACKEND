package com.atp.bdss.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionDTO {

    String id;

    UserDTO user;

    LandDTO land;

    short status;

    String code;

    LocalDateTime createdAt;

    LocalDateTime updateAt;

    ProjectDTO projectDTO;

}
