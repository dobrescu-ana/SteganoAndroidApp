package com.example.stegano;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class search_image extends AppCompatActivity {

    EditText search;
    CardView button;
    ListView listView;
    private View decorView;
    private String api_key = "35baa6643a9851439ec9183c021c332a";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_image);

        //fullscreen
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        //controls from layout
        search = (EditText)findViewById(R.id.searchtext);
        button = (CardView)findViewById(R.id.search_cardview);
        listView = (ListView)findViewById(R.id.gallery);

        checkAndRequestPermissions();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //parse json and display images in listview
                initAndDisplayListview();
            }
        });
    }

    private void initAndDisplayListview(){

        JSONParser getImages = new JSONParser()
        {
            @Override
            protected void onPostExecute (ArrayList < ImageInfo > imageInfos) {
                ImageInfoAdapter adapter = new ImageInfoAdapter(getApplicationContext(), imageInfos);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        ImageView img = findViewById(R.id.imageView);
                        BitmapDrawable bitmapDrawable = (BitmapDrawable)img.getDrawable();
                        Bitmap toSave = bitmapDrawable.getBitmap();

                        File myDir = new File(String.valueOf(getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
                        Random generator = new Random();
                        int n = 10000;
                        n = generator.nextInt(n);
                        String fname = "Image-" + n + ".jpg";
                        File file = new File(myDir, fname);
                        if(file.exists())
                            file.delete();
                        try{
                            FileOutputStream out = new FileOutputStream(file);
                            toSave.compress(Bitmap.CompressFormat.JPEG, 90, out);
                            out.flush();
                            out.close();
                            Intent intent = new Intent(search_image.this, encode.class);
                            startActivity(intent);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };

        getImages.execute("https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=" + api_key + "&text=" + search.getText().toString() + "&format=json&nojsoncallback=1");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

    private int hideSystemBars(){
        return View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
    }

    private void checkAndRequestPermissions() {
        int permissionWriteStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int ReadPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (ReadPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[0]), 1);
        }
    }
}
