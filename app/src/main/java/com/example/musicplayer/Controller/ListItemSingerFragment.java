package com.example.musicplayer.Controller;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.Model.Album;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.Model.Singer;
import com.example.musicplayer.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListItemSingerFragment extends Fragment {

    public static final String ARG_ALBUM = "ARG_ALBUM";
    public static final String ARG_SINGER = "ARG_SINGER";
    private Uri albumUri;
    private ArrayList musicList;
    private ListItemSingerFragment.Adapter musicAdapter;
    private RecyclerView mRecyclerView;
    private MediaPlayerControler mMediaPlayerControler;
    private CallBackList mCallBack;
    private ImageView mImageViewCoverAlbum;
    private TextView mTextViewSinger;
    private Singer mSinger;


    public static ListItemSingerFragment newInstance(Singer singer) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_ALBUM, singer);
        ListItemSingerFragment fragment = new ListItemSingerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ListItemSingerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSinger = (Singer) getArguments().getSerializable(ARG_ALBUM);
        mMediaPlayerControler = MediaPlayerControler.getInstance(getContext());

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CallBackList)
            mCallBack = (CallBackList) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallBack = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_item_singer, container, false);

        findView(view);

        setUI();
        setAdapter();

        return view;
    }

    private void findView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view_list_items);
        mImageViewCoverAlbum = view.findViewById(R.id.image_view_album_item_selected);
        mTextViewSinger = view.findViewById(R.id.text_view_singer_name_item_selected);
    }

    private void setUI() {
        mTextViewSinger.setText(mSinger.getSingerName());

        if (BitmapFactory.decodeFile(mSinger.getCoverMusic()) == null) {
            mImageViewCoverAlbum.setImageResource(R.drawable.music_cover);
        } else {
            mImageViewCoverAlbum.setImageBitmap(BitmapFactory.decodeFile(mSinger.getCoverMusic()));
        }

    }

    private void setAdapter() {
        musicList = mMediaPlayerControler.getMusics(getContext(), mSinger);
        musicAdapter = new Adapter(musicList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(musicAdapter);
    }

    private class MusicHolder extends RecyclerView.ViewHolder {

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
                    mMediaPlayerControler.play(getActivity(), musicUri);
                    int position = getPosition(mMusic);
                    mCallBack.onItemMusicSelected(mMusic, position, mMusics);
                }

            });
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        private int getPosition(Music m){
            for (Music music : mMusics) {
                if (music.equals(m))
                    return mMusics.indexOf(m);
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
            this.position = position;
            mTextViewMusic.setText(music.getTitle());
            mTextViewSinger.setText(music.getSinger());
            musicUri = mMediaPlayerControler.getMusicUri(mMusic);
            if (BitmapFactory.decodeFile(music.getCoverMusic()) == null)
                mImageViewMusicCover.setImageResource(R.drawable.music_cover);
            else
                mImageViewMusicCover.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mTextViewDuration.setText(music.getDuration());
        }

    }

    private class Adapter extends RecyclerView.Adapter<ListItemSingerFragment.MusicHolder> {

        private ArrayList<Music> itemsList;

        public Adapter(ArrayList itemsList) {
            this.itemsList = itemsList;
        }

        @NonNull
        @Override
        public ListItemSingerFragment.MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.music_item, parent, false);
            return new ListItemSingerFragment.MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ListItemSingerFragment.MusicHolder holder, int position) {
            holder.bind(itemsList.get(position), position, itemsList);
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }

    }

    public interface CallBackList {
        public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicArrayList);
    }

}
