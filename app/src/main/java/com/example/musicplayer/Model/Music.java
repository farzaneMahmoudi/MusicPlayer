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
        return String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        );
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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
