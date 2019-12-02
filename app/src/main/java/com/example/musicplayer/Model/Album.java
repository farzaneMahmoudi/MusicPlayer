package com.example.musicplayer.Model;

import java.io.Serializable;

public class Album implements Serializable {

    private Long id;
    private String albumName;
    private String singer;
    private String coverMusic;
    private String numOfSongs;

    public Album(Long id, String albumName, String singer, String coverMusic,String numOfSongs) {
        this.id = id;
        this.albumName = albumName;
        this.singer = singer;
        this.coverMusic = coverMusic;
        this.numOfSongs = numOfSongs;
    }

    public String getNumOfSongs() {
        return numOfSongs;
    }

    public Long getId() {
        return id;
    }

    public String getCoverMusic() {
        return coverMusic;
    }

    public String getAlbumName() {
        return albumName;
    }


    public String getSinger() {
        return singer;
    }


}
