package com.app.model.webrequestmodel;

import com.app.appbase.AppBaseRequestModel;
import com.app.model.PlayerModel;
import com.medy.retrofitwrapper.ParamJSON;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manish Kumar
 * @since 17/10/18
 */

public class CreateTeamRequestModel extends AppBaseRequestModel {

    public String match_unique_id;
    @ParamJSON
    public List<PlayerModel> player_json = new ArrayList<>();
}
