package com.angelinacondro.studytracker.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "scheduleCategory")
public class ScheduleCategoryData {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="category_id")
    private int ID;

    @ColumnInfo(name = "title")
    private String category;

    public ScheduleCategoryData(String category){
        this.category = category;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
