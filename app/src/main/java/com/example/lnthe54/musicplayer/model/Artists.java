package com.example.lnthe54.musicplayer.model;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class Artists {
    private int id;
    private String nameArtist;

    public Artists(int id, String nameArtist) {
        this.id = id;
        this.nameArtist = nameArtist;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameArtist() {
        return nameArtist;
    }

}
