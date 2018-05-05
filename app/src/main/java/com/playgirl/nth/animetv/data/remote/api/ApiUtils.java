package com.playgirl.nth.animetv.data.remote.api;

import com.playgirl.nth.animetv.data.remote.model.VideoData;

/**
 * Created by nguye on 4/28/2018.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://www.googleapis.com/youtube/v3/";

    public static ApiService getApiService() {
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
