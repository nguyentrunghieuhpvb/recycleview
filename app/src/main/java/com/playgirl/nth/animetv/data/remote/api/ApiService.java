package com.playgirl.nth.animetv.data.remote.api;

import com.playgirl.nth.animetv.data.remote.model.VideoData;
import com.playgirl.nth.animetv.data.remote.model.VideoDataNextVideo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by nguye on 4/28/2018.
 */

public interface ApiService {

    @GET
    Call<VideoData> getVideoData(@Url String url);

    // next vs search are same
    @GET
    Call<VideoDataNextVideo> getNextVideoData(@Url String url);
}
