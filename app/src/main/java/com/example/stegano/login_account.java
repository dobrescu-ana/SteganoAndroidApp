package com.example.stegano;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import static android.widget.Toast.makeText;

public class login_account extends AppCompatActivity{

    private View decorView;
    private ImageView imgClick;
    private EditText username;
    private EditText password;
    private CardView login;
    private TextView register;
    private CheckBox remember;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);

        decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == 0){
                    decorView.setSystemUiVisibility(hideSystemBars());
                }
            }
        });

        imgClick = (ImageView)findViewById(R.id.home);
        imgClick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_account.this, MainActivity.class);
                startActivity(intent);
            }
        });

        register = (TextView)findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(login_account.this, register_account.class);
                startActivity(intent);
            }
        });

        //init UI components
        username = findViewById(R.id.name);
        password = findViewById(R.id.password);
        login = (CardView) findViewById(R.id.loginBtn);


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if(MainActivity.database.userDAO().verifyUser(user, pass) != 0){
                    Intent intent = new Intent(login_account.this, menu_page.class);
                    startActivity(intent);
                }

                //if(username.getText().toString().equals("test") && password.getText().toString().equals("test")){
                  //  Intent intent = new Intent(login_account.this, menu_page.class);
                    //startActivity(intent);
                //}

                else{
                    username.setText("");
                    password.setText("");
                    AlertDialog alertDialog = new AlertDialog.Builder(login_account.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("Wrong username/password");
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
}
