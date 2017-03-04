package com.xuhong.baseclass.network;

import android.util.Log;

import com.xuhong.baseclass.bean.HttpResult;

import org.litepal.crud.DataSupport;

import rx.functions.Func1;

/**
 * Created by BHKJ on 2016/8/5.
 */

public class AllDoThingFunc1<T extends DataSupport> implements Func1<HttpResult<T>, T> {
    @Override
    public T call(HttpResult<T> tHttpResult) {
        Log.e("tHttpResult", tHttpResult.getStatus());
        if (!tHttpResult.getStatus().equals("success")) {

            throw new ApiException(tHttpResult.getMsg());
        }

        T result = tHttpResult.getData();
        result.save();
        return result;

    }
}
