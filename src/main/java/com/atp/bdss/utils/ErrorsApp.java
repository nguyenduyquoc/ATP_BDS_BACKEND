package com.atp.bdss.utils;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorsApp {
    SUCCESS(200, "msg.success", HttpStatus.OK),
    BAD_REQUEST(400, "msg.bad.request", HttpStatus.BAD_REQUEST),
    BAD_REQUEST_PATH(400, "msg.bad.request.path", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(401, "msg.unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(403, "msg.access.denied", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER(500, "msg.internal.server", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "msg.user-existed",HttpStatus.CREATED),
    PHONE_NUMBER_OR_PASSWORD_INCORRECT(1002, "auth.phone-number-or-password-are-incorrect", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1003, "auth.phone-number-existed", HttpStatus.BAD_REQUEST),
    PROJECT_TYPE_NOT_FOUND(2000, "project-type.project-type-not-found", HttpStatus.BAD_REQUEST),
    PROJECT_TYPE_EXISTED(2001, "project-type.project-type-existed", HttpStatus.BAD_REQUEST),
    PROJECT_NOT_FOUND(2000, "project.project-not-found", HttpStatus.BAD_REQUEST),
    PROJECT_EXISTED(2001, "project.project-existed", HttpStatus.BAD_REQUEST),
    LAND_NOT_FOUND(2002, "land.land-not-found", HttpStatus.BAD_REQUEST),
    LAND_EXISTED(2003, "land.land-existed", HttpStatus.BAD_REQUEST),
    RECORD_NOT_FOUND(3000, "msg.record-not-found", HttpStatus.BAD_REQUEST),
    STATUS_NOT_FOUND(8000, "status.status-not-found", HttpStatus.BAD_REQUEST),
    INVESTOR_NOT_FOUND(4000, "investor.investor-not-found",  HttpStatus.BAD_REQUEST),
    DISTRICT_NOT_FOUND(5000,"district.district-not-found" , HttpStatus.BAD_REQUEST),
    UPLOAD_FAIL(6000,"upload-file.upload-file-fail" , HttpStatus.BAD_REQUEST),
    DUPLICATE_AREA_NAME(7000, "area.area-duplicate", HttpStatus.BAD_REQUEST),
    DUPLICATE_LAND_NAME(7001, "land.land-duplicate", HttpStatus.BAD_REQUEST),;


    private final int code;
    private final String description;
    private HttpStatusCode statusCode;

    ErrorsApp(int code, String description, HttpStatusCode statusCode) {
        this.code = code;
        this.description = description;
        this.statusCode = statusCode;
    }

}
