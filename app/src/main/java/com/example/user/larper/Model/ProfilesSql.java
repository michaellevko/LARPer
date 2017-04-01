package com.example.user.larper.Model;

import com.example.user.larper.Model.Skill;
import android.content.ContentValues;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by event on 02/04/2017.
 */

public class ProfilesSql {

    final static String TABLE_NAME = "PROFILE";
    final static String UUID = "uuid";
    final static String NICKNAME = "nickName";
    final static String AGE = "age";
    final static String GENDER = "gender";
    final static String SCENARIOCLASS = "scenarioClass";
    final static String RACE = "race";
    final static String HITPOINTS = "hitPoints";
    final static String SKILLS = "skills";
    final static String BIOGRAPHY = "biography";

    public static void addProfile(SQLiteDatabase writableDatabase, Profile profile) {
        ContentValues values = new ContentValues();

        values.put(UUID, profile.getUuid());
        values.put(NICKNAME, profile.getNickName());
        values.put(AGE, profile.getAge());
        values.put(GENDER, profile.getGender());
        values.put(SCENARIOCLASS, profile.getScenarioClass());
        values.put(RACE, profile.getRace());
        values.put(HITPOINTS, profile.getHitPoints());
        values.put(SKILLS, new Gson().toJson(profile.getSkills()));
        values.put(BIOGRAPHY, profile.getBiography());

        long rowId = writableDatabase.insert(TABLE_NAME, UUID, values);
        if (rowId <= 0) {
            Log.e("TAG","fail to insert into profile");
        }
    }

    public static Profile getProfileByUuid(SQLiteDatabase readableDatabase, String uid) {
        String[] selectionArgs = {uid};
        Cursor cursor = readableDatabase.query(TABLE_NAME,null, UUID + " = ?",selectionArgs ,null,null,null);
        Profile profile = null;
        if (cursor.moveToFirst() == true){

             String uuid = cursor.getString(cursor.getColumnIndex(UUID));
             String nickName = cursor.getString(cursor.getColumnIndex(NICKNAME));
             String age = cursor.getString(cursor.getColumnIndex(AGE));
             String gender = cursor.getString(cursor.getColumnIndex(GENDER));
             String scenarioClass = cursor.getString(cursor.getColumnIndex(SCENARIOCLASS));
             String race = cursor.getString(cursor.getColumnIndex(RACE));
             String hitPoints = cursor.getString(cursor.getColumnIndex(HITPOINTS));
             String skills = cursor.getString(cursor.getColumnIndex(SKILLS));
             String biography = cursor.getString(cursor.getColumnIndex(BIOGRAPHY));

             profile = new Profile(nickName,
                                   Integer.parseInt(age),
                                   gender,
                                   scenarioClass,
                                   race,
                                   Integer.parseInt(hitPoints),
                                   (ArrayList<Skill>)new Gson().fromJson(skills,
                                           new TypeToken<ArrayList<Skill>>(){}.getType()),
                                   biography);
        }

        return profile;
    }

    public static List<Profile> GetAllProfiles(SQLiteDatabase readableDatabase){
        Profile profile = null;
        Cursor cursor;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        cursor = readableDatabase.rawQuery(selectQuery, null);

        List<Profile> profiles = new ArrayList<Profile>();

        if (cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex(UUID));
                String nickName = cursor.getString(cursor.getColumnIndex(NICKNAME));
                String age = cursor.getString(cursor.getColumnIndex(AGE));
                String gender = cursor.getString(cursor.getColumnIndex(GENDER));
                String scenarioClass = cursor.getString(cursor.getColumnIndex(SCENARIOCLASS));
                String race = cursor.getString(cursor.getColumnIndex(RACE));
                String hitPoints = cursor.getString(cursor.getColumnIndex(HITPOINTS));
                String skills = cursor.getString(cursor.getColumnIndex(SKILLS));
                String biography = cursor.getString(cursor.getColumnIndex(BIOGRAPHY));

                profile = new Profile(nickName,
                        Integer.parseInt(age),
                        gender,
                        scenarioClass,
                        race,
                        Integer.parseInt(hitPoints),
                        (ArrayList<Skill>)new Gson().fromJson(skills,
                                new TypeToken<ArrayList<Skill>>(){}.getType()),
                        biography);
                profiles.add(profile);
            }
            while (cursor.moveToNext());
        }
        return profiles;
    }

    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + UUID + " TEXT, " + NICKNAME +
                " TEXT, " + AGE + " TEXT, " + GENDER +
                " TEXT, "  + SCENARIOCLASS + " TEXT, " + RACE +
                " TEXT, " + HITPOINTS + " TEXT, " + SKILLS + " TEXT, " +
                BIOGRAPHY + " TEXT)");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }

    private static String[] getColumns(){
        String[] columns = {UUID,NICKNAME,AGE,GENDER,SCENARIOCLASS,RACE,HITPOINTS,SKILLS, BIOGRAPHY};
        return columns;
    }
}