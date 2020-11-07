package com.example.myapplication;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DataBaseHelper extends SQLiteOpenHelper {
    String DB_PATH = null;
    private static String DB_NAME = "foodDatabasess2";//without db
    private SQLiteDatabase myDataBase;
    private final Context myContext;


    public DataBaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }
    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }
    public synchronized void closeDB(){
        if (myDataBase != null)
            myDataBase.close();
        super.close();


    }


    //Check if Asstes DB exists
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }
    //copy DB from Assets folder to Local Folder
    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[10];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }
    public void createDataBase()  {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                //copy DataBase From Assets Folder into Local DB
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("foodtables", null, null, null, null, null, null);

    }


    public void updateDataBase(String oldText , String newText){

        DataBaseHelper db = this;
        SQLiteDatabase sq = db.getWritableDatabase();

        createDataBase();
        openDataBase();

        ContentValues cv = new ContentValues();
        cv.put("foodname",newText);



        int idOldText = (MainActivity.titles.indexOf(oldText) ) +1;// + 1 because in DB your id starts from 1 but in titles arr starts from 0
        sq.update("foodtables",cv,"_id="+idOldText,null);

        closeDB();

    }
    public void insertDataBase(String newText){
        DataBaseHelper db = this;
        SQLiteDatabase sq = db.getWritableDatabase();

        createDataBase();
        openDataBase();

        ContentValues cv = new ContentValues();
        cv.put("foodname",newText);
        sq.insert("foodtables",null,cv);


        closeDB();
    }






}