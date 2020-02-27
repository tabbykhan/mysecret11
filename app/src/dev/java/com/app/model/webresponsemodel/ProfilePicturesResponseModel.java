package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ProfilePicturesResponseModel extends AppBaseResponseModel {

    List<String> data;

    public List<String> getData() {
        return data;
    }
}
