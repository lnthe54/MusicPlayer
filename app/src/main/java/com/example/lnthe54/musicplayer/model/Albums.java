package com.example.lnthe54.musicplayer.model;

/**
 * @author lnthe54 on 8/20/2018
 * @project MusicPlayer
 */
public class Albums {
    protected String nameAlbum;
    private String author;
    private int image;

    public Albums() {
    }

    public Albums(int image, String nameAlbum, String author) {
        this.image = image;
        this.nameAlbum = nameAlbum;
        this.author = author;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
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
}
