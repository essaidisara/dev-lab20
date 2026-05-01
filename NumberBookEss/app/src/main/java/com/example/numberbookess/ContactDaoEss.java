package com.example.numberbookess;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;
import java.util.List;

@Dao
public interface ContactDaoEss {
    // Détection des doublons : si le contact existe, on le remplace (mise à jour)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ContactEss contact);

    @Query("SELECT * FROM local_contacts ORDER BY name ASC")
    List<ContactEss> getAll();

    @Update
    void update(ContactEss contact);

    @Delete
    void delete(ContactEss contact);

    @Query("DELETE FROM local_contacts")
    void deleteAll();
}