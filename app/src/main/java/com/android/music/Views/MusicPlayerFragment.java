package com.android.music.Views;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.music.R;

public class MusicPlayerFragment extends Fragment {

    private static final String KeyMusicPlayerFragment = "key_music_player_fragment";


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_music_player, container, false);
    }
}
