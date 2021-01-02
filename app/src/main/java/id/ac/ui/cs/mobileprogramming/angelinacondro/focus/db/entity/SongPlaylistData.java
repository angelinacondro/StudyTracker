package id.ac.ui.cs.mobileprogramming.angelinacondro.focus.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "SongPlaylistData")
public class SongPlaylistData {

    @PrimaryKey(autoGenerate = true)
    private int ID;

    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "artist")
    private String artists;
    @ColumnInfo(name = "data")
    private String data;
    @ColumnInfo(name = "user")
    private String user;


    public SongPlaylistData(String title, String artists, String data, String user){
        this.title = title;
        this.artists = artists;
        this.data = data;
        this.user = user;
    }

    public SongPlaylistData(){

    }

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

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

}

