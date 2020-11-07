package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;


//CREATING DATABASE
/*
https://www.youtube.com/watch?v=NzK5hHdblqk&feature=youtu.be
1.SQLLITE-->NEW DATABASE --> GIVE TABLENAME , ADD ROWS 1 ROW MUST ME PK _id,YOU ADD VALUES IN TABLE , GO TO SQLEXECUTE 2 COMMANDS
    ->CREATE TABLE "android_metadata" ("locale" TEXT DEFAULT 'en_US')
    ->INSERT INTO "android_metadata" VALUES ('en_US')
2.APP--> NEW --> FOLDER --> ADDET FOLDER-->NEW ASSET FOLDER --> COPY PASTE THE .DB file and paste without .db extension as tet file
3.ASSET DB(DB OF PROJECT) : PRESENT IN ASSETS DB
  LOCAL DB(DB OF MOBILE) : View->Toolwindow ->Device File Explorer ->data->data-> com.example.myproject-> database -> save with .db extension on desktop to view it

 */

public class DataBaseHelperr extends SQLiteOpenHelper {
    //0.INITIALISE
    String DB_PATH = "/data/data/com.example.myapplication/databases/";
    private static String DB_NAME = "foodDatabasess";//DB NAME WITHOUT DB
    private SQLiteDatabase sq;
    private Context context;
    private static int DATABASE_VERSION = 1;


    public static ArrayList<String > assetList = new ArrayList<>();//Declared so that DB in assets can be updated without changing code
                                                                    //During checkDatabase() we populate it ; During createDatabase() we use its size to populate DB
                                                                    //***In this prg  you have not populated in createdb() instead in MainActivity***
                                                                    //    ->FETCH IMAGES FROM ARRAY --ADD 0 AS   IMAGES EQUAL TO NO OF VALUES YU ADD IN ASSETS FOLDER
                                                                    //    ->FETCHTITLEFROMLOCALDB -- EQUILIZE ASSETS LIST TO TITLES,UPDATE LOCAL DB ACC TO ASSETLIST
                                                                    //     ->GIVE RECYCLER VIEW THIS ARRAYLLIST INSTEAD OF ITLE ARRAYLIST

    //1.CONSTRUCTOR(Remove Extra Parameters and keep only context)
    public DataBaseHelperr(@Nullable Context context) {
        super(context, DB_NAME, null,DATABASE_VERSION);
        this.context = context;

    }


    //2.ONCREATE --  runs on creating db
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }
    //ONUPGRADE -runs on upgrading db
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //OPENDB -Open Database so that we can query it
    public void openDB() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        sq = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }
    //CLOSEDB - close db
    public synchronized void closeDB(){
        if (sq != null)
            sq.close();
        super.close();


    }
    @Override
    public synchronized void close() {
        if (sq != null)
            sq.close();
        super.close();
    }


    //3.CHECK DB - CHECK IF DB EXISTS, COPYDB - COPY ASSET DB TO  LOCAL DB ; CREATE DB - CREATE DB
    // (Also used to change db if db changed in assets folder with help of assetlist  array)
    private boolean checkDB() {
        File path = new File(DB_PATH+DB_NAME);
        boolean ans = path.exists();

        /////////////////////FETCH TITLE FROM LOCAL DB AND POPULATE ASSETS /////////////////////////////////
        if(ans){
            SQLiteDatabase sq = getReadableDatabase();

            Cursor c = sq.query("foodtables" ,null,null,null,null,null,null);
            while (c.moveToNext()){
                assetList.add(c.getString(1));
            }
            c.close();
            close();

            path.delete();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        return path.exists();
    }
    private void copyDB() throws IOException {
        InputStream is = context.getAssets().open(DB_NAME);
        String out = DB_PATH + DB_NAME;
        OutputStream os = new FileOutputStream(out);
        byte[] buffer = new byte[20];
        int length;
        while ((length = is.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }
        os.flush();
        os.close();
        is.close();
    }
    public void createDB()  {
        boolean dbExist = checkDB();
        if (!dbExist) {
            this.getReadableDatabase();
            try {
                this.close();
                copyDB();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    //4.SPECIFIC METHODS
    //UPGRADE DB - UPGRADE DB BY CHANGING OLDSTRING TO NEW STRING
    public void updateDB(String oldText , String newText){
        DataBaseHelperr db = this;
        SQLiteDatabase sq = db.getWritableDatabase();

        openDB();

        ContentValues cv = new ContentValues();
        cv.put("foodname",newText);



        int idOldText = (MainActivity.titles.indexOf(oldText) ) + 1;// + 1 because in DB your id starts from 1 but in titles arr starts from 0
        sq.update("foodtables",cv,"_id="+idOldText,null);

        DataBaseHelperr.assetList.set(idOldText-1,newText);

        closeDB();

    }
    //INSERT DB - INSERT INTO DB NEWTEXT
    public void insertDB(String newText){
        DataBaseHelperr db = this;
        SQLiteDatabase sq = db.getWritableDatabase();

        createDB();
        openDB();

        ContentValues cv = new ContentValues();
        cv.put("foodname",newText);
        sq.insert("foodtables",null,cv);


        closeDB();
    }
    //QUERY - RETURNS A CURSOR OBJECT
    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return sq.query("foodtables", null, null, null, null, null, null);

    }



}

