package com.poiji.bind.mapping;

import com.poiji.annotation.ExcelCell;
import com.poiji.exception.CastingException;
import com.poiji.exception.ConvertValueException;
import com.poiji.exception.IllegalCastException;
import com.poiji.option.PoijiOptions;
import com.poiji.util.Casting;
import java.lang.reflect.InvocationTargetException;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFComment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.*;

/**
 * This class handles the processing of a .xlsx file,
 * and generates a list of instances of a given type
 *
 * Created by hakan on 22/10/2017
 */
final class PoijiHandler<T> implements SheetContentsHandler {

    private T instance;
    private List<T> dataset;
    private int internalCount;

    private Class<T> type;
    private PoijiOptions options;

    private final Casting casting;

    private final Locale locale;

    private boolean skipRow;

    PoijiHandler(Class<T> type, PoijiOptions options, Locale locale) {
        this.type = type;
        this.options = options;

        casting = Casting.getInstance();
        this.locale = locale;
    }

    List<T> getDataset() {
        return dataset;
    }

    private <T> T newInstanceOf(Class<T> type) {
        T newInstance;
        try {
            newInstance = type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException| InvocationTargetException | IllegalAccessException | InstantiationException e) {
            throw new IllegalCastException("Cannot create a new instance of " + type.getName());
        }

        return newInstance;
    }

    private void setFieldValue(String content, Class<? super T> subclass, int column, int row) {
        if (subclass != Object.class) {
            setValue(content, subclass, column, row);

            setFieldValue(content, subclass.getSuperclass(), column, row);
        }
    }

    private void setValue(String content, Class<? super T> type, int column, int row) {
        for (Field field : type.getDeclaredFields()) {

            ExcelCell index = field.getAnnotation(ExcelCell.class);
            if (index != null) {
                Class<?> fieldType = field.getType();

                if (column == index.value()) {
                    try {
                        Object o = null;
                        o = casting.castValue(fieldType, content, options, locale);
                        try {
                            field.setAccessible(true);
                            field.set(instance, o);
                        } catch (IllegalAccessException e) {
                            throw new IllegalCastException("Unexpected cast type {" + o + "} of field" + field.getName());
                        }
                    } catch (CastingException e) {
                        throw new ConvertValueException(String.format("Cannot convert value. Row: %s. Col: %s", row, column), e, row, column);
                    }
                }
            }
        }
    }

    @Override
    public void startRow(int rowNum) {
        if (rowNum == 1) {
            dataset = new ArrayList<>();
        }
        //when dataFlag is defined, all rows which does not have flag should be ignored
        this.skipRow = this.options.getDataFlag() != null;
    }

    @Override
    public void endRow(int rowNum) {

        if (internalCount != rowNum)
            return;

        if (rowNum + 1 > options.skip() && !this.skipRow) {
            dataset.add(instance);
            instance = null;
        }
    }

    @Override
    public void cell(String cellReference, String formattedValue, XSSFComment comment) {
        CellAddress cellAddress = new CellAddress(cellReference);
        int row = cellAddress.getRow();

        if (row + 1 <= options.skip()) {
            return;
        }

        if (cellAddress.getColumn() == 0) {
            String dataFlag = this.options.getDataFlag();
            String commentFlag = this.options.getCommentFlag();
            if (dataFlag != null || commentFlag != null) {
                if (commentFlag != null && commentFlag.equals(formattedValue)) {
                    this.skipRow = true;
                }
                if (dataFlag != null && dataFlag.equals(formattedValue)) {
                    this.skipRow = false;
                }
            }
        }

        if (row + 1 > options.skip() && !this.skipRow && instance == null) {
            instance = newInstanceOf(type);
        }

        internalCount = row;
        int column = cellAddress.getColumn();
        if (!this.skipRow) {
            setFieldValue(formattedValue, type, column, row);
        }
    }

    @Override
    public void headerFooter(String text, boolean isHeader, String tagName) {
        //no-op
    }
}
