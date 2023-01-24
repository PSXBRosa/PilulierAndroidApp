package com.example.app_mob_2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.core.location.GnssStatusCompat;
import android.widget.CursorAdapter;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class DbHelper extends SQLiteOpenHelper {


    public DbHelper(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table on database (I hope)
        db.execSQL(Constants.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // upgrade table if any structure change

        // drop table if exists
        db.execSQL("DROP TABLE IF EXISTS "+ Constants.TABLE_NAME);

        // create table again
        onCreate(db);

    }

    public void deleteContact(String id){

        SQLiteDatabase db =this.getWritableDatabase();

        db.execSQL("DELETE FROM " + Constants.TABLE_NAME + " WHERE " + Constants.M_ID + "=" + id);

        db.close();


    }

    // insert function to insert data in database
    public long insertContact(String med, String quant, String date, String time, String slot){

        // get writable database to write data on db
        SQLiteDatabase db =this.getWritableDatabase();

        // create ContentValue class object to save data
        ContentValues contentValues = new ContentValues();

        // id will save automatically a we write query
        contentValues.put(Constants.M_MED, med);
        contentValues.put(Constants.QUANT, quant);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.TIME, time);
        contentValues.put(Constants.SLOT, slot);

        // insert data in a row
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);

        //always close it
        db.close();

        return id;
    }

    public void updateContact(String id,String name,String quantity, String date, String time,String slot){

        //get writable database to write data on db
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValue class object to save data
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.M_MED, name);
        contentValues.put(Constants.QUANT, quantity);
        contentValues.put(Constants.DATE, date);
        contentValues.put(Constants.TIME, time);
        contentValues.put(Constants.SLOT, slot);


        //update data in row, It will return id of record
        db.update(Constants.TABLE_NAME,contentValues,Constants.M_ID+" =? ",new String[]{id} );

        // close db
        db.close();

    }

    public Cursor fetchData(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor resultSet = db.rawQuery("Select * from " + Constants.TABLE_NAME, null);
        return resultSet;
    }

    public void clearDataBase(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Constants.TABLE_NAME);
        db.close();
    }

    private void addJsonRow(JSONObject jsonRow, SQLiteDatabase db) throws JSONException {
        ContentValues contentValues = new ContentValues();

        Iterator<String> keys = jsonRow.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            contentValues.put(key, jsonRow.get(key).toString());
        }
        String time = jsonRow.getString("time");
        DateHandler dh = new DateHandler(time);
        contentValues.put("time",dh.getDate());
        contentValues.put("cr_t",dh.getTime());

        db.insert(Constants.TABLE_NAME, null, contentValues);
    }

    public void RepopulateDatabaseWithOneItem(JSONObject data) throws  JSONException{

        clearDataBase();

        SQLiteDatabase db = this.getWritableDatabase();

        addJsonRow(data, db);

        db.close();

    }

    public void RepopulateDatabase(JSONArray data) throws JSONException {
        // empties database and fills it with json data

        clearDataBase();

        SQLiteDatabase db = this.getWritableDatabase();


        for (int i = 0; i < data.length(); i++){
            addJsonRow(data.getJSONObject(i), db);
        }

        db.close();

    }

}
