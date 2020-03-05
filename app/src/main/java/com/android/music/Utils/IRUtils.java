package com.android.music.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.widget.Toast;

import com.android.music.Models.MusicBean;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music
 * @ClassName: IRUtils
 * @Description: function description
 * @Author: 27414
 * @CreateDate: 2020/2/28
 */
public final class IRUtils {

    private static StringBuilder mFormatBuilder = new StringBuilder();
    private static Formatter mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    private static MediaMetadataRetriever mMetadataRetriever = new MediaMetadataRetriever();
    private static Random mRandom = new Random();

    /**
     * 全局的Log开关
     * 开: true
     * 关: false
     */
    private static final Boolean isDebug = true;

    /**
     * 全局Log.TAG
     */
    private static final String TAG = "IRMusic";


    /**
     * {@link Log#d(String, String)}
     * @param title
     * @param text
     */
    public static void dLog(String title, String text) {
        if (isDebug) {
            Log.d(TAG, title + " : " + text);
        }
    }

    /**
     * {@link Log#e(String, String)}
     * @param title
     * @param text
     */
    public static void eLog(String title, String text) {
        if (isDebug) {
            Log.e(TAG, title + " : " + text);
        }
    }

    /**
     *
     * @param context
     * @param text
     */
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }

    /**
     *
     * @param context
     * @param resId
     */
    public static void showToast(Context context, int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static String calculateTime(long duration) {
        long totalSeconds = duration / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        mFormatBuilder.setLength(0);
        if(hours>0){
            return mFormatter.format("%02d:%02d:%02d",hours,minutes,seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d",minutes,seconds).toString();
        }
    }

    public static Bitmap getAlbumPicture(String musicUri) {
        mMetadataRetriever.setDataSource(musicUri);
        byte[] picture = mMetadataRetriever.getEmbeddedPicture();
        if (picture == null) {
            return null;
        }
        return BitmapFactory.decodeByteArray(picture, 0, picture.length);
    }

    public static int getRandomPosition(List<MusicBean> list) {
        int count = list.size();
        return mRandom.nextInt(count);
    }
}
