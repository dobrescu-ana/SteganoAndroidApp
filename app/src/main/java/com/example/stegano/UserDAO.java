package com.example.stegano;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;


@Dao
public interface UserDAO {

    @Insert
    void addUser(User user);

    @Query("SELECT COUNT(*) FROM User WHERE Username LIKE :user AND Password LIKE :pass")
    int verifyUser(String user, String pass);
}
