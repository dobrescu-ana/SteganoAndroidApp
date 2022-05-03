package com.example.stegano;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayush.imagesteganographylibrary.Text.AsyncTaskCallback.TextDecodingCallback;
import com.ayush.imagesteganographylibrary.Text.ImageSteganography;
import com.ayush.imagesteganographylibrary.Text.TextDecoding;

import org.w3c.dom.Text;

import java.io.IOException;

public class decode extends AppCompatActivity implements TextDecodingCallback {

    private static final int SELECT_PICTURE = 100;
    private static final String TAG = "Decode Class";
    private View decorView;

    //UI
    private TextView textView;
    private ImageView imageView;
    private EditText secret_key;
    private Uri filepath;
    private Bitmap original_image;
    private ImageView back;
    private TextView hidden_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

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

        //init UI
        textView = (TextView) findViewById(R.id.whether_encoded);
        imageView = (ImageView) findViewById(R.id.encoded);
        secret_key = (EditText) findViewById(R.id.secret_key);
        back = (ImageView)findViewById(R.id.back);
        CardView choose_image = findViewById(R.id.choose_image_button);
        CardView decode = findViewById(R.id.encode_button);
        hidden_message = findViewById(R.id.hidden);

        //back button
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(decode.this, menu_page.class);
                startActivity(intent);
            }
        });

        //Choose Image Button
        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageChooser();
            }
        });

        //Decode Button
        decode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filepath != null) {

                    //Making the ImageSteganography object
                    ImageSteganography imageSteganography = new ImageSteganography(secret_key.getText().toString(),
                            original_image);

                    //Making the TextDecoding object
                    TextDecoding textDecoding = new TextDecoding(decode.this, decode.this);

                    //Execute Task
                    textDecoding.execute(imageSteganography);
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(decode.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("Select an image first.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        });
    }

    private void ImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        hidden_message.setText("");
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
        if(result != null){
            if(!result.isDecoded()) {
                AlertDialog alertDialog = new AlertDialog.Builder(decode.this).create();
                alertDialog.setTitle("");
                alertDialog.setMessage("No message found");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
            else{
                if(!result.isSecretKeyWrong()){
                    textView.setText("");

                    //show decoded message somehow
                    hidden_message.setText("The hidded message is: " + result.getMessage());
                    /*Toast t = Toast.makeText(decode.this, result.getMessage(), Toast.LENGTH_LONG);
                    t.setGravity(Gravity.TOP, 40 , 1300);
                    t.show();*/
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(decode.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("Wrong secret key");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }
        }
    }
}
