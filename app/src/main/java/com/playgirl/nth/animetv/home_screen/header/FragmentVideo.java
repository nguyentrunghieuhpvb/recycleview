package com.playgirl.nth.animetv.home_screen.header;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.playgirl.nth.animetv.R;
import com.playgirl.nth.animetv.data.remote.api.ApiService;
import com.playgirl.nth.animetv.data.remote.api.ApiUtils;
import com.playgirl.nth.animetv.data.remote.model.Item;
import com.playgirl.nth.animetv.data.remote.model.VideoData;
import com.playgirl.nth.animetv.detail.PlayVideoActivity;
import com.playgirl.nth.animetv.entity.VideoInfo;
import com.playgirl.nth.animetv.utils.Constance;

import java.awt.font.TextAttribute;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nguye on 4/25/2018.
 */

public class FragmentVideo extends Fragment {

    String TAG = "FragmentVideo";
    VideoInfo videoInfo = new VideoInfo();
    VideoData videoData;
    ApiService apiService;
    ConstraintLayout layoutHeader;

    public static FragmentVideo newInstance(VideoInfo videoInfo) {
        Log.d("FragmentVideo", "newInstance");
        FragmentVideo fragmentVideo = new FragmentVideo();
        fragmentVideo.setVideoInfo(videoInfo);
        return fragmentVideo;
    }

    public VideoInfo getVideoInfo() {
        return videoInfo;
    }

    public void setVideoInfo(VideoInfo videoInfo) {
        this.videoInfo = videoInfo;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("FragmentVideo", "onCreate");
        apiService = (ApiService) ApiUtils.getApiService();
        String url = "videos?part=snippet%2CcontentDetails%2Cstatistics&key=AIzaSyB5Jgo4jTYPq5Nep-7k2KqQCHjV4wbWC-w&id=" + videoInfo.getId();
        apiService.getVideoData(url).enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                Item item = response.body().getItems().get(0);
                videoInfo.setName(item.getSnippet().getTitle());
                videoInfo.setDes(item.getSnippet().getDescription());
                videoInfo.setThumbUrl(item.getSnippet().getThumbnails().getMedium().getUrl());
                Log.d(TAG, "oncreat thumb : " + videoInfo.getThumbUrl());
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {

            }
        });
        super.onCreate(savedInstanceState);
    }


    ImageView img;
    TextView txtName;
    TextView txtDes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("FragmentVideo", "onCreateView");

        View view = inflater.inflate(R.layout.header, container, false);
        layoutHeader = view.findViewById(R.id.layout_header);
        img = view.findViewById(R.id.img_thumb);
        txtName = (TextView) view.findViewById(R.id.txt_film_name);
        txtDes = (TextView) view.findViewById(R.id.film_des);
        txtName.setText(videoInfo.getName());
        txtDes.setText(videoInfo.getDes());
        Glide.with(getContext()).load(videoInfo.getThumbUrl()).into(img);

        layoutHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PlayVideoActivity.class);
                intent.putExtra(Constance.VIDEO_INFO, videoInfo);
                getContext().startActivity(intent);
            }
        });
        return view;
    }

}
