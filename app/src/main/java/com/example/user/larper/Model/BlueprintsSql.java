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

public class BlueprintsSql {

    final static String TABLE_NAME = "BLUEPRINTS";
    final static String UUID = "uuid";
    final static String NAME = "Name";
    final static String INGERDIENTS = "ingredients";
    final static String PRICESUM = "priceSum";
    final static String CRAFTINGHOURS = "craftingTimeHours";
    final static String CRAFTINGMINUTES = "craftingTimeMinutes";

    public static void addBlueprint(SQLiteDatabase writableDatabase, Blueprint blueprint) {
        ContentValues values = new ContentValues();

        values.put(UUID, blueprint.getUUID());
        values.put(NAME, blueprint.getName());
        values.put(INGERDIENTS, new Gson().toJson(blueprint.getIngredients()));
        values.put(PRICESUM, blueprint.getPriceSum());
        values.put(CRAFTINGHOURS, blueprint.getCraftingTimeHours());
        values.put(CRAFTINGMINUTES, blueprint.getCraftingTimeMinutes());

        long rowId = writableDatabase.insert(TABLE_NAME, UUID, values);
        if (rowId <= 0) {
            Log.e("TAG","fail to insert into blueprints");
        }
    }

    public static Blueprint getBlueprintByUuid(SQLiteDatabase readableDatabase, String uid) {
        String[] selectionArgs = {uid};
        Cursor cursor = readableDatabase.query(TABLE_NAME,null, UUID + " = ?",selectionArgs ,null,null,null);
        Blueprint blueprint = null;
        if (cursor.moveToFirst() == true){

            String uuid = cursor.getString(cursor.getColumnIndex(UUID));
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String ingredients = cursor.getString(cursor.getColumnIndex(INGERDIENTS));
            String priceSum = cursor.getString(cursor.getColumnIndex(PRICESUM));
            String craftingTimeHours = cursor.getString(cursor.getColumnIndex(CRAFTINGHOURS));
            String craftingTimeMinutes = cursor.getString(cursor.getColumnIndex(CRAFTINGMINUTES));

            blueprint = new Blueprint(name,
                    (ArrayList<Ingredient>)new Gson().fromJson(ingredients,
                            new TypeToken<ArrayList<Ingredient>>(){}.getType()),
                    Integer.parseInt(craftingTimeHours),
                    Integer.parseInt(craftingTimeMinutes));

        }

        return blueprint;
    }

    public static List<Blueprint> GetAllBlueprints(SQLiteDatabase readableDatabase){
        Blueprint blueprint = null;
        Cursor cursor;
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        cursor = readableDatabase.rawQuery(selectQuery, null);

        List<Blueprint> blueprints = new ArrayList<Blueprint>();

        if (cursor.moveToFirst()){
            do {
                String uuid = cursor.getString(cursor.getColumnIndex(UUID));
                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String ingredients = cursor.getString(cursor.getColumnIndex(INGERDIENTS));
                String priceSum = cursor.getString(cursor.getColumnIndex(PRICESUM));
                String craftingTimeHours = cursor.getString(cursor.getColumnIndex(CRAFTINGHOURS));
                String craftingTimeMinutes = cursor.getString(cursor.getColumnIndex(CRAFTINGMINUTES));

                blueprint = new Blueprint(name,
                        (ArrayList<Ingredient>)new Gson().fromJson(ingredients,
                                new TypeToken<ArrayList<Ingredient>>(){}.getType()),
                        Integer.parseInt(craftingTimeHours),
                        Integer.parseInt(craftingTimeMinutes));
                blueprints.add(blueprint);
            }
            while (cursor.moveToNext());
        }
        return blueprints;
    }

    public static void create(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + UUID + " TEXT, " + NAME +
                " TEXT, " + INGERDIENTS + " TEXT, " + PRICESUM +
                " TEXT, "  + CRAFTINGHOURS + " TEXT, " + CRAFTINGMINUTES +
                " TEXT)");
    }

    public static void dropTable(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_NAME);
    }

    private static String[] getColumns(){
        String[] columns = {UUID,NAME,INGERDIENTS,PRICESUM,CRAFTINGHOURS,CRAFTINGMINUTES};
        return columns;
    }
}