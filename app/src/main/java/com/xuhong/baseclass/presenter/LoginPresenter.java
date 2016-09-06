package com.xuhong.baseclass.presenter;

import android.content.Context;

import com.xuhong.baseclass.bean.UserBean;
import com.xuhong.baseclass.iview.ILoginView;
import com.xuhong.baseclass.network.AllDoThingSubscriber;
import com.xuhong.baseclass.network.HttpApi;
import com.xuhong.baseclass.network.OnApiListener;
import com.xuhong.baseclass.utils.StringUtils;

/**
 * Created by BHKJ on 2016/5/11.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {


    public LoginPresenter(ILoginView view) {
        super(view);
    }

    public void login(Context context) {

        HttpApi.getInstance().login(getView().getUserName(),
                StringUtils.stringToMD5(getView().getUserPassword()),
                new AllDoThingSubscriber<UserBean>(context, new OnApiListener<UserBean>() {
                    @Override
                    public void OnSuccess(UserBean data) {
                       if (isViewAttached()){
                           getView().toOtherActivity();
                       }
                    }

                    @Override
                    public void OnFail(String error) {

                    }
                }));

    }


}
