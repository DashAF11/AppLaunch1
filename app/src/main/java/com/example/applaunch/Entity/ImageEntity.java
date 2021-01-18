package com.example.applaunch.Entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import static com.example.applaunch.RoomDB.RoomTables.COLUMN_IMAGE_FAVOURITE;
import static com.example.applaunch.RoomDB.RoomTables.COLUMN_IMAGE_ID;
import static com.example.applaunch.RoomDB.RoomTables.COLUMN_IMAGE_LIKE;
import static com.example.applaunch.RoomDB.RoomTables.COLUMN_IMAGE_URL;
import static com.example.applaunch.RoomDB.RoomTables.TABLE_IMAGE;

@Entity(tableName = TABLE_IMAGE)
public class ImageEntity {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = COLUMN_IMAGE_ID)
    private long image_id;

    @ColumnInfo(name = COLUMN_IMAGE_URL)
    private String image_url;

    @ColumnInfo(name = COLUMN_IMAGE_FAVOURITE)
    private String image_fav;

    @ColumnInfo(name = COLUMN_IMAGE_LIKE)
    private String image_like;

    public long getImage_id() {
        return image_id;
    }

    public void setImage_id(long image_id) {
        this.image_id = image_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getImage_fav() {
        return image_fav;
    }

    public void setImage_fav(String image_fav) {
        this.image_fav = image_fav;
    }

    public String getImage_like() {
        return image_like;
    }

    public void setImage_like(String image_like) {
        this.image_like = image_like;
    }

    @Override
    public String toString() {
        return "ImageEntity{" +
                "image_id=" + image_id +
                ", image_url='" + image_url + '\'' +
                ", image_fav='" + image_fav + '\'' +
                ", image_like='" + image_like + '\'' +
                '}';
    }
}
