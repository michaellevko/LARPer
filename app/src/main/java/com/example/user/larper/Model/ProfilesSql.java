package com.example.user.larper.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by event on 02/04/2017.
 */

public class ProfilesSql {

    final static String TABLE_NAME = "PROFILE";
    final static String NICKNAME = "nickName";
    final static String AGE = "age";
    final static String GENDER = "gender";
    final static String SCENARIOCLASS = "scenarioClass";
    final static String RACE = "race";
    final static String HITPOINTS = "hitPoints";
    final static String SKILLS = "skills";
    final static String BIOGRAPHY = "biography";
    final static String OWNER = "owner";

    public static void addProfile(SQLiteDatabase writableDatabase, Profile profile) {
        ContentValues values = new ContentValues();

        values.put(OWNER, StaticProfilesSql.curr_owner.toString());
        values.put(NICKNAME, profile.getNickName());
        values.put(AGE, profile.getAge());
        values.put(GENDER, profile.getGender());
        values.put(SCENARIOCLASS, profile.getScenarioClass());
        values.put(RACE, profile.getRace());
        values.put(HITPOINTS, profile.getHitPoints());
        values.put(SKILLS, new Gson().toJson(profile.getSkills()));
        values.put(BIOGRAPHY, profile.getBiography());

        long rowId = writableDatabase.insertWithOnConflict(TABLE_NAME, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
        if (rowId <= 0) {
            Log.e("TAG","fail to insert into profile");
        }
    }

    public static ArrayList<Profile> GetAllProfilesByOwner(SQLiteDatabase readableDatabase){
        String[] selectionArgs = {StaticProfilesSql.curr_owner.toString()};
        Profile profile = null;
        Cursor cursor = readableDatabase.query(TABLE_NAME,null, OWNER + " = ?",selectionArgs ,null,null,null);

        ArrayList<Profile> profiles = new ArrayList<Profile>();

        if (cursor.moveToFirst()){
            do {
                String nickName = cursor.getString(cursor.getColumnIndex(NICKNAME));
                String age = cursor.getString(cursor.getColumnIndex(AGE));
                String gender = cursor.getString(cursor.getColumnIndex(GENDER));
                String scenarioClass = cursor.getString(cursor.getColumnIndex(SCENARIOCLASS));
                String race = cursor.getString(cursor.getColumnIndex(RACE));
                String hitPoints = cursor.getString(cursor.getColumnIndex(HITPOINTS));
                String skills = cursor.getString(cursor.getColumnIndex(SKILLS));
                String biography = cursor.getString(cursor.getColumnIndex(BIOGRAPHY));

                profile = new Profile(nickName, age, gender, race, scenarioClass, biography, hitPoints,
                        (ArrayList<Skill>)new Gson().fromJson(skills,
                                new TypeToken<ArrayList<Skill>>(){}.getType()));
                profiles.add(profile);
            }
            while (cursor.moveToNext());
        }
        return profiles;
    }

    public static void deleteLore(SQLiteDatabase writableDatabase, Profile profile) {
        String owner = StaticProfilesSql.curr_owner.toString();
        String query = "SELECT rowid,* FROM " + TABLE_NAME + " WHERE " + NICKNAME + " = '" + profile.getNickName() +
                "' AND " + AGE + " = '" + profile.getAge() + "' AND " + GENDER + " = '" +
                profile.getGender() + "' AND "+ SCENARIOCLASS + " = '" + profile.getClass() + "' AND " +
                RACE + " = '" + profile.getRace() + "' AND " + HITPOINTS + " = '" + profile.getHitPoints() + "'";
        Cursor cursor = writableDatabase.rawQuery(query, null);

        try {
            if (cursor.moveToFirst()) {
                String rowId = ((Long)(cursor.getLong(cursor.getColumnIndex("rowid")))).toString();
                writableDatabase.delete(TABLE_NAME, "rowid = ? ", new String[]{rowId});

            }
        }
        catch(Exception e)
        {
            Log.d("tag", "error");
        }
    }

    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + OWNER + " TEXT, " + NICKNAME +
                " TEXT, " + AGE + " TEXT, " + GENDER +
                " TEXT, "  + SCENARIOCLASS + " TEXT, " + RACE +
                " TEXT, " + HITPOINTS + " TEXT, " + SKILLS + " TEXT, " +
                BIOGRAPHY + " TEXT)");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }

    private static String[] getColumns(){
        String[] columns = {OWNER,NICKNAME,AGE,GENDER,SCENARIOCLASS,RACE,HITPOINTS,SKILLS, BIOGRAPHY};
        return columns;
    }
}