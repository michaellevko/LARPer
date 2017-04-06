package com.example.user.larper.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.Query;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by event on 02/04/2017.
 */

public class BlueprintsSql {

    final static String TABLE_NAME = "BLUEPRINTS";
    final static String NAME = "Name";
    final static String OWNER ="nickname";
    final static String INGERDIENTS = "ingredients";
    final static String PRICESUM = "priceSum";
    final static String CRAFTINGHOURS = "craftingTimeHours";

    public static void writeBlueprint(SQLiteDatabase writableDatabase, Blueprint blueprint,
                                      String owner) {
        ContentValues values = new ContentValues();

        values.put(NAME, blueprint.getName());
        values.put(INGERDIENTS, new Gson().toJson(blueprint.getIngredients()));
        values.put(PRICESUM, blueprint.getTotalCost());
        values.put(CRAFTINGHOURS, blueprint.getCraftingTime());
        values.put(OWNER, owner);


        long rowId = writableDatabase.insertWithOnConflict(
                TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        if (rowId <= 0) {
            Log.e("TAG","fail to insert into blueprints");
        }
    }

    public static void deleteBlueprint(SQLiteDatabase writableDatabase, Blueprint blueprint) {
        String owner = StaticProfilesSql.curr_owner.toString();
        String[] selectionArgs = {blueprint.getName(),blueprint.getTotalCost(), blueprint.getCraftingTime()};
        String query = "SELECT rowid,* FROM " + TABLE_NAME + " WHERE " + NAME + " = '" + blueprint.getName() +
                "' AND " + PRICESUM + " = '" + blueprint.getTotalCost() + "' AND " + CRAFTINGHOURS + " = '" +
                blueprint.getCraftingTime() + "' ";
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

    /*public static Blueprint getBlueprintsByOwner(SQLiteDatabase readableDatabase, String owner) {
        String[] selectionArgs = {owner};
        Cursor cursor = readableDatabase.query(TABLE_NAME,null, owner + " = ?",selectionArgs ,null,null,null);
        Blueprint blueprint = null;
        if (cursor.moveToFirst() == true){
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String ingredients = cursor.getString(cursor.getColumnIndex(INGERDIENTS));
            String priceSum = cursor.getString(cursor.getColumnIndex(PRICESUM));
            String craftingTimeHours = cursor.getString(cursor.getColumnIndex(CRAFTINGHOURS));

            blueprint = new Blueprint(name,
                    (ArrayList<Ingredient>)new Gson().fromJson(ingredients,
                            new TypeToken<ArrayList<Ingredient>>(){}.getType()),
                    craftingTimeHours);

        }

        return blueprint;
    }*/

    public static ArrayList<Blueprint> GetBlueprintsByOwner(SQLiteDatabase readableDatabase){
        Blueprint blueprint = null;
        String owner = StaticProfilesSql.curr_owner.toString();
        String[] selectionArgs = {owner};
        Cursor cursor = readableDatabase.query(
                TABLE_NAME,null, OWNER + " = ?",
                selectionArgs ,null,null,null);

        ArrayList<Blueprint> blueprints = new ArrayList<Blueprint>();

        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(INGERDIENTS));
                String priceSum = cursor.getString(cursor.getColumnIndex(PRICESUM));
                String craftingTimeHours = cursor.getString(cursor.getColumnIndex(CRAFTINGHOURS));

                blueprint = new Blueprint(name,
                        (ArrayList<Ingredient>)new Gson().fromJson(ingredients,
                                new TypeToken<ArrayList<Ingredient>>(){}.getType()),
                        craftingTimeHours);
                blueprints.add(blueprint);
            }
            while (cursor.moveToNext());
        }
        return blueprints;
    }

    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                " (" + OWNER + " TEXT, " + NAME +
                " TEXT, " + INGERDIENTS + " TEXT, " + PRICESUM +
                " TEXT, "  + CRAFTINGHOURS +
                " TEXT)");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }

    private static String[] getColumns(){
        String[] columns = {OWNER,NAME,INGERDIENTS,PRICESUM,CRAFTINGHOURS};
        return columns;
    }
}