package com.android.music.Views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.music.R;
import com.android.music.Utils.IRUtils;

public class MusicPlayerFragment extends Fragment {

    public static final String KeyMusicPlayerFragment = "key_music_player_fragment";
    private RootBaseActivity mActivity;


    private MusicPlayerFragment() {

    }

    static MusicPlayerFragment newInstance(String flag) {
        MusicPlayerFragment musicPlayerFragment = new MusicPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyMusicPlayerFragment, flag);
        musicPlayerFragment.setArguments(bundle);
        return musicPlayerFragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (RootBaseActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        IRUtils.eLog("pzh", mActivity.mBinder.getNowPlayMusic().getTitle());
        return inflater.inflate(R.layout.fragment_music_player, container, false);
    }
}
