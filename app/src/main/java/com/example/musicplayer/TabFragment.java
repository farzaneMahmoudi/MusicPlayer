package com.example.musicplayer;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.Model.Music;
import com.example.musicplayer.Model.TabState;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {


    public static final String ARG_POSITION_TAB = "arg_position_tab";
    public static final String ARG_TAB_STATE = "tab_State";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private RecyclerView mRecyclerView;
    private musicAdapter musicAdapter;
    private BottomSheetBehavior mBottomSheetBehavior;
    private CallBack mCallBack;
    private ArrayList<Music> musicList;

    private boolean flagIsPlaying;
    private MediaPlayerControler mMediaPlayerControler;

    private android.media.MediaPlayer mMediaPlayer;

    private TabState mTabState;

    public static TabFragment newInstance(TabState tabState) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_TAB_STATE, tabState);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof CallBack)
            mCallBack = (CallBack) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTabState = (TabState) getArguments().getSerializable(ARG_TAB_STATE);

        mMediaPlayerControler = MediaPlayerControler.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab, container, false);

        initUI(view);
        setAdapter();

        return view;
    }

    private void setAdapter() {


        if (mTabState == TabState.musics) {




            /*    Collections.sort(mMediaPlayerControler.getMusicsList(getActivity()), new Comparator<Music>() {
                    public int compare(Music a, Music b) {
                        return a.getTitle().compareTo(b.getTitle());
                    }
                });*/
            musicList = mMediaPlayerControler.getMusics();
            musicAdapter = new musicAdapter(musicList);
            mRecyclerView.setAdapter(musicAdapter);


        }

    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        if (mTabState == TabState.musics)
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setPadding(8, 8, 8, 90);
    }

    private class musicHolder extends RecyclerView.ViewHolder {

        private Music mMusic;
        private TextView mTextViewMusic;
        private TextView mTextViewSinger;
        private ImageView mImageViewMusicCover;
        private Uri musicUri;
        private TextView mTextViewDuration;
        private int position;
        private ArrayList<Music> mMusics;

        public musicHolder(@NonNull final View itemView) {
            super(itemView);

            findView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {

                    mMediaPlayerControler.play(getActivity(), musicUri);
                    position = mMediaPlayerControler.getPosition(mMusic);
                    mCallBack.onItemMusicSelected(mMusic, position, mMusics);
                }

            });
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
            //   this.position = position;
            mTextViewMusic.setText(music.getTitle());
            mTextViewSinger.setText(music.getSinger());
            musicUri = mMediaPlayerControler.getMusicUri(mMusic);
            if(BitmapFactory.decodeFile(music.getCoverMusic()) == null)
                mImageViewMusicCover.setImageResource(R.drawable.music_cover);
            else
            mImageViewMusicCover.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mTextViewDuration.setText(music.getDuration());
        }

    }

    private class musicAdapter extends RecyclerView.Adapter<musicHolder> {

        private ArrayList<Music> mMusicList;

        public musicAdapter(ArrayList<Music> musicList) {
            mMusicList = musicList;
        }

        public void setMusicList(ArrayList<Music> musicList) {
            mMusicList = musicList;
        }

        @NonNull
        @Override
        public musicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.music_item, parent, false);
            return new musicHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull musicHolder holder, int position) {
            holder.bind(mMusicList.get(position), position, mMusicList);
        }

        @Override
        public int getItemCount() {
            return mMusicList.size();
        }

    }


    public interface CallBack {
        public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicArrayList);
    }

}
