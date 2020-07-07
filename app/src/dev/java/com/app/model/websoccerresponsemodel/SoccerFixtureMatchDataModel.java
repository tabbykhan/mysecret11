package com.app.model.websoccerresponsemodel;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseModel;
import com.app.ui.MyApplication;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.concurrent.TimeUnit;

public class SoccerFixtureMatchDataModel extends AppBaseModel {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("match_id")
    @Expose
    private String matchId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("match_date")
    @Expose
    private String matchDate;
    @SerializedName("close_date")
    @Expose
    private String closeDate;
    @SerializedName("match_progress")
    @Expose
    private String matchProgress;
    @SerializedName("server_date")
    @Expose
    private Integer serverDate;
    @SerializedName("match_limit")
    @Expose
    private String matchLimit;
    @SerializedName("contest_count")
    @Expose
    private String contestCount;
    @SerializedName("playing_squad_updated")
    @Expose
    private String playingSquadUpdated;
    @SerializedName("series")
    @Expose
    private Series series;
    @SerializedName("gametype")
    @Expose
    private Gametype gametype;
    @SerializedName("team1")
    @Expose
    private Team1 team1;
    @SerializedName("team2")
    @Expose
    private Team2 team2;
    private final static long serialVersionUID = 9023268213918826455L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public String getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(String closeDate) {
        this.closeDate = closeDate;
    }

    public String getMatchProgress() {
        return matchProgress;
    }

    public void setMatchProgress(String matchProgress) {
        this.matchProgress = matchProgress;
    }

    public Integer getServerDate() {
        return serverDate;
    }

    public void setServerDate(Integer serverDate) {
        this.serverDate = serverDate;
    }

    public String getMatchLimit() {
        return matchLimit;
    }

    public void setMatchLimit(String matchLimit) {
        this.matchLimit = matchLimit;
    }

    public String getContestCount() {
        return contestCount;
    }

    public void setContestCount(String contestCount) {
        this.contestCount = contestCount;
    }

    public String getPlayingSquadUpdated() {
        return playingSquadUpdated;
    }

    public void setPlayingSquadUpdated(String playingSquadUpdated) {
        this.playingSquadUpdated = playingSquadUpdated;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Gametype getGametype() {
        return gametype;
    }

    public void setGametype(Gametype gametype) {
        this.gametype = gametype;
    }

    public Team1 getTeam1() {
        return team1;
    }

    public void setTeam1(Team1 team1) {
        this.team1 = team1;
    }

    public Team2 getTeam2() {
        return team2;
    }

    public void setTeam2(Team2 team2) {
        this.team2 = team2;
    }



    public long getMatchRemainTime() {
        return Long.parseLong(getCloseDate()) - MyApplication.getInstance().getServerDate();
    }

    public String getRemainTimeText() {
        if (isFixtureMatch()) {
            long matchRemainTime = getMatchRemainTime();
            if (matchRemainTime < 0) {
                return "";
            }
            long days = TimeUnit.SECONDS.toDays(matchRemainTime);
            StringBuilder builder = new StringBuilder();
            if (days > 0) {
                builder.append(days);
                builder.append((days > 1) ? " days" : " day");
                builder.append(" ");
            }
            builder.append(getValidTimeText(matchRemainTime - TimeUnit.DAYS.toSeconds(days)));
            return builder.toString();
        } else if (isLiveMatch()) {
            if (isInReviewMatch()) {
                return "In Review";
            } else {
                return "In Progress";
            }

        } else {
            if (isAbondentMatch()) {
                return "Abandoned";
            } else {
                return "Completed";
            }

        }
    }

    public boolean isMatchTimeExpire(){
        if (isFixtureMatch()) {
            long matchRemainTime = getMatchRemainTime();
            if (matchRemainTime <= 0) {
                return true;
            }
            return false;
        }
        return true;
    }

    public int getTimerColor(){

        if ((ConstantsFlavor.type == ConstantsFlavor.Type.MySecreate)){
            if (isFixtureMatch()) {
                return R.color.colorWhite;
            } else if (isLiveMatch()) {
                return R.color.colorWhite;
            } else if (isAbondentMatch()) {
                return R.color.colorWhite;
            }  else {
                return R.color.colorActivateGreen;
            }
        }else {
            if (isFixtureMatch()) {

                return R.color.colorRed;
            } else if (isLiveMatch()) {
                return R.color.colorRed;
            } else if (isAbondentMatch()) {
                return R.color.colorRed;
            }  else {
                return R.color.colorActivateGreen;
            }

        }



    }

    public int getTimerColor2(){
        if (isFixtureMatch()) {
            return R.color.colorRed;
        } else if (isLiveMatch()) {
            return R.color.colorOrange;
        } else if (isAbondentMatch()) {
            return R.color.colorRed;
        }  else {
            return R.color.colorActivateGreen;
        }
    }


    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SoccerFixtureMatchDataModel)) return false;
        return getMatchId().equals(((SoccerFixtureMatchDataModel) obj).getMatchId());
    }

    public boolean isFixtureMatch() {
        return getMatchProgress().equals("F");
    }

    public boolean isLiveMatch() {
        return getMatchProgress().equals("L") || getMatchProgress().equals("IR");
    }

    public boolean isPastMatch() {
        return getMatchProgress().equals("R") || getMatchProgress().equals("AB");
    }

    public boolean isAbondentMatch() {
        return getMatchProgress().equals("AB");
    }

    public boolean isCompletedMatch() {
        return getMatchProgress().equals("R");
    }

    public boolean isInReviewMatch() {
        return getMatchProgress().equals("IR");
    }

}
