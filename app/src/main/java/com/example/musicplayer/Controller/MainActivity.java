package com.example.musicplayer.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.Adapter.AlbumAdapter;
import com.example.musicplayer.Adapter.MusicAdapter;
import com.example.musicplayer.Adapter.SingerAdapter;
import com.example.musicplayer.Model.Album;
import com.example.musicplayer.Model.Music;
import com.example.musicplayer.Model.Singer;
import com.example.musicplayer.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

import static com.example.musicplayer.Controller.TabFragment.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;
import static com.example.musicplayer.Controller.TabFragment.TAG_ALBUM__FRAGMENT_SELECTED;
import static com.example.musicplayer.Controller.TabFragment.TAG_SINGER__FRAGMENT_SELECTED;

public class MainActivity extends AppCompatActivity implements ListItemSingerFragment.CallBackList, MusicAdapter.CallBack,
        ListItemsAlbumFragment.CallBackList,
        AlbumAdapter.CallBack, SingerAdapter.CallBack {

    public static final String TAG_TAB_FRAGMENT = "TAG_TAB_FRAGMENT";
    public static final String TAG_ALBUM__ITEM_SELECTED = "TAG_ALBUM__ITEM_SELECTED";
    public static final String TAG_SINGER__ITEM_SELECTED = "TAG_SINGER__ITEM_SELECTED";

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
    ImageButton pausePlayButtonExpand;
    ImageButton prevButtonExpand;
    ImageButton nextButtonExpand;
    ImageButton mImageButtonRepeat;
    ImageButton mImageButtonShuffle;
    LinearLayout linearBottosheet;
    TabLayout tabLayout;

    private FragmentManager fragmentManager;

    SeekBar mSeekBar;
    TextView mTextViewDuratin;
    TextView mTextViewChangingDuration;
    MediaPlayerControler mMediaPlayerControler;
    LinearLayout mLinearLayoutCollapseSheet;
    ImageView mImageViewExpandCoverSheet;
    private ConstraintLayout collapsedConstraintBottomSheet;

    private Music music;
    private int pos;
    private ArrayList<Music> mMusicArrayList = new ArrayList<>();
    private Handler mHandler = new Handler();


    private ListItemsAlbumFragment listItemsFragment;

    @Override
    public void onBackPressed() {


        listItemsFragment = (ListItemsAlbumFragment) getSupportFragmentManager().findFragmentByTag(TAG_ALBUM__ITEM_SELECTED);
        ListItemSingerFragment listItemSingerFragment = (ListItemSingerFragment) getSupportFragmentManager().findFragmentByTag(TAG_SINGER__ITEM_SELECTED);
        if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        if (listItemSingerFragment != null) {
            if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                getSupportFragmentManager().beginTransaction().show(listItemSingerFragment).commit();
            else {

                getSupportFragmentManager().beginTransaction().detach(listItemSingerFragment).remove(listItemSingerFragment).commit();
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(TAG_TAB_FRAGMENT)).commit();
            }
        }
        else if (listItemsFragment != null) {
            if(mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                getSupportFragmentManager().beginTransaction().show(listItemsFragment).commit();
            else {

                getSupportFragmentManager().beginTransaction().detach(listItemsFragment).remove(listItemsFragment).commit();
                getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(TAG_TAB_FRAGMENT)).commit();
            }
        } else {
            super.onBackPressed();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        //setAdapter();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container_fragment);
        if(fragment == null)
            fragmentManager.beginTransaction().add(R.id.container_fragment,TabFragment.newInstance(), TAG_TAB_FRAGMENT).commit();

        if (checkPermissionREAD_EXTERNAL_STORAGE(this))
            mMediaPlayerControler = MediaPlayerControler.getInstance(this);

        setListeners();


        @SuppressLint("HandlerLeak") final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int current_position = msg.what;
                String cTime = createTimeLabel(current_position);
                mTextViewChangingDuration.setText(cTime);
            }
        };

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {


           /*     int duration =0;
                if (music != null) {
                    mSeekBar.setMax(music.getintDuration() / 1000);
                   duration = music.getintDuration() / 1000;
                }

                if (progress >= duration && music != null) {
                    mMediaPlayerControler.getMediaPlayer().seekTo(0);
                    pos = (pos + 1) % (mMusicArrayList.size());
                    music = mMusicArrayList.get(pos);
                    updateBottomSheet(mMusicArrayList.get(pos));
                    mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(pos));
                }*/

                if (mMediaPlayerControler.getMediaPlayer() != null && fromUser) {
                    mTextViewChangingDuration.setText(createTimeLabel(progress));
                    mMediaPlayerControler.getMediaPlayer().seekTo(progress * 1000);
                    Message msg = new Message();
                    msg.what = mMediaPlayerControler.getMediaPlayer().getCurrentPosition();
                    handler.sendMessage(msg);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }


            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        mBottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN: {
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    }

                    case BottomSheetBehavior.STATE_EXPANDED: {

                        ListItemSingerFragment listItemSingerFragment =(ListItemSingerFragment) getSupportFragmentManager().findFragmentByTag(TAG_SINGER__FRAGMENT_SELECTED);
                        listItemsFragment = (ListItemsAlbumFragment) getSupportFragmentManager().findFragmentByTag(TAG_ALBUM__FRAGMENT_SELECTED);
                        if(listItemsFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(listItemsFragment).commit();

                        if(listItemSingerFragment != null)
                            getSupportFragmentManager().beginTransaction().hide(listItemSingerFragment).commit();

                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        mLinearLayoutCollapseSheet.setVisibility(View.GONE);
                        mTextViewDuratin.setText(music.getDuration());

                        break;
                    }
                    case BottomSheetBehavior.STATE_COLLAPSED: {

                        listItemsFragment = (ListItemsAlbumFragment) getSupportFragmentManager().findFragmentByTag(TAG_ALBUM__FRAGMENT_SELECTED);
                        if(listItemsFragment != null)
                            getSupportFragmentManager().beginTransaction().show(listItemsFragment).commit();
                        mLinearLayoutCollapseSheet.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        if (checkPermissionREAD_EXTERNAL_STORAGE(this)) {
            mMediaPlayerControler = MediaPlayerControler.getInstance(this);
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                if (mMediaPlayerControler.getMediaPlayer() != null) {
                    int mCurrentPosition = mMediaPlayerControler.getMediaPlayer().getCurrentPosition() / 1000;
                    mSeekBar.setProgress(mCurrentPosition);
                    Message msg = new Message();
                    msg.what = mMediaPlayerControler.getMediaPlayer().getCurrentPosition();
                    handler.sendMessage(msg);
                }
                mHandler.postDelayed(this, 1000);
            }
        }).start();

    }

    private void setListeners() {


        nextButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                pos = (pos + 1) % (mMusicArrayList.size());
                music = mMusicArrayList.get(pos);
                mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(music));
                updateBottomSheet(mMusicArrayList.get(pos));
            }
        });

        prevButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (pos == 0)
                    pos = mMusicArrayList.size() - 1;
                else
                    pos--;
                music = mMusicArrayList.get(pos);
                mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(music));
                updateBottomSheet(mMusicArrayList.get(pos));
            }
        });

        pausePlayButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mMediaPlayerControler.pausePlay()) {
                    pausePlayButton.setImageDrawable(getDrawable(R.drawable.play_button));
                    pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.play_button));
                } else {
                    pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
                    pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.pause_button));
                }
            }
        });


        nextButtonExpand.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                pos = (pos + 1) % (mMusicArrayList.size());
                music = mMusicArrayList.get(pos);
                mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(pos));
                updateBottomSheet(mMusicArrayList.get(pos));
            }
        });

        prevButtonExpand.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (pos == 0)
                    pos = mMusicArrayList.size() - 1;
                else
                    pos--;
                music = mMusicArrayList.get(pos);
                mMediaPlayerControler.play(getApplicationContext(), mMediaPlayerControler.getMusicUri(pos));
                updateBottomSheet(mMusicArrayList.get(pos));
            }
        });


        pausePlayButtonExpand.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (mMediaPlayerControler.pausePlay()) {
                    pausePlayButton.setImageDrawable(getDrawable(R.drawable.play_button));
                    pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.play_button));
                } else {
                    pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
                    pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.pause_button));
                }
            }
        });
        linearBottosheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        mImageButtonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mImageButtonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void findView() {
        mViewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tab_layout);

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

        mImageViewExpandCoverSheet = findViewById(R.id.imageView_music_cover_item_expand_bottom_sheet);
        mTextViewDuratin = findViewById(R.id.text_view_duration_under_seekbar);
        mTextViewChangingDuration = findViewById(R.id.text_view_changing_duration_under_seekbar);

        nextButtonExpand = findViewById(R.id.image_button_next_expand);
        prevButtonExpand = findViewById(R.id.image_button_previous_expand);
        pausePlayButtonExpand = findViewById(R.id.image_button_pause_play_expand);
        linearBottosheet = findViewById(R.id.linear_layout_bottom_sheet);
        collapsedConstraintBottomSheet = findViewById(R.id.constraint_collapsed_bottom_sheet);

        mImageButtonShuffle = findViewById(R.id.image_button_shuffle);
        mImageButtonRepeat = findViewById(R.id.image_button_repeat_all);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicList, Uri uri) {

        mMediaPlayerControler.play(this,uri);
        mSeekBar.setMax(music.getintDuration() / 1000);
        if (mMediaPlayerControler.flagIsPlaying == true) {
            pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
            pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.pause_button));
        }
        this.pos = pos;
        this.mMusicArrayList = musicList;
        this.music = music;

        singerTextView.setText(music.getSinger());
        musicTextView.setText(music.getTitle());
        mTextViewDuratin.setText(music.getDuration());

        if (BitmapFactory.decodeFile(music.getCoverMusic()) == null) {
            mImageViewPic.setImageResource(R.drawable.music_cover);
            mImageViewExpandCoverSheet.setImageResource(R.drawable.music_cover);
        } else {
            mImageViewPic.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mImageViewExpandCoverSheet.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void updateBottomSheet(Music music) {
        if (mMediaPlayerControler.flagIsPlaying == true) {
            pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
            pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.pause_button));
        }
        singerTextView.setText(music.getSinger());
        musicTextView.setText(music.getTitle());
        mTextViewDuratin.setText(music.getDuration());
        mSeekBar.setMax(music.getintDuration() / 1000);

        if (BitmapFactory.decodeFile(music.getCoverMusic()) == null) {
            mImageViewPic.setImageResource(R.drawable.music_cover);
            mImageViewExpandCoverSheet.setImageResource(R.drawable.music_cover);
        } else {
            mImageViewPic.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mImageViewExpandCoverSheet.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
        }

    }

    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;

        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
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


    @Override
    public void onItemAlbumSelected(Album album) {
        FragmentManager fm = getSupportFragmentManager();
        TabFragment tabFragment = (TabFragment) fm.findFragmentByTag(TAG_TAB_FRAGMENT);
        fm.beginTransaction().add(R.id.container_fragment,ListItemsAlbumFragment.newInstance(album), TAG_ALBUM__ITEM_SELECTED).hide(tabFragment).commit();
    }

    @Override
    public void onItemSingerSelected(Singer singer) {
        FragmentManager fm = getSupportFragmentManager();
        TabFragment tabFragment = (TabFragment) fm.findFragmentByTag(TAG_TAB_FRAGMENT);
        fm.beginTransaction().add(R.id.container_fragment,ListItemSingerFragment.newInstance(singer), TAG_SINGER__ITEM_SELECTED).hide(tabFragment).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onItemMusicSelected(Music music, int pos, ArrayList<Music> musicArrayList) {
        mSeekBar.setMax(music.getintDuration() / 1000);
        if (mMediaPlayerControler.flagIsPlaying == true) {
            pausePlayButton.setImageDrawable(getDrawable(R.drawable.pause_button));
            pausePlayButtonExpand.setImageDrawable(getDrawable(R.drawable.pause_button));
        }
        this.pos = pos;
        this.mMusicArrayList = musicArrayList;
        this.music = music;

        singerTextView.setText(music.getSinger());
        musicTextView.setText(music.getTitle());
        mTextViewDuratin.setText(music.getDuration());

        if (BitmapFactory.decodeFile(music.getCoverMusic()) == null) {
            mImageViewPic.setImageResource(R.drawable.music_cover);
            mImageViewExpandCoverSheet.setImageResource(R.drawable.music_cover);
        } else {
            mImageViewPic.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
            mImageViewExpandCoverSheet.setImageBitmap(BitmapFactory.decodeFile(music.getCoverMusic()));
        }
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
