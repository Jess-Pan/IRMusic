package com.android.music.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.android.music.Models.MusicBean;
import com.android.music.R;
import com.android.music.Services.IRService;
import com.android.music.Utils.IRMusicDao;
import com.android.music.Utils.IRUtils;
import java.lang.ref.WeakReference;
import java.util.List;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music
 * @ClassName: MusicBean
 * @Description: 音乐Bean类
 * @Author: 27414
 * @CreateDate: 2020/2/27
 */

public class RootBaseActivity extends AppCompatActivity
        implements IRMusicDao.ToFragmentListener {

    ServiceConnection mConnection;
    IRService.IRServiceBinder mBinder;
    List<MusicBean> mMusicList;
    MusicLoaderTask mLoaderTask;
    Intent mServiceIntent;
    MusicListFragment mMusicListFragment;


    public void setMusicList(List<MusicBean> mMusicList) {
        this.mMusicList = mMusicList;
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_root);

        initView(savedInstanceState);
        initData();
        buildService();
    }

    protected void initView(Bundle savedInstanceState) {
        verifyStoragePermissions(this);
        mMusicListFragment = MusicListFragment.newInstance("test");
        if (savedInstanceState == null) {
            this.getSupportFragmentManager().beginTransaction()
                    .add(R.id.frameLayout, mMusicListFragment, "f1")
                    .commit();
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
                    IRUtils.eLog("pzh", "bind ok");
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mBinder = null;
                }
            };
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
    protected void onDestroy() {
        super.onDestroy();
        this.unbindService(mConnection);
        this.stopService(mServiceIntent);
        mBinder.freeCursor();
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
            IRUtils.eLog("pzh", "music = " + musicBeans.get(0).getTitle());
            activity.setMusicList(musicBeans);
            activity.mMusicListFragment.setMusicList(musicBeans);
            activity.mMusicListFragment.refreshView();
            activity.mMusicListFragment.mRefreshLayout.setRefreshing(false);
        }
    }
}
