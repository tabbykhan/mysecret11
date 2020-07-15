package com.app.model;

import com.app.appbase.AppBaseModel;

public class ScoreModel extends AppBaseModel {

    private TeamModel team1;
    private TeamModel team2;
    private String score_board_notes;

    public TeamModel getTeam1() {
        return team1;
    }

    public TeamModel getTeam2() {
        return team2;
    }

    public String getScore_board_notes() {
        return getValidString(score_board_notes);
    }

    public void setScore_board_notes(String score_board_notes) {
        this.score_board_notes = score_board_notes;
    }

    public boolean isScoreAvailable() {
        return (getTeam1() != null && getTeam1().isScoreAvailable()) || (getTeam2() != null && getTeam2().isScoreAvailable());
    }
    public boolean isSoccerScoreAvailable() {
        return (getTeam1() != null && getTeam1().isSoccerScoreAvailable()) || (getTeam2() != null && getTeam2().isSoccerScoreAvailable());
    }
}
