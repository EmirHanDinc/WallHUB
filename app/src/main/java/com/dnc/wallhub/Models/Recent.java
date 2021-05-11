package com.dnc.wallhub.Models;

import com.google.firebase.database.Exclude;

public class Recent {
    @Exclude
    public String id;
    public String title, desc, url;


    @Exclude
    public boolean isFavourite = false;

    public Recent(String id, String title, String desc, String url) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.url = url;
    }
}
