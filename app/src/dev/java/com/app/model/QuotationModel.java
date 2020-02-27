package com.app.model;

import com.app.appbase.AppBaseModel;

public class QuotationModel extends AppBaseModel {

    long width;
    long height;
    String image;
    String link;

    public long getWidth() {
        return width;
    }

    public void setWidth(long width) {
        this.width = width;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return getValidString(link);
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isValidQuotation(){
        return getWidth()>0 && getHeight()>0 && isValidString(getImage());
    }
}
