package com.app.model;

import com.app.appbase.AppBaseModel;

public class MatchUserModel extends AppBaseModel {

    private String id;
    private long unique_id;
    private String name;
    private String short_title;
    private String subtitle;
    private long match_date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(long unique_id) {
        this.unique_id = unique_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShort_title() {
        return getValidString(short_title);
    }

    public void setShort_title(String short_title) {
        this.short_title = short_title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public long getMatch_date() {
        return match_date;
    }

    public void setMatch_date(long match_date) {
        this.match_date = match_date;
    }

    public String getCreatedDateText() {
        return getFormatedDateString(DATE_DDMMYYYY, getMatch_date());
    }
}
