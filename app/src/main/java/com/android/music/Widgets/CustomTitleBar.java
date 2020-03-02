package com.android.music.Widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * @ProjectName: IRMusicPlayer
 * @Package: com.android.music.Widgets
 * @ClassName: CustomTitleBar
 * @Description: function description
 * @Author: 27414
 * @CreateDate: 2020/3/2
 */
public class CustomTitleBar extends ConstraintLayout {

    TextView mMusicTitle, mMusicArtist;
    ImageButton mLeftButton, mRightButton;

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

    }

}
