package com.example.user.larper.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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
    public static StaticProfile curr_owner;

    private StaticProfilesSql()
    {
    }

    public static void writeContact(SQLiteDatabase db, StaticProfile contact)
    {
        ContentValues values = new ContentValues();
        values.put(GOOGLEID, contact.getGoogle_id());
        values.put(NICKNAME, contact.getNickname());
        values.put(OWNER, curr_owner.getNickname());
        db.insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static ArrayList<StaticProfile> getContactsByOwner(SQLiteDatabase db)
    {
        StaticProfile profile = null;
        Cursor cursor;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE "
                             + OWNER + " = " + "'" + curr_owner + "'";
        cursor = db.rawQuery(selectQuery, null);

        ArrayList<StaticProfile> profiles = new ArrayList<StaticProfile>();

        if (cursor.moveToFirst()){
            do {
                String nickName = cursor.getString(cursor.getColumnIndex(NICKNAME));
                String google_id = cursor.getString(cursor.getColumnIndex(GOOGLEID));

                profile = new StaticProfile(nickName, google_id);
                profiles.add(profile);
            }
            while (cursor.moveToNext());
        }

        return profiles;
    }


    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + GOOGLEID + " TEXT, " + NICKNAME +
                " TEXT, " + OWNER + " TEXT, PRIMARY KEY("
                + PRIMARY_KEY1 + "," + PRIMARY_KEY2 + "))");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }
}
