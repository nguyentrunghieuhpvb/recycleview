package com.playgirl.nth.animetv.entity;

import java.io.Serializable;

/**
 * Created by nguye on 4/26/2018.
 */

public class VideoInfo implements Serializable {
    String type;
    String id, thumbUrl, name, des, publicer, time, view;

    public VideoInfo() {
    }


    public VideoInfo(String id, String thumbUrl, String name, String des, String publicer, String time, String view) {
        this.id = id;
        this.thumbUrl = thumbUrl;
        this.name = name;
        this.des = des;
        this.publicer = publicer;
        this.time = time;
        this.view = view;
    }

    public VideoInfo(String thumbUrl, String name, String des, String publicer, String time, String view) {
        this.thumbUrl = thumbUrl;
        this.name = name;
        this.des = des;
        this.publicer = publicer;
        this.time = time;
        this.view = view;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getPublicer() {
        return publicer;
    }

    public void setPublicer(String publicer) {
        this.publicer = publicer;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
