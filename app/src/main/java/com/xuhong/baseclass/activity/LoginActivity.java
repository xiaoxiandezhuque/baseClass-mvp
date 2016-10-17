package com.xuhong.baseclass.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.EditText;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.iview.ILoginView;
import com.xuhong.baseclass.presenter.LoginPresenter;
import com.xuhong.baseclass.template.selectmorepicture.TweetPublishContract;
import com.xuhong.baseclass.template.selectmorepicture.view.TweetPicturesPreviewer;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by BHKJ on 2016/8/3.
 */

public class LoginActivity extends BaseActivity implements ILoginView,TweetPublishContract.View {


    @BindView(R.id.edit_login_name)
    EditText editLoginName;
    @BindView(R.id.edit_login_password)
    EditText editLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.recycler_images)
    TweetPicturesPreviewer mLayImages;


    private LoginPresenter mLoginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        mLoginPresenter = new LoginPresenter(this);
//        editLoginName.setText("18180641438");
//        editLoginPassword.setText("123456");
        String a = "手机型号：" + android.os.Build.MODEL + ",系统版本：" + Build.VERSION.RELEASE + ",IMEI:"
                + ((TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();

//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
    }


    @OnClick(R.id.btn_login)
    public void onClick() {


        mLayImages.onLoadMoreClick();

//        SelectImageActivity.showImage(getContext(), 9, true, mImageAdapter.getPaths(), this);
//        mLoginPresenter.login(this);
    }

    @Override
    public String getUserName() {
        return editLoginName.getText().toString();
    }

    @Override
    public String getUserPassword() {
        return editLoginPassword.getText().toString();
    }


    @Override
    public void toOtherActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void clearUserName() {

    }

    @Override
    public void clearUserPassword() {

    }

    @Override
    protected void onDestroy() {
        if (mLoginPresenter != null) {
            mLoginPresenter.detachView();
        }
        mLayImages.destroy();
        super.onDestroy();
    }


    @Override
    public String getContent() {
        return null;
    }

    @Override
    public void setContent(String content) {

    }

    @Override
    public String[] getImages() {
        return mLayImages.getPaths();
    }

    @Override
    public void setImages(String[] paths) {
        mLayImages.set(paths);
    }


}
