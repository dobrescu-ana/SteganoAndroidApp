package com.example.stegano;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class register_account extends AppCompatActivity {

    private View decorView;
    private ImageView imgClick;
    private CardView register_button;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText repreatPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

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
                Intent intent = new Intent(register_account.this, MainActivity.class);
                startActivity(intent);
            }
        });

        username = findViewById(R.id.name);
        username.setText("");
        email = findViewById(R.id.email);
        email.setText("");
        password = findViewById(R.id.password);
        password.setText("");
        repreatPass = findViewById(R.id.repeatPassword);
        repreatPass.setText("");

        register_button = (CardView)findViewById(R.id.registerBtn);
        register_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!username.getText().toString().equals("")) {
                    if(!email.getText().toString().equals("")) {
                        if(!password.getText().toString().equals("") && !repreatPass.getText().toString().equals("")) {
                            if (password.getText().toString().equals(repreatPass.getText().toString())) {
                                AlertDialog alertDialog = new AlertDialog.Builder(register_account.this).create();
                                alertDialog.setTitle("");
                                alertDialog.setMessage("Succes");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(register_account.this, login_account.class);
                                                startActivity(intent);
                                            }
                                        });
                                alertDialog.show();
                                User user = new User(username.getText().toString(), password.getText().toString(), email.getText().toString());
                                MainActivity.database.userDAO().addUser(user);
                            }
                            else{
                                AlertDialog alertDialog = new AlertDialog.Builder(register_account.this).create();
                                alertDialog.setTitle("");
                                alertDialog.setMessage("Passwords do not match");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        });
                                alertDialog.show();
                                password.setText("");
                                repreatPass.setText("");
                            }
                        }
                        else{
                            AlertDialog alertDialog = new AlertDialog.Builder(register_account.this).create();
                            alertDialog.setTitle("");
                            alertDialog.setMessage("Please choose a password");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                    }
                    else{
                        AlertDialog alertDialog = new AlertDialog.Builder(register_account.this).create();
                        alertDialog.setTitle("");
                        alertDialog.setMessage("Please enter username");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(register_account.this).create();
                    alertDialog.setTitle("");
                    alertDialog.setMessage("Please enter username");
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
