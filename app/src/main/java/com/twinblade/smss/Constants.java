package com.twinblade.smss;

public class Constants {

    public static final int RSA_KEY_LENTH = 1024;
    public static final short DATA_SMS_PORT = 6969;

    public static final String DATABASE_NAME = "smss.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_MSG = "msg";
    public static final String MSG_COLUMN_ID = "_id";
    public static final String MSG_COLUMN_CONTACT = "contact";
    public static final String MSG_COLUMN_STATUS = "status";
    public static final String MSG_COLUMN_TIME = "time";
    public static final String MSG_COLUMN_BODY = "body";
    public static final String[] MSG_COLUMNS = {MSG_COLUMN_ID, MSG_COLUMN_CONTACT,
            MSG_COLUMN_STATUS, MSG_COLUMN_TIME, MSG_COLUMN_BODY};

    public static final String TABLE_KEY = "keys";
    public static final String KEY_COLUMN_ID = "_id";
    public static final String KEY_COLUMN_CONTACT = "contact";
    public static final String KEY_COLUMN_PUBLIC = "public";
    public static final String KEY_COLUMN_PRIVATE = "private";
    public static final String[] KEY_COLUMNS = {KEY_COLUMN_ID, KEY_COLUMN_CONTACT,
            KEY_COLUMN_PRIVATE, KEY_COLUMN_PUBLIC};

    public static final String INTENT_NEW_MESSAGE_RECEIVED = "com.twinblade.smss.NEW_MESSAGE_RECEIVED";
}
