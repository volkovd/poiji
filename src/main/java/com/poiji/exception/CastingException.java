package com.poiji.exception;

/**
 * User: Dmitry Volkov
 * Date: 20.02.2018
 * Time: 11:41
 */
public class CastingException extends Exception {
    public CastingException() {
    }

    public CastingException(String message) {
        super(message);
    }

    public CastingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CastingException(Throwable cause) {
        super(cause);
    }

    public CastingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
