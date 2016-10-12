package com.xuhong.baseclass.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuhong.baseclass.R;


/**
 * Created by BHKJ on 2016/8/25.
 *
 * 图片加文字的显示  图片在上  文字在下
 */

public class PicAndTextView extends LinearLayout {

    private ImageView  mImageView;
    private TextView mTextView;

    private String mText;
    private float mTextSize;
    private int  mTextColor;

    private float  mImgWidth,mImgHeight;
    private Drawable mImgSrc;



    public PicAndTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PicAndTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.view_pic_text,this);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.picandtextview);
        mImgWidth= a.getDimension(R.styleable.picandtextview_imgWidth,0);
        mImgHeight= a.getDimension(R.styleable.picandtextview_imgHeight,0);
        mImgSrc= a.getDrawable(R.styleable.picandtextview_imgSrc);

        mText= a.getString(R.styleable.picandtextview_text);
        mTextColor= a.getColor(R.styleable.picandtextview_textColor,0);
        mTextSize= a.getDimension(R.styleable.picandtextview_textSize,0);
        a.recycle();
        setGravity(Gravity.CENTER);
        setOrientation(LinearLayout.VERTICAL);

        mImageView = (ImageView) findViewById(R.id.iv);
        mTextView = (TextView) findViewById(R.id.tv);
        if (mImgWidth!=0&&mImgHeight!=0){
            ViewGroup.LayoutParams  layoutParams = mImageView.getLayoutParams();
            layoutParams.width = (int) mImgWidth;
            layoutParams.height = (int) mImgHeight;
        }
        if (mImgSrc!=null){
            mImageView.setImageDrawable(mImgSrc);
        }
        if (!"".equals(mText)){
            mTextView.setText(mText);
        }
        if (mTextColor!=0){
            mTextView.setTextColor(mTextColor);
        }
        if (mTextSize!=0){
            mTextView.setTextSize(mTextSize);
        }
    }


    public void setText(String str){
        mTextView.setText(str);
    }
    public ImageView getImageView(){
        return  mImageView;
    }
    public TextView getTextView(){
        return  mTextView;
    }
    public void setImgWidthAndHeight(int size){
        ViewGroup.LayoutParams  layoutParams = mImageView.getLayoutParams();
        layoutParams.width = size;
        layoutParams.height = size;
    }


}
