package com.app.model;

import com.app.appbase.AppBaseModel;

public class PlayerEventModel extends AppBaseModel {
    String key;
    float points;
    String value;


    public String getKey() {
        return getValidString(key);
    }

    public void setKey(String key) {
        this.key = key;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getValue() {
        return getValidString(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPointsText() {
        String s = getValidDecimalFormat(getPoints());
        return s.replaceAll("\\.00", "");
    }

}
