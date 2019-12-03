package com.example.musicplayer.Adapter;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Controller.TabFragment;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.R;

import java.io.Serializable;
import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicHolder> implements Serializable {


    private ArrayList<Music> itemsList;
    private Context mContext;
    private CallBack mCallBack;

    public MusicAdapter(ArrayList itemsList, Context context) {
        this.itemsList = itemsList;
        mContext = context;

        if(context instanceof  CallBack)
            mCallBack = (MusicAdapter.CallBack) context;
    }

    public void setMusicList(ArrayList<Music> musicList) {
        itemsList = musicList;
    }

    @NonNull
    @Override
    public MusicAdapter.MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.music_item, parent, false);
        return new MusicAdapter.MusicHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.MusicHolder holder, int position) {
        holder.bind(itemsList.get(position), position, itemsList);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class MusicHolder extends RecyclerView.ViewHolder {

        private Music mMusic;
        private TextView mTextViewMusic;
        private TextView mTextViewSinger;
        private ImageView mImageViewMusicCover;
        private Uri musicUri;
        private TextView mTextViewDuration;
        private int position;
        private ArrayList<Music> mMusics;


        public MusicHolder(@NonNull final View itemView) {
            super(itemView);

            findView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
               //    mMediaPlayerControler.play(getActivity(), musicUri);
                    position = getPosition(mMusic);
                    mCallBack.onItemMusicSelected(mMusic, position, mMusics,musicUri);
                }

            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public int getPosition(Music musicItem) {
            for (Music music : mMusics) {
                if (music.equals(musicItem))
                    return mMusics.indexOf(musicItem);
            }
            return 0;
        }


        private void findView(@NonNull View itemView) {

            findViewHolderItems(itemView);
        }

        private void findViewHolderItems(@NonNull View itemView) {
            mTextViewMusic = itemView.findViewById(R.id.text_view_music_name);
            mTextViewSinger = itemView.findViewById(R.id.text_view_singer_name);
            mImageViewMusicCover = itemView.findViewById(R.id.imageView_music_cover_item);
            mTextViewDuration = itemView.findViewById(R.id.text_view_music_duration);
        }

        public void bind(Music music, int position, ArrayList<Music> musics) {
            this.mMusics = musics;
            this.mMusic = music;
            mTextViewMusic.setText(music.getTitle());
            mTextViewSinger.setText(music.getSinger());
            musicUri =getMusicUri(mMusic);
            if (BitmapFactory.decodeFile(music.getCoverMusic()) == null)
                mImageViewMusicCover.setImageResource(R.drawable.music_cover);
            else
                mImageViewMusicCover.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mTextViewDuration.setText(music.getDuration());
        }

        public Uri getMusicUri(Music music) {
            long currMusicId = music.getId();
            Uri musicUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currMusicId);
            return musicUri;
        }
    }

    public interface CallBack {
        public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicArrayList,Uri uri);
    }
}
