package com.poiji.bind.mapping;

import com.poiji.annotation.ExcelCell;
import com.poiji.exception.CastingException;
import com.poiji.exception.ConvertValueException;
import com.poiji.exception.IllegalCastException;
import com.poiji.exception.PoijiInstantiationException;
import com.poiji.bind.PoijiWorkbook;
import com.poiji.option.PoijiOptions;
import com.poiji.util.Casting;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.LocaleUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is the main class that converts the excel sheet fromExcel Java object
 * Created by hakan on 16/01/2017.
 */
final class HSSFUnmarshaller extends Unmarshaller {

    private final DataFormatter dataFormatter;
    private final PoijiOptions options;
    private final PoijiWorkbook poijiWorkbook;
    private final Casting casting;
    private final Locale locale;

    HSSFUnmarshaller(final PoijiWorkbook poijiWorkbook, PoijiOptions options, Locale locale) {
        this.poijiWorkbook = poijiWorkbook;
        this.options = options;
        if (locale == null) {
            this.locale = LocaleUtil.getUserLocale();
        } else {
            this.locale = locale;
        }
        this.dataFormatter = new DataFormatter(this.locale);
        this.casting = Casting.getInstance();
    }

    HSSFUnmarshaller(final PoijiWorkbook poijiWorkbook, PoijiOptions options) {
        this(poijiWorkbook, options, null);
    }



    public <T> List<T> unmarshal(Class<T> type) {
        Workbook workbook = poijiWorkbook.workbook();
        Sheet sheet = workbook.getSheetAt(options.sheetIndex());

        int skip = options.skip();
        int maxPhysicalNumberOfRows = sheet.getPhysicalNumberOfRows() + 1 - skip;
        List<T> list;
        list = new ArrayList<T>(maxPhysicalNumberOfRows);

        for (Row currentRow : sheet) {

            if (skip(currentRow, skip))
                continue;

            if (isRowEmpty(currentRow))
                continue;

            if (maxPhysicalNumberOfRows > list.size()) {
                T t = deserialize0(currentRow, type);
                if (t != null) {
                    list.add(t);
                }
            }
        }

        return list;
    }

    private <T> T deserialize0(Row currentRow, Class<T> type) {
        String dataFlag = this.options.getDataFlag();
        String commentFlag = this.options.getCommentFlag();
        if (dataFlag != null || commentFlag != null) {
            Cell firstCell = currentRow.getCell(0);
            String firstCellStringValue = firstCell.getStringCellValue();
            if (commentFlag != null && commentFlag.equals(firstCellStringValue)) {
                return null;
            }
            if (dataFlag != null && !dataFlag.equals(firstCellStringValue)) {
                return null;
            }
        }
        T instance;
        try {
            instance = type.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new PoijiInstantiationException("Cannot create a new instance of " + type.getName());
        } catch (InvocationTargetException e) {
            throw new PoijiInstantiationException("Cannot create a new instance of " + type.getName());
        } catch (IllegalAccessException e) {
            throw new PoijiInstantiationException("Cannot create a new instance of " + type.getName());
        } catch (InstantiationException e) {
            throw new PoijiInstantiationException("Cannot create a new instance of " + type.getName());
        }

        return setFieldValue(currentRow, type, instance);
    }

    private <T> T tailSetFieldValue(Row currentRow, Class<? super T> type, T instance) {
        for (Field field : type.getDeclaredFields()) {

            ExcelCell index = field.getAnnotation(ExcelCell.class);
            if (index != null) {
                Class<?> fieldType = field.getType();
                Cell cell = currentRow.getCell(index.value());
                Object o;
                try {
                    if (cell != null) {
                        String value = dataFormatter.formatCellValue(cell);
                        o = casting.castValue(fieldType, value, options, locale);
                    } else {
                        o = casting.castValue(fieldType, "", options, locale);
                    }
                    try {
                        field.setAccessible(true);
                        field.set(instance, o);
                    } catch (IllegalAccessException e) {
                        throw new IllegalCastException("Unexpected cast type {" + o + "} of field" + field.getName());
                    }
                } catch (CastingException e) {
                    Integer rowNum = currentRow.getRowNum();
                    Integer colNum = cell != null ? cell.getColumnIndex() : null;
                    throw new ConvertValueException(String.format("Cannot convert value. Row: %s. Col: %s", rowNum, colNum), e, rowNum, colNum);
                }
            }
        }
        return instance;
    }

    private <T> T setFieldValue(Row currentRow, Class<? super T> subclass, T instance) {
        return subclass == null
                ? instance
                : tailSetFieldValue(currentRow, subclass, setFieldValue(currentRow, subclass.getSuperclass(), instance));
    }

    private boolean skip(final Row currentRow, int skip) {
        return currentRow.getRowNum() + 1 <= skip;
    }

    private boolean isRowEmpty(Row row) {
        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
            Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && cell.getCellTypeEnum() != CellType.BLANK)
                return false;
        }
        return true;
    }
}
