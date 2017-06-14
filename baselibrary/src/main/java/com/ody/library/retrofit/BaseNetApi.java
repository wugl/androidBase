package com.ody.library.retrofit;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2016/11/30.
 */

public interface BaseNetApi<T> {
    @GET("/api/dolphin/list?&platform=3&platformId=0&pageCode=APP_HOME&adCode=ad_banner&areaCode=310115")
    Observable<T> getAd(@QueryMap Map<String, String> params);
}