package com.weibangong.camel;

/**
 * Created by jianghailong on 15/6/22.
 */
public class CamelGridFsException extends Exception {

    public CamelGridFsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CamelGridFsException(String message) {
        super(message);
    }

    public CamelGridFsException(Throwable cause) {
        super(cause);
    }
}
