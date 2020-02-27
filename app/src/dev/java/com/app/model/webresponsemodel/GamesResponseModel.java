package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.GameModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class GamesResponseModel extends AppBaseResponseModel {

    List<GameModel> data;

    public List<GameModel> getData() {
        return data;
    }

}
