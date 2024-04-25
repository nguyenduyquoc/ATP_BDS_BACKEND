package com.atp.bdss.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserInfoFromGoogle {
    public String email;
    public String name;
}