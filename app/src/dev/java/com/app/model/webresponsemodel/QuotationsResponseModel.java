package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.QuotationModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class QuotationsResponseModel extends AppBaseResponseModel {

    List<QuotationModel> data;

    public List<QuotationModel> getData() {
        return data;
    }

    public QuotationModel getQuotation(){
        if(data==null || data.size()==0)
            return null;

        return data.get(0);
    }
}
