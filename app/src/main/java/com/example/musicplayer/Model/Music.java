package com.example.musicplayer.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class Music {

    private Long id;
    private String title;
    private String album;
    private  String singer;
    private String coverMusic;
    private int position;
    private int duration;

    public String getDuration() {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public int getintDuration() {
        return this.duration;
    }

    public Music(Long id, String title, String album, String singer,String coverPic,int duration) {
        this.id = id;
        this.title = title;
        this.album = album;
        this.singer = singer;
        this.coverMusic = coverPic;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public String getSinger() {
        return singer;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getCoverMusic(){return coverMusic;}

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Music music = (Music) o;
        return position == music.position &&
                duration == music.duration &&
                Objects.equals(id, music.id) &&
                Objects.equals(title, music.title) &&
                Objects.equals(album, music.album) &&
                Objects.equals(singer, music.singer) &&
                Objects.equals(coverMusic, music.coverMusic);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, title, album, singer, coverMusic, position, duration);
    }
}
