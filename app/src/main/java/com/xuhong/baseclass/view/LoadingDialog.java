package com.xuhong.baseclass.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xuhong.baseclass.R;


/**
 * 自定义加载dialog效果
 *
 * @author BHKJ
 */
public class LoadingDialog extends Dialog {
    private static final int CHANGE_TITLE_WHAT = 1;
    private static final int CHNAGE_TITLE_DELAYMILLIS = 300;
    private static final int MAX_SUFFIX_NUMBER = 3;
    private static final char SUFFIX = '.';
    private TextView point;
    private final String tips;



    public LoadingDialog(Context context) {
        this(context, "加载中");
    }

    /**
     * 自定义主题及布局的构造方法
     *
     * @param context
     * @param tips
     */
    public LoadingDialog(Context context, String tips) {
        super(context, R.style.MyDialogStyle);

        this.tips = tips;
        init(context);
    }

    private void init(Context context) {
        // 指定布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.dialog_loading, null);// 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局

        TextView tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字
        point = (TextView) v.findViewById(R.id.tv_point);// ...

        tipTextView.setText(tips);// 设置加载信息

        LoadingDialog.this.setCancelable(true);// 可以用“返回键”取消
        LoadingDialog.this.setContentView(layout, new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT));// 设置布局
    }

    @Override
    public void show() {
        handler.sendEmptyMessage(CHANGE_TITLE_WHAT);
        super.show();
    }

    private final Handler handler = new Handler() {
        private int num = 0;

        public void handleMessage(android.os.Message msg) {
            if (msg.what == CHANGE_TITLE_WHAT) {
                StringBuilder builder = new StringBuilder();
                if (num >= MAX_SUFFIX_NUMBER) {
                    num = 0;
                }
                num++;
                for (int i = 0; i < num; i++) {
                    builder.append(SUFFIX);
                }
                point.setText(builder.toString());
                if (isShowing()) {
                    handler.sendEmptyMessageDelayed(CHANGE_TITLE_WHAT, CHNAGE_TITLE_DELAYMILLIS);
                } else {
                    num = 0;
                }
            }
        }
    };

    @Override
    public void dismiss() {
        super.dismiss();
        handler.removeMessages(CHANGE_TITLE_WHAT);

    }
}