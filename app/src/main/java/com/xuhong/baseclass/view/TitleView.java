package com.xuhong.baseclass.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.utils.StringUtils;


/**
 * Created by BHKJ on 2016/8/30.
 *
 * 自己定义的actionbar  。。。
 */

public class TitleView extends RelativeLayout {

    private TextView tvTitle;
    private TextView tvBack;
    private TextView tvRight;
    private ImageView ivRight;


    private OnClickRightListener mListener;


    public TitleView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View.inflate(context, R.layout.view_title, this);

        setBackgroundColor(Color.parseColor("#f02e2e"));
        tvTitle = (TextView) getChildAt(0);
        tvBack = (TextView) getChildAt(1);
        tvRight = (TextView) getChildAt(2);
        ivRight = (ImageView) getChildAt(3);

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.title_view);
        String titleText = a.getString(R.styleable.title_view_titleText);
        String rightText = a.getString(R.styleable.title_view_rightText);
        Drawable image = a.getDrawable(R.styleable.title_view_rightImg);
        a.recycle();
        setTitleText(titleText);

        if (image != null) {
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageDrawable(image);
            ivRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v);
                    }
                }
            });
        }
        setRightText(rightText);


        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) context).finish();
            }
        });
    }

    public void setTitleText(String titleText) {
        if (!StringUtils.isEmpty(titleText)) {
            if (titleText.length() > 10) {
                titleText = titleText.substring(0, 10) + "...";
            }
            tvTitle.setText(titleText);
        }
    }

    public void setRightText(String titleText) {
        if (!StringUtils.isEmpty(titleText)) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(titleText);
            tvRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClick(v);
                    }
                }
            });
        }
    }
    public void  hideRight(){
        if (tvRight.getVisibility() ==View.VISIBLE){
            tvRight.setVisibility(View.GONE);
        }
        if (ivRight.getVisibility() ==View.VISIBLE){
            ivRight.setVisibility(View.GONE);
        }
    }

    public void hideBack() {
        tvBack.setVisibility(View.GONE);
    }


    public void setOnClickRightListener(OnClickRightListener mListener) {
        this.mListener = mListener;
    }

    public interface OnClickRightListener {
        void onClick(View v);
    }
}
