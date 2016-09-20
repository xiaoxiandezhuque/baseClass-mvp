package com.xuhong.baseclass.network.api;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by BHKJ on 2016/9/19.
 */

public interface DownloadFileApi {

    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

}
