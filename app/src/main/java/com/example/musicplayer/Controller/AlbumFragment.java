package com.example.musicplayer.Controller;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.Adapter.AlbumAdapter;

import com.example.musicplayer.Model.Album;
import com.example.musicplayer.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumFragment extends Fragment {


    public static final String ARG_ALBUM_LIST = "albumList";
    private ArrayList<Album> mAlbumArrayList;
    private AlbumAdapter mAlbumAdapter;
    private RecyclerView mRecyclerView;

    public static AlbumFragment newInstance(ArrayList<Album> albumList) {

        Bundle args = new Bundle();

        args.putSerializable(ARG_ALBUM_LIST,albumList);
        AlbumFragment fragment = new AlbumFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbumArrayList = (ArrayList<Album>) getArguments().getSerializable(ARG_ALBUM_LIST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_album, container, false);

        initUI(view);
        setAdapter();

        return view;
    }


    private void setAdapter() {
        mAlbumAdapter = new AlbumAdapter(mAlbumArrayList,getContext());
        mRecyclerView.setAdapter(mAlbumAdapter);
    }

    private void initUI(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);

            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }

}
