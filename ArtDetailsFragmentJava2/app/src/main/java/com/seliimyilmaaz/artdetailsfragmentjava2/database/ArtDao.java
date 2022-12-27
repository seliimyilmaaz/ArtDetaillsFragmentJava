package com.seliimyilmaaz.artdetailsfragmentjava2.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.seliimyilmaaz.artdetailsfragmentjava2.model.Art;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
@Dao
public interface ArtDao {

    @Query("Select * From Art Where id = :id")
    Flowable<Art> getAll(int id);

    @Query("Select artName,id From Art")
    Flowable<List<Art>> getArtById();
    @Delete
    Completable delete(Art art);

    @Insert
    Completable insert(Art art);

}
