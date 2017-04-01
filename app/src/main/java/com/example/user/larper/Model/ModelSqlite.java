package com.example.user.larper.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.user.larper.LARPer;

import java.util.List;

/**
 * Created by event on 01/04/2017.
 */

public class ModelSqlite {
    private SQLiteOpenHelper profilesHelper;
    private SQLiteOpenHelper blueprintsHelper;
    private SQLiteOpenHelper staticProfileHelper;
    private int version = 1;
    private static ModelSqlite modelSql;

    public ModelSqlite(){
        try {
            profilesHelper = new ProfilesHelper(LARPer.getAppContext());
            blueprintsHelper = new BlueprintsHelper(LARPer.getAppContext());
            staticProfileHelper = new StaticProfilesHelper(LARPer.getAppContext());
        }
        catch(Exception e){
            Log.d("exception", "exception while trying to install the local db " + e.getMessage());
        }
    }

    public static ModelSqlite getInstance(){
        if(modelSql == null)
            modelSql = new ModelSqlite();

        return modelSql;
    }

    public void createProfile(Profile profile)
    {
        try {
            ProfilesSql.addProfile(profilesHelper.getWritableDatabase(), profile);
        }
        catch (Exception e)
        {
            Log.d("exception:" , "exception while trying add profile to local db " + e.getMessage());
        }
    }

    public Profile getProfileByUID(String uuid){
        return ProfilesSql.getProfileByUuid(profilesHelper.getReadableDatabase(), uuid);
    }

    public List<Profile> getAllProfiles(boolean isCreated) {
        return ProfilesSql.GetAllProfiles(profilesHelper.getReadableDatabase());
    }


    class ProfilesHelper extends SQLiteOpenHelper{


        public ProfilesHelper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            ProfilesSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            ProfilesSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }

    class BlueprintsHelper extends SQLiteOpenHelper{


        public BlueprintsHelper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            BlueprintsSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            BlueprintsSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }

    class StaticProfilesHelper extends SQLiteOpenHelper{


        public StaticProfilesHelper(Context context) {
            super(context, "database.db", null, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            StaticProfilesSql.create(sqLiteDatabase);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            StaticProfilesSql.dropTable(sqLiteDatabase);
            onCreate(sqLiteDatabase);
        }
    }
}
