package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.PageContentModel;

public class PageContentResponseModel extends AppBaseResponseModel {

    PageContentModel data;

    public PageContentModel getData() {
        return data;
    }

    public void setData(PageContentModel data) {
        this.data = data;
    }
}
