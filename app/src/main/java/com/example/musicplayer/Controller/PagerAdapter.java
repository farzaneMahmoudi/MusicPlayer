package com.example.musicplayer.Controller;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.musicplayer.Model.Album;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.Model.Singer;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    ArrayList mTabItemList = new ArrayList<>();

    ArrayList<Music> mMusicList = new ArrayList<>();
    ArrayList<Album> mAlbumList = new ArrayList<>();
    ArrayList<Singer> mSingerList = new ArrayList<>();

    String tagMusicTab;
    String tagAlbumTab;
    String tagSingerTab;

    public PagerAdapter(FragmentManager fm, ArrayList tabItemList) {
        super(fm);

        this.mMusicList = (ArrayList<Music>) tabItemList.get(0);
        this.mAlbumList = (ArrayList<Album>) tabItemList.get(1);
        this.mSingerList = (ArrayList<Singer>) tabItemList.get(2);

          mTabItemList = tabItemList;
    }

    @Override
    public Fragment getItem(int position) {
/*        TabState tabState = TabState.valueOf(mTabStatesList.get(position));
        return TabFragment.newInstance(tabState);*/
        switch (position) {
            case 0:return MusicFragment.newInstance(mMusicList);
            case 1:return AlbumFragment.newInstance(mAlbumList);
            case 2:return SingerFragment.newInstance(mSingerList);

        }
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mTabItemList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        switch (position) {
            case 0:
                tagMusicTab = createdFragment.getTag();
                break;
            case 1:
                tagAlbumTab = createdFragment.getTag();
                break;
            case 2:
                tagSingerTab = createdFragment.getTag();
                break;
        }
        return createdFragment;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "ARTISTS";
            case 1:
                return "ALBUMS";
            default:
                return "SONGS";
        }
    }
}

