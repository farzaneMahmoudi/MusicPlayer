package com.example.musicplayer;
import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;


public class PlayMusicActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context, PlayMusicActivity.class);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        return PlayMusicFragment.newInstance();
    }
}
