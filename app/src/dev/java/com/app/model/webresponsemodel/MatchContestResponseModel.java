package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.ContestCategoryModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class MatchContestResponseModel extends AppBaseResponseModel {

    List<ContestCategoryModel> data;
    List<ContestCategoryModel> practice;
    List<ContestCategoryModel> beat_the_expert;
    DetailBean detail;

    public List<ContestCategoryModel> getData() {
        return data;
    }

    public List<ContestCategoryModel> getPractice() {
        return practice;
    }

    public List<ContestCategoryModel> getBeatTheExpert() {
        return beat_the_expert;
    }

    public DetailBean getDetail() {
        return detail;
    }

    public static class DetailBean extends AppBaseModel {
        long total_teams;
        long total_joined_contest;

        public long getTotal_teams() {
            return total_teams;
        }

        public void setTotal_teams(long total_teams) {
            this.total_teams = total_teams;
        }

        public long getTotal_joined_contest() {
            return total_joined_contest;
        }

        public void setTotal_joined_contest(long total_joined_contest) {
            this.total_joined_contest = total_joined_contest;
        }
    }
}
