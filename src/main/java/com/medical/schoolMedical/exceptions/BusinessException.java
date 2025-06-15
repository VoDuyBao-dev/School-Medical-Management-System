package com.medical.schoolMedical.exceptions;


public class BusinessException extends RuntimeException{
//    private final ErrorCode errorCode;
//
//    public BusinessException(ErrorCode errorCode, String message) {
//        super(message);
//        this.errorCode = errorCode;
//    }
//
//    public ErrorCode getErrorCode() {
//        return errorCode;
//    }

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public String getCode() {
        return errorCode.getCode();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
