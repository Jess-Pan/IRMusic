package com.android.music.Utils;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music.Utils
 * @ClassName: BlurJNI
 * @Description: function description
 * @Author: 27414
 * @CreateDate: 2020/3/6
 */
public class BlurJNI {
    static {
        System.loadLibrary("BlurJni");
    }

    public static native void initCBlur1(int[] pix, int w, int h, int r);
    public static native void initCBlur2(int[] pix, int w, int h, int r);
}
