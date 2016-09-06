package com.xuhong.baseclass.network;

/**
 * Created by BHKJ on 2016/5/10.
 */
public interface OnApiListener<T> {

    void OnSuccess(T data);
    void OnFail(String error);

}
