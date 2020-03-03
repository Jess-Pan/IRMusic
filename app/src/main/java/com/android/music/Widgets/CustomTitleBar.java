package com.android.music.Widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.music.R;

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
    int mLeftButtonImageID, mRightButtonImageID;
    String mDefaultCenterTitleText, mDefaultCenterArtistText, mCenterTitleText, mCenterArtistText;

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.custom_title_bar, this);
        mMusicArtist = inflate.findViewById(R.id.musicArtist);
        mRightButton = inflate.findViewById(R.id.rightImageButton);
        mLeftButton = inflate.findViewById(R.id.leftImageButton);
        mMusicTitle = inflate.findViewById(R.id.musicTitle);
        initData(context, attrs);
    }

    private void initData(Context context, AttributeSet attrs) {

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);
        mLeftButtonImageID = array.getResourceId(R.styleable.CustomTitleBar_leftButtonImageID, Color.TRANSPARENT);
        mRightButtonImageID = array.getResourceId(R.styleable.CustomTitleBar_rightButtonImageID, Color.TRANSPARENT);
        mDefaultCenterArtistText = array.getString(R.styleable.CustomTitleBar_centerArtistText);
        mDefaultCenterTitleText = array.getString(R.styleable.CustomTitleBar_centerTitleText);
        array.recycle();

        mRightButton.setBackgroundResource(mRightButtonImageID);
        mLeftButton.setBackgroundResource(mLeftButtonImageID);
        mMusicTitle.setText(mDefaultCenterTitleText);
        mMusicArtist.setText(mDefaultCenterArtistText);
    }

    public void setCenterTitleText(String centerTitleText) {
        mMusicTitle.setText(centerTitleText);
    }

    public void setCenterArtistText(String centerArtistText) {
        mMusicArtist.setText(centerArtistText);
    }

    public void setLeftButtonOnClickListener(OnClickListener listener) {
        mLeftButton.setOnClickListener(listener);
    }

    public void setRightButtonOnClickListener(OnClickListener listener) {
        mRightButton.setOnClickListener(listener);
    }

    public void setCenterArtistTextOnClickListener(OnClickListener listener) {
        mMusicArtist.setOnClickListener(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
