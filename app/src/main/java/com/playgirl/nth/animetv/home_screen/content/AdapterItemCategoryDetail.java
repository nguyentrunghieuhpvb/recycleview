package com.playgirl.nth.animetv.home_screen.content;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
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
import com.playgirl.nth.animetv.utils.RecycleViewOnclick;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nguye on 4/26/2018.
 */

public class AdapterItemCategoryDetail extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "AdapterItemVideo";

    ApiService apiService;
    public final int TYPE_VIDEO = 0;
    public final int TYPE_LOAD = 1;
    private String category;
    private Context context;
    private ArrayList<VideoInfo> listVideoInfo;
    int layoutResource;

    public AdapterItemCategoryDetail(Context context, ArrayList<VideoInfo> lsitVideoInfo, int layoutResource) {
        this.context = context;
        this.listVideoInfo = lsitVideoInfo;
        this.layoutResource = layoutResource;

    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ArrayList<VideoInfo> getListVideoInfo() {
        return listVideoInfo;
    }

    public void setListVideoInfo(ArrayList<VideoInfo> listVideoInfo) {
        this.listVideoInfo.clear();
        this.listVideoInfo.addAll(listVideoInfo);
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_detail_video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final VideoViewHolder videoViewHolder = (VideoViewHolder) holder;
        final VideoInfo videoInfo = listVideoInfo.get(position);
        apiService = (ApiService) ApiUtils.getApiService();
        String url = "videos?part=snippet%2CcontentDetails%2Cstatistics&key=AIzaSyBVbU2eWJWVEBmanOeVPZNpV_RP5PjEwJY&id=" + videoInfo.getId();
        apiService.getVideoData(url).enqueue(new Callback<VideoData>() {
            @Override
            public void onResponse(Call<VideoData> call, Response<VideoData> response) {
                Item item = response.body().getItems().get(0);
                videoInfo.setName(item.getSnippet().getTitle());
                videoInfo.setDes(item.getSnippet().getDescription());
                videoInfo.setThumbUrl(item.getSnippet().getThumbnails().getMedium().getUrl());
                Log.d(TAG, "title : " + item.getSnippet().getTitle());
                videoViewHolder.txtName.setText(videoInfo.getName());
                Glide.with(context).load(videoInfo.getThumbUrl()).into(videoViewHolder.imgThumb);
            }

            @Override
            public void onFailure(Call<VideoData> call, Throwable t) {
            }
        });

        videoViewHolder.imgThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("xxxxx", "id  : " + videoInfo.getId());
                if (videoInfo.getName() != null) {
                    Intent intent = new Intent(context, PlayVideoActivity.class);
                    intent.putExtra(Constance.VIDEO_INFO, videoInfo);
                    context.startActivity(intent);
                }
            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        if (listVideoInfo.get(position) == null) return TYPE_LOAD;
        else return TYPE_VIDEO;
    }

    @Override
    public int getItemCount() {
        return listVideoInfo.size();
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        ImageView imgThumb;

        public VideoViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_name);
            imgThumb = (ImageView) itemView.findViewById(R.id.img_thumb);
        }
    }
}
