package com.app.model;

import com.app.appbase.AppBaseModel;

public class ReferEarnModel extends AppBaseModel {

    String title;
    String subtitle;
    String image;
    String share_content;
    long team_count;
    float team_earn;
    float total_received_amount;


    public String getTitle() {
        return getValidString(title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return getValidString(subtitle);
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_content() {
        return getValidString(share_content);
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public long getTeam_count() {
        return team_count;
    }

    public void setTeam_count(long team_count) {
        this.team_count = team_count;
    }

    public float getTeam_earn() {
        return team_earn;
    }

    public String getTeamEarnText() {
        String validDecimalFormat = getValidDecimalFormat(getTeam_earn());
        return validDecimalFormat.replaceAll("\\.00", "");
    }

    public String getToBeEarnedText(){
        float v = getTeam_earn() - getTotal_received_amount();
        String validDecimalFormat = getValidDecimalFormat(v);
        return validDecimalFormat.replaceAll("\\.00", "");
    }

    public void setTeam_earn(float team_earn) {
        this.team_earn = team_earn;
    }

    public float getTotal_received_amount() {
        return total_received_amount;
    }

    public void setTotal_received_amount(float total_received_amount) {
        this.total_received_amount = total_received_amount;
    }

    public String getTotalReceivedAmountText() {
        String validDecimalFormat = getValidDecimalFormat(getTotal_received_amount());
        return validDecimalFormat.replaceAll("\\.00", "");
    }

}
