package com.xuhong.baseclass.network;


import android.support.annotation.NonNull;

import com.xuhong.baseclass.network.api.DownloadFileApi;
import com.xuhong.baseclass.network.download.DownloadProgressInterceptor;
import com.xuhong.baseclass.network.download.DownloadProgressListener;
import com.xuhong.baseclass.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by BHKJ on 2016/5/10.
 */
public class HttpApi {

    public static final String BASE_URL = "http://download.fir.im/";

    private String token = "";
//    private final Retrofit mRetrofit;

    //单列
    public static HttpApi getInstance() {
        return SingeInstance.INSTANCE;
    }

    private static class SingeInstance {
        private static final HttpApi INSTANCE = new HttpApi();
    }

//    private HttpApi() {
//        OkHttpClient okHttpClient = new OkHttpClient()
//                .newBuilder()
//                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                .build();
//
//        mRetrofit = new Retrofit.Builder()
//                .client(okHttpClient)
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .build();
//
//    }

//    //loginByAccount
//    public void login(String userName, String password, AllDoThingSubscriber<UserBean> progressSubscriber) {
//
//        LoginApi loginApi = mRetrofit.create(LoginApi.class);
//        loginApi.loginByAccount(token, userName, password)
//                .map(new AllDoThingFunc1<UserBean>())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(progressSubscriber);
//
//    }


    public void downloadAPK(DownloadProgressListener listener,
                            @NonNull String url,
                            final File file,
                            Subscriber subscriber) {
        DownloadProgressInterceptor interceptor = new DownloadProgressInterceptor(listener);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        retrofit.create(DownloadFileApi.class)
                .download(url)
                .map(new Func1<ResponseBody, InputStream>() {
                    @Override
                    public InputStream call(ResponseBody responseBody) {
                        return responseBody.byteStream();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .doOnNext(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {
                        try {
                            FileUtils.writeFile(inputStream, file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<InputStream>() {
                    @Override
                    public void call(InputStream inputStream) {

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {

                    }
                });
    }


}
