package com.example.musicplayer;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.musicplayer.Model.TabState;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> mTabStatesList = new ArrayList<>();

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
    public int getCount() {
        return mTabStatesList.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTabStatesList.get(position);
    }
}

