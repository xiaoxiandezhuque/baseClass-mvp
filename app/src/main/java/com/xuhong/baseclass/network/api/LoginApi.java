package com.xuhong.baseclass.network.api;

import com.xuhong.baseclass.bean.HttpResult;
import com.xuhong.baseclass.bean.UserBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by BHKJ on 2016/8/5.
 */

public interface LoginApi {
    @FormUrlEncoded
    @POST("loginByAccount")
    Observable<HttpResult<UserBean>> loginByAccount(@Header("token") String token,
                                                    @Field("username") String username,
                                                    @Field("userpwd") String password);
}
