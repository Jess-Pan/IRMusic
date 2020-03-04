package com.android.music.Models;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music
 * @ClassName: MusicBean
 * @Description: 音乐Bean类
 * @Author: 27414
 * @CreateDate: 2020/2/27
 */

public class MusicBean implements Parcelable {

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
     * 路径
     */
    private String data;

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
     * @param data
     * @param artist
     */
    MusicBean(int uid, String title, String artist, String data) {
        this.uid = uid;
        this.title = title;
        this.artist = artist;
        this.data = data;
    }

    private MusicBean(Parcel source) {
        this.uid = source.readLong();
        this.title = source.readString();
        this.artist = source.readString();
        this.data = source.readString();
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeLong(uid);
        dest.writeString(artist);
        dest.writeString(data);
    }

    private static final Creator<MusicBean> CREATOR = new Creator<MusicBean>() {
        @Override
        public MusicBean createFromParcel(Parcel source) {
            return new MusicBean(source);
        }

        @Override
        public MusicBean[] newArray(int size) {
            return new MusicBean[0];
        }
    };
}
