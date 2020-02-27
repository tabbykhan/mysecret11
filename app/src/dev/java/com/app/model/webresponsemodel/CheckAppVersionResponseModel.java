package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.AppVersionModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CheckAppVersionResponseModel extends AppBaseResponseModel {

    AppVersionModel data;

    public AppVersionModel getData() {
        return data;
    }

}
