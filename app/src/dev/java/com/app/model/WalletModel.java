package com.app.model;

import com.app.appbase.AppBaseModel;

public class WalletModel extends AppBaseModel {

    double winning_wallet;
    double bonus_wallet;
    double deposit_wallet;

    public double getWinning_wallet() {
        return winning_wallet;
    }

    public String getWinning_walletText() {
        String s = getValidDecimalFormat(getWinning_wallet());
        return s.replaceAll("\\.00", "");
    }


    public void setWinning_wallet(double winning_wallet) {
        this.winning_wallet = winning_wallet;
    }

    public double getBonus_wallet() {
        return bonus_wallet;
    }

    public String getBonus_walletText() {
        String s = getValidDecimalFormat(getBonus_wallet());
        return s.replaceAll("\\.00", "");
    }


    public void setBonus_wallet(double bonus_wallet) {
        this.bonus_wallet = bonus_wallet;
    }

    public double getDeposit_wallet() {
        return deposit_wallet;
    }

    public String getDeposit_walletText() {
        String s = getValidDecimalFormat(getDeposit_wallet());
        return s.replaceAll("\\.00", "");
    }

    public void setDeposit_wallet(double deposit_wallet) {
        this.deposit_wallet = deposit_wallet;
    }

    public String getTotalWalletBalanceText() {
        String s = getValidDecimalFormat(getWinning_wallet() + getDeposit_wallet() + getBonus_wallet());
        return s.replaceAll("\\.00", "");

    }
}
