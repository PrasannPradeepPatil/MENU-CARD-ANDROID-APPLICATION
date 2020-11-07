package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    //INITIALISING
     public static List<String> titles = new ArrayList<>() ;//titles must be static because you are going to change it in other class
     public static List<Integer> images = new ArrayList<>();
     private DataBaseHelperr db = new DataBaseHelperr(this);



     //ONCREATE -- RUNS ONLY WHEN ACTIVITY OPENED
     @Override
     protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_main);

     fetchImageFromArrays();
     //fetchTitlesFromArray();
     db.createDB();
   }
    //ONRESUME- RUNS EVERYTIME ACTIVITY RESUMED
    @Override
    protected void onResume() {
        super.onResume();

        db.openDB();
        fetchTitleFromLocalDB();
        setRecyclerView();

    }
    //ONACTIVITY -- RUNS ON RETURN  TO ACTIVITY
    @Override
    protected void onActivityResult(int  requestCode,int resultCode , @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        onResume();
    }
    //ONBACKPRESSED--RUNS WHEN YOU PRESS BACK KEY ON ACTIVITY
    @Override
    public void onBackPressed(){
       setAlertBox();
    }



    private void fetchImageFromArrays(){
         //PUT IMAGE IN DRAWABLE -->https://www.youtube.com/watch?v=Y7JTkXoN8OE

        images.clear();                    //You enter main activity arr is populated
                                           // move to other activity and return to main activity arr again populated so itreiterates so
                                          // you need to flush array
        images.add(R.drawable.chocolate);
        images.add(R.drawable.icecream);
        images.add(R.drawable.cake);
        images.add(R.drawable.rice);
        images.add(R.drawable.tomato);




        /////////////////ADD 0 AS   IMAGES EQUAL TO NO OF VALUES YU ADD IN ASSETS FOLDER/////////////////////////
       // images.add(0);



    }
    private void fetchTitlesFromArray(){
        titles.clear();        //Reason explained i fetchimageFromArrays()
        titles.add("chocolate");
        titles.add("icecream");
        titles.add("cake");
        titles.add("rice");
        titles.add("tomato");

    }
    private void fetchTitleFromLocalDB(){
         // MOVE THROUGH DB USING CURSOR AND ADD TO LIST
         Cursor cursor = db.query("foodtables",null,null,null,null,null,null);
         titles.clear();    //Reason explained in fetchimageFromArrays()
         while (cursor.moveToNext()){
            titles.add(cursor.getString(1));
         }
         cursor.close();
         db.closeDB();

         ///////////EQUILIZE ASSETS LIST TO TITLES/////////////////////////////
         if(titles.size() > DataBaseHelperr.assetList.size()){
             for (int i = DataBaseHelperr.assetList.size(); i < titles.size(); i++){
                 DataBaseHelperr.assetList.add(titles.get(i));
             }
       }

         DataBaseHelperr db = new DataBaseHelperr(MainActivity.this);
           SQLiteDatabase sq =  db.getWritableDatabase();
         db.openDB();
        ///////////EQUILIZE ASSETS LIST TO TITLES/////////////////////////////

         ////////////////////////UPDATE LOCAL DB ACC TO ASSETLIST/////////////////
        ContentValues cv = new ContentValues();
        for(int i = 0; i < DataBaseHelperr.assetList.size(); i++){
            cv.put("foodname",DataBaseHelperr.assetList.get(i));
            sq.update("foodtables",cv,"_id=" + (i+1),null);
        }
        db.closeDB();
        ///////////EQUILIZE ASSETS LIST TO TITLES/////////////////////////////





    }
    private void setRecyclerView(){

        Adapter myAdapter = new Adapter(MainActivity.this,this,DataBaseHelperr.assetList,images);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void setAlertBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to exit")                                         //Message at top of dialog box
                //.setview(view)                                                          //custom dialog set acc to view
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {    //Button for + ve response
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.super.onBackPressed();


                    }
                })
                .setNegativeButton("NO",null)                                 //Button for -ve response
                .setCancelable(false);                                                     //if you click outside dialog box it will not  cancel dialog box

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

















}


