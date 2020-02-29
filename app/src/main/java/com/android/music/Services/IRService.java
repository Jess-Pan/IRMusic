package com.android.music.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import com.android.music.Models.MusicBean;
import com.android.music.Utils.IRMusicFactory;
import com.android.music.Utils.IRUtils;

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
    public static class IRServiceBinder extends Binder {

        private List<MusicBean> mMusicList;
        private Context mContext;

        IRServiceBinder(Context context) {
            this.mContext = context;
        }

        public List<MusicBean> getMusicList() {
            IRUtils.dLog("pzh", "start search");
            this.mMusicList = IRMusicFactory.getMusicList(mContext);
            return mMusicList;
        }

        public MusicBean getMusic(int position) {
            return mMusicList.get(position);
        }

        public void freeCursor() {
            IRMusicFactory.getMusicList(mContext);
        }

    }

}
