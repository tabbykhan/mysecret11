package com.app.model;

import com.app.appbase.AppBaseModel;

public class SettingsModel extends AppBaseModel {
    float WITHDRAW_AMOUNT_MIN;
    float WITHDRAW_AMOUNT_MAX;
    float CASH_BONUS_PERCENTAGES;
    String WINNING_BREAKUP_MESSAGE;
    String JOIN_CONTEST_MESSAGE;
    String PROFILE_UPDATE_MESSAGE;

    public float getWITHDRAW_AMOUNT_MAX() {
        return WITHDRAW_AMOUNT_MAX;
    }

    public String getWITHDRAW_AMOUNT_MAX_Text() {
        String s = getValidDecimalFormat(getWITHDRAW_AMOUNT_MAX());
        return s.replaceAll("\\.00", "");
    }


    public float getWITHDRAW_AMOUNT_MIN() {
        return WITHDRAW_AMOUNT_MIN;
    }

    public String getWITHDRAW_AMOUNT_MIN_Text() {
        String s = getValidDecimalFormat(getWITHDRAW_AMOUNT_MIN());
        return s.replaceAll("\\.00", "");
    }

    public float getCASH_BONUS_PERCENTAGES() {
        return CASH_BONUS_PERCENTAGES;
    }

    public String getCASH_BONUS_PERCENTAGES_Text() {
        String s = getValidDecimalFormat(getCASH_BONUS_PERCENTAGES());
        return s.replaceAll("\\.00", "");
    }

    public String getWINNING_BREAKUP_MESSAGE() {
        return getValidString(WINNING_BREAKUP_MESSAGE);
    }

    public void setWINNING_BREAKUP_MESSAGE(String WINNING_BREAKUP_MESSAGE) {
        this.WINNING_BREAKUP_MESSAGE = WINNING_BREAKUP_MESSAGE;
    }

    public String getJOIN_CONTEST_MESSAGE() {
        return getValidString(JOIN_CONTEST_MESSAGE);
    }

    public void setJOIN_CONTEST_MESSAGE(String JOIN_CONTEST_MESSAGE) {
        this.JOIN_CONTEST_MESSAGE = JOIN_CONTEST_MESSAGE;
    }

    public String getPROFILE_UPDATE_MESSAGE() {
        return getValidString(PROFILE_UPDATE_MESSAGE);
    }

    public void setPROFILE_UPDATE_MESSAGE(String PROFILE_UPDATE_MESSAGE) {
        this.PROFILE_UPDATE_MESSAGE = PROFILE_UPDATE_MESSAGE;
    }
}
