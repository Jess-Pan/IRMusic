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
 * @Description: MusicRecyclerView 适配器
 * @Author: lrcoder
 * @CreateDate: 2020/2/28
 */

public class IRMusicRecyclerViewAdapter extends
        RecyclerView.Adapter<IRMusicRecyclerViewAdapter.IRMusicViewHolder> {

    private static final String TAG = "MUSIC_ADAPTER";
    private List<MusicBean> mMusicList;
    private RootBaseActivity mActivity;

    /**
     * 构造函数，传入Activity对象，便于获取服务的Binder对象，进行当前播放曲目的设置
     * @param activity
     */
    public IRMusicRecyclerViewAdapter(RootBaseActivity activity) {
        this.mActivity = activity;
    }

    /**
     * 传入创建RecyclerView的数据源 --> List<MusicBean>
     * @param musicList
     */
    public void setMusicList(List<MusicBean> musicList) {
        this.mMusicList = musicList;
    }

    /**
     * 在视图onCreate中进行点击事件绑定，减少资源开销
     * @param parent
     * @param viewType
     * @return holder
     */
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
                if (music != mActivity.mBinder.getNowPlayMusic()) {
                    mActivity.mBinder.setNowPlayMusic(music);
                    mActivity.mBinder.newMusicPlay();
                } else {
                    mActivity.mBinder.continuePlayMusic();
                }
                mActivity.updateFragment(KeyMusicPlayerFragment);

            }
        });
        return holder;
    }

    /**
     * 按照position进行视图绑定
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(IRMusicViewHolder holder, int position) {
        holder.mMusicTitle.setText(mMusicList.get(position).getTitle());
        holder.mMusicArtist.setText(mMusicList.get(position).getArtist());
        holder.mMusicIcon.setImageResource(R.drawable.ic_music);
    }

    /**
     * 返回数据源个数
     * @return
     */
    @Override
    public int getItemCount() {
        return mMusicList.size();
    }

    /**
     * 数据源布局Holder
     */
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
