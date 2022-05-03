package com.example.stegano;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "User")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "Username")
    private String username;

    @ColumnInfo(name = "Password")
    private String password;

    @ColumnInfo(name = "Email")
    private String email;

    public User(){
    }

    public User(String name, String pass, String mail){
        this.username = name;
        this.password = pass;
        this.email = mail;
    }

    public int getID(){ return ID; }
    public String getUsername(){ return username; }
    public String getPassword(){ return password; }
    public String getEmail(){ return email; }

    public void setID(int id){
        ID = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
