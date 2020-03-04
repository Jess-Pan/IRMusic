package com.android.music.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import com.android.music.Utils.IRDefault;
import com.android.music.Models.MusicBean;
import com.android.music.Utils.IRMusicDao;
import com.android.music.Utils.IRMusicFactory;
import com.android.music.Utils.IRUtils;

import java.io.IOException;
import java.util.List;

/**
 * 数据库操作服务
 */
public class IRService extends Service {



    /**
     * 空的构造器
     */
    public IRService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 重启服务时调用
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 服务绑定方法，返回一个Binder对象
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {

        return new IRServiceBinder(this);
    }

    /**
     * @ProjectName: IRMusicPlayer
     * @Package: com.android.music
     * @ClassName: IRServiceBinder
     * @Description: function description
     * @Author: 27414
     * @CreateDate: 2020/2/28
     */
    public static class IRServiceBinder extends Binder implements MediaPlayer.OnPreparedListener  {

        private List<MusicBean> mMusicList;
        private Context mContext;
        private MusicBean mNowPlayMusic;
        private MediaPlayer mMediaPlayer;
        boolean mIsPlaying = true;
        int arrayType = 1;

        IRServiceBinder(Context context) {
            this.mContext = context;
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
        }

        public List<MusicBean> getMusicList() {
            IRUtils.dLog("pzh", "start search");
            this.mMusicList = IRMusicFactory.getMusicList(mContext);
            return mMusicList;
        }

        public MusicBean getNowPlayMusic() {
            return mNowPlayMusic;
        }

        public void setNowPlayMusic(MusicBean mNowPlayMusic) {
            this.mNowPlayMusic = mNowPlayMusic;
        }

        public void freeCursor() {
            IRMusicFactory.getMusicList(mContext);
        }

        public void playMusic() {
            if (mIsPlaying) {
                pauseMusic();
            } else {
                continuePlayMusic();
            }
        }

        void pauseMusic() {
            // 暂停播放
            mIsPlaying = false;
            mMediaPlayer.pause();
        }

        public void preMusic() {
            // 前一首
            int position = mMusicList.indexOf(mNowPlayMusic);
            if (position == 0) {
                position = mMusicList.size() - 1;
            } else {
                position -= 1;
            }

            setNowPlayMusic(mMusicList.get(position));
            newMusicPlay();


        }

        public void nextMusic() {
            // 后一首
            int position = mMusicList.indexOf(mNowPlayMusic);
            if (position == mMusicList.size() - 1) {
                position = 0;
            } else {
                position += 1;
            }
            setNowPlayMusic(mMusicList.get(position));
            newMusicPlay();
        }

        public void continuePlayMusic() {
            // 继续播放
            mIsPlaying = true;
            mMediaPlayer.start();
        }

        public void newMusicPlay() {
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mContext, Uri.parse(mNowPlayMusic.getData()));
                IRUtils.eLog("pzh", mNowPlayMusic.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (arrayType == IRDefault.ARRAY_ONE_MUSIC) {
                mMediaPlayer.setLooping(true);
            }
            mIsPlaying = true;
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(this);
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            mp.start();
        }

        public void releaseMediaPlayer() {
            mMediaPlayer.release();
        }
    }

}
