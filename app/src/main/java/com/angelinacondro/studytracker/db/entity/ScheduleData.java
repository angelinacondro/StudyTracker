package com.angelinacondro.studytracker.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.angelinacondro.studytracker.db.DateConverter;
import com.angelinacondro.studytracker.db.dao.ScheduleCategoryDao;

import java.util.Date;

@Entity(tableName = "ScheduleData")
public class ScheduleData {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    @TypeConverters({DateConverter.class})
    private Date date;
    @ColumnInfo(name = "timeStarts")
    private String timeStarts;
    @ColumnInfo(name = "timeEnds")
    private String timeEnds;
    @ColumnInfo(name = "category")
    private String category;

//    public MainData(int ID, String title, Date date, Time timeStarts, Time timeEnds){
//        this.ID = ID;
//        this.title = title;
//        this.date = date;
//        this.timeStarts = timeStarts;
//        this.timeEnds = timeEnds;
//    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimeStarts() {
        return timeStarts;
    }

    public void setTimeStarts(String timeStarts) {
        this.timeStarts = timeStarts;
    }

    public String getTimeEnds() {
        return timeEnds;
    }

    public void setTimeEnds(String timeEnds) {
        this.timeEnds = timeEnds;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

