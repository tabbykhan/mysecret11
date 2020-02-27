package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.WalletModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CustomerJoinContestResponseModel extends AppBaseResponseModel {

    PreJoinedModel data;
    String match_contest_id;

    public String getMatch_contest_id() {
        return getValidString(match_contest_id);
    }

    public void setMatch_contest_id(String match_contest_id) {
        this.match_contest_id = match_contest_id;
    }

    public PreJoinedModel getData() {
        return data;
    }

    public static class PreJoinedModel extends AppBaseModel {
        WalletModel wallet;
        float used_bonus;
        float used_deposit;
        float used_winning;
        float need_pay;
        float entry_fees;
        float to_pay;
        List<Long> amount_suggest;

        public WalletModel getWallet() {
            return wallet;
        }

        public void setWallet(WalletModel wallet) {
            this.wallet = wallet;
        }

        public float getUsed_bonus() {
            return used_bonus;
        }

        public String getUsedBonusText(){
            return getValidDecimalFormat(getUsed_bonus());
        }

        public void setUsed_bonus(float used_bonus) {
            this.used_bonus = used_bonus;
        }

        public float getUsed_deposit() {
            return used_deposit;
        }

        public void setUsed_deposit(float used_deposit) {
            this.used_deposit = used_deposit;
        }

        public float getUsed_winning() {
            return used_winning;
        }

        public void setUsed_winning(float used_winning) {
            this.used_winning = used_winning;
        }

        public float getNeed_pay() {
            return need_pay;
        }

        public void setNeed_pay(float need_pay) {
            this.need_pay = need_pay;
        }

        public float getEntry_fees() {
            return entry_fees;
        }

        public String getEnteryFeestext(){
            return getValidDecimalFormat(getEntry_fees());
        }

        public void setEntry_fees(float entry_fees) {
            this.entry_fees = entry_fees;
        }

        public float getTo_pay() {
            return to_pay;
        }

        public String getToPayText(){
            return getValidDecimalFormat(getTo_pay());
        }

        public void setTo_pay(float to_pay) {
            this.to_pay = to_pay;
        }

        public List<Long> getAmount_suggest() {
            return amount_suggest;
        }

        public void setAmount_suggest(List<Long> amount_suggest) {
            this.amount_suggest = amount_suggest;
        }

        public String getRemainBalancetext() {
            WalletModel wallet = getWallet();
            if (wallet != null) {
                return getValidDecimalFormat(wallet.getDeposit_wallet() + wallet.getWinning_wallet());
            }
            return "0";
        }
    }
}
