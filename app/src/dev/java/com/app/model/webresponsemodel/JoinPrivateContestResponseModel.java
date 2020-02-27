package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseResponseModel;
import com.app.model.ContestCategoryModel;
import com.app.model.ContestWinnerBreakUpModel;
import com.app.model.MatchModel;

import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class JoinPrivateContestResponseModel extends AppBaseResponseModel {


    private List<ContestCategoryModel> data;
    private MatchModel match_detail;
    private ContestWinnerBreakUpModel winner_breakup;
    private long server_date;

    public List<ContestCategoryModel> getData() {
        return data;
    }


    public MatchModel getMatch_detail() {
        return match_detail;
    }

    public void setMatch_detail(MatchModel match_detail) {
        this.match_detail = match_detail;
    }


    public ContestWinnerBreakUpModel getWinner_breakup() {
        return winner_breakup;
    }

    public void setWinner_breakup(ContestWinnerBreakUpModel winner_breakup) {
        this.winner_breakup = winner_breakup;
    }

    public long getServer_date() {
        return server_date;
    }

    public void setServer_date(long server_date) {
        this.server_date = server_date;
    }

    
}
