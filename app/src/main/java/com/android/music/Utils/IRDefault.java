package com.android.music.Utils;

import android.net.Uri;
import static android.provider.MediaStore.*;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music.Utils
 * @ClassName: IRDefault
 * @Description: function description
 * @Author: 27414
 * @CreateDate: 2020/2/28
 */

final class IRDefault {
    static final Uri EXTERNAL_MUSIC_CONTENT_URI = Audio.Media.EXTERNAL_CONTENT_URI;
    static final Uri INTERNAL_MUSIC_CONTENT_URI = Audio.Media.INTERNAL_CONTENT_URI;
    static final String COLUMN_MUSIC_UID = Audio.Media._ID;
    static final String COLUMN_MUSIC_TITLE = Audio.Media.TITLE;
    static final String COLUMN_MUSIC_ARTIST = Audio.Media.ARTIST;
    static final String COLUMN_MUSIC_DURATION = Audio.Media.DURATION;
    static final String COLUMN_MUSIC_SIZE = Audio.Media.SIZE;
    static final String COLUMN_MUSIC_DATA = Audio.Media.DATA;
    static final String COLUMN_MUSIC_IS_MUSIC = Audio.Media.IS_MUSIC;
    static final String COLUMN_MUSIC_DEFAULT_SORT_ORDER = Audio.Media.DEFAULT_SORT_ORDER;
}
