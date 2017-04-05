package com.example.user.larper.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
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

    public ModelSqlite(Context context){
        try {
            profilesHelper = new ProfilesHelper(context);
            blueprintsHelper = new BlueprintsHelper(context);
            staticProfileHelper = new StaticProfilesHelper(context);
        }
        catch(Exception e){
            Log.d("exception", "exception while trying to install the local db " + e.getMessage());
        }
    }

    public static ModelSqlite getInstance(Context context){
        if(modelSql == null)
            modelSql = new ModelSqlite(context);

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

    public void saveContact(StaticProfile contact)
    {
        StaticProfilesSql.writeContact(
                staticProfileHelper.getWritableDatabase(),
                contact);
    }

    public void saveBlueprint(Blueprint blueprint, String owner)
    {
        BlueprintsSql.writeBlueprint(blueprintsHelper.getWritableDatabase(),
                                     blueprint, owner);
    }

    public ArrayList<StaticProfile> getOwnerContacts()
    {
        return StaticProfilesSql.getContactsByOwner(
                staticProfileHelper.getReadableDatabase());
    }

    public ArrayList<Blueprint> getOwnerBlueprints()
    {
        SQLiteDatabase sql = blueprintsHelper.getReadableDatabase();
        return BlueprintsSql.GetBlueprintsByOwner(
                (sql));
    }

    class ProfilesHelper extends SQLiteOpenHelper{


        public ProfilesHelper(Context context) {
            super(context, "profiles.db", null, version);
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
            super(context, "blueprints.db", null, version);
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
            super(context, "staticprofiles.db", null, version);
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
