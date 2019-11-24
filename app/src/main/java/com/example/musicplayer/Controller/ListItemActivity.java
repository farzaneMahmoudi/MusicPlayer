package com.example.musicplayer.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.example.musicplayer.R;

public class ListItemActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_fragment_list_item);
        if(fragment == null)
            fragmentManager.beginTransaction().add(R.id.container_fragment_list_item,ListItemsFragment.newInstance()).commit();

    }
}
