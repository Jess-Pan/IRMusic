package com.android.music.Views;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.music.Adapter.IRPopHistoryMusicRecyclerAdapter;
import com.android.music.Models.MusicBean;
import com.android.music.R;

import java.util.Collections;
import java.util.List;

public class HistoryMusicWindows extends PopupWindow {

    private RecyclerView mHistoryRecyclerList;
    private IRPopHistoryMusicRecyclerAdapter mHistoryRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RootBaseActivity mActivity;
    private LayoutInflater mLayoutInflater;
    private int mLayoutId;
    private View mPopListView;

    HistoryMusicWindows(int layoutId, RootBaseActivity activity) {
        super(activity);
        this.mActivity = activity;
        this.mLayoutId = layoutId;
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    private void initView() {
        mPopListView = mLayoutInflater.inflate(mLayoutId, null);
        setContentView(mPopListView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(displayMetrics.heightPixels * 3 / 4);
        setBackgroundDrawable(ContextCompat.getDrawable(mActivity, R.drawable.pop_window_corners));
        setFocusable(true);
        mPopListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int height = mPopListView.findViewById(R.id.popMusicHistory).getTop();
                int y = (int) motionEvent.getY();
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (y < height) {
                            dismiss();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        view.performClick();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        adapterHistoryList();
    }

    private void adapterHistoryList() {
        mHistoryRecyclerList = mPopListView.findViewById(R.id.historyMusicList);
        this.mLinearLayoutManager = new LinearLayoutManager(mActivity);
        this.mHistoryRecyclerAdapter = new IRPopHistoryMusicRecyclerAdapter(mActivity);
        configHistoryList();
    }

    private void configHistoryList() {
        mHistoryRecyclerList.setLayoutManager(mLinearLayoutManager);
        mHistoryRecyclerList.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mHistoryRecyclerList.setAdapter(mHistoryRecyclerAdapter);
    }

    void addHistoryMusic(MusicBean music) {
        List<MusicBean> musicBeanList = mHistoryRecyclerAdapter.getHistoryMusicList();
        if (mHistoryRecyclerAdapter.getHistoryMusicList().contains(music)) {
            int position = musicBeanList.indexOf(music);
            Collections.swap(mHistoryRecyclerAdapter.getHistoryMusicList(), position, musicBeanList.size() - 1);
        } else {
            mHistoryRecyclerAdapter.getHistoryMusicList().add(music);
        }
        configHistoryList();
    }

    public View getPopListView() {
        return mPopListView;
    }


}
