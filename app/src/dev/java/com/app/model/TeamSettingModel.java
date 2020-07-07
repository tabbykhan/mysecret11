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

    int MIN_GOALKEEPER;
    int MAX_GOALKEEPER;

    int MIN_DEFENDER;
    int MAX_DEFENDER;

    int MIN_MIDFIELDER;
    int MAX_MIDFIELDER;

    int MIN_FORWARD;
    int MAX_FORWARD;


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

    public int getTotalMinPlayer() {
        return getMIN_WICKETKEEPER() + getMIN_BATSMAN() + getMIN_ALLROUNDER() + getMIN_BOWLER();
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

    public void setMAX_CREDITS(float MAX_CREDITS) {
        this.MAX_CREDITS = MAX_CREDITS;
    }

    public String getMaxCreditsText() {
        String s = getValidDecimalFormat(getMAX_CREDITS());
        return s.replaceAll("\\.00", "");
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


    // soccer data
    public int getMINGOALKEEPER() {
        return MIN_GOALKEEPER;
    }

    public void setMINGOALKEEPER(int mINGOALKEEPER) {
        this.MIN_GOALKEEPER = mINGOALKEEPER;
    }

    public int getMAXGOALKEEPER() {
        return MAX_GOALKEEPER;
    }

    public void setMAXGOALKEEPER(int mAXGOALKEEPER) {
        this.MAX_GOALKEEPER = mAXGOALKEEPER;
    }

    public int getMINDEFENDER() {
        return MIN_DEFENDER;
    }

    public void setMINDEFENDER(int mINDEFENDER) {
        this.MIN_DEFENDER = mINDEFENDER;
    }

    public int getMAXDEFENDER() {
        return MAX_DEFENDER;
    }

    public void setMAXDEFENDER(int mAXDEFENDER) {
        this.MAX_DEFENDER = mAXDEFENDER;
    }

    public int getMINMIDFIELDER() {
        return MIN_MIDFIELDER;
    }

    public void setMINMIDFIELDER(int mINMIDFIELDER) {
        this.MIN_MIDFIELDER = mINMIDFIELDER;
    }

    public int getMAXMIDFIELDER() {
        return MAX_MIDFIELDER;
    }

    public void setMAXMIDFIELDER(int mAXMIDFIELDER) {
        this.MAX_MIDFIELDER = mAXMIDFIELDER;
    }

    public int getMINFORWARD() {
        return MIN_FORWARD;
    }

    public void setMINFORWARD(int mINFORWARD) {
        this.MIN_FORWARD = mINFORWARD;
    }

    public int getMAXFORWARD() {
        return MAX_FORWARD;
    }

    public void setMAXFORWARD(int mAXFORWARD) {
        this.MAX_FORWARD = mAXFORWARD;
    }

}
