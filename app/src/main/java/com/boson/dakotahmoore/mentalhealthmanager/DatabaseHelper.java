package com.boson.dakotahmoore.mentalhealthmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dakotah on 3/29/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="mhm.db";
    public static final String MEASURABLE_TABLE="measurables";
    public static final String LOG_TABLE="logs";
    public static final String MEASURABLE_ID="id";
    public static final String MIN="min";
    public static final String MAX="max";
    public static final String TYPE="type";
    public static final String NAME="name";
    public static final String USER="USER_ID";
    public static final String LOG="log";
    public static final String TIME="timestamp";
    public static final String PARENT="parent";
    public static final String VALUE="value";



    public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +MEASURABLE_TABLE +" (ID INTEGER, NAME TEXT,TYPE TEXT,MIN INTEGER,MAX,INTEGER,USER_ID INTEGER)");
        db.execSQL("CREATE TABLE " +LOG_TABLE +" (ID INTEGER, TIME INTEGER,LOG TEXT,VALUE,INTEGER,USER_ID INTEGER,PARENT INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +MEASURABLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " +LOG_TABLE);
        onCreate(db);
    }

    public boolean insertMeasurable(int id,String name, String Type, int max, int min, int user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(MEASURABLE_ID,id);
        contentValues.put(NAME,name);
        contentValues.put(TYPE,Type);
        contentValues.put(MIN,min);
        contentValues.put(MAX,max);
        contentValues.put(USER,user);
       long result= db.insert(MEASURABLE_TABLE,null,contentValues);
        if (result==-1)
                return false;
        else
            return true;
    }

    public boolean insertLog(int id,String name, String log,Long time, int user, int parent, int value){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(MEASURABLE_ID,id);
        contentValues.put(NAME,name);
        contentValues.put(VALUE,value);
        contentValues.put(PARENT,parent);
        contentValues.put(LOG,log);
        contentValues.put(USER,user);
        contentValues.put(TIME,time);
        long result= db.insert(LOG_TABLE,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Cursor getMeasurables(int user){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+MEASURABLE_TABLE+" WHERE ("+USER+"="+user+")",null);
        return result;
    }

    public Cursor checkMeasurables(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("SELECT * FROM "+MEASURABLE_TABLE+" WHERE ("+MEASURABLE_ID+"="+id+")",null);
        return result;
    }
}
