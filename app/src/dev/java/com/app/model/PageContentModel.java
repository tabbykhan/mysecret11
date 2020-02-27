package com.app.model;

import com.app.appbase.AppBaseModel;

public class PageContentModel extends AppBaseModel {

    private String title;
    private String content;

    public String getTitle() {
        return getValidString(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return getValidString(content);
    }

    public void setContent(String content) {
        this.content = content;
    }
}
