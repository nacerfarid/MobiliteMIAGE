package com.example.nacerfarid.finaltest.Utils.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dataBaseTAG extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "TAG_DB";
    private static final int DATABASE_VERSION = 3;

    private static final String CREATE_TABLE_ARRET =
            "CREATE TABLE ARRETS (" +
                    "id TEXT," +
                    "shortName TEXT," +
                    "label TEXT," +
                    "color TEXT," +
                    "code TEXT," +
                    "textColor TEXT);";
    private static final String CREATE_TABLE_LAST_VISITED =
            "CREATE TABLE LAST_VISITED (" +
                    "id TEXT," +
                    "shortName TEXT," +
                    "label TEXT," +
                    "color TEXT," +
                    "code TEXT," +
                    "textColor TEXT," +
                    "lastVisitedTime TEXT);";

    private static final String DELETE_TABLE_ARRET =
            "DROP TABLE IF EXISTS ARRETS";
    private static final String DELETE_TABLE_LAST_VISITED =
            "DROP TABLE IF EXISTS LASTUPDATE";


    public dataBaseTAG(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(final SQLiteDatabase db) {
        //db.execSQL(DELETE_TABLE_ARRET);
        //db.execSQL(DELETE_TABLE_LAST_VISITED);
        db.execSQL(CREATE_TABLE_ARRET);
        db.execSQL(CREATE_TABLE_LAST_VISITED);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE_ARRET);
        db.execSQL(DELETE_TABLE_LAST_VISITED);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db,newVersion,oldVersion);
    }

    public void insertToFavorite(SQLiteDatabase db, String shortName, String arretLabel, String color, String txtColor, String arretCode) {
        Cursor cursor = null;
        String sql = "SELECT * FROM ARRETS WHERE shortName = ? AND label =?;";
        cursor = db.rawQuery(sql,new String[]{shortName,arretLabel});
        if(cursor.getCount()>0){

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("shortName", shortName);
            contentValues.put("label", arretLabel);
            contentValues.put("color", color);
            contentValues.put("textColor", txtColor);
            contentValues.put("code", arretCode);

            db.insert("ARRETS", null, contentValues);
        }
        cursor.close();
    }


    public void removeFromFavorite(SQLiteDatabase db, String shortName, String arretLabel) {
        db.delete("ARRETS","shortName = ? AND label = ?",new String[]{shortName,arretLabel});
    }

    public void insertToLastVisited(SQLiteDatabase db, String shortName, String arretLabel, String color, String txtColor, String arretCode, String lastVisited) {
        Cursor cursor = null;
        String sql = "SELECT * FROM LAST_VISITED WHERE shortName = ? AND label = ? ORDER BY lastVisitedTime DESC;";
        cursor = db.rawQuery(sql,new String[]{shortName,arretLabel});
        if(cursor.getCount()>0){
            ContentValues contentValues = new ContentValues();
            contentValues.put("shortName", shortName);
            contentValues.put("label", arretLabel);
            contentValues.put("color", color);
            contentValues.put("textColor", txtColor);
            contentValues.put("code", arretCode);
            contentValues.put("lastVisitedTime",lastVisited);
            db.update("LAST_VISITED",contentValues,"shortName = ? AND label = ?",new String[]{shortName,arretLabel});
            if(cursor.getCount()>10){
                String sql1 = "DELETE FROM LAST_VISITED WHERE shortName IN (SELECT TOP (1) shortName FROM LAST_VISITED ORDER BY lastVisitedTime DESC);";
                db.execSQL(sql1);
            }

        }else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("shortName", shortName);
            contentValues.put("label", arretLabel);
            contentValues.put("color", color);
            contentValues.put("textColor", txtColor);
            contentValues.put("code", arretCode);
            contentValues.put("lastVisitedTime",lastVisited);

            db.insert("LAST_VISITED", null, contentValues);
        }
        cursor.close();
    }


    public void removeFromLastVisited(SQLiteDatabase db, String shortName, String arretLabel) {
        db.delete("LAST_VISITED","shortName = ? AND label = ?",new String[]{shortName,arretLabel});
    }
}