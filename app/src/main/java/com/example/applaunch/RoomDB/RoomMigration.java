package com.example.applaunch.RoomDB;

import androidx.annotation.NonNull;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public interface RoomMigration {

    Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {

            database.execSQL("CREATE TABLE IF NOT EXISTS `image_table` (`image_id` INTEGER NOT NULL, `image_url` TEXT, `image_fav` TEXT, `image_like` TEXT, PRIMARY KEY(`image_id`))");

        }
    };
}
