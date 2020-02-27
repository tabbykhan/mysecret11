package com.app.model;

import com.app.appbase.AppBaseModel;

public class SliderModel extends AppBaseModel {

    long id;
    String image_thumb;
    String image_large;
    MatchModel match;
    String content;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage_thumb() {
        return getValidString(image_thumb);
    }

    public void setImage_thumb(String image_thumb) {
        this.image_thumb = image_thumb;
    }

    public String getImage_large() {
        return getValidString(image_large);
    }

    public void setImage_large(String image_large) {
        this.image_large = image_large;
    }

    public MatchModel getMatch() {
        return match;
    }

    public void setMatch(MatchModel match) {
        this.match = match;
    }

    public String getContent() {
        return getValidString(content);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
