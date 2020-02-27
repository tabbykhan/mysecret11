package com.app.model;

import com.app.appbase.AppBaseModel;

public class TeamSettingModel extends AppBaseModel {
    int MAX_PLAYERS;
    float MAX_CREDITS;
    int MAX_PLAYERS_PER_TEAM;
    int MIN_WICKETKEEPER;
    int MAX_WICKETKEEPER;
    int MIN_BATSMAN;
    int MAX_BATSMAN;
    int MIN_ALLROUNDER;
    int MAX_ALLROUNDER;
    int MIN_BOWLER;
    int MAX_BOWLER;


    public int getMinPlayer(int type) {
        if (type == 1) {
            return getMIN_WICKETKEEPER();
        } else if (type == 2) {
            return getMIN_BATSMAN();
        } else if (type == 3) {
            return getMIN_ALLROUNDER();
        } else if (type == 4) {
            return getMIN_BOWLER();
        }
        return 0;
    }

    public int getTotalMinPlayer(){
        return getMIN_WICKETKEEPER()+getMIN_BATSMAN()+getMIN_ALLROUNDER()+getMIN_BOWLER();
    }


    public int getMAX_PLAYERS() {
        return MAX_PLAYERS;
    }

    public void setMAX_PLAYERS(int MAX_PLAYERS) {
        this.MAX_PLAYERS = MAX_PLAYERS;
    }

    public float getMAX_CREDITS() {
        return MAX_CREDITS;
    }

    public String getMaxCreditsText() {
        String s = getValidDecimalFormat(getMAX_CREDITS());
        return s.replaceAll("\\.00", "");
    }

    public void setMAX_CREDITS(float MAX_CREDITS) {
        this.MAX_CREDITS = MAX_CREDITS;
    }

    public int getMAX_PLAYERS_PER_TEAM() {
        return MAX_PLAYERS_PER_TEAM;
    }

    public void setMAX_PLAYERS_PER_TEAM(int MAX_PLAYERS_PER_TEAM) {
        this.MAX_PLAYERS_PER_TEAM = MAX_PLAYERS_PER_TEAM;
    }

    public int getMIN_WICKETKEEPER() {
        return MIN_WICKETKEEPER;
    }

    public void setMIN_WICKETKEEPER(int MIN_WICKETKEEPER) {
        this.MIN_WICKETKEEPER = MIN_WICKETKEEPER;
    }

    public int getMAX_WICKETKEEPER() {
        return MAX_WICKETKEEPER;
    }

    public void setMAX_WICKETKEEPER(int MAX_WICKETKEEPER) {
        this.MAX_WICKETKEEPER = MAX_WICKETKEEPER;
    }

    public int getMIN_BATSMAN() {
        return MIN_BATSMAN;
    }

    public void setMIN_BATSMAN(int MIN_BATSMAN) {
        this.MIN_BATSMAN = MIN_BATSMAN;
    }

    public int getMAX_BATSMAN() {
        return MAX_BATSMAN;
    }

    public void setMAX_BATSMAN(int MAX_BATSMAN) {
        this.MAX_BATSMAN = MAX_BATSMAN;
    }

    public int getMIN_ALLROUNDER() {
        return MIN_ALLROUNDER;
    }

    public void setMIN_ALLROUNDER(int MIN_ALLROUNDER) {
        this.MIN_ALLROUNDER = MIN_ALLROUNDER;
    }

    public int getMAX_ALLROUNDER() {
        return MAX_ALLROUNDER;
    }

    public void setMAX_ALLROUNDER(int MAX_ALLROUNDER) {
        this.MAX_ALLROUNDER = MAX_ALLROUNDER;
    }

    public int getMIN_BOWLER() {
        return MIN_BOWLER;
    }

    public void setMIN_BOWLER(int MIN_BOWLER) {
        this.MIN_BOWLER = MIN_BOWLER;
    }

    public int getMAX_BOWLER() {
        return MAX_BOWLER;
    }

    public void setMAX_BOWLER(int MAX_BOWLER) {
        this.MAX_BOWLER = MAX_BOWLER;
    }
}
