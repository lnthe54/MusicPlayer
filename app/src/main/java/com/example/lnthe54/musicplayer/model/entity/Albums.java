package com.example.lnthe54.musicplayer.model.entity;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * @author lnthe54 on 8/20/2018
 * @project MusicPlayer
 */
public class Albums {
    private int id;
    private String nameAlbum;
    private String author;
    private String pathArtAlbum;
    private Bitmap artAlbum;
    private ArrayList<Songs> listSong;

    public Albums(int id, String nameAlbum, String author, String pathArtAlbum) {
        this.id = id;
        this.nameAlbum = nameAlbum;
        this.author = author;
        this.pathArtAlbum = pathArtAlbum;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String nameAlbum) {
        this.nameAlbum = nameAlbum;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPathArtAlbum() {
        return pathArtAlbum;
    }

    public void setPathArtAlbum(String pathArtAlbum) {
        this.pathArtAlbum = pathArtAlbum;
    }

    public Bitmap getArtAlbum() {
        return artAlbum;
    }

    public void setArtAlbum(Bitmap artAlbum) {
        this.artAlbum = artAlbum;
    }

    public ArrayList<Songs> getListSong() {
        return listSong;
    }

    public void setListSong(ArrayList<Songs> listSong) {
        this.listSong = listSong;
    }
}
