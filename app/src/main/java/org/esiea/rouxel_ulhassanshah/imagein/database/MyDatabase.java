package org.esiea.rouxel_ulhassanshah.imagein.database;

/**
 * Created by bachi on 30/12/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MyDatabase extends SQLiteOpenHelper {

    private static final String TABLE_DATES = "TABLE_DATES";
    private static final String COL_ID = "ID";
    private static final String COL_DATE = "DATE";

    private static final String CREATE_BDD = "CREATE TABLE " + TABLE_DATES + " ("
            + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_DATE + " TEXT);";

    private SQLiteDatabase db;

    public MyDatabase(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_BDD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_DATES + ";");
        onCreate(db);
    }

    public void open(){
        db = this.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public long insertCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ROOT);
        Date date = new Date();

        ContentValues values = new ContentValues();

        String dateString = dateFormat.format(date);

        values.put(COL_DATE, dateString);

        return db.insert(TABLE_DATES, null, values);
    }

    public List<String> getDates() {
        List<String> dateList = new ArrayList<String>();
        String selectQuery = "SELECT " + COL_DATE + " FROM " + TABLE_DATES + " ORDER BY " + COL_DATE + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                dateList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return dateList;
    }
}