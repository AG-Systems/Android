package com.example.root.chrono;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 3/9/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    // String name, SQLiteDatabase.CursorFactory factory, int version
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);

    }
    public static final String DATABASE_NAME = "main.db";
    public static final String TABLE_NAME = "main_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "WORKX";
    public static final String COL_3 = "WORKY";
    public static final String COL_4 = "HOMEX";
    public static final String COL_5 = "HOMEY";
    public static final String COL_6 = "TIME";
    public static final String COL_7 = "HOURS";
    /*

    public static final String COL_8 = "WORKTIME";
    public static final String COL_9 = "HOMETIME";
     */

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, WORKX TEXT, WORKY TEXT, HOMEX TEXT, HOMEY TEXT, TIME TEXT, HOURS INTEGER) ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
    public boolean insertData(String workx, String worky, String homex, String homey, String time, String hours)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,workx);
        contentValues.put(COL_3,worky);
        contentValues.put(COL_4,homex);
        contentValues.put(COL_5,homey);
        contentValues.put(COL_6,time);
        contentValues.put(COL_7,hours);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if (result == -1)
        {
            return false;
        }
        else
            return true;
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME,null);
        return res;
    }
    public boolean updateData(String id, String workx, String worky, String homex, String homey, String time, String hours)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,workx);
        contentValues.put(COL_3,worky);
        contentValues.put(COL_4,homex);
        contentValues.put(COL_5,homey);
        contentValues.put(COL_6,time);
        contentValues.put(COL_7,hours);
        db.update(TABLE_NAME, contentValues, "id = ?",new String[] { id } );
        return true;
    }
}
