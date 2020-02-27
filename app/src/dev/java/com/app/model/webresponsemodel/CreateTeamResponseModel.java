package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.CustomerTeamModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CreateTeamResponseModel extends AppBaseResponseModel {

    CustomerTeamModel data;

    public CustomerTeamModel getData() {
        return data;
    }
}
