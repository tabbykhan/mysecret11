package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ShareCodeResponseModel extends AppBaseResponseModel {


    /**
     * data : eEtVz3qqYBO910_
     * image : http://139.162.28.58/snozer/admin/img/noimage.png
     * server_date : 1570968388
     */

    private String data;
    private String image;
    private int server_date;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServer_date() {
        return server_date;
    }

    public void setServer_date(int server_date) {
        this.server_date = server_date;
    }
}
