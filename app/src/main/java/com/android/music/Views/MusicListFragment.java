package com.android.music.Views;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.music.Adapter.IRMusicRecyclerViewAdapter;
import com.android.music.Models.MusicBean;
import com.android.music.R;
import com.android.music.Utils.IRUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment
        implements SwipeRefreshLayout.OnRefreshListener {

    static final String KeyMusicListFragment = "key_music_list_fragment";
    private final String KeySaveMusicList = "onSaveList";
    SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mMusicRecyclerList;
    private TextView mNullText;
    private LinearLayoutManager mManager;
    private IRMusicRecyclerViewAdapter mAdapter;
    private RootBaseActivity mActivity;
    private List<MusicBean> mMusicList;
    private String mParam;

    private MusicListFragment() {

    }

    static MusicListFragment newInstance(String flag) {
        MusicListFragment musicListFragment = new MusicListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KeyMusicListFragment, flag);
        musicListFragment.setArguments(bundle);
        return musicListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        if (savedInstanceState != null) {
//            IRUtils.eLog("pzh", "=========");
//            mMusicList = savedInstanceState.getParcelableArrayList(KeySaveMusicList);
//            mActivity.mBinder.setMusicList(mMusicList);
//            mActivity.setMusicList(mMusicList);
//            IRUtils.eLog("pzh", mMusicList.get(1).getTitle());
//        }
        View rootView = inflater.inflate(R.layout.fragment_music_list, container, false);
        initView(rootView);
        initListener();
        refreshView();
        return rootView;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        this.mActivity = (RootBaseActivity) context;
        if (getArguments() != null) {
            this.mParam = getArguments().getString(KeyMusicListFragment);
        }
    }

    private void initView(View rootView) {
        this.mMusicRecyclerList = rootView.findViewById(R.id.musicRecyclerList);
        this.mNullText = rootView.findViewById(R.id.nullText);
        this.mRefreshLayout = rootView.findViewById(R.id.refreshLayout);
        this.mManager = new LinearLayoutManager(mActivity);
        this.mAdapter = new IRMusicRecyclerViewAdapter(mActivity);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
    }

    private void initListener() {
        mRefreshLayout.setOnRefreshListener(this);
    }

    void setMusicList(List<MusicBean> mMusicList) {
        this.mMusicList = mMusicList;
    }

    void refreshView() {
        if (mMusicList == null) {
            mMusicRecyclerList.setVisibility(View.GONE);
            mNullText.setVisibility(View.VISIBLE);
        } else {
            configRecyclerList();
            mMusicRecyclerList.setVisibility(View.VISIBLE);
            mNullText.setVisibility(View.GONE);
        }
    }

    private void configRecyclerList() {
        mAdapter.setMusicList(mMusicList);
        mMusicRecyclerList.setLayoutManager(mManager);
        mMusicRecyclerList.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        IRUtils.eLog("pzh", "on refresh");
        mRefreshLayout.setRefreshing(true);
        mActivity.goMusicTask();
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        IRUtils.eLog("pzh", "save");
//        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(KeySaveMusicList, (ArrayList<? extends Parcelable>) mMusicList);
//    }
}
