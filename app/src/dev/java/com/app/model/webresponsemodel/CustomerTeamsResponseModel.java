package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.CustomerTeamModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CustomerTeamsResponseModel extends AppBaseResponseModel {

    List<CustomerTeamModel> data;

    public List<CustomerTeamModel> getData() {
        return data;
    }
}
