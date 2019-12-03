package com.example.musicplayer.Controller;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.Adapter.SingerAdapter;
import com.example.musicplayer.Model.Singer;

import com.example.musicplayer.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingerFragment extends Fragment {


    private static final String ARG_SINGER_LIST = "singer_list";
    private ArrayList<Singer> mSingerArrayList;
    private RecyclerView mRecyclerView;
    private SingerAdapter mSingerAdapter;

    public static SingerFragment newInstance(ArrayList<Singer> singerList) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_SINGER_LIST,singerList);
        SingerFragment fragment = new SingerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public SingerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSingerArrayList = (ArrayList<Singer>) getArguments().getSerializable(ARG_SINGER_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_singer, container, false);

        initUI(view);
        setAdapter();

        return view;
    }


    private void setAdapter() {


            mSingerAdapter = new SingerAdapter(mSingerArrayList,getContext());
            mRecyclerView.setAdapter(mSingerAdapter);

    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);

            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }

}
