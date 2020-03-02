package com.android.music.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.android.music.Models.MusicBean;
import com.android.music.R;
import com.android.music.Utils.IRUtils;
import com.android.music.Views.RootBaseActivity;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static com.android.music.Views.MusicPlayerFragment.KeyMusicPlayerFragment;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music.Widgets
 * @ClassName: IRMusicRecyclerView
 * @Description: function description
 * @Author: 27414
 * @CreateDate: 2020/2/28
 */

public class IRMusicRecyclerViewAdapter extends
        RecyclerView.Adapter<IRMusicRecyclerViewAdapter.IRMusicViewHolder> {

    private static final String TAG = "MUSIC_ADAPTER";
    private List<MusicBean> mMusicList;
    private RootBaseActivity mActivity;

    public IRMusicRecyclerViewAdapter(RootBaseActivity activity) {
        this.mActivity = activity;
    }

    public void setMusicList(List<MusicBean> musicList) {
        this.mMusicList = musicList;
    }

    @NotNull
    @Override
    public IRMusicViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_music, parent, false);
        final IRMusicViewHolder holder = new IRMusicViewHolder(view);
        holder.mItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                MusicBean music = mMusicList.get(position);
                IRUtils.dLog(TAG, "title = " + music.getTitle());
                mActivity.mBinder.setNowPlayMusic(music);
                mActivity.updateFragment(KeyMusicPlayerFragment);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(IRMusicViewHolder holder, int position) {
        holder.mMusicTitle.setText(mMusicList.get(position).getTitle());
        holder.mMusicArtist.setText(mMusicList.get(position).getArtist());
        holder.mMusicIcon.setImageResource(R.drawable.ic_music);
    }

    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    static class IRMusicViewHolder extends RecyclerView.ViewHolder {

        TextView mMusicTitle, mMusicArtist;
        ImageView mMusicIcon;
        ConstraintLayout mItemLayout;

        IRMusicViewHolder(View itemView) {
            super(itemView);
            mMusicArtist = itemView.findViewById(R.id.musicArtist);
            mMusicTitle = itemView.findViewById(R.id.musicTitle);
            mMusicIcon = itemView.findViewById(R.id.musicIcon);
            mItemLayout = itemView.findViewById(R.id.musicItemLayout);
        }
    }
}
