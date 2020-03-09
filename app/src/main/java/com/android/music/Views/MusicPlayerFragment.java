package com.android.music.Views;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.music.R;
import com.android.music.Utils.IRDefault;
import com.android.music.Utils.IRUtils;
import com.android.music.Widgets.CustomCircleImageView;
import com.android.music.Widgets.CustomTitleBar;

import java.util.ArrayList;

public class MusicPlayerFragment extends Fragment implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    public static final String KeyMusicPlayerFragment = "key_music_player_fragment";
    private static RootBaseActivity mActivity;
    private CustomCircleImageView mCustomCircleImageView;
    private CustomTitleBar mCustomTitleBar;
    private static AppCompatSeekBar mSeekBar;
    private AppCompatImageButton mBtnPrior, mBtnNext, mBtnStopStart, mBtnOption, mBtnMusicHistoryList;
    private TextView mPlayerNowTime;
    private TextView mPlayerTotalTime;
    private static int mNowPlayPosition;
    private HistoryMusicWindows mHistoryMusicWindows;
    private ConstraintLayout mPlayerFragmentLayout;
    OnCompletionBroadcast mReceiver;

    private ArrayList<Integer> mRangeControllerList;

    public MusicPlayerFragment() {}

    static MusicPlayerFragment newInstance() {
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyMusicPlayerFragment, RootBaseActivity.FLAG_MUSIC_PLAYER_FRAGMENT);
        musicPlayerFragment.setArguments(bundle);
        return musicPlayerFragment;
    }

    private class OnCompletionBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initData();
        }
    }


    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 0x11) {
                mSeekBar.setProgress(mNowPlayPosition);
                mPlayerNowTime.setText(IRUtils.calculateTime(mNowPlayPosition));
                mHandler.postDelayed(mMusicSeekBarRunnable, 1000);
                IRUtils.eLog("pzh", String.valueOf(mNowPlayPosition));
            }
            return false;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (RootBaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_music_player, container, false);
        initView(rootView);
        initData();
        initListener();
        return rootView;
    }

    private void initView(View rootView) {
        mCustomCircleImageView = rootView.findViewById(R.id.musicPicture);
        mCustomTitleBar = rootView.findViewById(R.id.customTitleBar);
        mBtnPrior = rootView.findViewById(R.id.mBtnPrevious);
        mBtnNext = rootView.findViewById(R.id.mBtnNext);
        mBtnStopStart = rootView.findViewById(R.id.mBtnPlay);
        mSeekBar = rootView.findViewById(R.id.mPlayProgress);
        mPlayerNowTime = rootView.findViewById(R.id.mPlayerNowTime);
        mPlayerTotalTime = rootView.findViewById(R.id.mPlayerTotalTime);
        mBtnOption = rootView.findViewById(R.id.mBtnOption);
        mBtnMusicHistoryList = rootView.findViewById(R.id.mBtnMusicHistoryList);
        mPlayerFragmentLayout = rootView.findViewById(R.id.mMusicPlayerFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initData() {
        String mMusicTitle = mActivity.mBinder.getNowPlayMusic().getTitle();
        String mMusicArtist = mActivity.mBinder.getNowPlayMusic().getArtist();
        mCustomTitleBar.setCenterArtistText(mMusicArtist);
        mCustomTitleBar.setCenterTitleText(mMusicTitle);
        mSeekBar.setMax(Math.toIntExact(mActivity.mBinder.getNowPlayMusic().getDuration()));
        mPlayerTotalTime.setText(IRUtils.calculateTime(mActivity.mBinder.getNowPlayMusic().getDuration()));
        Bitmap nowBackgroundBitmap = IRUtils.getAlbumPicture(mActivity.mBinder.getNowPlayMusic().getData());
        if (nowBackgroundBitmap != null) {
            mCustomCircleImageView.setImageBitmap(nowBackgroundBitmap);
            if (IRDefault.USE_RenderScript_Blur) {
                // 推荐
                mPlayerFragmentLayout.setBackground(new BitmapDrawable(IRUtils.getBlurBitmap(mActivity, IRUtils.getAlbumPicture(mActivity.mBinder.getNowPlayMusic().getData()))));
                mPlayerFragmentLayout.getBackground().setAlpha(125);
            } else {
                // todo : 异步获取图片并设置到背景图层
                Bitmap blurImage = IRUtils.gaussBlurUseGauss(nowBackgroundBitmap, 20);
                mPlayerFragmentLayout.setBackground(new BitmapDrawable(blurImage));
            }

        } else {
            mCustomCircleImageView.setImageResource(R.mipmap.ic_launcher);
        }
        configHandler();
        configMusicPlayButtonImage();
        initMusicController();
        mHistoryMusicWindows = new HistoryMusicWindows(R.layout.pop_music_layout, mActivity);
        mHistoryMusicWindows.addHistoryMusic(mActivity.mBinder.getNowPlayMusic());
    }

    public void updateForHistory() {
        initData();
    }

    private void initListener() {
        mCustomTitleBar.setLeftButtonOnClickListener(this);
        mCustomTitleBar.setRightButtonOnClickListener(this);
        mCustomCircleImageView.setOnClickListener(this);
        mBtnMusicHistoryList.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
        mBtnStopStart.setOnClickListener(this);
        mBtnPrior.setOnClickListener(this);
        mBtnNext.setOnClickListener(this);
        mBtnOption.setOnClickListener(this);
        IntentFilter mIntentFilter = new IntentFilter();
        mReceiver = new OnCompletionBroadcast();
        mIntentFilter.addAction("OnCompletePlayer");
        mActivity.registerReceiver(mReceiver, mIntentFilter);
    }

    private void configHandler() {
        Message msg = mHandler.obtainMessage();
        msg.what = 0x11;
        mHandler.sendMessage(msg);
    }

    private void initMusicController() {
        mRangeControllerList = new ArrayList<>(3);
        mRangeControllerList.add(IRDefault.ARRAY_RANDOM_MUSIC, R.drawable.ic_random);
        mRangeControllerList.add(IRDefault.ARRAY_ONE_MUSIC, R.drawable.ic_repeat_one);
        mRangeControllerList.add(IRDefault.ARRAY_ONE_LIST, R.drawable.ic_repeat);
        mBtnOption.setBackgroundResource(mRangeControllerList.get(getRangeTypeFromService()));
    }

    private int getRangeTypeFromService() {
        return mActivity.mBinder.arrayType;
    }

    private void setRangeType() {
        int typeCount = mActivity.mBinder.arrayType;
        if (typeCount == 2) {
            mActivity.mBinder.arrayType = 0;
        } else {
            mActivity.mBinder.arrayType += 1;
        }
    }

    void configMusicPlayButtonImage() {
        if (mActivity.mBinder.mIsPlaying) {
            mBtnStopStart.setBackgroundResource(R.drawable.ic_pause_circle);
        } else {
            mBtnStopStart.setBackgroundResource(R.drawable.ic_play_circle);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtnPlay:
                mActivity.mBinder.playMusic();
                configMusicPlayButtonImage();
                break;
            case R.id.mBtnNext:
                mActivity.mBinder.nextMusic();
                initData();
                break;
            case R.id.mBtnPrevious:
                mActivity.mBinder.preMusic();
                initData();
                break;
            case R.id.mBtnOption:
                setRangeType();
                mBtnOption.setBackgroundResource(mRangeControllerList.get(getRangeTypeFromService()));
                break;
            case R.id.leftImageButton:
                mActivity.onBackPressed();
                break;
            case R.id.mBtnMusicHistoryList:
                IRUtils.eLog("pzh", "pzh");
                mHistoryMusicWindows.showAtLocation(mActivity.findViewById(R.id.mMusicPlayerFragment), Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, 0, 0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int progress = seekBar.getProgress();
        mActivity.mBinder.seekToMusic(progress);
    }

    Runnable mMusicSeekBarRunnable = new Runnable() {
        @Override
        public void run() {
            mNowPlayPosition = mActivity.mBinder.getCurrentPosition();
            Message msg = mHandler.obtainMessage();
            msg.what = 0x11;
            mHandler.sendMessage(msg);
        }
    };
}
