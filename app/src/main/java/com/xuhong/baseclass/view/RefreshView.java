package com.xuhong.baseclass.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.utils.DensityUtils;


/**
 * Created by BHKJ on 2016/6/3.
 * 基于recyclerview的下拉刷新
 */

public class RefreshView extends FrameLayout {

    private Context mContext;

    private final ViewDragHelper mViewDragHelper;
    //判断是否拦截事件
    private boolean isOpenEvent = true;
    //    刷新的view
    private View mTopView;
    private TextView mTextView;
    private ImageView mImageView;

    private RecyclerView mView;
    //宽高
    private int mTopViewHeight;
    private int mViewWidth, mViewHeight;
    private final int mRefreshHeight = DensityUtils.dip2px(70);


    private int downY;
    // recyclerview 是否滑动到了顶部
    private boolean isTop;

    //是否在刷新中
    private boolean isRefresh;
    //recyclerview距离顶部的长度
    private int layoutToTop;
    //旋转动画
    private RotateAnimation animation;
    //刷新监听
    private OnRefreshListener mListener;

    //刷新完成  退回初始状态
    private boolean isRefreshToStart;

    private boolean isFirst= true;

    public void setOnRefreshListener(OnRefreshListener mListener) {
        this.mListener = mListener;
    }

    public interface OnRefreshListener {
        void onRefresh();

    }

    //刷新完成
    public void onRefreshComplete() {
        if (isRefresh) {
            mTextView.setText("　刷新成功　");
            isRefreshToStart =true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mImageView.clearAnimation();
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                }
            },1000);

        }
    }

    //    开始刷新
    public void onRefreshStart() {
        if (isFirst){
            isFirst=false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            },500);
        }else {
            refresh();
        }

    }

    private void refresh(){
        mImageView.startAnimation(animation);
        mTextView.setText("　正在刷新　");
        isRefresh = true;

        mViewDragHelper.smoothSlideViewTo(mView, 0, mRefreshHeight);
        ViewCompat.postInvalidateOnAnimation(RefreshView.this);
        if (mListener != null) {
            mListener.onRefresh();
        }
    }


    public RecyclerView getRecycleView() {
        return mView;
    }

    public void setOpenEvent(boolean openEvent) {
        isOpenEvent = openEvent;
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置一个默认是布局
        mContext = context;
        View.inflate(context, R.layout.view_refresh, this);
        mViewDragHelper = ViewDragHelper.create(this, 1f, callback);

    }

    private final ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mView;
        }


        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            if (top < 0) {
                return 0;
            } else if (top >= 0 && top <= mTopViewHeight) {
                //滑动速率
                return mView.getTop() + (top - mView.getTop()) / 2;
            } else {
                return mTopViewHeight;
            }

        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            layoutToTop = top;

            if (top == 0) {
                mTopView.layout(0, -mTopViewHeight, mViewWidth, 0);
                if (isRefresh){
                    isRefresh= false;
                    isRefreshToStart = false;
                }
            } else if (top > 0 && top <= mTopViewHeight) {
                if (!isRefresh) {
                    if (top > mRefreshHeight) {
                        mTextView.setText("释放立即刷新");
                    } else {
                        mTextView.setText("　下拉刷新　");
                    }
                }

                mTopView.layout(0, -mTopViewHeight + top, mViewWidth, top);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (isRefresh) {
                if (isRefreshToStart){
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                    return;
                }
                if (mView.getTop() >= mRefreshHeight / 1.5) {
                    mViewDragHelper.smoothSlideViewTo(mView, 0, mRefreshHeight);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                } else {
                    isRefresh = false;
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                }

            } else {
                if (mView.getTop() >= mRefreshHeight) {
                    onRefreshStart();
                } else {
                    mViewDragHelper.smoothSlideViewTo(mView, 0, 0);
                    ViewCompat.postInvalidateOnAnimation(RefreshView.this);
                }
            }

        }
    };

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (getChildCount() == 2) {
            mView = (RecyclerView) getChildAt(1);
            mView.setHasFixedSize(true);
            mView.setItemAnimator(new DefaultItemAnimator());
            mView.setLayoutManager(new LinearLayoutManager(mContext));
            mTopView = getChildAt(0);
            mTextView = (TextView) mTopView.findViewById(R.id.refresh_textview);
            mImageView = (ImageView) mTopView.findViewById(R.id.refresh_imageview);

            animation = new RotateAnimation(0, 359, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(500);
            animation.setInterpolator(new LinearInterpolator());
            animation.setRepeatCount(-1);

        } else {
            throw new IllegalStateException("只能有2个子view");
        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTopViewHeight = mTopView.getMeasuredHeight();
        mViewWidth = mView.getMeasuredWidth();
        mViewHeight = mView.getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTopView.layout(0, -mTopViewHeight + layoutToTop, mViewWidth, layoutToTop);
        mView.layout(0, layoutToTop, mViewWidth, mViewHeight + layoutToTop);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean helper = mViewDragHelper.shouldInterceptTouchEvent(ev);
        if (isOpenEvent) {
            if (isRefresh) {
                return true;
            } else {
                boolean result = false;
                switch (MotionEventCompat.getActionMasked(ev)) {
                    case MotionEvent.ACTION_DOWN:
                        downY = (int) ev.getY();

                        LinearLayoutManager lm = (LinearLayoutManager) mView.getLayoutManager();
                        int i = lm.findFirstVisibleItemPosition();
                        int ii = 0;
                        //快速滑动的时候，有时候getTop会报空
                        try {
                            ii = lm.findViewByPosition(lm.findFirstVisibleItemPosition()).getTop();
                        } catch (NullPointerException e) {
                            ii = 10;
                        }

                        isTop = Math.abs(ii) < ViewConfiguration.get(mContext).getScaledTouchSlop() && i == 0;

                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (isTop) {
                            int moveY = (int) ev.getY() - downY;
                            if (moveY > ViewConfiguration.get(mContext).getScaledTouchSlop()) {

                                result = true;
                                mImageView.clearAnimation();
                                mViewDragHelper.captureChildView(mView, ev.getPointerId(0));
                            }

                        }

                        break;
                }
                return helper || result;
            }
        }

        return false;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }
}
