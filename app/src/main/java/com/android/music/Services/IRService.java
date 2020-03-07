package com.android.music.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import com.android.music.Utils.IRDefault;
import com.android.music.Models.MusicBean;
import com.android.music.Utils.IRMusicFactory;
import com.android.music.Utils.IRUtils;
import java.util.List;

/**
 * 数据库操作服务
 */
public class IRService extends Service {

    IRServiceBinder mIRServiceBinder;
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

        mIRServiceBinder = new IRServiceBinder(this);
        return mIRServiceBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @ProjectName: IRMusicPlayer
     * @Package: com.android.music
     * @ClassName: IRServiceBinder
     * @Description: function description
     * @Author: 27414
     * @CreateDate: 2020/2/28
     */
    public static class IRServiceBinder extends Binder implements MediaPlayer.OnPreparedListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

        private List<MusicBean> mMusicList;
        private Context mContext;
        private MusicBean mNowPlayMusic;
        private MediaPlayer mMediaPlayer;
        public boolean mIsPlaying = true;
        public int arrayType = 0;
        Intent mIntentBroadcast;

        /**
         * 构造函数，声明时创建MediaPlayer对象，并设置监听，便于后续对Media Player的操作
         * @param context
         */
        IRServiceBinder(Context context) {
            this.mContext = context;
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
            }
            mIntentBroadcast = new Intent();
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
        }

        /**
         * 获取音乐列表，返回一个列表项，该，列表为最终实际调用的音乐，由Service统一调度
         * @return
         */
        public List<MusicBean> getMusicList() {
            IRUtils.dLog("pzh", "start search");
            this.mMusicList = IRMusicFactory.getMusicList(mContext);
            return mMusicList;
        }

        /**
         * 返回当前播放的音乐
         * @return
         */
        public MusicBean getNowPlayMusic() {
            return mNowPlayMusic;
        }

        /**
         * 设置当前播放的音乐
         * @param mNowPlayMusic
         */
        public void setNowPlayMusic(MusicBean mNowPlayMusic) {
            this.mNowPlayMusic = mNowPlayMusic;
        }

        /**
         * 调用{@link IRMusicFactory#closeCursor(Context)}释放游标对象
         */
        public void freeCursor(Context context) {
            IRMusicFactory.closeCursor(context);
        }

        /**
         * 判断当前是否处于播放状态，选则继续播放或者暂停
         */
        public void playMusic() {
            if (mIsPlaying) {
                pauseMusic();
            } else {
                continuePlayMusic();
            }
        }

        /**
         * 暂停播放
         */
        void pauseMusic() {

            mIsPlaying = false;
            mMediaPlayer.pause();
        }

        /**
         * 前一首
         */
        public void preMusic() {

            int position = mMusicList.indexOf(mNowPlayMusic);
            if (position == 0) {
                position = mMusicList.size() - 1;
            } else {
                position -= 1;
            }

            setNowPlayMusic(mMusicList.get(position));
            newMusicPlay();
        }

        /**
         * 下一首
         */
        public void nextMusic() {

            int position = mMusicList.indexOf(mNowPlayMusic);
            if (position == mMusicList.size() - 1) {
                position = 0;
            } else {
                position += 1;
            }
            setNowPlayMusic(mMusicList.get(position));
            newMusicPlay();
        }

        /**
         * 继续播放
         */
        public void continuePlayMusic() {

            mIsPlaying = true;
            mMediaPlayer.start();
        }

        /**
         * 播放一首新歌
         */
        public void newMusicPlay() {
            mMediaPlayer.reset();
            try {
                mMediaPlayer.setDataSource(mContext, Uri.parse(mNowPlayMusic.getData()));
                IRUtils.eLog("pzh", mNowPlayMusic.getData());
                mIsPlaying = true;
                mMediaPlayer.prepareAsync();
            } catch (Exception e) {
                e.printStackTrace();
                getMusicList();
            }

        }

        /**
         * 缓存结束时间监听回调
         * @param player
         */
        @Override
        public void onPrepared(MediaPlayer player) {
            player.start();
        }

        /**
         * 释放Media Player对象
         */
        public void releaseMediaPlayer() {
            mMediaPlayer.release();
        }

        /**
         * 取随机数进行播放
         */
        void randomMusicPlay() {
            int seedPosition = IRUtils.getRandomPosition(mMusicList);
            setNowPlayMusic(mMusicList.get(seedPosition));
            newMusicPlay();
        }

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mIntentBroadcast.setAction("OnCompletePlayer");
            switch (arrayType) {
                case IRDefault.ARRAY_ONE_MUSIC:
                    mMediaPlayer.seekTo(0);
                    continuePlayMusic();
                    break;
                case IRDefault.ARRAY_RANDOM_MUSIC:
                    randomMusicPlay();
                    mContext.sendBroadcast(mIntentBroadcast);
                    break;
                case IRDefault.ARRAY_ONE_LIST:
                    nextMusic();
                    mContext.sendBroadcast(mIntentBroadcast);
                    break;
                default:
                    break;
            }
        }

        /**
         * 播放到当前位置
         * @param progress
         */
        public void seekToMusic(int progress) {
            mMediaPlayer.seekTo(progress);
        }

        /**
         * 获取当前播放的位置
         * @return
         */
        public int getCurrentPosition() {
            try {
                return mMediaPlayer.getCurrentPosition();
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }

        }

        /**
         * 播放错误的回调
         * @param mediaPlayer
         * @param i
         * @param i1
         * @return
         */
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            IRUtils.eLog("pzh", "error");
            return false;
        }
    }

}
