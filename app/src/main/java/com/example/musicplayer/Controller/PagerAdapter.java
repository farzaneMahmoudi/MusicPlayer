package com.example.musicplayer.Controller;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.musicplayer.Model.TabState;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    ArrayList<String> mTabStatesList = new ArrayList<>();
    String tagMusicTab;
    String tagAlbumTab;
    String tagSingerTab;

    public PagerAdapter(FragmentManager fm, ArrayList<String> tabStatesList) {
        super(fm);

        mTabStatesList = tabStatesList;
    }

    @Override
    public Fragment getItem(int position) {
        TabState tabState = TabState.valueOf(mTabStatesList.get(position));
        return TabFragment.newInstance(tabState);

    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return mTabStatesList.size();
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
        return mTabStatesList.get(position);
    }
}

