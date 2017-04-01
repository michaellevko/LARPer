package com.example.user.larper.Model;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by event on 02/04/2017.
 */

public class StaticProfilesSql {
    final static String TABLE_NAME = "STATICPROFILES";
    final static String GOOGLEID = "googleId";
    final static String NICKNAME = "nickName";


    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + GOOGLEID + " TEXT, " + NICKNAME +
                " TEXT)");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }
}
