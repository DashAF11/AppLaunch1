package com.example.applaunch.RoomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.applaunch.Dao.ImageDao;
import com.example.applaunch.Entity.ImageEntity;

import static com.example.applaunch.RoomDB.RoomMigration.MIGRATION_1_2;
import static com.example.applaunch.RoomDB.RoomTables.ROOM_DB_NAME;
import static com.example.applaunch.RoomDB.RoomTables.ROOM_DB_VERSION;

@Database(entities = {ImageEntity.class}, version = ROOM_DB_VERSION, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    public static RoomDB roomDBHelper;

    public static RoomDB getRoomDB(Context context) {
        if (roomDBHelper == null) {
            synchronized (RoomDB.class) {
                roomDBHelper = Room.databaseBuilder(context.getApplicationContext(),
                        RoomDB.class, ROOM_DB_NAME)
                        .addMigrations(MIGRATION_1_2)
                        .build();
            }
        }
        return roomDBHelper;
    }

    public abstract ImageDao imageDao();
}
