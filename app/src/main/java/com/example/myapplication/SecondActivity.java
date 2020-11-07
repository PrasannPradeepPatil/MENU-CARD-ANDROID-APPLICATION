package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


public class SecondActivity extends AppCompatActivity {

    //INITIALISE
    private TextView text_secondactivity;
    private TextView editText_secondactivity;
    private TextView addText_secondativity;
    private ImageView image_secondactivity;
    private Button copy_button;
    private Button share_button;
    private Button download_button;
    private Button update_button;
    private Button add_button;

    private String textstorer;
    private int imageStorer;


    //ONCREATE --RUNS ONLY WHEN ACTIVITY OPENED
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        findViewById();
        getViewFromMainActivity();
        setViewFromMainActivity();
        copyTextView();
        //shareTextView();
        shareImageView();
        downloadImageview();
        updateTextview();
        //insertTextView();
    }


    private void findViewById() {
        text_secondactivity = (TextView) findViewById(R.id.text_secondactivity);//id of text in activity_second.xml
        editText_secondactivity = (TextView) findViewById(R.id.editText_secondActivity);
        addText_secondativity = (TextView)findViewById(R.id.addText_secondActivity);
        image_secondactivity = (ImageView) findViewById(R.id.image_secondactivity);
        copy_button = (Button) findViewById(R.id.copy_button);
        share_button = (Button) findViewById(R.id.share_button);
        download_button = (Button) findViewById(R.id.download_button);
        update_button = (Button) findViewById(R.id.update_button);
        add_button = (Button)findViewById(R.id.add_button);

    }
    private void getViewFromMainActivity() {
        /*
        AndroidManifest.xml
            <manifest..
             ..
             ..
            <application
               ..
               ..
               <activity android:name=".2ndActivity"
                    android:parentActivityName=".1stActivity"
                     android:label
                           /application>

         */
        //the "string values" are same to MyAdapter's OnBindVieHolders  Intent.putExtra()
        if (getIntent().hasExtra("text") && getIntent().hasExtra("image")) {
            textstorer = getIntent().getStringExtra("text");
            imageStorer = getIntent().getIntExtra("image", 1);


        } else {
            Toast.makeText(this, "NoData", Toast.LENGTH_SHORT).show();
        }


    }
    private void setViewFromMainActivity() {
        text_secondactivity.setText(textstorer);
        image_secondactivity.setImageResource(imageStorer);
    }

    private void copyTextView() {
        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
                builder.setMessage("Do you want to copy")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                     /////////////////////CODE TO COPY TEXTVIEW  TO CLIPBOARD////////////////////////////////////
                                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                //ClipData.newPlainText(label:"text label", text:"text to be copied in str");
                                ClipData clip = ClipData.newPlainText("text label", text_secondactivity.getText().toString());
                                clipboard.setPrimaryClip(clip);
                     ////////////////////////CODE TO COPY TEXTVIEW  TO CLIPBOARD////////////////////////////////////
                            }
                        })
                        .setNegativeButton("Cancel",null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

    }
    private void shareTextView() {
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
                // sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, value:"text to be shared in string ");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text_secondactivity.getText().toString());
                //startActivity(Intent.createChooser(sharingIntent, "message you want to see while sharing"));
                startActivity(Intent.createChooser(sharingIntent, "Share text via"));

            }
        });
    }
    private void shareImageView(){
        //https://sites.google.com/view/share-image/home(find code here)
        //1.In android Manifest.XML add <provider>code of "provider"</provider> before </application> ends
        //2.press on  android:resource="@xml/provider_path" / and create xml file and while creating in root element type paths
        //3.In provider_path.xml add <paths>  code of "add paths"  </paths>i

        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /////////////////////CODE TO SHARE IMAGE ////////////////////////////////////
                //Drawable drawable=imageView to be shared.getDrawable();
                Drawable drawable=image_secondactivity.getDrawable();

                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();

                try {
                    //File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +"image_name_yoe_want _at_bottom.pmg");
                    File file = new File(getApplicationContext().getExternalCacheDir(), File.separator +text_secondactivity.getText().toString()+".png");
                    FileOutputStream fOut = new FileOutputStream(file);
                    //JPEG -> PNG if PNG image
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.flush();
                    fOut.close();
                    file.setReadable(true, false);
                    final Intent intent = new Intent(android.content.Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri photoURI = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID +".provider", file);

                    intent.putExtra(Intent.EXTRA_STREAM, photoURI);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //jpg->png if image is png
                    intent.setType("image/jpg");

                    startActivity(Intent.createChooser(intent, "Share image via"));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            /////////////////////CODE TO SHARE IMAGE ////////////////////////////////////
        });

    }
    private void downloadImageview(){
        //In AndroidManifest.xml add the 2 lines
        // <application>
        //      android:theme="@style/AppTheme"
        //      android:requestLegacyExternalStorage="true"
        // <activity android:name=".MainActivity">
        //https://www.youtube.com/watch?v=SKMtVSK_Kg0

        download_button.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

          ///////////////////////////////CODE TO DOWNLOAD  IMAGE /////////////////////////////////////////////////////////////
            View vieww = LayoutInflater.from(SecondActivity.this).inflate(R.layout.activity_custom_dialog2,null);
             AlertDialog.Builder builder = new AlertDialog.Builder(SecondActivity.this);
             builder.setMessage("Do you want to download")
                     .setView(vieww)
                     .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                    Drawable drawable = image_secondactivity.getDrawable();
                                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                                    //File file = new File(getApplicationContext().getExternalFilesDir(null),child:"imagename.png");
                                    File file = new File(getApplicationContext().getExternalFilesDir(null),text_secondactivity.getText().toString() + ".png");
                                    Toast.makeText(getApplicationContext(),file +"",Toast.LENGTH_LONG).show();

                                    try(FileOutputStream fos = new FileOutputStream(file);) {
                                        bitmap.compress(Bitmap.CompressFormat.PNG,100,fos);
                                        //Toast.makeText(getApplicationContext(),"Msg to br prompted on download button click",Toast.LENGTH_LONG).show();
                                        //Toast.makeText(getApplicationContext(),"Image saved in folder promted ",Toast.LENGTH_LONG).show();


                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
            ///////////////////////////////CODE TO DOWNLOAD  IMAGE /////////////////////////////////////////////////////////////

             /////////////////////CODE TO OPEN SECOND ALERT BOX///////////////////////////////////////////////////////////////
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SecondActivity.this);
                                builder2.setMessage("You Have Downloaded")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                AlertDialog alertDialog = builder2.create();
                                alertDialog.show();

                    ////////////////////CODE TO OPEN SECOND ALERT BOX///////////////////////////////////////////////////////////////



                            }
                        })
                        .setNegativeButton("NO",null)
                        .setCancelable(false);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
    private void updateTextview(){
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = MainActivity.titles.indexOf(text_secondactivity.getText().toString());


                DataBaseHelperr db = new DataBaseHelperr(SecondActivity.this);
                db.updateDB(text_secondactivity.getText().toString(),editText_secondactivity.getText().toString());

                Toast.makeText(SecondActivity.this,"You have Updated Text,Please press Back",Toast.LENGTH_LONG).show();






            }
        });


    }
    private void insertTextView(){
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelperr db = new DataBaseHelperr(SecondActivity.this);
                db.insertDB(addText_secondativity.getText().toString());
                Toast.makeText(SecondActivity.this,"You have Updated Text,Please press Back",Toast.LENGTH_LONG).show();

            }
        });


    }
















}


