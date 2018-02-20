package com.poiji.exception;

/**
 * User: Dmitry Volkov
 * Date: 20.02.2018
 * Time: 11:25
 */
public class ConvertValueException extends PoijiException {
    private Integer rowNum;
    private Integer cellNum;

    public ConvertValueException(String message, Integer rowNum, Integer cellNum) {
        super(message);
        this.rowNum = rowNum;
        this.cellNum = cellNum;
    }

    public ConvertValueException(String message, Throwable cause, Integer rowNum, Integer cellNum) {
        super(message, cause);
        this.rowNum = rowNum;
        this.cellNum = cellNum;
    }
}
