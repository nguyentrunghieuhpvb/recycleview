package com.playgirl.nth.animetv.detail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.playgirl.nth.animetv.R;
import com.playgirl.nth.animetv.data.remote.api.ApiService;
import com.playgirl.nth.animetv.data.remote.api.ApiUtils;
import com.playgirl.nth.animetv.data.remote.model.ItemNextVideo;
import com.playgirl.nth.animetv.data.remote.model.VideoDataNextVideo;
import com.playgirl.nth.animetv.entity.VideoInfo;
import com.playgirl.nth.animetv.home_screen.content.AdapterItemCategoryDetail;
import com.playgirl.nth.animetv.utils.Constance;
import com.playgirl.nth.animetv.utils.EqualSpacingItemDecoration;
import com.playgirl.nth.animetv.utils.GridAutofitLayoutManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryDetailActivity extends AppCompatActivity {

    String TAG = "CategoryDetailAcivity";
    RecyclerView recyCategoryDetail;
    String category;
    ArrayList<VideoInfo> listVideoInfo = new ArrayList<>();
    TextView txtTitle;
    ImageView imgBack;
    MaterialSearchView searchView;
    ApiService apiService;
    AdapterItemCategoryDetail adapterItemVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        searchView = findViewById(R.id.search_view);
        txtTitle = findViewById(R.id.txt_category);
        imgBack = findViewById(R.id.img_backfromcategory);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        category = getIntent().getStringExtra(Constance.CATEGORY);
        txtTitle.setText(category + "");

        recyCategoryDetail = findViewById(R.id.recy_category_detail);

        GridAutofitLayoutManager layoutManager = new GridAutofitLayoutManager(this, 400);
        recyCategoryDetail.setLayoutManager(layoutManager);
        recyCategoryDetail.addItemDecoration(new EqualSpacingItemDecoration(20, EqualSpacingItemDecoration.HORIZONTAL));
        recyCategoryDetail.setHasFixedSize(true);


        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(category);

        adapterItemVideo = new AdapterItemCategoryDetail(CategoryDetailActivity.this, new ArrayList<VideoInfo>(), R.layout.category_detail_video_item);
        recyCategoryDetail.setAdapter(adapterItemVideo);
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
                adapterItemVideo.setListVideoInfo(listVideoInfo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, " onCancelled");
            }
        });

        setSearchView();
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            adapterItemVideo.setListVideoInfo(listVideoInfo);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    private void getListSearchVideo(String textQuery) {
        final ArrayList<VideoInfo> listTmp = new ArrayList<>();
        apiService = (ApiService) ApiUtils.getApiService();
        String url = "search?part=snippet&maxResults=40&key=AIzaSyBVbU2eWJWVEBmanOeVPZNpV_RP5PjEwJY&q=" + textQuery;
        apiService.getNextVideoData(url).enqueue(new Callback<VideoDataNextVideo>() {
            @Override
            public void onResponse(Call<VideoDataNextVideo> call, Response<VideoDataNextVideo> response) {
                Log.d(TAG, "onResponse");
                for (ItemNextVideo item : response.body().getItems()) {
                    VideoInfo vf = new VideoInfo();
                    vf.setId(item.getId().getVideoId());
                    listTmp.add(vf);
                }
                Log.d(TAG, "size : " + listTmp.size());
                adapterItemVideo.setListVideoInfo(listTmp);
            }

            @Override
            public void onFailure(Call<VideoDataNextVideo> call, Throwable t) {
                Log.d(TAG, "api onFailure : " + t.toString());
            }
        });
    }

    private void setSearchView() {
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getListSearchVideo(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }
}
