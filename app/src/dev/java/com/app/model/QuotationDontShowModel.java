package com.app.model;

import com.app.appbase.AppBaseModel;

import java.util.ArrayList;
import java.util.List;

public class QuotationDontShowModel extends AppBaseModel {


    List<GameModel> data;

    public List<GameModel> getData() {
        return data;
    }

    public void setData(List<GameModel> data) {
        this.data = data;
    }

    public void addGame(GameModel gameModel){
        if(data==null){
            data=new ArrayList<>();
        }

        data.add(gameModel);
    }


    public boolean needShowQuotation(GameModel gameModel){
        boolean alreadyShow=gameModel.isQuotationShowing();
        if(alreadyShow)return false;
        if(data==null || data.isEmpty())return true;
        for (GameModel datum : data) {

            if(datum.getId()==gameModel.getId()){
                return false;
            }
        }
        return true;

    }
    public void addGameRemove(){
        if(data !=null){
            data.clear();
        }


    }
}
