package com.win.dunzi.utils;

import com.win.dunzi.entitys.EnjoyEntity;
import com.win.dunzi.entitys.VideoEntity;

import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * author：WangShuang
 * Date: 2016/1/25 13:34
 * email：m15046658245_1@163.com
 */
public class HttpUtils {
    private static Service mService;

    public static interface Service {

        //专享
        @GET("article/list/suggest")
        public Call<EnjoyEntity> getEnjoy(@Query("page") int page);

        //视频专区
        @GET("article/list/video")
        public Call<VideoEntity> getVideo(@Query("page") int page);

        //纯文专区
        @GET("article/list/text")
        public Call<EnjoyEntity> getText(@Query("page") int page);

        //纯图专区
        @GET("article/list/image")
        public Call<EnjoyEntity> getImage(@Query("page") int page);

        //评论 http://m2.qiushibaike.com/article/%d/comments?page=2 10906479
        //评论 http://m2.qiushibaike.com/article/10906479/comments?page=2

        @GET("article/{id}/comments")
        public Call<EnjoyEntity> getPinglun(@Path("id") int id ,@Query("page") int page);



    }

    static {
        mService = new Retrofit.Builder()
                .baseUrl("http://m2.qiushibaike.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(HttpUtils.Service.class);
    }
    public static Service getService() {
        return mService;
    }
}
