package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.ContestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ContestPdfResponseModel extends AppBaseResponseModel {

    ContestModel data;

    public ContestModel getData() {
        return data;
    }
}
