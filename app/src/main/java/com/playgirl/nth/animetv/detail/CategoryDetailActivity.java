package com.playgirl.nth.animetv.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.playgirl.nth.animetv.R;
import com.playgirl.nth.animetv.entity.VideoInfo;
import com.playgirl.nth.animetv.home_screen.content.AdapterItemCategoryDetail;
import com.playgirl.nth.animetv.utils.Constance;
import com.playgirl.nth.animetv.utils.EqualSpacingItemDecoration;
import com.playgirl.nth.animetv.utils.GridSpacingItemDecoration;

import java.io.SequenceInputStream;
import java.util.ArrayList;

public class CategoryDetailActivity extends AppCompatActivity {

    String TAG = "CategoryDetailAcivity";
    RecyclerView recyCategoryDetail;
    String category;
    ArrayList<VideoInfo> listVideoInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);


        category = getIntent().getStringExtra(Constance.CATEGORY);
        setTitle(category);

        recyCategoryDetail = findViewById(R.id.recy_category_detail);
        recyCategoryDetail.setHasFixedSize(true);
        recyCategoryDetail.setLayoutManager(new GridLayoutManager(this, 2));
        recyCategoryDetail.addItemDecoration(new EqualSpacingItemDecoration(10));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(category);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange");
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    String id = (String) x.getValue();
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setId(id);
                    listVideoInfo.add(videoInfo);
                }
                AdapterItemCategoryDetail adapterItemVideo = new AdapterItemCategoryDetail(CategoryDetailActivity.this, listVideoInfo, R.layout.category_detail_video_item);
                recyCategoryDetail.setAdapter(adapterItemVideo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, " onCancelled");
            }
        });

    }
}
