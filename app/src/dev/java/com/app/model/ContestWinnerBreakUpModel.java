package com.app.model;

import com.app.appbase.AppBaseModel;

import java.util.List;

public class ContestWinnerBreakUpModel extends AppBaseModel {
    List<String> per_min_p;
    List<String> per_max_p;
    List<String> per_price;
    List<String> per_percent;

    public List<String> getPer_min_p() {
        return per_min_p;
    }

    public void setPer_min_p(List<String> per_min_p) {
        this.per_min_p = per_min_p;
    }

    public List<String> getPer_max_p() {
        return per_max_p;
    }

    public void setPer_max_p(List<String> per_max_p) {
        this.per_max_p = per_max_p;
    }

    public List<String> getPer_price() {
        return per_price;
    }

    public void setPer_price(List<String> per_price) {
        this.per_price = per_price;
    }

    public int size() {
        return per_price == null ? 0 : per_price.size();
    }

    public List<String> getPer_percent() {
        return per_percent;
    }

    public void setPer_percent(List<String> per_percent) {
        this.per_percent = per_percent;
    }
}
