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

    //User table
    public static final String USERNAME="username";
    public static final String USER_TABLE="user_table";
    public static final String STATUS="status";





    public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " +MEASURABLE_TABLE +" (ID INTEGER, NAME TEXT,TYPE TEXT,MIN INTEGER,MAX,INTEGER,USER_ID INTEGER)");
        db.execSQL("CREATE TABLE " +LOG_TABLE +" (ID INTEGER, TIME INTEGER,LOG TEXT,VALUE,INTEGER,USER_ID INTEGER,PARENT INTEGER)");
        db.execSQL("CREATE TABLE " +USER_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER,USERNAME TEXT,STATUS INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +MEASURABLE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " +LOG_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " +USER_TABLE);
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
        System.out.println("SELECT * FROM "+MEASURABLE_TABLE+" WHERE "+MEASURABLE_ID+"="+id);
        Cursor result=db.rawQuery("SELECT * FROM "+MEASURABLE_TABLE+" WHERE "+MEASURABLE_ID+"="+id,null);
        return result;
    }

    public int checkUsers(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM "+USER_TABLE+" WHERE "+STATUS+"="+1,null);
        if(cursor.getCount()==0){
            return -1;
        }else{

            if (cursor.moveToFirst()){
                do{
                    return cursor.getInt(1);
                }while(cursor.moveToNext());
            }
            cursor.close();
        }
        return -1;
    }
    public Boolean insertUser(int userId, String UserName){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(USER,userId);
        contentValues.put(USERNAME,UserName);
        contentValues.put(STATUS,1);
        long result=db.insert(USER_TABLE,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public  Boolean UserDoesNotExist(int userID){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor =db.rawQuery("SELECT * FROM "+USER_TABLE+" WHERE "+USER+"="+userID,null);
        return cursor.getCount()==0;
    }

    public void UpdateStatus(int UserID,int Status){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(STATUS,Status);
        db.update(USER_TABLE,contentValues,USER+"="+UserID,null);
    }
    public void CreateUserTable(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("CREATE TABLE " +USER_TABLE +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, USER_ID INTEGER,USERNAME TEXT,STATUS INTEGER)");
    }

}
