package com.xuhong.baseclass.presenter;

import android.content.Context;

import com.xuhong.baseclass.iview.ILoginView;
import com.xuhong.baseclass.view.LoadingDialog;

/**
 * Created by BHKJ on 2016/5/11.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {


    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(final Context context) {
        LoadingDialog loadingDialog =new LoadingDialog(context);
        loadingDialog.show();


    }


}
