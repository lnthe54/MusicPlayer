package com.example.lnthe54.musicplayer.model;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class Artists {
    private String nameArtist;
    private String inforArtist;

    public Artists() {
    }

    public Artists(String nameArtist, String inforArtist) {
        this.nameArtist = nameArtist;
        this.inforArtist = inforArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }

    public void setNameArtist(String nameArtist) {
        this.nameArtist = nameArtist;
    }

    public String getInforArtist() {
        return inforArtist;
    }

    public void setInforArtist(String inforArtist) {
        this.inforArtist = inforArtist;
    }
}
