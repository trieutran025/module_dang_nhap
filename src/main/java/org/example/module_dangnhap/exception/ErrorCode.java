package org.example.module_dangnhap.exception;


public enum ErrorCode {
    CODE_LANDING_BLANK(1, "Vui lòng nhập mã mặt bằng"),
    CODE_LANDING_FORMAT(2, "Mã mặt bằng phải đúng định dạng MBxxx"),
    CODE_LANDING_SPECIAL_CHARACTERS(3, "Mã mặt bằng phải là số và không được có ký tự đặc biệt"),
    CODE_LANDING_AT_LEAST(4, "Mã mặt bằng phải có ít nhất 5 ký tự"),
    CODE_LANDING_MAX(5, "Mã mặt bằng phải có tối nhất 25 ký tự"),
    CODE_LANDING_AVAILABLE(6, "Mã mặt bằng đã tồn tại."),
    AREA_LANDING_BLANK(7, "Vui lòng nhập diện tích."),
    AREA_LANDING_REAL_NUMBER(8, "Vui lòng nhập diện tích lớn hơn 0."),
    AREA_LANDING_SPECIAL_CHARACTERS(9, "Diện tích phải là số và không được có ký tự đặc biệt."),
    FEEMAGER_LANDING_NOT_BLANK(10, "Vui lòng nhập phí quản lý lớn hơn 0."),
    FEEMAGER_LANDING_NOT_SPECIAL_CHARACTERS(11, "Phí quản lý phải là số và không được có ký tự đặc biệt."),
    FEEPERMONTH_LANDING_NOTBLANK(12, "Vui lòng nhập giá tiền lớn hơn 0."),
    FEEPERMONTH_LANDING_NOT_SPECIAL_CHARACTERS(13, "Giá tiền phải là số và không được có ký tự đặc biệt."),
    FLOOR_NOT_BLANK(14, "Vui lòng chọn tầng"),
    TYPE_NOT_BLANK(15, "Vui lòng chọn loại mặt bằng"),
    DESCRIPTION_MAX_LENGTH(16, "Chú thích có độ dài tối đa 200 ký tự"),
    DESCRIPTION_NO_SPECIAL_CHARACTERS(17, "Chú thích không được có ký tự đặc biệt."),
    VALIDATION_ERROR, CUSTOM_VALIDATION_ERROR;

    private int code;
    private String message;

    ErrorCode() {
    }

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

