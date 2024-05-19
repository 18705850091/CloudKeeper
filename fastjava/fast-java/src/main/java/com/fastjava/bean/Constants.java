package com.fastjava.bean;

import com.fastjava.utils.PropertiesUtils;

public class Constants {

    public static String AUTHOR_COMMENT;

    public static Boolean IGNORE_TABLE_PREFIX;

    public static String SUFFIX_BEAN_QUERY;

    public static String SUFFIX_BEAN_QUERY_FUZZY;

    public static String SUFFIX_BEAN_QUERY_TIME_START;

    public static String SUFFIX_BEAN_QUERY_TIME_END;

    public static String SUFFIX_MAPPERS;

    //需要忽略的属性
    public static String IGNORE_BEAN_TOJSON_FIELD;
    public static String IGNORE_BEAN_TOJSON_EXPRESSION;
    public static String IGNORE_BEAN_TOJSON_CLASS;

    //日期序列化，反序列化
    public static String BEAN_DATE_FORMAT_EXPRESSION;
    public static String BEAN_DATE_FORMAT_CLASS;

    public static String BEAN_DATE_UNFORMAT_EXPRESSION;
    public static String BEAN_DATE_UNFORMAT_CLASS;

    private static String PATH_JAVA = "java";

    private static String PATH_RESOURCE = "resources";

    public static String PACKAGE_BASE;

    public static String PACKAGE_PO;

    public static String PACKAGE_QUERY;

    public static String PACKAGE_UTILS;

    public static String PACKAGE_ENUMS;

    public static String PACKAGE_MAPPERS;

    public static String PATH_UTILS;

    public static String PATH_BASE;

    public static String PATH_PO;

    public static String PATH_ENUMS;

    public static String PATH_QUERY;

    public static String PATH_MAPPERS;

    public static String PATH_MAPPERS_XMLS;
    static {

        IGNORE_BEAN_TOJSON_FIELD = PropertiesUtils.getString("ignore.bean.tojson.field");
        IGNORE_BEAN_TOJSON_EXPRESSION = PropertiesUtils.getString("ignore.bean.tojson.expression");
        IGNORE_BEAN_TOJSON_CLASS = PropertiesUtils.getString("ignore.bean.tojson.class");


        BEAN_DATE_FORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.format.expression");
        BEAN_DATE_FORMAT_CLASS = PropertiesUtils.getString("bean.date.format.class");

        BEAN_DATE_UNFORMAT_EXPRESSION = PropertiesUtils.getString("bean.date.unformat.expression");
        BEAN_DATE_UNFORMAT_CLASS = PropertiesUtils.getString("bean.date.unformat.class");

        AUTHOR_COMMENT = PropertiesUtils.getString("author.comment");
        IGNORE_TABLE_PREFIX = Boolean.valueOf(PropertiesUtils.getString("ignore.table.prefix"));
        SUFFIX_BEAN_QUERY = (PropertiesUtils.getString("suffix.bean.query"));

        SUFFIX_BEAN_QUERY_FUZZY = (PropertiesUtils.getString("suffix.bean.query.fuzzy"));
        SUFFIX_BEAN_QUERY_TIME_START = (PropertiesUtils.getString("suffix.bean.query.time.start"));
        SUFFIX_BEAN_QUERY_TIME_END = (PropertiesUtils.getString("suffix.bean.query.time.end"));
        SUFFIX_MAPPERS = (PropertiesUtils.getString("suffix.mappers"));

        PACKAGE_BASE = PropertiesUtils.getString("package.base");
        PACKAGE_PO = PACKAGE_BASE + "." + PropertiesUtils.getString("package.po");
        PACKAGE_QUERY = PACKAGE_BASE + "." + PropertiesUtils.getString("package.query");
        PACKAGE_UTILS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.utils");
        PACKAGE_ENUMS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.enums");
        PACKAGE_MAPPERS = PACKAGE_BASE + "." + PropertiesUtils.getString("package.mappers");

        PATH_BASE = PropertiesUtils.getString("path.base");
        PATH_BASE = PATH_BASE + PATH_JAVA;

        PATH_PO = PATH_BASE + "/" + PACKAGE_PO.replace(".", "/");
        PATH_QUERY = PATH_BASE + "/" + PACKAGE_QUERY.replace(".", "/");
        PATH_UTILS = PATH_BASE + "/" + PACKAGE_UTILS.replace(".", "/");
        PATH_ENUMS = PATH_BASE + "/" + PACKAGE_ENUMS.replace(".", "/");
        PATH_MAPPERS = PATH_BASE + "/" + PACKAGE_MAPPERS.replace(".", "/");

        PATH_MAPPERS_XMLS = PropertiesUtils.getString("path.base") + PATH_RESOURCE;
    }

    public static String[] SQL_DATE_TIME_TYPES = new String[]{"datetime", "timestamp"};

    public static String[] SQL_DATE_TYPES = new String[]{"date"};

    public static String[] SQL_DECIMAL_TYPE = new String[]{"decimal", "double", "float"};

    public static String[] SQL_STRING_TYPE = new String[]{"char", "varchar", "text", "mediumtext", "longtext"};

    public static String[] SQL_INTEGER_TYPE = new String[]{"int", "tinyint"};

    public static String[] SQL_LONG_TYPE = new String[]{"bigint"};

    public static void main(String[] args) {
        System.out.println(PATH_MAPPERS_XMLS);
    }
}
