package com.example.user.larper.Model;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by event on 02/04/2017.
 */

final public class StaticProfilesSql {
    final static String TABLE_NAME = "STATICPROFILES";
    final static String PRIMARY_KEY1 = "googleId";
    final static String PRIMARY_KEY2 = "ownerNickName";
    final static String GOOGLEID = "googleId";
    final static String NICKNAME = "nickName";
    final static String OWNER = "ownerNickName";
    public static String curr_owner = "";

    private StaticProfilesSql()
    {
    }

    public static void writeContact(SQLiteDatabase db, StaticProfile contact, StaticProfile owner)
    {
        ContentValues values = new ContentValues();
        values.put(GOOGLEID, contact.getGoogle_id());
        values.put(NICKNAME, contact.getNickname());
        values.put(OWNER, owner.getGoogle_id());
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + GOOGLEID + " TEXT, " + NICKNAME +
                " TEXT, " + OWNER + " TEXT), PRIMARY KEY("
                + PRIMARY_KEY1 + "," + PRIMARY_KEY2 + ")");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }
}
