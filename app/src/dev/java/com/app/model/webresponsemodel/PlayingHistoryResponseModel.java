package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.PlayingHistoryModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class PlayingHistoryResponseModel extends AppBaseResponseModel {

    PlayingHistoryModel data;

    public PlayingHistoryModel getData() {
        return data;
    }

}
