package com.poiji.bind.mapping;

import com.poiji.exception.PoijiException;
import com.poiji.bind.PoijiFile;
import com.poiji.option.PoijiOptions;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.util.SAXHelper;
import org.apache.poi.xssf.binary.XSSFBSheetHandler;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.poi.xssf.eventusermodel.XSSFReader.*;

/**
 * Created by hakan on 22/10/2017
 */
final class XSSFUnmarshaller extends Unmarshaller {

    private final PoijiFile poijiFile;
    private final PoijiOptions options;
    private final Locale locale;

    XSSFUnmarshaller(PoijiFile poijiFile, PoijiOptions options, Locale locale) {
        this.poijiFile = poijiFile;
        this.options = options;
        if (locale == null) {
            this.locale = LocaleUtil.getUserLocale();
        } else {
            this.locale = locale;
        }
    }

    public XSSFUnmarshaller(PoijiFile poijiFile, PoijiOptions options) {
        this(poijiFile, options, null);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> unmarshal(Class<T> type) {
        OPCPackage open = null;
        try {
            open = OPCPackage.open(poijiFile.file());
            ReadOnlySharedStringsTable readOnlySharedStringsTable = new ReadOnlySharedStringsTable(open);
            XSSFReader xssfReader = new XSSFReader(open);
            StylesTable styles = xssfReader.getStylesTable();

            SheetIterator iter = (SheetIterator) xssfReader.getSheetsData();
            int index = 0;

            while (iter.hasNext()) {
                InputStream stream = null;
                try {
                    stream = iter.next();
                    if (index == options.sheetIndex()) {
                        return processSheet(styles, readOnlySharedStringsTable, type, stream);
                    }
                } finally {
                    if (stream != null) {
                        stream.close();
                    }
                }
                ++index;
            }
            return new ArrayList<T>();
        } catch (SAXException e) {
            throw new PoijiException("Problem occurred while reading data", e);

        } catch (IOException e) {
            throw new PoijiException("Problem occurred while reading data", e);

        } catch (OpenXML4JException e) {
            throw new PoijiException("Problem occurred while reading data", e);

        } finally {
            if (open != null) {
                try {
                    open.close();
                } catch (IOException e) {
                    //noop
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> processSheet(StylesTable styles, ReadOnlySharedStringsTable readOnlySharedStringsTable,
                                     Class<T> type, InputStream sheetInputStream) {

        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = SAXHelper.newXMLReader();
            PoijiHandler poijiHandler = new PoijiHandler(type, options, this.locale);
            ContentHandler contentHandler =
                    new XSSFSheetXMLHandler(styles, null, readOnlySharedStringsTable, poijiHandler, formatter, false);
            sheetParser.setContentHandler(contentHandler);
            sheetParser.parse(sheetSource);
            return poijiHandler.getDataset();
        } catch (ParserConfigurationException e) {
            throw new PoijiException("Problem occurred while reading data", e);
        } catch (SAXException e) {
            throw new PoijiException("Problem occurred while reading data", e);
        } catch (IOException e) {
            throw new PoijiException("Problem occurred while reading data", e);
        }
    }
}
