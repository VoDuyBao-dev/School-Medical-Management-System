package com.medical.schoolMedical.exceptions;

public enum ErrorCode {

    // Đăng nhập
    USERNAME_NOT_EXISTS("ERR001", "Tên đăng nhập không tồn tại."),
    INVALID_PASSWORD("ERR002", "Mật khẩu không chính xác."),
    USERNAME_EXISTS("ERR003", "Tên đăng nhập đã tồn tại."),
    CONTAINS_WHITESPACE("ERR004", "Tên đăng nhập không được chứa khoảng trắng."),

//    INTERNAL_ERROR,
//health check consent
    HEALTH_CHECK_CONSENT_NOT_FOUND("ERR050", "Phiếu đồng ý kiểm tra sức khỏe không tồn tại."),
    SURVEY_EXPIRED("ERR051", "Phiếu đã hết hạn."),
    SAVE_HEALTH_CHECK_CONSENT_FAILED("ERR052", "Lưu phiếu đồng ý kiểm tra sức khỏe thất bại."),
    CHECK_DATE_INVALID("ERR057", "Ngày kiểm tra không được là ngày trong quá khứ"),

//    Mã lỗi của health check record
    SCHOOL_NURSE_NOT_EXISTS("ERR053", "School nurse không tồn tại."),
    HEALTH_CHECK_RECORD_NOT_EXISTS("ERR054", "Health check record không tồn tại."),
    SAVE_HEALTH_CHECK_RECORD_FAILED("ERR055", "Lưu health check record thất bại."),

//Lỗi tìm kiếm role:
    PARENT_NOT_EXISTS("ERR056", "Parent không tồn tại."),

//    Lỗi health check schedule
SAVE_HEALTH_CHECK_SCHEDULE_FAILED("ERR058", "Lưu lịch kiểm tra sức khỏe thất bại."),
    HEALTH_CHECK_SCHEDULE_NOT_EXISTS("ERR059", "lịch kiểm tra sức khỏe không tồn tại."),
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
