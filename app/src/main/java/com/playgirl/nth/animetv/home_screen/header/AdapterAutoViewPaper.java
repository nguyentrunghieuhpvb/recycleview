package com.playgirl.nth.animetv.home_screen.header;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.playgirl.nth.animetv.entity.VideoInfo;

import java.util.ArrayList;

/**
 * Created by nguye on 4/25/2018.
 */

public class AdapterAutoViewPaper extends FragmentPagerAdapter {
    String TAG = "AdapterAutoViewPaper";
    ArrayList<Fragment> lisFragment;


    public AdapterAutoViewPaper(FragmentManager fm, ArrayList<Fragment> lisFragment) {
        super(fm);
        this.lisFragment = lisFragment;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d(TAG,"id : " + lisFragment.get(position).getId());
        return lisFragment.get(position);
    }

    @Override
    public int getCount() {
        return lisFragment.size();
    }
}
