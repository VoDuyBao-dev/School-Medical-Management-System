package com.medical.schoolMedical.exceptions;

public enum ErrorCode {
//    USERNAME_EXISTS,
//    CONTAINS_WHITESPACE,
//    INVALID_PASSWORD,
//    INTERNAL_ERROR,
//    USERNAME_NOT_EXISTS,
    // Đăng nhập
    USERNAME_NOT_EXISTS("ERR001", "Tên đăng nhập không tồn tại."),
    INVALID_PASSWORD("ERR002", "Mật khẩu không chính xác."),
    USERNAME_EXISTS("ERR003", "Tên đăng nhập đã tồn tại."),
    CONTAINS_WHITESPACE("ERR004", "Tên đăng nhập không được chứa khoảng trắng."),

    // Lỗi hệ thống
    INTERNAL_ERROR("ERR999", "Lỗi hệ thống. Vui lòng thử lại sau.");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }

    public String getMessage() { return message; }

    @Override
    public String toString() {
        return "[" + code + "] " + message;
    }

}
