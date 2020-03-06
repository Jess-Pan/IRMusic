package com.android.music.Views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.music.Adapter.IRPopHistoryMusicRecyclerAdapter;
import com.android.music.Models.MusicBean;
import com.android.music.R;

public class HistoryMusicWindows extends PopupWindow {

    private RecyclerView mHistoryRecyclerList;
    private IRPopHistoryMusicRecyclerAdapter mHistoryRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private RootBaseActivity mActivity;
    private LayoutInflater mLayoutInflater;
    private int mLayoutId;
    private View mPopListView;

    public HistoryMusicWindows(int layoutId, RootBaseActivity activity) {
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
        setBackgroundDrawable(new ColorDrawable(0xbb000000));
        setFocusable(true);
        mPopListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int height = mPopListView.findViewById(R.id.popMusicHistory).getTop();
                int y = (int) motionEvent.getY();
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
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
        //addHistoryMusic(mActivity.mBinder.getNowPlayMusic());
        mHistoryRecyclerList.setLayoutManager(mLinearLayoutManager);
        mHistoryRecyclerList.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL));
        mHistoryRecyclerList.setAdapter(mHistoryRecyclerAdapter);
    }

    public void addHistoryMusic(MusicBean music) {
        mHistoryRecyclerAdapter.getHistoryMusicList().add(music);
        configHistoryList();
    }

    public View getPopListView() {
        return mPopListView;
    }


}
