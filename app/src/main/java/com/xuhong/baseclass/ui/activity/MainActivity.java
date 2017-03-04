package com.xuhong.baseclass.ui.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.ui.adapter.MyFragmentPagerAdapter;
import com.xuhong.baseclass.ui.fragment.MainFragment;
import com.xuhong.baseclass.utils.MyToast;
import com.xuhong.baseclass.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class MainActivity extends BaseActivity {


    @BindView(R.id.vp_main)
    NoScrollViewPager vpMain;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;


    private List<Fragment> mFragments;
    private LinearLayout[] llTabs;
    private TextView[] tvTabs;
    private ImageView[] ivTabs;

    private int[][] imgRes;
    private int[] textColors;


    private int mCurIndex;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initView() {

        mFragments = new ArrayList<>();
        mFragments.add(new MainFragment());
        mFragments.add(new MainFragment());
        mFragments.add(new MainFragment());
        mFragments.add(new MainFragment());


        textColors = new int[]{
                Color.GRAY,
                Color.parseColor("#009cff"),
        };
        imgRes = new int[][]{
                {R.mipmap.main_1, R.mipmap.main_1_select},
                {R.mipmap.main_2, R.mipmap.main_2_select},
                {R.mipmap.main_3, R.mipmap.main_3_select},
                {R.mipmap.main_4, R.mipmap.main_4_select},


        };


        vpMain.setAdapter(new MyFragmentPagerAdapter(mFragments, getSupportFragmentManager()));
        vpMain.setCurrentItem(0);
        vpMain.setOffscreenPageLimit(4);

        int tabNum = llBottom.getChildCount();
        llTabs = new LinearLayout[tabNum];
        tvTabs = new TextView[tabNum];
        ivTabs = new ImageView[tabNum];
        for (int i = 0; i < tabNum; i++) {
            llTabs[i] = (LinearLayout) llBottom.getChildAt(i);
            ivTabs[i] = (ImageView) llTabs[i].getChildAt(0);
            tvTabs[i] = (TextView) llTabs[i].getChildAt(1);
            if (i == 0) {
                tvTabs[i].setTextColor(textColors[1]);
//                tvTitle.setTitleText(titleStrings[0]);
            }
            llTabs[i].setTag(i);
            llTabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i = (int) v.getTag();
                    vpMain.setCurrentItem(i);
                }
            });
        }
        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                tvTabs[position].setTextColor(textColors[1]);
//                tvTitle.setTitleText(titleStrings[position]);
                ivTabs[position].setImageResource(imgRes[position][1]);


                tvTabs[mCurIndex].setTextColor(textColors[0]);
                ivTabs[mCurIndex].setImageResource(imgRes[mCurIndex][0]);

                mCurIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    private  long firstClick;
    @Override
    public void onBackPressed() {
        long secondClick = System.currentTimeMillis();
        if (secondClick - firstClick > 2000) {
            MyToast.showToast("再次点击退出");
            firstClick = secondClick;
        } else {
            finish();
        }
    }
}
