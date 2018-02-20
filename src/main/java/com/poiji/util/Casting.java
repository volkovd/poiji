package com.poiji.util;

import com.poiji.exception.CastingException;
import com.poiji.option.PoijiOptions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by hakan on 22/01/2017.
 */
public final class Casting {

    private static final Casting instance = new Casting();

    public static Casting getInstance() {
        return instance;
    }

    private Casting() {
    }

    private Integer integerValue(String value, Locale locale) throws CastingException {
        try {
            if (isValueEmpty(value)) {
                return null;
            } else {
                return parseNumber(value, locale).intValue();
            }
        } catch (ParseException e) {
            throw new CastingException(e);
        }
    }

    private Number parseNumber(String value, Locale locale) throws ParseException {
        NumberFormat numberFormat = NumberFormat.getInstance(locale);
        return numberFormat.parse(value);
    }

    private Long longValue(String value, Locale locale) throws CastingException {
        try {
            if (isValueEmpty(value)) {
                return null;
            } else {
                return parseNumber(value, locale).longValue();
            }
        } catch (ParseException e) {
            throw new CastingException(e);
        }
    }

    private Double doubleValue(String value, Locale locale) throws CastingException {
        try {
            if (isValueEmpty(value)) {
                return null;
            } else {
                return parseNumber(value, locale).doubleValue();
            }
        } catch (ParseException e) {
            throw new CastingException(e);
        }
    }

    private Float floatValue(String value, Locale locale) throws CastingException {
        try {
            if (isValueEmpty(value)) {
                return null;
            } else {
                return parseNumber(value, locale).floatValue();
            }
        } catch (ParseException e) {
            throw new CastingException(e);
        }
    }

    private Date dateValue(String value, PoijiOptions options, Locale locale) throws CastingException {
        try {
            if (isValueEmpty(value)) {
                return null;
            }
            final SimpleDateFormat sdf = new SimpleDateFormat(options.datePattern());
            return sdf.parse(value);
        } catch (ParseException e) {
            throw new CastingException(e);
        }
    }

    public Object castValue(Class<?> fieldType, String value, PoijiOptions options, Locale locale) throws CastingException {
        Object o;
        if (fieldType.equals(Integer.class) || fieldType.equals(Integer.TYPE)) {
            o = integerValue(value, locale);
        } else if (fieldType.equals(Long.class) || fieldType.equals(Long.TYPE)) {
            o = longValue(value, locale);
        } else if (fieldType.equals(Double.class) || fieldType.equals(Double.TYPE)) {
            o = doubleValue(value, locale);
        } else if (fieldType.equals(Float.class) || fieldType.equals(Float.TYPE)) {
            o = floatValue(value, locale);
        } else if (fieldType.equals(Boolean.class) || fieldType.equals(Boolean.TYPE)) {
            o = booleanValue(value, locale);
        } else if (fieldType.equals(Date.class)) {
            o = dateValue(value, options, locale);
        } else
            o = value;
        return o;
    }

    private Object booleanValue(String value, Locale locale) throws CastingException {
        if (isValueEmpty(value)) {
            return null;
        } else {
            if ("true".equalsIgnoreCase(value) || "y".equalsIgnoreCase(value) || "j".equalsIgnoreCase(value) || "yes".equalsIgnoreCase(value) || "ja".equalsIgnoreCase(value)) {
                return true;
            } else if ("false".equalsIgnoreCase(value) || "n".equalsIgnoreCase(value) || "no".equalsIgnoreCase(value) || "nein".equalsIgnoreCase(value)) {
                return false;
            } else {
                throw new CastingException(String.format("Cannot parse \"%s\" as boolean", value));
            }
        }
    }

    private boolean isValueEmpty(String value) {
        if (value == null || "".equals(value.trim())) {
            return true;
        } else {
            return false;
        }
    }
}
