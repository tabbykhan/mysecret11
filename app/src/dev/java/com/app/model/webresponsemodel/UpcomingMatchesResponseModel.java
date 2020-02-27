package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.MatchModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class UpcomingMatchesResponseModel extends AppBaseResponseModel {

    List<MatchModel> data;
    long server_date;

    public List<MatchModel> getData() {
        return data;
    }

    public long getServer_date() {
        return server_date;
    }
}
