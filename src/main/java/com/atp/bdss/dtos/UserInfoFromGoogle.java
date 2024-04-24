package com.atp.bdss.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class UserInfoFromGoogle {
    public String aud;
    public String azp;
    public String email;
    public Boolean email_verified;
    public String exp;
    public String family_name;
    public String given_name;
    public String iat;
    public String iss;
    public String jti;
    public String name;
    public String nbf;
    public Date picture;
}