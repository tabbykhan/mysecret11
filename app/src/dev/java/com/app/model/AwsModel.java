package com.app.model;

import com.app.appbase.AppBaseModel;

public class AwsModel extends AppBaseModel {

    String AWS_KEY;
    String AWS_SECRET;
    String AWS_REGION;
    String AWS_BUCKET;
    String PANCARD_IMAGE_PATH;
    String BANK_IMAGE_PATH;

    public String getAWS_KEY() {
        return getValidString(AWS_KEY);
    }

    public String getAWS_SECRET() {
        return getValidString(AWS_SECRET);
    }

    public String getAWS_REGION() {
        return getValidString(AWS_REGION);
    }

    public String getAWS_BUCKET() {
        return getValidString(AWS_BUCKET);
    }

    public String getPANCARD_IMAGE_PATH() {
        return getValidString(PANCARD_IMAGE_PATH);
    }

    public String getBANK_IMAGE_PATH() {
        return getValidString(BANK_IMAGE_PATH);
    }

    public String getFullBucketPath(String subBucket) {
        return getAWS_BUCKET() + "/" + subBucket;
    }
}
