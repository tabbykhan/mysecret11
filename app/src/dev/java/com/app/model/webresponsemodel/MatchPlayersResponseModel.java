package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.MatchModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class MatchPlayersResponseModel extends AppBaseResponseModel {

    MatchModel data;

    public MatchModel getData() {
        return data;
    }
}
