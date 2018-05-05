package com.playgirl.nth.animetv.home_screen.content;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playgirl.nth.animetv.R;
import com.playgirl.nth.animetv.detail.CategoryDetailActivity;
import com.playgirl.nth.animetv.entity.VideoInfo;
import com.playgirl.nth.animetv.utils.Constance;

import java.util.ArrayList;

/**
 * Created by nguye on 4/26/2018.
 */

public class AdapterListVideo extends RecyclerView.Adapter<AdapterListVideo.MyViewHolder> {
    String TAG = "AdapterListVideo";
    private Context context;
    private String listCategory[];



    public AdapterListVideo(Context context, String listCategory[]) {
        this.context = context;
        this.listCategory = listCategory;

    }


    public void setListCategory(String listCategory[]) {
        this.listCategory = listCategory;
    }

    @Override
    public AdapterListVideo.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_video, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterListVideo.MyViewHolder holder, final int position) {
        final ArrayList<String> lisId = new ArrayList<>();
        final ArrayList<VideoInfo> listVideoInfo = new ArrayList<>();
        final RecyclerView recy = holder.recyListVideo;
        holder.txtCategory.setText(listCategory[position]);
        recy.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recy.setLayoutManager(layoutManager);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(listCategory[position]);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG,"onDataChange");
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    String id = (String) x.getValue();
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setId(id);
                    listVideoInfo.add(videoInfo);
                    Log.d(TAG,listCategory[position] + id);
                }
                AdapterItemVideo adapterItemVideo = new AdapterItemVideo(context, listVideoInfo,R.layout.video_item);
                recy.setAdapter(adapterItemVideo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG,listCategory[position] +" onCancelled");
            }
        });

        holder.imgMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryDetailActivity.class);
                intent.putExtra(Constance.CATEGORY, listCategory[position]);
                context.startActivity(intent);
            }
        });
//        recy.addItemDecoration(new EqualSpacingItemDecoration(16));
    }

    @Override
    public int getItemCount() {
        return listCategory.length ;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory;
        RecyclerView recyListVideo;
        ImageView imgMore;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgMore = (ImageView) itemView.findViewById(R.id.img_more_detail);
            txtCategory = (TextView) itemView.findViewById(R.id.txt_category);
            recyListVideo = (RecyclerView) itemView.findViewById(R.id.recy_list_item);
        }
    }
}
