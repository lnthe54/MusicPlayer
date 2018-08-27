package com.example.lnthe54.musicplayer.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author lnthe54 on 8/20/2018
 * @project MusicPlayer
 */
public class Songs implements Parcelable {
    private String nameSong;
    private String author;

    public Songs(String nameSong, String author) {
        this.nameSong = nameSong;
        this.author = author;
    }

    public String getNameSong() {
        return nameSong;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Songs createFromParcel(Parcel in) {
            return new Songs(in);
        }

        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };

    public Songs(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        this.nameSong = data[0];
        this.author = data[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringArray(new String[]{this.nameSong, this.author,});
    }
}
