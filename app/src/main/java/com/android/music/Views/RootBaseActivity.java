package com.android.music.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.android.music.Models.MusicBean;
import com.android.music.R;
import com.android.music.Services.IRService;
import com.android.music.Utils.IRFragmentManager;
import com.android.music.Utils.IRUtils;
import java.lang.ref.WeakReference;
import java.util.List;

import static com.android.music.Views.MusicListFragment.KeyMusicListFragment;
import static com.android.music.Views.MusicPlayerFragment.KeyMusicPlayerFragment;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music
 * @ClassName: MusicBean
 * @Description: 音乐Bean类
 * @Author: 27414
 * @CreateDate: 2020/2/27
 */

public class RootBaseActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    public static final String TAG_MUSIC_LIST_FRAGMENT = "f1";
    public static final String TAG_MUSIC_PLAYER_FRAGMENT = "f2";
    public static final String FLAG_MUSIC_LIST_FRAGMENT = "list";
    public static final String FLAG_MUSIC_PLAYER_FRAGMENT = "player";
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    ServiceConnection mConnection;
    public IRService.IRServiceBinder mBinder;
    List<MusicBean> mMusicList;
    MusicLoaderTask mLoaderTask;
    Intent mServiceIntent;
    public MusicListFragment mMusicListFragment;
    public MusicPlayerFragment mMusicPlayerFragment;
    IntentFilter mIntentFilter;
    BroadcastReceiver mHeadsetReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_root);
        verifyStoragePermissions(this);
        fullScreenSetting();
        initView();
        initData();
        buildService();
    }

    private void fullScreenSetting() {
        Window window = getWindow();
        View decorView = window.getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    protected void initView() {
        mMusicListFragment = MusicListFragment.newInstance();
        mMusicPlayerFragment = MusicPlayerFragment.newInstance();
        this.getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.frameLayout, mMusicListFragment, TAG_MUSIC_LIST_FRAGMENT)
                .commit();
    }

    public void updateFragment(String key) {
        switch (key) {
            case KeyMusicPlayerFragment:
                    IRFragmentManager.switchFragment(mMusicPlayerFragment, this, TAG_MUSIC_PLAYER_FRAGMENT);
                break;
            case KeyMusicListFragment:
                    IRFragmentManager.switchFragment(mMusicListFragment, this, TAG_MUSIC_LIST_FRAGMENT);
                break;
            default:
                break;
        }
    }

    private void buildService() {
        mServiceIntent = new Intent(RootBaseActivity.this, IRService.class);
        this.bindService(mServiceIntent, mConnection, Context.BIND_AUTO_CREATE);
        this.startService(mServiceIntent);
    }

    protected void initData() {
        mConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = (IRService.IRServiceBinder) service;
                goMusicTask();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBinder = null;
            }
        };

        mIntentFilter = new IntentFilter();
        mHeadsetReceiver = new HeadsetBroadcast();
        mIntentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(mHeadsetReceiver, mIntentFilter);
    }

    public void setMusicList(List<MusicBean> mMusicList) {
        this.mMusicList = mMusicList;
    }

    protected void goMusicTask() {
        MusicLoaderTask mLoaderTask = new MusicLoaderTask(this);
        mLoaderTask.execute(mBinder);
    }

    public static void verifyStoragePermissions(Activity activity) {
        try {
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            IRFragmentManager.back(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IRUtils.eLog("pzh", "onDestroy");
        getContentResolver().unregisterContentObserver(mMusicListFragment.mMediaObserver);
        mBinder.freeCursor(this);
        mBinder.releaseMediaPlayer();
        mMusicPlayerFragment.mHandler.removeCallbacks(mMusicPlayerFragment.mMusicSeekBarRunnable);
        if (mMusicPlayerFragment.mCompleteReceiver != null) {
            unregisterReceiver(mMusicPlayerFragment.mCompleteReceiver);
        }

        if (mMusicPlayerFragment.mErrorReceiver != null) {
            unregisterReceiver(mMusicPlayerFragment.mErrorReceiver);
        }
        unregisterReceiver(mHeadsetReceiver);
        this.unbindService(mConnection);
        this.stopService(mServiceIntent);
        mLoaderTask = null;
        System.gc();
    }


    @Override
    protected void onStop() {
        super.onStop();
        IRUtils.eLog("pzh", "onStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        IRUtils.eLog("pzh", "onPause");
    }

    /**
     * 音乐界面加载本地音乐文件的后台异步任务
     */
    static class MusicLoaderTask extends AsyncTask<IRService.IRServiceBinder, Integer, List<MusicBean>> {

        private final WeakReference<RootBaseActivity> mWeakActivity;

        MusicLoaderTask(RootBaseActivity activity) {
            this.mWeakActivity = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<MusicBean> doInBackground(IRService.IRServiceBinder... binders) {
            return binders[0].getMusicList();
        }

        @Override
        protected void onPostExecute(List<MusicBean> musicBeans) {
            super.onPostExecute(musicBeans);
            RootBaseActivity activity = mWeakActivity.get();
            if (activity == null || activity.isFinishing() || activity.isDestroyed()) {
                return;
            }
            activity.setMusicList(musicBeans);
            activity.mMusicListFragment.setMusicList(musicBeans);
            activity.mMusicListFragment.refreshView();
            activity.mMusicListFragment.mRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 耳机拨出、插入状态广播事件处理
     * 广播Action: android.intent.action.HEADSET_PLUG
     * 广播Status: 0 --- 拔出, 1 --- 连接
     * 断开状态:
     *      1. 判断 Service 有没有绑定, 判断当前有无播放音乐
     *      2. 调用{@link IRService.IRServiceBinder#pauseMusic()} 暂停播放
     *      3. 调用{@link MusicPlayerFragment#configMusicPlayButtonImage()} 根据音乐播放状态调整UI
     * 连接状态:
     *      1. 判断 Service 有没有绑定, 判断当前有无播放音乐
     *      2. 调用{@link IRService.IRServiceBinder#continuePlayMusic()} ()} 继续播放
     *      3. 调用{@link MusicPlayerFragment#configMusicPlayButtonImage()} 根据音乐播放状态调整UI
     */
    public class HeadsetBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    // 拔出
                    if (mBinder != null && mBinder.getNowPlayMusic() != null) {
                        mBinder.pauseMusic();
                        mMusicPlayerFragment.configMusicPlayButtonImage();
                    }

                    IRUtils.eLog("pzh", "disconnect");
                } else if (intent.getIntExtra("state", 0) == 1) {
                    // 连接
                    if (mBinder != null && mBinder.getNowPlayMusic() != null) {
                        IRUtils.eLog("pzh", "connect");
                        mBinder.continuePlayMusic();
                        mMusicPlayerFragment.configMusicPlayButtonImage();
                    }
                }
            }
        }
    }
}
