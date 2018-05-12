package com.playgirl.nth.animetv.detail;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.playgirl.nth.animetv.R;
import com.playgirl.nth.animetv.data.remote.api.ApiService;
import com.playgirl.nth.animetv.data.remote.api.ApiUtils;
import com.playgirl.nth.animetv.data.remote.model.ItemNextVideo;
import com.playgirl.nth.animetv.data.remote.model.VideoDataNextVideo;
import com.playgirl.nth.animetv.entity.VideoInfo;
import com.playgirl.nth.animetv.home_screen.content.AdapterItemCategoryDetail;
import com.playgirl.nth.animetv.utils.Constance;
import com.playgirl.nth.animetv.utils.EqualSpacingItemDecoration;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayVideoActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, View.OnClickListener {

    YouTubePlayerView playerView;
    TextView txtName;
    RecyclerView recySuggest;
    VideoInfo videoInfo;
    ApiService apiService;
    ArrayList<VideoInfo> listVideoInfo = new ArrayList<>();
    AdapterItemCategoryDetail adapterItemVideo;
    ImageView imgDowload, imgShare;
    long currentTimes = 0;
    String TAG = "PlayVideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playvideo);

        if (savedInstanceState != null) {
            currentTimes = savedInstanceState.getLong("currenttimes");
        }

        videoInfo = (VideoInfo) getIntent().getSerializableExtra(Constance.VIDEO_INFO);
        init();
        adapterItemVideo = new AdapterItemCategoryDetail(this, new ArrayList<VideoInfo>(), R.layout.category_detail_video_item);
        recySuggest.setHasFixedSize(true);
        recySuggest.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
        recySuggest.addItemDecoration(new EqualSpacingItemDecoration(10));
        recySuggest.setAdapter(adapterItemVideo);

        imgShare.setOnClickListener(this);
        imgDowload.setOnClickListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        Log.d("zzzz", "onSaveInstanceState");
        super.onSaveInstanceState(bundle);
        bundle.putLong("currenttimes", currentTimes);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "video id : " + videoInfo.getId());
        apiService = (ApiService) ApiUtils.getApiService();
        String url = "search?part=snippet&maxResults=40&type=video&key=AIzaSyBVbU2eWJWVEBmanOeVPZNpV_RP5PjEwJY&relatedToVideoId=" + videoInfo.getId();
        apiService.getNextVideoData(url).enqueue(new Callback<VideoDataNextVideo>() {
            @Override
            public void onResponse(Call<VideoDataNextVideo> call, Response<VideoDataNextVideo> response) {
                Log.d(TAG, "onResponse");
                for (ItemNextVideo item : response.body().getItems()) {
                    VideoInfo vf = new VideoInfo();
                    vf.setId(item.getId().getVideoId());
                    vf.setName(item.getSnippet().getTitle());
                    vf.setDes(item.getSnippet().getDescription());
                    vf.setThumbUrl(item.getSnippet().getThumbnails().getMedium().getUrl());

                    listVideoInfo.add(vf);
                }
                Log.d(TAG, "size : " + listVideoInfo.size());
                adapterItemVideo.setListVideoInfo(listVideoInfo);

            }

            @Override
            public void onFailure(Call<VideoDataNextVideo> call, Throwable t) {
                Log.d(TAG, "api onFailure : " + t.toString());
            }
        });
    }

    private void init() {
        playerView = findViewById(R.id.playerview);
        txtName = findViewById(R.id.txt_video_name);
        recySuggest = findViewById(R.id.recy_list_suggest);
        imgDowload = findViewById(R.id.img_dowload);
        imgShare = findViewById(R.id.img_share);
        txtName.setText(videoInfo.getName());
        playerView.initialize(Constance.DEVELOPER_KEY, this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {
        Log.d(TAG, "vidoe id :" + videoInfo.getId());
        youTubePlayer.loadVideo(videoInfo.getId(), (int) currentTimes);
        youTubePlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                currentTimes = youTubePlayer.getCurrentTimeMillis();
            }
        });
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Log.d(TAG, "onInitializationFailure");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_dowload:
                setDowload();
                break;
            case R.id.img_share:
                setShare();
                break;
        }
    }

    private void setShare() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post");
        share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=" + videoInfo.getId());

        startActivity(Intent.createChooser(share, "Share link!"));
    }

    private void setDowload() {
    }
}
