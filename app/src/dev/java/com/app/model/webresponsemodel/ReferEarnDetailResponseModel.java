package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ReferEarnDetailResponseModel extends AppBaseResponseModel {

    DataBean data;

    public DataBean getData() {
        return data;
    }

    public class DataBean extends AppBaseModel {

        ReferEarnModel refer_data;
        List<UserModel> user_refer_data;

        public ReferEarnModel getRefer_data() {
            return refer_data;
        }

        public List<UserModel> getUser_refer_data() {
            return user_refer_data;
        }
    }
}


