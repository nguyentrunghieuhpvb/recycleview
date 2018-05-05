package com.playgirl.nth.animetv.data.remote.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nguye on 5/1/2018.
 */

public class VideoDataNextVideo {
    @SerializedName("items")
    @Expose
    private List<ItemNextVideo> items = null;

    public List<ItemNextVideo> getItems() {
        return items;
    }

    public void setItems(List<ItemNextVideo> items) {
        this.items = items;
    }

}
