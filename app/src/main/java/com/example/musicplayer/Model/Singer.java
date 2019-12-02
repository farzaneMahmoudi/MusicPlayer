package com.example.musicplayer.Model;

import java.io.Serializable;

public class Singer implements Serializable {

    private Long id;
    private String singerName;
    private String coverMusic;

    public String getCoverMusic() {
        return coverMusic;
    }

    public Singer(Long id, String singerName,String coverMusic) {
        this.id = id;
        this.singerName = singerName;
        this.coverMusic = coverMusic;
    }

    public Long getId() {
        return id;
    }

    public String getSingerName() {
        return singerName;
    }

}
