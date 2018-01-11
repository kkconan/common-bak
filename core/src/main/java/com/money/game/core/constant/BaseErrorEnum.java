package com.money.game.core.constant;

/**
 * @author conan
 *         2017/10/27 15:46
 **/
public enum BaseErrorEnum {

    SUCCESS("0", "成功");

    private String code;

    private String value;

    BaseErrorEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
