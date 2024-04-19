package com.atp.bdss.exceptions;

import com.atp.bdss.utils.ErrorsApp;

public class CustomException extends RuntimeException{
    private ErrorsApp errorApp;
    private Integer codeError;

    public CustomException(ErrorsApp errorApp) {
        super(errorApp.getDescription());
        this.errorApp = errorApp;
        this.codeError = errorApp.getCode();
    }

    public CustomException(int code, String mess) {
        super(mess);
        codeError = code;
    }

    public CustomException(String mess) {
        super(mess);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public ErrorsApp getErrorApp() {
        return errorApp;
    }

    public Integer getCodeError() {
        return codeError;
    }
}
