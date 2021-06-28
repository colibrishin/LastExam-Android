package com.test.lastexam_20151652;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    public static final String DB_NAME = "MemoDB";
    public static final String TABLE_NAME = "Memo";
    public static final int DB_VERSION = 1;

    public DBManager(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String query = "create table " + TABLE_NAME + " (hash text, path text, date text, title text, description text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
