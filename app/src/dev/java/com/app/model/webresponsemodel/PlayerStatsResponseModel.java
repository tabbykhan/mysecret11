package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.PlayerStatsModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class PlayerStatsResponseModel extends AppBaseResponseModel {

    List<PlayerStatsModel> data;

    public List<PlayerStatsModel> getData() {
        return data;
    }
}
