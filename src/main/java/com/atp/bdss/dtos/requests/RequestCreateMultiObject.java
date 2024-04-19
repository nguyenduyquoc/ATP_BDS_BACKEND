package com.atp.bdss.dtos.requests;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestCreateMultiObject<T> {

    String id;

    List<T> multiObject;

}

