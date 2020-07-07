package com.app.model.websoccerresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.MatchModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SoccerFixtureMatchResponse extends AppBaseResponseModel {

//    @SerializedName("code")
//    @Expose
//    private Integer code;
//    @SerializedName("error")
//    @Expose
//    private Boolean error;
//    @SerializedName("message")
//    @Expose
//    private String message;
    @SerializedName("data")
    @Expose
    private List<MatchModel> data = null;
    @SerializedName("server_date")
    @Expose
    private Integer serverDate;
    private final static long serialVersionUID = 7636579687198640787L;

//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(Integer code) {
//        this.code = code;
//    }

//    public Boolean getError() {
//        return error;
//    }
//
//    public void setError(Boolean error) {
//        this.error = error;
//    }

//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }

    public List<MatchModel> getData() {
        return data;
    }

    public void setData(List<MatchModel> data) {
        this.data = data;
    }

    public Integer getServerDate() {
        return serverDate;
    }

    public void setServerDate(Integer serverDate) {
        this.serverDate = serverDate;
    }

}
