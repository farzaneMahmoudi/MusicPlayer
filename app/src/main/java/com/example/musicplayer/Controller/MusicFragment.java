package com.example.musicplayer.Controller;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.Adapter.MusicAdapter;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment {

    public static final String ARG_MUSIC_LIST = "musicList";

    private  ArrayList<Music> mMusicArrayList;
    private RecyclerView mRecyclerView;
    private MusicAdapter musicAdapter;

    public static MusicFragment newInstance(ArrayList<Music> musicArrayList) {

        Bundle args = new Bundle();


        args.putSerializable(ARG_MUSIC_LIST,musicArrayList);
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(args);
        return fragment;
    }
    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMusicArrayList = (ArrayList<Music>) getArguments().getSerializable(ARG_MUSIC_LIST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_music, container, false);

        initUI(view);
        setAdapter();

        return view;
    }

    private void setAdapter() {
            musicAdapter = new MusicAdapter(mMusicArrayList,getActivity());
            mRecyclerView.setAdapter(musicAdapter);
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


}
