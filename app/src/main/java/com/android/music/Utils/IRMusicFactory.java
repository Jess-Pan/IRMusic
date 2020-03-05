package com.android.music.Utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import com.android.music.Models.MusicBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music.Utils
 * @ClassName: MusicFactory
 * @Description: 音乐数据源仓库 --> 获取音乐文件数据 ---> List
 * @Author: 27414
 * @CreateDate: 2020/2/28
 */
public class IRMusicFactory {

    /**
     * 返回手机中的歌曲信息， 打包成对象
     * @param context
     * @return
     */
    public static List<MusicBean> getMusicList(Context context) {

        Cursor cursor = getCursor(context.getContentResolver());
        List<MusicBean> musicList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i ++) {
                MusicBean music = new MusicBean();
                long uid = cursor.getLong(cursor.getColumnIndex(IRDefault.COLUMN_MUSIC_UID));
                String title = cursor.getString(cursor.getColumnIndex(IRDefault.COLUMN_MUSIC_TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(IRDefault.COLUMN_MUSIC_ARTIST));
                long duration = cursor.getLong(cursor.getColumnIndex(IRDefault.COLUMN_MUSIC_DURATION));
                String data = cursor.getString(cursor.getColumnIndex(IRDefault.COLUMN_MUSIC_DATA));
                int isMusic = cursor.getInt(cursor.getColumnIndex(IRDefault.COLUMN_MUSIC_IS_MUSIC));
                if (isMusic != 0 && duration / (500 * 60) >= 1) {
                    music.setUid(uid);
                    music.setTitle(title);
                    music.setArtist(artist);
                    music.setData(data);
                    music.setDuration(duration);
                    musicList.add(music);
                }

                cursor.moveToNext();
            }
        }
        return musicList;
    }

    /**
     * 创建并返回一个查询游标，减小内存开销
     * @param resolver
     * @return
     */
    private static Cursor getCursor(ContentResolver resolver) {
        return resolver.query(
                IRDefault.EXTERNAL_MUSIC_CONTENT_URI,
                null,
                null,
                null,
                IRDefault.COLUMN_MUSIC_DEFAULT_SORT_ORDER);
    }

    /**
     * 释放游标对象，减小开销
     * @param context
     */
    public static void closeCursor(Context context) {
        getCursor(context.getContentResolver()).close();
    }
}
