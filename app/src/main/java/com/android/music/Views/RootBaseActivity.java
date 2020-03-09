package com.android.music.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.android.music.Models.MusicBean;
import com.android.music.R;
import com.android.music.Services.IRService;
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
    BroadcastReceiver mReceiver;


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
                    packFragmentManager(mMusicPlayerFragment, TAG_MUSIC_PLAYER_FRAGMENT);
                break;
            case KeyMusicListFragment:
                    packFragmentManager(mMusicListFragment, TAG_MUSIC_LIST_FRAGMENT);
                break;
            default:
                break;
        }
    }

    private void packFragmentManager(Fragment fragment, String tag) {
        this.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout, fragment, tag)
                .addToBackStack(null)
                .commit();
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

    }

    public void setMusicList(List<MusicBean> mMusicList) {
        this.mMusicList = mMusicList;
    }

//    public List<MusicBean> getMusicList() {
//        return mMusicList;
//    }

    protected void goMusicTask() {
        MusicLoaderTask mLoaderTask = new MusicLoaderTask(this);
        mLoaderTask.execute(mBinder);
        mIntentFilter = new IntentFilter();
        mReceiver = new HeadsetBroadcast();
        mIntentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(mReceiver, mIntentFilter);
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
    protected void onDestroy() {
        super.onDestroy();
        IRUtils.eLog("pzh", "onDes");
        getContentResolver().unregisterContentObserver(mMusicListFragment.mMediaObserver);
        mBinder.freeCursor(this);
        mBinder.releaseMediaPlayer();
        mMusicPlayerFragment.mHandler.removeCallbacks(mMusicPlayerFragment.mMusicSeekBarRunnable);
        if (mMusicPlayerFragment.mReceiver != null) {
            unregisterReceiver(mMusicPlayerFragment.mReceiver);
        }
        unregisterReceiver(mReceiver);
        this.unbindService(mConnection);
        this.stopService(mServiceIntent);
        mLoaderTask = null;
        System.gc();
    }

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



    public class HeadsetBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    // 拔出
                    if (mBinder != null && mBinder.getNowPlayMusic() != null) {
                        mBinder.playMusic();
                        mMusicPlayerFragment.configMusicPlayButtonImage();
                    }

                    IRUtils.eLog("pzh", "disconn");
                } else if (intent.getIntExtra("state", 0) == 1) {
                    // 连接
                    if (mBinder != null && mBinder.getNowPlayMusic() != null) {
                        IRUtils.eLog("pzh", "conn");
                        mBinder.playMusic();
                        mMusicPlayerFragment.configMusicPlayButtonImage();
                    }
                }
            }
        }
    }
}
