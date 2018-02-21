package com.poiji.util;

import com.poiji.exception.CastingException;
import com.poiji.option.PoijiOptions;
import com.poiji.option.PoijiOptions.PoijiOptionsBuilder;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CastingTest {

    private Casting casting;

    @Before
    public void setUp() {

        casting = Casting.getInstance();
    }

    @Test
    public void getInstance() {

        assertNotNull(casting);
    }

    @Test
    public void castDate() throws Exception {

        PoijiOptions options = PoijiOptionsBuilder.settings().datePattern("dd.MM.yyyy").build();

        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        Date expectedDate = formatter.parse("05.01.2016");

        Date testDate = (Date) casting.castValue(Date.class, "05.01.2016", options, Locale.GERMANY);

        assertEquals(expectedDate, testDate);
    }

    @Test
    public void castInteger() throws CastingException {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();

        Integer testVal = (Integer) casting.castValue(int.class, "10", options, Locale.GERMANY);

        assertEquals(new Integer(10), testVal);
    }

    @Test
    public void castDouble() throws CastingException {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();

        Double testVal = (Double) casting.castValue(double.class, "81,56891", options, Locale.GERMANY);

        assertEquals(new Double(81.56891), testVal);
    }

    @Test
    public void castDoubleException() {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();

        try {
            Integer value = (Integer) casting.castValue(int.class, "81,56891", options, Locale.GERMANY);
            fail("Exception should be thrown");
        } catch (CastingException e) {
            //OK!
        }
    }

    @Test
    public void castBoolean() throws CastingException {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();

        Boolean testVal = (Boolean) casting.castValue(boolean.class, "True", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "TruE", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "ja", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "Ja", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "j", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "J", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "Y", options, Locale.GERMANY);
        testVal &= (Boolean) casting.castValue(boolean.class, "y", options, Locale.GERMANY);
        testVal &= !(Boolean) casting.castValue(boolean.class, "FalSe", options, Locale.GERMANY);
        testVal &= !(Boolean) casting.castValue(boolean.class, "nein", options, Locale.GERMANY);
        testVal &= !(Boolean) casting.castValue(boolean.class, "n", options, Locale.GERMANY);
        testVal &= !(Boolean) casting.castValue(boolean.class, "N", options, Locale.GERMANY);
        assertEquals(true, testVal);
    }

    @Test
    public void castFloat() throws CastingException {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();

        Float testVal = (Float) casting.castValue(float.class, "81,56891", options, Locale.GERMANY);

        assertEquals(new Float(81.56891), testVal);
    }

    @Test(expected = CastingException.class)
    public void castLongWrongFormat() throws CastingException {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();
        Long testVal = (Long) casting.castValue(int.class, "9223372036854775808", options, Locale.GERMANY);
    }

    @Test
    public void castLong() throws CastingException {

        PoijiOptions options = PoijiOptionsBuilder.settings().build();

        Long testVal = (Long) casting.castValue(long.class, "9223372036854775807", options, Locale.GERMANY);

        assertEquals(new Long("9223372036854775807"), testVal);
    }
}