package com.example.musicplayer.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Model.Album;
import com.example.musicplayer.R;

import java.io.Serializable;
import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.AlbumHolder> implements Serializable {


    private ArrayList<Album> itemsList;
    private Context mContext;
    private CallBack mCallBack;

    public AlbumAdapter(ArrayList itemsList, Context context) {
        this.itemsList = itemsList;
        mContext = context;
        if(context instanceof CallBack)
            mCallBack = (CallBack) context;
    }

    public void setAlbumList(ArrayList<Album> albumList) {
        itemsList = albumList;
    }

    @NonNull
    @Override
    public AlbumAdapter.AlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.album_item, parent, false);
        return new AlbumAdapter.AlbumHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumAdapter.AlbumHolder holder, int position) {
        holder.bind(itemsList.get(position), position, itemsList);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
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
                 mCallBack.onItemAlbumSelected(mAlbum);
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
    public interface CallBack {
        public void onItemAlbumSelected(Album album);
    }
}
