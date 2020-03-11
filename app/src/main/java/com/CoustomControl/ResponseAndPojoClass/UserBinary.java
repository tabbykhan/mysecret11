package com.CoustomControl.ResponseAndPojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserBinary {
    @SerializedName("left_id")
    @Expose
    private Integer leftId;
    @SerializedName("right_id")
    @Expose
    private Integer rightId;
    @SerializedName("left_bv")
    @Expose
    private Integer leftBv;
    @SerializedName("right_bv")
    @Expose
    private Integer rightBv;

    public Integer getLeftId() {
        return leftId;
    }

    public void setLeftId(Integer leftId) {
        this.leftId = leftId;
    }

    public Integer getRightId() {
        return rightId;
    }

    public void setRightId(Integer rightId) {
        this.rightId = rightId;
    }

    public Integer getLeftBv() {
        return leftBv;
    }

    public void setLeftBv(Integer leftBv) {
        this.leftBv = leftBv;
    }

    public Integer getRightBv() {
        return rightBv;
    }

    public void setRightBv(Integer rightBv) {
        this.rightBv = rightBv;
    }

    public UserBinary() {

    }
}
