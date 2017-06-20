package com.wt.app.banner.bean;

import com.wt.app.banner.view.RecyclerBanner;

/**
 * Created by wuguilin on 6/15/2017.
 */

public class Entity implements RecyclerBanner.BannerEntity {

    String url;

    public Entity(String url) {
        this.url = url;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
