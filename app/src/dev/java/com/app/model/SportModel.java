package com.app.model;

import com.app.appbase.AppBaseModel;

public class SportModel extends AppBaseModel {

    /**
     * sport_id : 0
     * name : Cricket
     */

    private long sport_id;
    private String name;

    public long getSport_id() {
        return sport_id;
    }

    public void setSport_id(long sport_id) {
        this.sport_id = sport_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
