package com.twinblade.smss;

public class Constants {

    public static final int RSA_KEY_LENTH = 1024;

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

    public static final int MSG_STATUS_SENDING = 0;
    public static final int MSG_STATUS_SENT = 1;
    public static final int MSG_STATUS_RECEIVED = 2;
    public static final int MSG_STATUS_ERROR = 3;

    public static final String TABLE_KEY = "keys";
    public static final String KEY_COLUMN_ID = "_id";
    public static final String KEY_COLUMN_CONTACT = "contact";
    public static final String KEY_COLUMN_PUBLIC = "public";
    public static final String KEY_COLUMN_PRIVATE = "private";
    public static final String[] KEY_COLUMNS = {KEY_COLUMN_ID, KEY_COLUMN_CONTACT,
            KEY_COLUMN_PRIVATE, KEY_COLUMN_PUBLIC};
}
