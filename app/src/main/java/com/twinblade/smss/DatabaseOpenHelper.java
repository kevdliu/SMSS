package com.twinblade.smss;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseOpenHelper extends SQLiteOpenHelper {

    private static final String CMD_CREATE_TABLE_MSG = "CREATE TABLE "
            + Constants.TABLE_MSG + " ( "
            + Constants.MSG_COLUMN_ID + " integer primary key autoincrement, "
            + Constants.MSG_COLUMN_CONTACT + " text not null, "
            + Constants.MSG_COLUMN_TIME + " integer not null, "
            + Constants.MSG_COLUMN_STATUS + " integer not null, "
            + Constants.MSG_COLUMN_BODY + " text not null "
            + ");";

    private static final String CMD_CREATE_INDEX_MSG = "CREATE INDEX "
            + "IX_" + Constants.TABLE_MSG + " ON " + Constants.TABLE_MSG
            + " ( " + Constants.KEY_COLUMN_CONTACT + " );";

    private static final String CMD_CREATE_TABLE_KEYS = "CREATE TABLE "
            + Constants.TABLE_KEY + " ( "
            + Constants.KEY_COLUMN_ID + " integer primary key autoincrement, "
            + Constants.KEY_COLUMN_CONTACT + " text not null, "
            + Constants.KEY_COLUMN_PUBLIC + " text not null, "
            + Constants.KEY_COLUMN_PRIVATE + " text not null "
            + ");";

    public DatabaseOpenHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CMD_CREATE_TABLE_MSG);
        database.execSQL(CMD_CREATE_INDEX_MSG);
        database.execSQL(CMD_CREATE_TABLE_KEYS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_MSG);
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_KEY);
        onCreate(db);
    }
}
