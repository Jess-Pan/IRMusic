package com.android.music.Views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.music.R;
import com.android.music.Utils.IRUtils;
import com.android.music.Widgets.CustomCircleImageView;
import com.android.music.Widgets.CustomTitleBar;

public class MusicPlayerFragment extends Fragment implements
        View.OnClickListener,
        SeekBar.OnSeekBarChangeListener {

    public static final String KeyMusicPlayerFragment = "key_music_player_fragment";
    private RootBaseActivity mActivity;
    private CustomCircleImageView mCustomCircleImageView;
    private CustomTitleBar mCustomTitleBar;
    private AppCompatSeekBar mSeekBar;
    private ImageButton mBtnPrior, mBtnNext, mBtnStopStart;
    private TextView mPlayerNowTime, mPlayerTotalTime;
    private String mMusicTitle, mMusicArtist;


    public MusicPlayerFragment() {}

    static MusicPlayerFragment newInstance(String flag) {
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyMusicPlayerFragment, flag);
        musicPlayerFragment.setArguments(bundle);
        return musicPlayerFragment;
    }

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
    }

    private void initData() {
        mMusicTitle = mActivity.mBinder.getNowPlayMusic().getTitle();
        mMusicArtist = mActivity.mBinder.getNowPlayMusic().getArtist();

        mCustomTitleBar.setCenterArtistText(mMusicArtist);
        mCustomTitleBar.setCenterTitleText(mMusicTitle);
    }

    private void initListener() {
        mCustomTitleBar.setLeftButtonOnClickListener(this);
        mCustomTitleBar.setRightButtonOnClickListener(this);
        mCustomCircleImageView.setOnClickListener(this);
        mSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
