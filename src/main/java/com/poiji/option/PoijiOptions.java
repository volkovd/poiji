package com.poiji.option;

import static com.poiji.util.PoijiConstants.DEFAULT_DATE_PATTERN;

/**
 * Created by hakan on 17/01/2017.
 */
public final class PoijiOptions {

    private int skip;
    private String datePattern;
    private boolean preferNullOverDefault = true;
    private int sheetIndex;
    private String dataFlag;
    private String commentFlag;

    private PoijiOptions() {
        super();
    }

    private PoijiOptions setSkip(int skip) {
        this.skip = skip;
        return this;
    }

    private PoijiOptions setDatePattern(String datePattern) {
        this.datePattern = datePattern;
        return this;
    }

    private PoijiOptions setPreferNullOverDefault(boolean preferNullOverDefault) {
        this.preferNullOverDefault = preferNullOverDefault;
        return this;
    }

    private PoijiOptions setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
        return this;
    }

    public int sheetIndex() {
        return sheetIndex;
    }

    public String datePattern() {
        return datePattern;
    }

    public boolean preferNullOverDefault() {
        return preferNullOverDefault;
    }

    /**
     * the number of skipped rows
     *
     * @return n rows skipped
     */
    public int skip() {
        return skip;
    }

    /**
     * Text in first column used to mark data rows.
     * If null all rows will be used as data except rows marked as comment.
     * @return
     */
    public String getDataFlag() {
        return dataFlag;
    }

    public PoijiOptions setDataFlag(String dataFlag) {
        this.dataFlag = dataFlag;
        return this;
    }

    /**
     * Text in first column used to mark row as comment.
     * @return
     */
    public String getCommentFlag() {
        return commentFlag;
    }

    public PoijiOptions setCommentFlag(String commentFlag) {
        this.commentFlag = commentFlag;
        return this;
    }

    public static class PoijiOptionsBuilder {

        private int skip = 1;
        private String datePattern = DEFAULT_DATE_PATTERN;
        private boolean preferNullOverDefault = false;
        private int sheetIndex;
        private String dataFlag;
        private String commentFlag;

        private PoijiOptionsBuilder() {
        }

        private PoijiOptionsBuilder(int skip) {
            this.skip = skip;
        }

        public PoijiOptions build() {
            return new PoijiOptions()
                    .setSkip(skip)
                    .setPreferNullOverDefault(preferNullOverDefault)
                    .setDatePattern(datePattern)
                    .setSheetIndex(sheetIndex)
                    .setDataFlag(dataFlag)
                    .setCommentFlag(commentFlag);
        }

        public static PoijiOptionsBuilder settings() {
            return new PoijiOptionsBuilder();
        }

        /**
         * set date pattern, default pattern is dd/M/yyyy
         *
         * @param datePattern date pattern
         * @return this
         */
        public PoijiOptionsBuilder datePattern(String datePattern) {
            this.datePattern = datePattern;
            return this;
        }

        /**
         * set whether or not to use null instead of default object
         *
         * @param preferNullOverDefault boolean
         * @return this
         */
        public PoijiOptionsBuilder preferNullOverDefault(boolean preferNullOverDefault) {
            this.preferNullOverDefault = preferNullOverDefault;
            return this;
        }

        /**
         * skip number of row
         *
         * @param skip number
         * @return this
         */
        public PoijiOptionsBuilder skip(int skip) {
            this.skip = skip;
            return this;
        }

        /**
         * set sheet index, default is 0
         *
         * @param sheetIndex number
         * @return this
         */
        public PoijiOptionsBuilder sheetIndex(int sheetIndex) {
            this.sheetIndex = sheetIndex;
            return this;
        }

        public PoijiOptionsBuilder dataFlag(String dataFlag) {
            this.dataFlag = dataFlag;
            return this;
        }

        public PoijiOptionsBuilder commentFlag(String commentFlag) {
            this.commentFlag = commentFlag;
            return this;
        }


        /**
         * Skip the n rows of the excel data. Default is 1
         *
         * @param skip ignored row number
         * @return builder itself
         */
        public static PoijiOptionsBuilder settings(int skip) {
            return new PoijiOptionsBuilder(skip);
        }

    }
}
