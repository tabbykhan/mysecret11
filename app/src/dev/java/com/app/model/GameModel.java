package com.app.model;

import com.app.appbase.AppBaseModel;

public class GameModel extends AppBaseModel {

    long id;
    String name;
    String image;
    QuotationModel quotation;
    boolean quotationShowing;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return getValidString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public QuotationModel getQuotation() {
        return quotation;
    }

    public void setQuotation(QuotationModel quotation) {
        this.quotation = quotation;
    }

    public boolean isQuotationShowing() {
        return quotationShowing;
    }

    public void setQuotationShowing(boolean quotationShowing) {
        this.quotationShowing = quotationShowing;
    }

    public boolean isCricket(){
        return getId()==0;
    }

    public boolean isKabaddi(){
        return getId()==1;
    }

    public boolean isSoccer(){
        return getId()==2;
    }
}
