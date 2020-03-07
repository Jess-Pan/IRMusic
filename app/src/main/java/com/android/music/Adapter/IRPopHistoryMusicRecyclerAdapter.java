package com.android.music.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.music.Models.MusicBean;
import com.android.music.R;
import com.android.music.Utils.IRUtils;
import com.android.music.Views.RootBaseActivity;
import com.android.music.Widgets.CustomCircleImageView;

import java.util.ArrayList;
import java.util.List;

public class IRPopHistoryMusicRecyclerAdapter extends RecyclerView.Adapter<IRPopHistoryMusicRecyclerAdapter.PopMusicViewHolder> {

    private static List<MusicBean> mHistoryMusicList;
    private RootBaseActivity mActivity;

    public IRPopHistoryMusicRecyclerAdapter(RootBaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    public List<MusicBean> getHistoryMusicList() {
        if (mHistoryMusicList == null) {
            mHistoryMusicList = new ArrayList<>();
        }
        return mHistoryMusicList;
    }

    @NonNull
    @Override
    public PopMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pop_music_item, parent, false);

        final PopMusicViewHolder holder = new PopMusicViewHolder(inflate);
        holder.mHistoryMusicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                MusicBean historyMusic = mHistoryMusicList.get(position);
                // todo:播放这首歌
                if (historyMusic != mActivity.mBinder.getNowPlayMusic()) {
                    mActivity.mBinder.setNowPlayMusic(historyMusic);
                    mActivity.mBinder.newMusicPlay();
                } else {
                    mActivity.mBinder.continuePlayMusic();
                }
                mActivity.mMusicPlayerFragment.updateForHistory();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PopMusicViewHolder holder, int position) {
        holder.mHistoryMusicTitle.setText(mHistoryMusicList.get(position).getTitle());
        holder.mHistoryMusicDuration.setText(IRUtils.calculateTime(mHistoryMusicList.get(position).getDuration()));
        holder.mHistoryMusicIcon.setImageResource(R.drawable.ic_music);
    }

    @Override
    public int getItemCount() {
        return mHistoryMusicList.size();
    }

    static class PopMusicViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout mHistoryMusicLayout;
        CustomCircleImageView mHistoryMusicIcon;
        TextView mHistoryMusicTitle, mHistoryMusicDuration;

        PopMusicViewHolder(@NonNull View itemView) {
            super(itemView);
            mHistoryMusicDuration = itemView.findViewById(R.id.historyMusicDuration);
            mHistoryMusicIcon = itemView.findViewById(R.id.historyMusicIcon);
            mHistoryMusicTitle = itemView.findViewById(R.id.historyMusicTitle);
            mHistoryMusicLayout = itemView.findViewById(R.id.historyMusicLayout);
        }
    }
}
