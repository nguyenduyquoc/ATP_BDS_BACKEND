package com.atp.bdss.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserInfoFullOptionDTO {

    String id;

    String name;

    String email;

    String phone;

    String password;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    Short isDeleted;

    List<TransactionDTO> transaction;
}
