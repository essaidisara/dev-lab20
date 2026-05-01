package com.example.numberbookess;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ContactEss.class}, version = 1)
public abstract class AppDatabaseEss extends RoomDatabase {
    private static AppDatabaseEss instance;
    public abstract ContactDaoEss contactDao();

    public static synchronized AppDatabaseEss getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabaseEss.class, "contact_ess_db")
                    .allowMainThreadQueries() // Pour simplifier au début
                    .build();
        }
        return instance;
    }
}