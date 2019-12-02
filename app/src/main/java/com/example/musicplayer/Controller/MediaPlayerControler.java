package com.example.musicplayer.Controller;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;

import com.example.musicplayer.Model.Album;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.Model.Singer;

import java.util.ArrayList;

public class MediaPlayerControler implements MediaPlayer.OnCompletionListener {

    private static ArrayList<Music> musics;
    private static ArrayList<Album> albums;
    private static ArrayList<Singer> singers;

    private static Context mContext;
    public static MediaPlayerControler instanse = new MediaPlayerControler();
    private MediaPlayer mMediaPlayer;
    public boolean flagIsPlaying;
    public static callback mCallback;


    public MediaPlayerControler() {
        mMediaPlayer = new android.media.MediaPlayer();
    }

    public static MediaPlayerControler getInstance(Context context) {
        if (mContext == null && musics == null) {
            if (context instanceof callback)
                mCallback = (callback) context;
            mContext = context.getApplicationContext();
            albums = loadAlbums(mContext);
            musics = loadMusics(mContext);
            singers = loadArtists(mContext);
        }
        return instanse;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public static ArrayList<Album> getAlbums() {
        if (albums == null)
            albums = loadAlbums(mContext);
        return albums;
    }


    public ArrayList<Music> getMusics() {
        if (musics == null)
            musics = loadMusics(mContext);
        return musics;
    }

    public ArrayList<Singer> getSingers() {
        if (singers == null)
            singers = loadArtists(mContext);
        return singers;
    }

    public static void setMusics(ArrayList<Music> musics) {
        MediaPlayerControler.musics = musics;
    }

    public Uri getMusicUri(int pos) {
        long currMusicId = musics.get(pos).getId();
        Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currMusicId);
        return musicUri;
    }

    public Uri getMusicUri(Music music) {
        long currMusicId = music.getId();
        Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currMusicId);
        return musicUri;
    }

    public static ArrayList<Album> loadAlbums(Context context) {
        ArrayList<Album> albumsList = new ArrayList<>();

        ContentResolver musicResolver = context.getContentResolver();
        Uri albumURI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Uri musicURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        Cursor albumCursor = musicResolver.query(albumURI, null, null, null, null);
        if (albumCursor != null && albumCursor.moveToFirst()) {
            do {
                String albumColumn = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                String singerColumn = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                Long idColumn = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String pictureColumn = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                String numOfSongs = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));

                albumsList.add(new Album(idColumn, albumColumn, singerColumn, pictureColumn, numOfSongs));

            } while (albumCursor.moveToNext());
        }
        albumCursor.close();
        albums = albumsList;
        return albumsList;
    }

    public static ArrayList<Singer> loadArtists(Context context) {
        ArrayList<Singer> singersList = new ArrayList<>();

        ContentResolver musicResolver = context.getContentResolver();
        Uri singerURI = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Uri albumURI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;

        Cursor singerCursor = musicResolver.query(singerURI, null, null, null, null);
        Cursor albumCursor = musicResolver.query(albumURI, null, null, null, null);

        if (singerCursor != null && singerCursor.moveToFirst()) {
            do {

                    if (albumCursor != null && albumCursor.moveToFirst())
                        do{
                        if(albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))
                            .equals(singerCursor.getString(singerCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST))))
                    {
                        String singerColumn = singerCursor.getString(singerCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                        String pictureColumn = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        Long idColumn = singerCursor.getLong(singerCursor.getColumnIndex(MediaStore.Audio.Artists._ID));

                        singersList.add(new Singer(idColumn, singerColumn,pictureColumn));
                    }
                }while (albumCursor.moveToNext());


            } while (singerCursor.moveToNext());
        }
        albumCursor.close();
        singerCursor.close();

        singers = singersList;
        return singers;
    }

    public static ArrayList<Music> getMusics(Context context, Album malbum) {
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
                    if (malbum.getAlbumName().equals(musicCursor.getString(albumColumn))) {
                        {
                            Long id = musicCursor.getLong(idColumn);
                            String title = musicCursor.getString(titleColumn);
                            String singer = musicCursor.getString(singerColumn);
                            String album = musicCursor.getString(albumColumn);
                            String picture = albumCursor.getString(pictureColumn);
                            int duration = musicCursor.getInt(durationcolumn);

                            musicslist.add(new Music(id, title, album, singer, picture, duration));
                        }
                    }
                }
            } while (musicCursor.moveToNext());
            albumCursor.close();
        }
        musicCursor.close();
        return musicslist;
    }

    public static ArrayList<Music> getMusics(Context context, Singer mSinger) {
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
                    if (mSinger.getSingerName().equals(musicCursor.getString(singerColumn))) {
                        {
                            Long id = musicCursor.getLong(idColumn);
                            String title = musicCursor.getString(titleColumn);
                            String singer = musicCursor.getString(singerColumn);
                            String album = musicCursor.getString(albumColumn);
                            String picture = albumCursor.getString(pictureColumn);
                            int duration = musicCursor.getInt(durationcolumn);

                            musicslist.add(new Music(id, title, album, singer, picture, duration));
                        }
                    }
                }
            } while (musicCursor.moveToNext());
            albumCursor.close();
        }
        musicCursor.close();
        return musicslist;
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

    public void play(Context context, Uri musicUri) {
        mMediaPlayer.release();
        mMediaPlayer = null;
        mMediaPlayer = android.media.MediaPlayer.create(context, musicUri);
        mMediaPlayer.start();
        flagIsPlaying = true;

    }


 /*   public MediaPlayerControler(Context context)  {
        mContext = context.getApplicationContext();
        musics = new ArrayList<>();
        mMediaPlayer = new android.media.MediaPlayer();
        }*/

    public boolean pausePlay() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            flagIsPlaying = false;
            return true;
        } else {
            mMediaPlayer.start();
            flagIsPlaying = true;
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int getPosition(Music musicItem) {
        for (Music music : musics) {
            if (music.equals(musicItem))
                return musics.indexOf(musicItem);
        }
        return 0;
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        mCallback.changeUi();

    }


    public interface callback {
        public void changeUi();
    }
}
