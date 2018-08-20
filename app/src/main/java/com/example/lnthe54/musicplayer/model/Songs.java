package com.example.lnthe54.musicplayer.model;

/**
 * @author lnthe54 on 8/20/2018
 * @project MusicPlayer
 */
public class Songs extends Albums {
    private String nameSong;
    private String author;

    public Songs() {
    }

    public Songs(String nameSong, String author) {
        this.nameSong = nameSong;
        this.author = author;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
