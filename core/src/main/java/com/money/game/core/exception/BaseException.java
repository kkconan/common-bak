package com.money.game.core.exception;


public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 5117980659382038192L;
   private String code;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BaseException(Throwable e) {
        super(e.getMessage());
        this.code = "-1";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
