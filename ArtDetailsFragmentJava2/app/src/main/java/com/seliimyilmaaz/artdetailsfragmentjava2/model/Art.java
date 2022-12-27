package com.seliimyilmaaz.artdetailsfragmentjava2.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Art implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "artName")
    public String artName;

    @ColumnInfo(name = "artistName")
    public String artistName;

    @ColumnInfo(name = "yearOfArt")
    public String yearOfArt;

    @ColumnInfo(name = "image")
    public byte[] image;

    public Art(String artName, String artistName, String yearOfArt, byte[] image) {
        this.artName = artName;
        this.artistName = artistName;
        this.yearOfArt = yearOfArt;
        this.image = image;
    }
}