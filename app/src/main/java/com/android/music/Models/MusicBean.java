package com.android.music.Models;


/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music
 * @ClassName: MusicBean
 * @Description: 音乐Bean类
 * @Author: 27414
 * @CreateDate: 2020/2/27
 */

public class MusicBean {

    /**
     * 音乐唯一标识
     */
    private long uid;

    /**
     *  音乐名
     */
    private String title;

    /**
     * 歌手名
     */
    private String artist;

    /**
     * 空的构造函数
     */
    public MusicBean() {
    }

    /**
     *
     * 构造函数
     * @param uid
     * @param title
     * @param artist
     */
    MusicBean(int uid, String title, String artist) {
        this.uid = uid;
        this.title = title;
        this.artist = artist;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
