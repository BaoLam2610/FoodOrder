package com.example.foodorderapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Banner implements Serializable {
    @SerializedName("banner")
    private String image;
    @SerializedName("content")
    private String content;

    public Banner(String image, String content) {
        this.image = image;
        this.content = content;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
