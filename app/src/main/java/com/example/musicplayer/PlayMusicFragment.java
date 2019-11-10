package com.example.musicplayer;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment {


    private SeekBar mSeekBar;
    private ImageButton mImageButtonPsuse;
    private ImageButton mImageButtonPlay;
    private ImageButton mImageButtonNext;
    private ImageButton mImageButtonPreviouse;

    public static PlayMusicFragment newInstance() {

        Bundle args = new Bundle();

        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public PlayMusicFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_play_music, container, false);

        findView(view);
        setOnClickLisener();


        return view;
    }

    private void setOnClickLisener() {
        mImageButtonPsuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mImageButtonPreviouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mImageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mImageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void findView(View view) {
        mImageButtonNext = view.findViewById(R.id.image_button_next);
        mImageButtonPreviouse = view.findViewById(R.id.image_button_previous);
        mImageButtonPsuse = view.findViewById(R.id.image_button_pause);
    }


}
