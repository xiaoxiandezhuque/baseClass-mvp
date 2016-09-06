package com.xuhong.baseclass.network;

import android.content.Context;

import com.xuhong.baseclass.utils.MyLog;
import com.xuhong.baseclass.utils.MyToast;
import com.xuhong.baseclass.view.LoadingDialog;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * Created by BHKJ on 2016/8/5.
 */

public class AllDoThingSubscriber<T> extends Subscriber<T> {


    private final OnApiListener<T> mListener;
    private final WeakReference<Context> mContext;
    private LoadingDialog mLoadingDialog;
    private final boolean isDialog;

    public AllDoThingSubscriber(Context context, OnApiListener<T> onApiListener) {
        this(context, onApiListener, true);

    }

    public AllDoThingSubscriber(Context context, OnApiListener<T> onApiListener, boolean isDialog) {
        this.mListener = onApiListener;
        this.mContext = new WeakReference<>(context);
        this.isDialog = isDialog;
    }


    @Override
    public void onCompleted() {
        hideDialog();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isDialog && mContext.get() != null) {
            mLoadingDialog = new LoadingDialog(mContext.get());

        }
        if (mLoadingDialog!=null){
            mLoadingDialog.show();
        }

    }

    @Override
    public void onError(Throwable e) {
        String error = null;
        if (mContext.get() != null) {

            if (e instanceof SocketTimeoutException) {
                error = "网络中断，请检查您的网络状态";
            } else if (e instanceof ConnectException) {
                error = "网络中断，请检查您的网络状态";
            } else if(e instanceof UnknownHostException){
                error = "还没联网";

            }else {
                error = e.getMessage();
            }
            MyToast.showToast(error);
            MyLog.e("onError", e.toString());
        }
        if (mListener != null) {
            mListener.OnFail(error);
        }

        hideDialog();
    }

    @Override
    public void onNext(T t) {

        if (mListener != null) {
            mListener.OnSuccess(t);
        }
    }

    private void hideDialog() {
        if (isDialog && mLoadingDialog != null && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
