package com.example.musicplayer.Adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.Controller.ListItemSingerFragment;
import com.example.musicplayer.Controller.TabFragment;
import com.example.musicplayer.Model.Album;
import com.example.musicplayer.Model.Singer;
import com.example.musicplayer.R;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.musicplayer.Controller.TabFragment.TAG_SINGER__FRAGMENT_SELECTED;

public class SingerAdapter extends RecyclerView.Adapter<SingerAdapter.SingerHolder> implements Serializable{



    private ArrayList<Singer> itemsList;
    private Context mContext;
    private CallBack mCallBack;

    public SingerAdapter(ArrayList<Singer> itemsList,Context context) {
        this.itemsList = itemsList;
        mContext = context;

        if(context instanceof CallBack)
            mCallBack = (CallBack) context;
    }

    public void setAlbumList(ArrayList<Singer> albumList) {
        itemsList = albumList;
    }

    @NonNull
    @Override
    public SingerAdapter.SingerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.singer_item, parent, false);
        return new SingerAdapter.SingerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingerAdapter.SingerHolder holder, int position) {
        holder.bind(itemsList.get(position), position, itemsList);
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
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
                    mCallBack.onItemSingerSelected(mSinger);
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

    public interface CallBack {
        public void onItemSingerSelected(Singer singer);
    }
}
