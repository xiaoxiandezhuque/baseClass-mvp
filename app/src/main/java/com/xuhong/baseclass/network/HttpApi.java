package com.xuhong.baseclass.network;


import com.xuhong.baseclass.bean.UserBean;
import com.xuhong.baseclass.network.api.LoginApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by BHKJ on 2016/5/10.
 */
public class HttpApi {

    private  static  final String  BASE_URL ="";

    private  String  token ="" ;
    private final Retrofit mRetrofit;

    //单列
    public static HttpApi getInstance() {
        return SingeInstance.INSTANCE;
    }

    private static class SingeInstance {
        private static final HttpApi INSTANCE = new HttpApi();
    }

    private  HttpApi() {


        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

    }

    //loginByAccount
    public void login(String userName, String password, AllDoThingSubscriber<UserBean> progressSubscriber) {

        LoginApi loginApi = mRetrofit.create(LoginApi.class);
        loginApi.loginByAccount(token, userName, password)
                .map(new AllDoThingFunc1<UserBean>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(progressSubscriber);

    }


}
