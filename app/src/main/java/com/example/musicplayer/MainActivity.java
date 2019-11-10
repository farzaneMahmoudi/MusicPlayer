package com.example.musicplayer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.Model.Music;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.musicplayer.TabFragment.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements TabFragment.CallBack {

    private ViewPager mViewPager;
    private TabLayout mTableLayout;
    private PagerAdapter pagerAdapter;

    private BottomSheetBehavior mBottomSheetBehavior;
    TextView singerTextView;
    TextView musicTextView;
    private ImageView mImageViewPic;
    private CoordinatorLayout mCoordinatorLayout;
    ImageButton pausePlayButton;
    ImageButton prevButton;
    ImageButton nextButton;
    SeekBar mSeekBar;
    MediaPlayerControler mMediaPlayerControler;
    LinearLayout mLinearLayoutCollapseSheet;

    private int pos;
    private ArrayList<Music> mMusicArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

            findView();
            setAdapter();
            setListeners();
        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            mMediaPlayerControler = MediaPlayerControler.getInstance(this);
        }

    }

    private void setListeners() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (pos == mMusicArrayList.size() - 1)
                    pos = 0;
                else
                    pos++;
                mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(pos));
                updateBottomSheet(mMusicArrayList.get(pos));
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
               if(pos ==0)
                   pos = mMusicArrayList.size()-1;
               else
                   pos--;
                mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(pos));
                updateBottomSheet(mMusicArrayList.get(pos));
            }
        });

        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mMediaPlayerControler.pausePlay())
                    pausePlayButton.setImageDrawable(getDrawable(R.drawable.play_button));
                else
                    pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));

            }
        });

        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        mBottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.fragment_play_music));
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    }
                    case BottomSheetBehavior.STATE_EXPANDED:{
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        mLinearLayoutCollapseSheet.setVisibility(View.GONE);
                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED:{
                        mLinearLayoutCollapseSheet.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    private void setAdapter() {
        ArrayList<String> tabList = new ArrayList<>();
        tabList.add("musics");
        tabList.add("albums");
        tabList.add("singers");
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabList);
        mViewPager.setAdapter(pagerAdapter);
        mTableLayout.setupWithViewPager(mViewPager);
    }

    private void findView() {
        mViewPager = findViewById(R.id.viewpager);
        mTableLayout = findViewById(R.id.tab_layout);

        mCoordinatorLayout = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(mCoordinatorLayout);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        singerTextView = findViewById(R.id.text_view_singer_name_sheet);
        musicTextView = findViewById(R.id.text_view_music_name_sheet);
        mImageViewPic = findViewById(R.id.imageView_sheet);
        nextButton = findViewById(R.id.image_button_next);
        prevButton = findViewById(R.id.image_button_previous);
        pausePlayButton = findViewById(R.id.image_button_pause_play);
        mSeekBar = findViewById(R.id.seekBar);
        mLinearLayoutCollapseSheet = findViewById(R.id.linear_layout_icons_collapse_sheet);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicList) {
        if (mMediaPlayerControler.flagIsPlaying == true)
            pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
        this.pos = pos;
        this.mMusicArrayList = (ArrayList<Music>) musicList;
        singerTextView.setText(music.getSinger());
        musicTextView.setText(music.getTitle());
        if(BitmapFactory.decodeFile(music.getCoverMusic()) == null)
            mImageViewPic.setImageResource(R.drawable.music_cover);
            else

        mImageViewPic.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateBottomSheet(Music music) {
        if (mMediaPlayerControler.flagIsPlaying == true)
            pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
        singerTextView.setText(music.getSinger());
        musicTextView.setText(music.getTitle());
        mImageViewPic.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context, Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[]{permission},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
}
