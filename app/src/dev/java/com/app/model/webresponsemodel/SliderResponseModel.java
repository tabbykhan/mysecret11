package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.SliderModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class SliderResponseModel extends AppBaseResponseModel {

    List<SliderModel> data;

    public List<SliderModel> getData() {
        return data;
    }
}
