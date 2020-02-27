package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.ScoreModel;

public class ScoreResponseModel extends AppBaseResponseModel {

    ScoreModel data;

    public ScoreModel getData() {
        return data;
    }

    public void setData(ScoreModel data) {
        this.data = data;
    }
}
