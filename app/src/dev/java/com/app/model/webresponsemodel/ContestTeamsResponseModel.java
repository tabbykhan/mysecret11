package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.ContestTeamModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ContestTeamsResponseModel extends AppBaseResponseModel {

    List<ContestTeamModel> data;
    ContestTeamModel admin_data;
    long total_teams;

    public List<ContestTeamModel> getData() {
        return data;
    }

    public long getTotal_teams() {
        return total_teams;
    }


    public ContestTeamModel getAdmin_data() {
        return admin_data;
    }

    public void setAdmin_data(ContestTeamModel admin_data) {
        this.admin_data = admin_data;
    }
}
