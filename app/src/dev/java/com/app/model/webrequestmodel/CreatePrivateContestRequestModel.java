package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CreatePrivateContestRequestModel extends AppBaseRequestModel {

    public String contest_size;
    public String prize_pool;
    public String winning_breakup_id;
    public String match_id;
    public String match_unique_id;
    public String is_multiple;
    public String team_id;
    public String pre_join;

}
