package com.example.stegano;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextEncodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextEncoding;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

public class encode extends AppCompatActivity implements TextEncodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Encode Class";
    private View decorView;

    //UI
    private TextView whether_encoded;
    private ImageView imageView;
    private EditText message;
    private EditText secret_key;
    CardView choose_image;
    CardView encode;
    CardView save_image;
    ImageView back;
    ImageView micro;
    TextView gallery;
    ImageView map;

    //for encoding
    private TextEncoding textEncoding;
    private ImageSteganography imageSteganography;
    private ProgressDialog save;
    private Uri filepath;

    //bitmaps
    private Bitmap original_image;
    private Bitmap encoded_image;

    boolean save_flag = false;
    public static boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        //full screen mode
        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        //init UI components
        whether_encoded = findViewById(R.id.whether_encoded);
        imageView = findViewById(R.id.carrier);
        message = findViewById(R.id.message);
        secret_key = findViewById(R.id.secret_key);
        choose_image = findViewById(R.id.choose_image_button);
        encode = findViewById(R.id.encode_button);
        save_image = findViewById(R.id.save_image_button);
        back = findViewById(R.id.back);
        gallery = findViewById(R.id.get_gallery);
        map = findViewById(R.id.map);

        if(clicked == true) {
            Bundle extras = getIntent().getExtras();
            if(extras != null){
                double latitude = extras.getDouble("latitude");
                double longitude = extras.getDouble("longitude");
                String coord = String.valueOf(latitude) + " " + String.valueOf(longitude);
                message.setText(coord);
            }
        }

        checkAndRequestPermissions();

        //back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(encode.this, menu_page.class);
                startActivity(intent);
            }
        });

        //choose image button
        choose_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        //online gallery button --> consume service
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(encode.this, search_image.class);
                startActivity(intent);
            }
        });

        //open map to get current location coordinates
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(encode.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        //encode button
        encode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                whether_encoded.setText("");

                if(filepath == null) {
                    AlertDialog alertDialog = new AlertDialog.Builder(encode.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("Please choose an image first");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

                else if(filepath != null) {

                    if(message.getText().toString().equals("")) {
                        AlertDialog alertDialog = new AlertDialog.Builder(encode.this).create();
                        alertDialog.setTitle("");
                        alertDialog.setMessage("Please enter the message you want to hide");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else{
                        if(secret_key.getText().toString().equals("")) {
                            AlertDialog alertDialog = new AlertDialog.Builder(encode.this).create();
                            alertDialog.setTitle("");
                            alertDialog.setMessage("Please enter a secret key");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else{
                            imageSteganography = new ImageSteganography(message.getText().toString(),
                                    secret_key.getText().toString(),
                                    original_image);
                            textEncoding = new TextEncoding(encode.this, encode.this);
                            textEncoding.execute(imageSteganography);
                            message.setText("");
                            secret_key.setText("");
                        }
                    }
                }
            }
        });

        //save image button
        save_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(save_flag == false){
                    AlertDialog alertDialog = new AlertDialog.Builder(encode.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("Encode an image first.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else if(save_flag == true) {
                    final Bitmap imgToSave = encoded_image;
                    Thread PerformEncoding = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            saveToInternalStorage(imgToSave);
                        }
                    });
                    save = new ProgressDialog(encode.this);
                    save.setMessage("Saving, Please Wait...");
                    save.setTitle("Saving Image");
                    save.setIndeterminate(false);
                    save.setCancelable(false);
                    save.show();
                    PerformEncoding.start();
                    Toast t = Toast.makeText(encode.this, "Saved", Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP, 40, 1400);
                    t.show();
                }
            }
        });
    }

    private void ImageChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //image set to imageView
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                original_image = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
                imageView.setImageBitmap(original_image);
            } catch (IOException e) {
                Log.d(TAG, "Error : " + e);
            }
        }
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

    @Override
    public void onStartTextEncoding() {

    }

    @Override
    public void onCompleteTextEncoding(ImageSteganography result) {
        if (result != null && result.isEncoded()){
            encoded_image = result.getEncoded_image();
            Toast t = Toast.makeText(this, "Encoded", Toast.LENGTH_LONG);
            t.setGravity(Gravity.TOP, 40 , 1400);
            t.show();
            imageView.setImageBitmap(encoded_image);
        }
        save_image.setCardBackgroundColor(Color.parseColor("#900C3F"));
        save_flag = true;
    }

    private void saveToInternalStorage(Bitmap bitmapImage) {
        OutputStream fOut;
        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),  "Encoded" + ".png"); // the File to save ,
        try {
            fOut = new FileOutputStream(file);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fOut); // saving the Bitmap to a file
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream
            whether_encoded.post(new Runnable() {
                @Override
                public void run() {
                    save.dismiss();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
