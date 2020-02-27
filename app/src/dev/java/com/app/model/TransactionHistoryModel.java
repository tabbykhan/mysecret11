package com.app.model;

import com.app.appbase.AppBaseModel;

public class TransactionHistoryModel extends AppBaseModel {


    long id;
    long customer_id;
    String transaction_type;
    String transaction_id;
    String type;
    float amount;
    String description;
    long created;

    boolean isOpened = false;

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getTransaction_type() {
        return getValidString(transaction_type);
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_id() {
        return getValidString(transaction_id);
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getType() {
        return getValidString(type);
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public String getAmountText() {
        String s = getValidDecimalFormat(getAmount());
        return s.replaceAll("\\.00", "");
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return getValidString(description);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreated() {
        return created;
    }

    public String getCreatedText() {
        return getFormatedDateString(DATETIME_MMMDDYYYY_hh_mm_ss_a, getCreated());
    }

    public String getCreatedDateText() {
        return getFormatedDateString(DATE_DDMMYYYY, getCreated());
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isCredit() {
        return getTransaction_type().equals("CREDIT");
    }

    public boolean isWalletRecharge() {
        return getType().equals("CUSTOMER_WALLET_RECHARGE");
    }

    public boolean isWinContest() {
        return getType().equals("CUSTOMER_WIN_CONTEST");
    }

    public String getTypeText() {
        switch (getType()) {
            case "CUSTOMER_WALLET_RECHARGE":
                return "Deposit Cash";
            case "WALLET_RECHARGE_ADMIN":
                return "Deposit Cash (Admin)";
            case "WALLET_WITHDRAW_ADMIN":
                return "Withdraw (Admin)";
            case "CUSTOMER_WIN_CONTEST":
                return "Won A Contest";
            case "CUSTOMER_JOIN_CONTEST":
                return "Joined A Contest";
            case "CUSTOMER_RECEIVED_RCB":
                return "Cash Bonus Received";
            case "CUSTOMER_RECEIVED_REFCB":
                return "Cash Bonus Received";
            case "REGISTER_CASH_BONUS":
                return "Register Cash Bonus";
            case "CUSTOMER_REFUND_CONTEST":
                return "Refund Contest";
            case "CUSTOMER_REFUND_ABCONTEST":
                return "Refund Contest";

        }
        return "";
    }
}
