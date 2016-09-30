package com.twinblade.smss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataHelper {

    private SQLiteDatabase mDatabase;
    private DatabaseOpenHelper mDatabaseOpenHelper;

    public DataHelper(Context context) {
        mDatabaseOpenHelper = new DatabaseOpenHelper(context);
    }

    public Key createKey(String contact, String publicKey, String privateKey) {
        ContentValues values = new ContentValues();
        values.put(Constants.KEY_COLUMN_CONTACT, contact);
        values.put(Constants.KEY_COLUMN_PUBLIC, publicKey);
        values.put(Constants.KEY_COLUMN_PRIVATE, privateKey);

        long insertId = mDatabase.insert(Constants.TABLE_KEY, null, values);
        Cursor cursor = mDatabase.query(Constants.TABLE_KEY,
                Constants.KEY_COLUMNS, Constants.KEY_COLUMN_ID + " = " + insertId,
                null, null, null, null, "1");
        cursor.moveToFirst();
        Key newKey = cursorToKey(cursor);
        cursor.close();
        return newKey;
    }

    public Message createMessage(String contact, String body, int status) {
        ContentValues values = new ContentValues();
        values.put(Constants.MSG_COLUMN_CONTACT, contact);
        values.put(Constants.MSG_COLUMN_BODY, body);
        values.put(Constants.MSG_COLUMN_TIME, System.currentTimeMillis());
        values.put(Constants.MSG_COLUMN_STATUS, status);

        long insertId = mDatabase.insert(Constants.TABLE_MSG, null, values);
        Cursor cursor = mDatabase.query(Constants.TABLE_MSG,
                Constants.MSG_COLUMNS, Constants.MSG_COLUMN_ID + " = " + insertId,
                null, null, null, null, "1");
        cursor.moveToFirst();
        Message newMessage = cursorToMessage(cursor);
        cursor.close();
        return newMessage;
    }

    public void deleteMessage(Message message) {
        long id = message.getId();
        mDatabase.delete(Constants.TABLE_MSG,
                Constants.MSG_COLUMN_ID + " = " + id, null);
    }

    public void deleteKey(Key key) {
        long id = key.getId();
        mDatabase.delete(Constants.TABLE_KEY,
                Constants.KEY_COLUMN_ID + " = " + id, null);
    }

    public List<Message> getMessagesForContact(String contact) {
        List<Message> messages = new ArrayList<Message>();

        Cursor cursor = mDatabase.query(Constants.TABLE_MSG,
                Constants.MSG_COLUMNS, Constants.MSG_COLUMN_CONTACT + " = " + contact,
                null, null, null, Constants.MSG_COLUMN_TIME, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = cursorToMessage(cursor);
            messages.add(message);
            cursor.moveToNext();
        }

        cursor.close();
        return messages;
    }

    public Key getKeyForContact(String contact) {
        Key key = null;

        Cursor cursor = mDatabase.query(Constants.TABLE_KEY,
                Constants.KEY_COLUMNS, Constants.KEY_COLUMN_CONTACT + " = " + contact,
                null, null, null, null, "1");

        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            key = cursorToKey(cursor);
        }

        cursor.close();
        return key;
    }

    private Key cursorToKey(Cursor cursor) {
        return new Key(cursor.getLong(cursor.getColumnIndex(Constants.KEY_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_COLUMN_CONTACT)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_COLUMN_PUBLIC)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_COLUMN_PRIVATE)));
    }

    private Message cursorToMessage(Cursor cursor) {
        return new Message(cursor.getLong(cursor.getColumnIndex(Constants.MSG_COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Constants.MSG_COLUMN_CONTACT)),
                cursor.getLong(cursor.getColumnIndex(Constants.MSG_COLUMN_TIME)),
                cursor.getInt(cursor.getColumnIndex(Constants.MSG_COLUMN_STATUS)),
                cursor.getString(cursor.getColumnIndex(Constants.MSG_COLUMN_BODY)));
    }

    public void open() throws SQLException {
        mDatabase = mDatabaseOpenHelper.getWritableDatabase();
    }

    public void close() {
        mDatabaseOpenHelper.close();
    }
}
