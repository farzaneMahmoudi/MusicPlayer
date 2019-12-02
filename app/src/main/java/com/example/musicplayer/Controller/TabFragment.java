package com.example.musicplayer.Controller;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.musicplayer.Model.TabState;
import com.example.musicplayer.R;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {


    public static final String ARG_POSITION_TAB = "arg_position_tab";
    public static final String ARG_TAB_STATE = "tab_State";
    public static final String EXTRA_ITEM = "Extra_item";
    public static final String TAG_ALBUM__FRAGMENT_SELECTED = "fragment_album_selected";
    public static final String TAG_SINGER__FRAGMENT_SELECTED = "fragment_singer_selected";


    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    private RecyclerView mRecyclerView;
    private Adapter musicAdapter;
    private AlbumAdapter mAlbumAdapter;
    private SingerAdapter mSingerAdapter;


    private ImageView mImageViewCoverAlbum;
    private TextView mTextViewSinger;
    private TextView mTextView;

    private CallBack mCallBack;
    private ArrayList<Music> musicList;
    private ArrayList<Album> albumList;
    private ArrayList<Singer> singerList;

    private boolean flagIsPlaying;
    private MediaPlayerControler mMediaPlayerControler;
    private boolean itemClick;

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


        findView(view);

        initUI(view);

        musicList = mMediaPlayerControler.getMusics();
        albumList = mMediaPlayerControler.getAlbums();
        singerList = mMediaPlayerControler.getSingers();

        setAdapter();

        return view;
    }

    private void findView(View view) {
        mImageViewCoverAlbum = view.findViewById(R.id.image_view_album_item_selected);
        mTextViewSinger = view.findViewById(R.id.text_view_singer_name_item_selected);
        mTextView = view.findViewById(R.id.text_view_album_name_item_selected);
    }

    private void setAdapter() {

        if (mTabState == TabState.musics || mTabState == null) {
            musicAdapter = new Adapter(musicList);
            mRecyclerView.setAdapter(musicAdapter);
        }

        if (mTabState == TabState.albums) {
            mAlbumAdapter = new AlbumAdapter(albumList);
            mRecyclerView.setAdapter(mAlbumAdapter);
        }

        if (mTabState == TabState.singers) {
            mSingerAdapter = new SingerAdapter(singerList);
            mRecyclerView.setAdapter(mSingerAdapter);
        }

    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);

        if (mTabState == TabState.musics || mTabState == null )
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (mTabState == TabState.albums)
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        if (mTabState == TabState.singers)
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mRecyclerView.setPadding(8, 8, 8, 90);
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
                    position = mMediaPlayerControler.getPosition(mMusic);
                    mCallBack.onItemMusicSelected(mMusic, position, mMusics);
                 //   itemClick = true;
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
            if (BitmapFactory.decodeFile(music.getCoverMusic()) == null)
                mImageViewMusicCover.setImageResource(R.drawable.music_cover);
            else
                mImageViewMusicCover.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mTextViewDuration.setText(music.getDuration());
        }

    }

    private class Adapter extends RecyclerView.Adapter<MusicHolder> {

        private ArrayList<Music> itemsList;

        public Adapter(ArrayList itemsList) {
            this.itemsList = itemsList;
        }

        public void setMusicList(ArrayList<Music> musicList) {
            itemsList = musicList;
        }

        @NonNull
        @Override
        public MusicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.music_item, parent, false);
            return new MusicHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MusicHolder holder, int position) {
            holder.bind(itemsList.get(position), position, itemsList);
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }

    }

    public class SingerHolder extends RecyclerView.ViewHolder implements Serializable {
        private Singer mSinger;
        private TextView mTextView;
        private ImageView mImageViewAlbumCover;


        public SingerHolder(@NonNull final View itemView) {
            super(itemView);

            findView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(TabFragment.this).commit();
                    mCallBack.hideTab();
                    fragmentManager.beginTransaction().add(R.id.container_fragment, ListItemSingerFragment.newInstance(mSinger), TAG_SINGER__FRAGMENT_SELECTED)
                            .commit();
                }

            });
        }

        private void findView(@NonNull View itemView) {

            findViewHolderItems(itemView);
        }

        private void findViewHolderItems(@NonNull View itemView) {
            mTextView = itemView.findViewById(R.id.text_view_singer_name_s_item);
            mImageViewAlbumCover = itemView.findViewById(R.id.image_view_album_cover);
        }

        public void bind(Singer singer, int position, ArrayList<Singer> singerArrayList) {
            this.mSinger = singer;
            mTextView.setText(singer.getSingerName());

            if (BitmapFactory.decodeFile(singer.getCoverMusic()) == null)
                mImageViewAlbumCover.setImageResource(R.drawable.music_cover);
            else
                mImageViewAlbumCover.setImageBitmap(BitmapFactory.decodeFile(singer.getCoverMusic()));
        }

    }

    private class SingerAdapter extends RecyclerView.Adapter<SingerHolder> {

        private ArrayList<Singer> itemsList;

        public SingerAdapter(ArrayList<Singer> itemsList) {
            this.itemsList = itemsList;
        }

        public void setAlbumList(ArrayList<Singer> albumList) {
            itemsList = albumList;
        }

        @NonNull
        @Override
        public SingerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.singer_item, parent, false);
            return new SingerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull SingerHolder holder, int position) {
            holder.bind(itemsList.get(position), position, itemsList);
        }


        @Override
        public int getItemCount() {
            return itemsList.size();
        }
    }

    public class AlbumHolder extends RecyclerView.ViewHolder implements Serializable {

        private Album mAlbum;
        private TextView mTextViewAlbum;
        private TextView mTextViewSinger;
        private ImageView mImageViewAlbumCover;
        private CoordinatorLayout mAlbumFragment;

        private Uri albumUri;
        private ArrayList<Album> mAlbums;

        public AlbumHolder(@NonNull final View itemView) {
            super(itemView);

            findView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().hide(TabFragment.this).commit();
                    mCallBack.hideTab();
                    fragmentManager.beginTransaction().add(R.id.container_fragment, ListItemsAlbumFragment.newInstance(mAlbum), TAG_ALBUM__FRAGMENT_SELECTED)
                            .commit();

                }
            });
        }

        private void findView(@NonNull View itemView) {

            findViewHolderItems(itemView);
        }

        private void findViewHolderItems(@NonNull View itemView) {
            mTextViewAlbum = itemView.findViewById(R.id.text_view_album_name);
            mTextViewSinger = itemView.findViewById(R.id.text_view_album_singer_name);
            mImageViewAlbumCover = itemView.findViewById(R.id.image_view_album_cover);
        }

        public void bind(Album album, int position, ArrayList<Album> albumArrayList) {
            this.mAlbum = album;
            this.mAlbums = albumArrayList;

            mTextViewAlbum.setText(album.getAlbumName());
            mTextViewSinger.setText(album.getSinger());
            if (BitmapFactory.decodeFile(album.getCoverMusic()) == null)
                mImageViewAlbumCover.setImageResource(R.drawable.music_cover);
            else
                mImageViewAlbumCover.setImageBitmap(BitmapFactory.decodeFile(album.getCoverMusic()));

        }

    }

    private class AlbumAdapter extends RecyclerView.Adapter<AlbumHolder> {

        private ArrayList<Album> itemsList;

        public AlbumAdapter(ArrayList itemsList) {
            this.itemsList = itemsList;
        }

        public void setAlbumList(ArrayList<Album> albumList) {
            itemsList = albumList;
        }

        @NonNull
        @Override
        public AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.album_item, parent, false);
            return new AlbumHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AlbumHolder holder, int position) {
            holder.bind(itemsList.get(position), position, itemsList);
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }
    }

    public interface CallBack {
        public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicArrayList);
        public void hideTab();

    }

}
