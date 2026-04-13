package com.example.fixitnow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "FixItDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "bookings";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE + " (" +
                "id      INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name    TEXT NOT NULL," +
                "phone   TEXT NOT NULL," +
                "service TEXT NOT NULL," +
                "date    TEXT NOT NULL," +
                "address TEXT NOT NULL," +
                "status  TEXT DEFAULT 'Confirmed')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public long insertBooking(String name, String phone, String service, String date, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",    name);
        values.put("phone",   phone);
        values.put("service", service);
        values.put("date",    date);
        values.put("address", address);
        long id = db.insert(TABLE, null, values);
        db.close();
        return id;
    }

    public ArrayList<HashMap<String, String>> getAllBookings() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE + " ORDER BY id DESC", null);
            while (cursor.moveToNext()) {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",      cursor.getString(0));
                map.put("name",    cursor.getString(1));
                map.put("phone",   cursor.getString(2));
                map.put("service", cursor.getString(3));
                map.put("date",    cursor.getString(4));
                map.put("address", cursor.getString(5));
                map.put("status",  cursor.getString(6));
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }
}