package com.CoustomControl.ResponseAndPojoClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Record {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("fullname")
    @Expose
    private String fullname;
    @SerializedName("sponser_id")
    @Expose
    private String sponserId;
    @SerializedName("upline_id")
    @Expose
    private String uplineId;
    @SerializedName("position")
    @Expose
    private String position;
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
    @SerializedName("pin_number")
    @Expose
    private String pinNumber;
    @SerializedName("joining_date")
    @Expose
    private String joiningDate;
    @SerializedName("total_investment")
    @Expose
    private Object totalInvestment;
    @SerializedName("product")
    @Expose
    private Product product;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getSponserId() {
        return sponserId;
    }

    public void setSponserId(String sponserId) {
        this.sponserId = sponserId;
    }

    public String getUplineId() {
        return uplineId;
    }

    public void setUplineId(String uplineId) {
        this.uplineId = uplineId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

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

    public String getPinNumber() {
        return pinNumber;
    }

    public void setPinNumber(String pinNumber) {
        this.pinNumber = pinNumber;
    }

    public String getJoiningDate() {
        return joiningDate;
    }

    public void setJoiningDate(String joiningDate) {
        this.joiningDate = joiningDate;
    }

    public Object getTotalInvestment() {
        return totalInvestment;
    }

    public void setTotalInvestment(Object totalInvestment) {
        this.totalInvestment = totalInvestment;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
