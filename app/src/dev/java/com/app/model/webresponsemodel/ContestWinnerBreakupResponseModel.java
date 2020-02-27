package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.ContestWinnerBreakUpModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class ContestWinnerBreakupResponseModel extends AppBaseResponseModel {

    DataBean data;

    public ContestWinnerBreakUpModel getData() {
        return data == null ? null : data.getWinner_breakup();
    }

    public String getBottomMessage(){
        return data == null ? "" : data.getWinner_breakup_message();
    }

    public static class DataBean extends AppBaseModel {
        ContestWinnerBreakUpModel winner_breakup;
        String winner_breakup_message;

        public ContestWinnerBreakUpModel getWinner_breakup() {
            return winner_breakup;
        }

        public String getWinner_breakup_message() {
            return getValidString(winner_breakup_message);
        }

        public void setWinner_breakup_message(String winner_breakup_message) {
            this.winner_breakup_message = winner_breakup_message;
        }
    }
}
