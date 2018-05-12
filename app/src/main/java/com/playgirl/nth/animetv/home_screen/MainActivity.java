package com.playgirl.nth.animetv.home_screen;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.playgirl.nth.animetv.R;
import com.playgirl.nth.animetv.detail.CategoryDetailActivity;
import com.playgirl.nth.animetv.detail.PlayVideoActivity;
import com.playgirl.nth.animetv.entity.VideoInfo;
import com.playgirl.nth.animetv.home_screen.content.AdapterListVideo;
import com.playgirl.nth.animetv.home_screen.header.FragmentVideo;
import com.playgirl.nth.animetv.home_screen.header.AdapterAutoViewPaper;
import com.rd.PageIndicatorView;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    String TAG = "MainActivity";
    Dialog dialog;
    AutoScrollViewPager viewPager;
    PageIndicatorView pageIndicatorView;
    RecyclerView recyMain;


    EditText edtFeedBack;
    ImageView imgSendFeedBack;

    ArrayList<Fragment> listFragmentSlide = new ArrayList<>();
    ArrayList<VideoInfo> listHeaderVideo = new ArrayList<>();
    String listCategory[] = {"Hot", "TopComment", "Action", "Funny"};

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("header");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.icon_menu, getTheme());
        toggle.setHomeAsUpIndicator(drawable);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();

        setHeader();
        setListContentVideo();
        setDialogFeedBack();
    }

    private void setDialogFeedBack() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.send_feed_back);

        imgSendFeedBack = dialog.findViewById(R.id.img_send_feedback);
        edtFeedBack = dialog.findViewById(R.id.edt_feed_back);

        imgSendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = edtFeedBack.getText().toString();
                DatabaseReference myRef1 = database.getReference("FeedBack");
                if (text.length() < 10) {
                    Toast.makeText(MainActivity.this, "Write feed back content before", Toast.LENGTH_LONG).show();
                } else {
                    myRef1.child(text).setValue(text, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                            if (databaseError == null) {
//                                Toast.makeText(MainActivity.this, "send feed back fail. Let try this again", Toast.LENGTH_LONG).show();
//                            } else {
                                Toast.makeText(MainActivity.this, "Suscess. Thanks you for your feed back ", Toast.LENGTH_LONG).show();
                                dialog.hide();
//                            }
                        }
                    });
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }


    void setHeader() {
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot x : dataSnapshot.getChildren()) {
                    String id = (String) x.getValue();
                    Log.d(TAG, "id :" + id);
                    VideoInfo videoInfo = new VideoInfo();
                    videoInfo.setId(id);
                    FragmentVideo fv = FragmentVideo.newInstance(videoInfo);
                    listFragmentSlide.add(fv);
                }
                Log.d("xxxx", "size : " + listFragmentSlide.size());
                final AdapterAutoViewPaper adaper = new AdapterAutoViewPaper(getSupportFragmentManager(), listFragmentSlide);
                viewPager.setAdapter(adaper);
                viewPager.setInterval(5000);
                viewPager.startAutoScroll();
                pageIndicatorView = findViewById(R.id.pageIndicatorView);
                pageIndicatorView.setCount(5); // specify total count of indicators

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });
    }

    private void setListContentVideo() {
        AdapterListVideo adapterListVideo = new AdapterListVideo(MainActivity.this, listCategory);
        recyMain.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyMain.setAdapter(adapterListVideo);
    }

    private void init() {
//        mSwipeRefreshLayout = findViewById(R.id.)
        viewPager = (AutoScrollViewPager) findViewById(R.id.view_pager);
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        recyMain = (RecyclerView) findViewById(R.id.recy_main);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_feedback) {
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
