package com.android.music.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.widget.Toast;

import com.android.music.Models.MusicBean;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static com.android.music.Utils.BlurJNI.initCBlur1;
import static com.android.music.Utils.BlurJNI.initCBlur2;

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

    public static Bitmap gaussBlurUseGauss(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //生成一张新的图片
        Bitmap outBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //定义一个临时数组存储原始图片的像素 值
        int[] pix = new int[w * h];

        //将图片像素值写入数组
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        //进行模糊
        initCBlur1(pix, w, h, radius);

        //将数据写入到 图片
        outBitmap.setPixels(pix, 0, w, 0, 0, w, h);

        //返回结果
        return outBitmap;
    }

    //利用均值模糊 逼近 高斯模糊
    public static Bitmap gaussBlurUseAvg(Bitmap bitmap, int radius) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        //生成一张新的图片
        Bitmap outBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

        //定义一个临时数组存储原始图片的像素 值
        int[] pix = new int[w * h];

        //将图片像素值写入数组
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        //进行模糊
        initCBlur2(pix, w, h, radius);

        //将数据写入到 图片
        outBitmap.setPixels(pix, 0, w, 0, 0, w, h);

        //返回结果
        return outBitmap;
    }

    public static Bitmap getBlurBitmap(Context context, Bitmap map) {
        //创建一个缩小后的bitmap
        Bitmap inputBitmap = Bitmap.createScaledBitmap(map, 50, 50, false);
        //创建将在ondraw中使用到的经过模糊处理后的bitmap
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        //创建RenderScript，ScriptIntrinsicBlur固定写法
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        //根据inputBitmap，outputBitmap分别分配内存
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);

        //设置模糊半径取值0-25之间，不同半径得到的模糊效果不同
        blurScript.setRadius(10);
        blurScript.setInput(tmpIn);
        blurScript.forEach(tmpOut);

        //得到最终的模糊bitmap
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
}
