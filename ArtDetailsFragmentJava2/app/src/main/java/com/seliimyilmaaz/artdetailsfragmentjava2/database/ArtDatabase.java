package com.seliimyilmaaz.artdetailsfragmentjava2.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.seliimyilmaaz.artdetailsfragmentjava2.model.Art;

@Database(entities = Art.class,version = 1)
public abstract class ArtDatabase extends RoomDatabase {

   public abstract ArtDao artDao();


}
