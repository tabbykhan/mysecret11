package com.app.model;

import com.app.appbase.AppBaseModel;

import java.util.List;

public class TeamModel extends AppBaseModel {

    long id;
    String name;
    String sort_name;
    String image;
    List<PlayerModel> players;

    String team_run;
    String team_wicket;
    String team_overs;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return getValidString(name);
    }

    public String getModifiedName(){
        String name = getName();
        if(name.length()>5){
            return name.substring(0,4);
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSort_name() {
        return getValidString(sort_name);
    }

    public void setSort_name(String sort_name) {
        this.sort_name = sort_name;
    }

    public String getName(int tag) {
        if (tag == 0) {
            return getName();
        }
        if (isValidString(getSort_name())) {
            return getSort_name();
        }
        return getModifiedName();
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<PlayerModel> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerModel> players) {
        this.players = players;
    }

    public String getTeam_run() {
        return (team_run == null || team_run.trim().length() == 0) ? "0" : getValidString(team_run);
    }


    public String getTeam_wicket() {
        return (team_wicket == null || team_wicket.trim().length() == 0) ? "0" : getValidString(team_wicket);
    }

    public String getTeam_overs() {
        return getValidString(team_overs);
    }

    public void setTeam_overs(String team_overs) {
        this.team_overs = team_overs;
    }

    public boolean isScoreAvailable() {
        return !getTeam_run().equals("0") || !getTeam_wicket().equals("0");
    }

    public String getScore() {
        StringBuilder builder=new StringBuilder();
        builder.append(getTeam_run()+"/"+getTeam_wicket());
        if(isValidString(getTeam_overs())){
            builder.append(" (").append(getTeam_overs()).append(")");
        }
        return builder.toString();
    }

    public String getKabaddiScore() {
        return String.valueOf(getTeam_run() );
    }

    public String getSoccerScore() {
        return String.valueOf(getTeam_run() );
    }


}
