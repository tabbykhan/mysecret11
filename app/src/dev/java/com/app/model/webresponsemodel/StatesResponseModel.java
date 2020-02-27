package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.StateModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class StatesResponseModel extends AppBaseResponseModel {

    List<StateModel> data;

    public List<StateModel> getData() {
        return data;
    }
}
