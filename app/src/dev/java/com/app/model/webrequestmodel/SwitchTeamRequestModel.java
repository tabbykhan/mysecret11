package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class SwitchTeamRequestModel extends AppBaseRequestModel {

    public String match_unique_id;
    public long match_contest_id;
    public long customer_team_id_old;
    public long customer_team_id_new;

}
