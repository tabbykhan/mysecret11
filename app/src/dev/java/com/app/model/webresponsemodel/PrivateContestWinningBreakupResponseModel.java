package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.ContestCategoryModel;
import com.app.model.ContestWinnerBreakUpModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class PrivateContestWinningBreakupResponseModel extends AppBaseResponseModel {

    DataBean data;

    public DataBean getData() {
        return data;
    }

    public class DataBean extends AppBaseModel {
        List<WinnerBreakUpData> winning_breakups;
        ContestCategoryModel private_contest_category;


        public List<WinnerBreakUpData> getWinning_breakups() {
            return winning_breakups;
        }

        public void setWinning_breakups(List<WinnerBreakUpData> winning_breakups) {
            this.winning_breakups = winning_breakups;
        }

        public ContestCategoryModel getPrivate_contest_category() {
            return private_contest_category;
        }

        public void setPrivate_contest_category(ContestCategoryModel private_contest_category) {
            this.private_contest_category = private_contest_category;
        }
    }

    public static class WinnerBreakUpData extends AppBaseModel{
        String id;
        String total_winners;
        ContestWinnerBreakUpModel contest_json;

        public String getId() {
            return getValidString(id);
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTotal_winners() {
            return getValidString(total_winners);
        }

        public void setTotal_winners(String total_winners) {
            this.total_winners = total_winners;
        }

        public ContestWinnerBreakUpModel getContest_json() {
            return contest_json;
        }

        public void setContest_json(ContestWinnerBreakUpModel contest_json) {
            this.contest_json = contest_json;
        }

        public int size() {
            return contest_json == null ? 0 : contest_json.size();
        }
    }
}
