package com.example.musicplayer;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.example.musicplayer.Model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MediaPlayerControler {

    public static final String TAG = "Musics";

    private static ArrayList<Music> musics;
    private static Context mContext;
    public static MediaPlayerControler instanse = new MediaPlayerControler();
    private static android.media.MediaPlayer mMediaPlayer;
    public  boolean flagIsPlaying;

    public static MediaPlayerControler getInstance(Context context){
        if(mContext ==null && musics==null &&mMediaPlayer == null) {
            mContext = context.getApplicationContext();
            mMediaPlayer = new android.media.MediaPlayer();
        }
        return instanse;
    }

    public MediaPlayerControler(){}

    public ArrayList<Music> getMusics() {
        if(musics == null)
            musics = loadMusics(mContext);
        return musics;
    }

    public static void setMusics(ArrayList<Music> musics) {
        MediaPlayerControler.musics = musics;
    }

    public Uri getMusicUri(int pos){
        long currMusicId =   musics.get(pos).getId();
        Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currMusicId);
        return musicUri;
    }

    public Uri getMusicUri(Music music) {
        long currMusicId = music.getId();
        Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currMusicId);
        return musicUri;
    }

    public static ArrayList<Music> loadMusics(Context context) {

        ArrayList<Music> musicslist = new ArrayList<>();


        ContentResolver musicResolver = context.getContentResolver();

        Uri musicURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumURI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor musicCursor = musicResolver.query(musicURI, null, null, null, null);
        Cursor albumCursor;
        if (musicCursor != null && musicCursor.moveToFirst()) {

            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int albumColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ALBUM);
            int singerColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.ARTIST);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int durationcolumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.DURATION);

            do {
                Long albumId = Long.valueOf(musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                albumCursor = musicResolver.query(albumURI,
                        new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                        MediaStore.Audio.Albums._ID + "=" + albumId, null, null);

                int pictureColumn = albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);

                if (albumCursor != null && albumCursor.moveToFirst()) {
                    Long id = musicCursor.getLong(idColumn);
                    String title = musicCursor.getString(titleColumn);
                    String singer = musicCursor.getString(singerColumn);
                    String album = musicCursor.getString(albumColumn);
                    String picture = albumCursor.getString(pictureColumn);
                    int duration = musicCursor.getInt(durationcolumn);

                    musicslist.add(new Music(id, title, album, singer, picture, duration));
                }
            } while (musicCursor.moveToNext());
            albumCursor.close();

        }

        musicCursor.close();

        musics = musicslist;
        return musicslist;
    }

    public void play(Context context,Uri musicUri) {
            mMediaPlayer.release();
            mMediaPlayer = null;
            mMediaPlayer = android.media.MediaPlayer.create(context, musicUri);
            mMediaPlayer.start();
            flagIsPlaying=true;

    }


 /*   public MediaPlayerControler(Context context)  {
        mContext = context.getApplicationContext();
        musics = new ArrayList<>();
        mMediaPlayer = new android.media.MediaPlayer();
        }*/

        public boolean pausePlay() {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                flagIsPlaying=false;
                return true;
            } else {
                mMediaPlayer.start();
                flagIsPlaying=true;
                return false;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public int getPosition(Music musicItem){
            for(Music music : musics){
                if(music.equals(musicItem))
                    return musics.indexOf(musicItem);
            }
            return 0;
        }

}
