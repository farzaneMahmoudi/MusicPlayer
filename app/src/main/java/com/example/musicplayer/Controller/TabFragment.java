package com.example.musicplayer.Controller;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicplayer.Model.Album;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.Model.Singer;

import com.example.musicplayer.R;
import com.google.android.material.tabs.TabLayout;

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

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private PagerAdapter pagerAdapter;

    private ImageView mImageViewCoverAlbum;
    private TextView mTextViewSinger;
    private TextView mTextView;

    private ArrayList<Music> musicList;
    private ArrayList<Album> albumList;
    private ArrayList<Singer> singerList;

    private MediaPlayerControler mMediaPlayerControler;



    public static TabFragment newInstance() {

        Bundle args = new Bundle();

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

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         mMediaPlayerControler = MediaPlayerControler.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab, container, false);


        findView(view);

        musicList = mMediaPlayerControler.getMusics();
        albumList = mMediaPlayerControler.getAlbums();
        singerList = mMediaPlayerControler.getSingers();
        setPagerAdapter(view);

        return view;
    }

    private void findView(View view) {
        mImageViewCoverAlbum = view.findViewById(R.id.image_view_album_item_selected);
        mTextViewSinger = view.findViewById(R.id.text_view_singer_name_item_selected);
        mTextView = view.findViewById(R.id.text_view_album_name_item_selected);
    }



    private void setPagerAdapter(View view) {

        mViewPager = view.findViewById(R.id.viewpager);
        mTabLayout = view.findViewById(R.id.tab_layout);
        ArrayList tabItemList = new ArrayList<>();
        tabItemList.add(musicList);
        tabItemList.add(albumList);
        tabItemList.add(singerList);
        pagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager(), tabItemList);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(pagerAdapter);

    }

}
