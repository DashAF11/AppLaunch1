package com.example.applaunch.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.applaunch.Entity.ImageEntity;

import java.util.List;

import static com.example.applaunch.RoomDB.RoomTables.COLUMN_IMAGE_FAVOURITE;
import static com.example.applaunch.RoomDB.RoomTables.COLUMN_IMAGE_ID;
import static com.example.applaunch.RoomDB.RoomTables.TABLE_IMAGE;

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ImageEntity imageEntity);

    @Update
    void update(ImageEntity imageEntity);

    @Delete
    void delete(ImageEntity imageEntity);

    @Query("UPDATE " + TABLE_IMAGE
            + " SET " + COLUMN_IMAGE_FAVOURITE + " =:fav"
            + " WHERE " + COLUMN_IMAGE_ID + " =:imgId ")
    void updateFavImage(long imgId, String fav);

    @Query("SELECT * FROM " + TABLE_IMAGE)
    LiveData<List<ImageEntity>> getImageLiveData();

    @Query("SELECT * FROM " + TABLE_IMAGE
     + " WHERE "+ COLUMN_IMAGE_FAVOURITE + " =:fav")
    LiveData<List<ImageEntity>> getFavImageLiveData(String fav);

}
