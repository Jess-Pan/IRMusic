package com.android.music.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music
 * @ClassName: IRUtils
 * @Description: function description
 * @Author: 27414
 * @CreateDate: 2020/2/28
 */
public final class IRUtils {
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
}
