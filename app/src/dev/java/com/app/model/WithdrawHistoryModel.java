package com.app.model;

import com.app.appbase.AppBaseModel;

public class WithdrawHistoryModel extends AppBaseModel {


    long id;
    long customer_id;
    String transaction_id;
    String status;
    String reason;
    float amount;
    long created_at;
    long updated_at;

    boolean isOpened = false;


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

    public String getTransaction_id() {
        return getValidString(transaction_id);
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getStatus() {
        return getValidString(status);
    }

    public boolean isPending() {
        return getStatus().equals("P");
    }

    public boolean isCompleted() {
        return getStatus().equals("C");
    }

    public boolean isRejected() {
        return getStatus().equals("R");
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return getValidString(reason);
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public float getAmount() {
        return amount;
    }


    public void setAmount(float amount) {
        this.amount = amount;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        isOpened = opened;
    }

    public String getStatusText() {
        switch (getStatus()) {
            case "P":
                return "In Review";
            case "C":
                return "Completed";
            case "R":
                return "Rejected";
            default:
                return "";
        }
    }

    public String getAmountText() {
        String s = getValidDecimalFormat(getAmount());
        return s.replaceAll("\\.00", "");
    }

    public String getCreatedText() {
        return getFormatedDateString(DATETIME_MMMDDYYYY_hh_mm_ss_a, getCreated_at());
    }

    public String getCreatedDateText() {
        return getFormatedDateString(DATE_DDMMYYYY, getCreated_at());
    }

}
