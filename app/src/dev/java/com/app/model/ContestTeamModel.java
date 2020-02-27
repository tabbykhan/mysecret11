package com.app.model;

import com.app.appbase.AppBaseModel;

public class ContestTeamModel extends AppBaseModel {

    long customer_id;
    String firstname;
    String lastname;
    String customer_team_name;
    String team_name;
    long team_id;
    String image;
    long old_rank;
    long new_rank;
    float user_entry_fees;
    float total_points;
    float win_amount;
    float refund_amount;


    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirstname() {
        return getValidString(firstname);
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return getValidString(lastname);
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCustomer_teamname() {
        return getValidString(customer_team_name);
    }

    public String getTeamname() {
        return getValidString(team_name);
    }

    public long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(long team_id) {
        this.team_id = team_id;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getOld_rank() {
        return old_rank;
    }

    public void setOld_rank(long old_rank) {
        this.old_rank = old_rank;
    }

    public long getNew_rank() {
        return new_rank;
    }

    public String getNewRankText() {
        return getNew_rank() == 0 ? "-" : "#" + getNew_rank();
    }

    public void setNew_rank(long new_rank) {
        this.new_rank = new_rank;
    }

    public boolean isNewRankUp() {
        return old_rank > new_rank;
    }

    public boolean isNewRankDown() {
        return old_rank < new_rank;
    }

    public float getTotal_points() {
        return total_points;
    }

    public float getUser_entry_fees() {
        return user_entry_fees;
    }

    public void setUser_entry_fees(float user_entry_fees) {
        this.user_entry_fees = user_entry_fees;
    }


    public String getUserEntryFeesText() {
        String s = getValidDecimalFormat(getUser_entry_fees());
        return s.replaceAll("\\.00", "");
    }

    public String getTotalPointstext() {
        String s = getValidDecimalFormat(getTotal_points());
        return s.replaceAll("\\.00", "");
    }

    public float getWin_amount() {
        return win_amount;
    }

    public float getRefund_amount() {
        return refund_amount;
    }

    public String getWinAmountText() {
        String s = getValidDecimalFormat(getWin_amount());
        return s.replaceAll("\\.00", "");
    }

    public String getRefundAmountText() {
        String s = getValidDecimalFormat(getRefund_amount());
        return s.replaceAll("\\.00", "");
    }

    public String getFullTeamName() {
        return getCustomer_teamname() + "(T" + getTeamname() + ")";
    }

    public boolean isMyTeam(long customerId) {
        return getCustomer_id()==customerId;
    }


    public String getFullName() {
        if (isValidString(getLastname())) {
            return getFirstname() + " " + getLastname();
        }
        return getFirstname();
    }
}
