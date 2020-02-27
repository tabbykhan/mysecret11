package com.app.model;

import androidx.annotation.Nullable;

import com.app.appbase.AppBaseModel;

import java.util.List;

public class PlayerStatsModel extends AppBaseModel {

    String name;
    String image;
    String match_unique_id;
    String match_name;
    String position;
    long match_date;
    String player_unique_id;
    String dream_team_player;
    float points;
    long match_team_count;
    long player_team_count;
    float selected_by;
    String is_my_player;
    List<PlayerEventModel> player_events;
    TeamModel team_data;


    public TeamModel getTeam_data() {
        return team_data;
    }

    public void setTeam_data(TeamModel team_data) {
        this.team_data = team_data;
    }

    public String getName() {
        return getValidString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMatch_unique_id() {
        return getValidString(match_unique_id);
    }

    public void setMatch_unique_id(String match_unique_id) {
        this.match_unique_id = match_unique_id;
    }

    public String getMatch_name() {
        return getValidString(match_name);
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }

    public String getPosition() {
        return getValidString(position);
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public long getMatch_date() {
        return match_date;
    }

    public void setMatch_date(long match_date) {
        this.match_date = match_date;
    }

    public String getMatchDateText() {
        return getFormatedDateString(DATE_DDMMYYYY, getMatch_date());
    }

    public String getPlayer_unique_id() {
        return getValidString(player_unique_id);
    }

    public void setPlayer_unique_id(String player_unique_id) {
        this.player_unique_id = player_unique_id;
    }

    public String getDream_team_player() {
        return getValidString(dream_team_player);
    }

    public void setDream_team_player(String dream_team_player) {
        this.dream_team_player = dream_team_player;
    }

    public boolean isDreamTeamPlayer(){
        return  getDream_team_player().equals("Y");
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getPointsText() {
        String s = getValidDecimalFormat(getPoints());
        return s.replaceAll("\\.00", "");
    }

    public long getMatch_team_count() {
        return match_team_count;
    }

    public void setMatch_team_count(long match_team_count) {
        this.match_team_count = match_team_count;
    }

    public long getPlayer_team_count() {
        return player_team_count;
    }

    public void setPlayer_team_count(long player_team_count) {
        this.player_team_count = player_team_count;
    }

    public float getSelected_by() {
        return selected_by;
    }

    public void setSelected_by(float selected_by) {
        this.selected_by = selected_by;
    }

    public boolean isMyPlayer() {
        return getIs_my_player().equals("Y");
    }

    public String getIs_my_player() {
        return getValidString(is_my_player);
    }

    public void setIs_my_player(String is_my_player) {
        this.is_my_player = is_my_player;
    }


    public String getSelectedByText() {
        String s = getValidDecimalFormat(getSelected_by()) + " %";
        return s.replaceAll("\\.00", "");
    }

    public List<PlayerEventModel> getPlayer_events() {
        return player_events;
    }

    public void setPlayer_events(List<PlayerEventModel> player_events) {
        this.player_events = player_events;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PlayerStatsModel) {
            return getPlayer_unique_id().equals(((PlayerStatsModel) obj).getPlayer_unique_id());
        }
        return false;
    }

    public String getPositionShortName(){
        if (getPosition().equalsIgnoreCase("allrounder")) {
            return "All";
        } else if (getPosition().equalsIgnoreCase("batsman")) {
            return "BAT";
        } else if (getPosition().equalsIgnoreCase("bowler")) {
            return "BOWL";
        }  else if (getPosition().equalsIgnoreCase("wicketkeeper")) {
            return "WK";
        }  else if (getPosition().equalsIgnoreCase("raider")) {
            return "RID";
        }  else if (getPosition().equalsIgnoreCase("defender")) {
            return "DEF";
        }  else if (getPosition().equalsIgnoreCase("goalkeeper")) {
            return "GK";
        }  else if (getPosition().equalsIgnoreCase("midfielder")) {
            return "MID";
        }  else if (getPosition().equalsIgnoreCase("forward")) {
            return "ST";
        }else {
            return "";
        }
    }

    public String getTeamName(){
        if(team_data==null)return "";
        return team_data.getName(1);
    }
}
