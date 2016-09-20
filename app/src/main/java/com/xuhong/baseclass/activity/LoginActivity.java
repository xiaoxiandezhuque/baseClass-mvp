package com.xuhong.baseclass.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.xuhong.baseclass.R;
import com.xuhong.baseclass.iview.ILoginView;
import com.xuhong.baseclass.presenter.LoginPresenter;
import com.xuhong.baseclass.service.MyService;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by BHKJ on 2016/8/3.
 */

public class LoginActivity extends BaseActivity implements ILoginView {


    @BindView(R.id.edit_login_name)
    EditText editLoginName;
    @BindView(R.id.edit_login_password)
    EditText editLoginPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;


    private LoginPresenter mLoginPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

        mLoginPresenter = new LoginPresenter(this);
        editLoginName.setText("18180641438");
        editLoginPassword.setText("123456");

        Intent intent = new Intent(this, MyService.class);
        startService(intent);
    }


    @OnClick(R.id.btn_login)
    public void onClick() {

        mLoginPresenter.login(this);
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

        super.onDestroy();
    }
}
