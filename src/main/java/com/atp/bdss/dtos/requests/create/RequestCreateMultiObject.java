package com.atp.bdss.dtos.requests.create;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestCreateMultiObject<T> {

    String id;

    List<T> multiObject;

}

