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
    UNAUTHORIZED(403, "msg.access.you-do-not-have-permission", HttpStatus.FORBIDDEN),
    INTERNAL_SERVER(500, "msg.internal.server", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "msg.user-existed",HttpStatus.CREATED),
    PHONE_NUMBER_OR_PASSWORD_INCORRECT(1002, "auth.phone-number-or-password-are-incorrect", HttpStatus.BAD_REQUEST),
    PHONE_NUMBER_EXISTED(1003, "auth.phone-number-existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1004, "auth.email-existed", HttpStatus.BAD_REQUEST),
    USER_NOT_YET_AUTHENTICATED(1004,"user.user-not-yet-authenticated", HttpStatus.UNAUTHORIZED),
    LOGIN_FAILED(1005,"user.Phone-number-or-password-incorrect!", HttpStatus.BAD_REQUEST),
    CREATE_TOKEN_FAIL(1006, "token.generate-token-fail", HttpStatus.BAD_REQUEST),
    PROJECT_TYPE_NOT_FOUND(2000, "project-type.project-type-not-found", HttpStatus.BAD_REQUEST),
    PROJECT_TYPE_EXISTED(2001, "project-type.project-type-existed", HttpStatus.BAD_REQUEST),
    CAN_NOT_DELETE_PROJECT_TYPE(2002, "project-type.project-type-existed", HttpStatus.BAD_REQUEST),
    PROJECT_NOT_FOUND(3000, "project.project-not-found", HttpStatus.BAD_REQUEST),
    PROJECT_EXISTED(3001, "project.project-existed", HttpStatus.BAD_REQUEST),
    DUPLICATE_PROJECT_NAME(3002, "project.project-name-duplicate", HttpStatus.BAD_REQUEST),
    CAN_NOT_DELETE_PROJECT(3003, "project.can-not-delete", HttpStatus.BAD_REQUEST),
    AREA_NOT_FOUND(4000, "area-not-found", HttpStatus.BAD_REQUEST),
    DUPLICATE_AREA_NAME(4001, "area.area-duplicate", HttpStatus.BAD_REQUEST),
    LAND_NOT_FOUND(5000, "land.land-not-found", HttpStatus.BAD_REQUEST),
    DUPLICATE_LAND_NAME(5001, "land.land-duplicate", HttpStatus.BAD_REQUEST),
    LAND_EXISTED(5002, "land.land-existed", HttpStatus.BAD_REQUEST),
    STATUS_LAND_NOT_FOUND(5003, "land.status-land-not-found", HttpStatus.BAD_REQUEST),
    DIRECTION_NOT_FOUND(5004, "land.direction-not-found", HttpStatus.BAD_REQUEST),
    TYPE_OF_APARTMENT_NOT_FOUND(5005, "land.type-of-apartment-not-found", HttpStatus.BAD_REQUEST),
    CAN_NOT_BUY_LAND(5006, "land.can-not-buy-this-land", HttpStatus.BAD_REQUEST),
    RECORD_NOT_FOUND(9999, "msg.record-not-found", HttpStatus.BAD_REQUEST),
    STATUS_INCORRECT(6001, "status.status-incorrect", HttpStatus.BAD_REQUEST),
    INVESTOR_NOT_FOUND(8888, "investor.investor-not-found", HttpStatus.BAD_REQUEST),
    DISTRICT_NOT_FOUND(7000,"district.district-not-found", HttpStatus.BAD_REQUEST),
    UPLOAD_FAIL(8000,"upload-file.upload-file-fail", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(9000, "account.user-not-existed", HttpStatus.BAD_REQUEST),
    TRANSACTION_NOT_FOUND(10000, "transaction.transaction-not-found", HttpStatus.BAD_REQUEST),
    CANNOT_UPDATE_ANYMORE(10001, "transaction.transaction-cannot-update-anymore", HttpStatus.BAD_REQUEST),
    OTP_INCORRECT(10002, "otp.otp-incorrect", HttpStatus.BAD_REQUEST),
    STATUS_NOT_FOUND(10003, "status.status-not-found", HttpStatus.BAD_REQUEST),
    UNCATEGORIZED_EXCEPTION(10004, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),;


    private final int code;
    private final String description;
    private HttpStatusCode statusCode;

    ErrorsApp(int code, String description, HttpStatusCode statusCode) {
        this.code = code;
        this.description = description;
        this.statusCode = statusCode;
    }

}
