package com.app.model;

import com.ConstantsFlavor;
import com.R;
import com.app.appbase.AppBaseModel;
import com.app.ui.MyApplication;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MatchModel extends AppBaseModel {

    long id;
    String match_id;
    String name;
    String match_progress;
    long close_date;
    long match_date;
    long match_limit;
    long contest_count;
    String playing_squad_updated;
    SeriesModel series;
    GameTypeModel gametype;
    TeamModel team1;
    TeamModel team2;
    List<PlayerModel> batsmans;
    List<PlayerModel> bowlers;
    List<PlayerModel> wicketkeapers;
    List<PlayerModel> allrounders;
    TeamSettingModel team_settings;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMatch_id() {
        return getValidString(match_id);
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getName() {
        return getValidString(name);
    }

    public String getShortName() {
        if (getTeam1() != null && getTeam2() != null
                && isValidString(getTeam1().getSort_name()) && isValidString(getTeam2().getSort_name())) {
            return getTeam1().getSort_name() + " vs " + getTeam2().getSort_name();
        }
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMatch_progress() {
        return getValidString(match_progress);
    }

    public void setMatch_progress(String match_progress) {
        this.match_progress = match_progress;
    }

    public long getMatch_date() {
        return match_date;
    }

    public void setMatch_date(long match_date) {
        this.match_date = match_date;
    }

    public long getClose_date() {
        return close_date;
    }

    public void setClose_date(long close_date) {
        this.close_date = close_date;
    }

    public long getMatch_limit() {
        return match_limit;
    }

    public void setMatch_limit(long match_limit) {
        this.match_limit = match_limit;
    }

    public long getContest_count() {
        return contest_count;
    }

    public void setContest_count(long contest_count) {
        this.contest_count = contest_count;
    }

    public boolean isInPlayingSquadUpdated() {
        return getPlaying_squad_updated().equals("Y");
    }

    public String getMatchDateText() {
        return getFormatedDateString(DATE_DDMMYYYY, getMatch_date());
    }

    public String getPlaying_squad_updated() {
        return getValidString(playing_squad_updated);
    }

    public void setPlaying_squad_updated(String playing_squad_updated) {
        this.playing_squad_updated = playing_squad_updated;
    }


    public SeriesModel getSeries() {
        return series;
    }

    public void setSeries(SeriesModel series) {
        this.series = series;
    }

    public GameTypeModel getGametype() {
        return gametype;
    }

    public void setGametype(GameTypeModel gametype) {
        this.gametype = gametype;
    }

    public TeamModel getTeam1() {
        return team1;
    }

    public void setTeam1(TeamModel team1) {
        this.team1 = team1;
    }

    public TeamModel getTeam2() {
        return team2;
    }

    public void setTeam2(TeamModel team2) {
        this.team2 = team2;
    }

    public List<PlayerModel> getBatsmans() {
        return batsmans;
    }

    public void setBatsmans(List<PlayerModel> batsmans) {
        this.batsmans = batsmans;
    }

    public List<PlayerModel> getBowlers() {
        return bowlers;
    }

    public void setBowlers(List<PlayerModel> bowlers) {
        this.bowlers = bowlers;
    }

    public List<PlayerModel> getWicketkeapers() {
        return wicketkeapers;
    }

    public void setWicketkeapers(List<PlayerModel> wicketkeapers) {
        this.wicketkeapers = wicketkeapers;
    }

    public List<PlayerModel> getAllrounders() {
        return allrounders;
    }

    public void setAllrounders(List<PlayerModel> allrounders) {
        this.allrounders = allrounders;
    }

    public TeamSettingModel getTeam_settings() {
        return team_settings;
    }

    public void setTeam_settings(TeamSettingModel team_settings) {
        this.team_settings = team_settings;
    }

    public long getMatchRemainTime() {
        return getClose_date() - MyApplication.getInstance().getServerDate();
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
        if (!(obj instanceof MatchModel)) return false;
        return getMatch_id().equals(((MatchModel) obj).getMatch_id());
    }

    public boolean isFixtureMatch() {
        return getMatch_progress().equals("F");
    }

    public boolean isLiveMatch() {
        return getMatch_progress().equals("L") || getMatch_progress().equals("IR");
    }

    public boolean isPastMatch() {
        return getMatch_progress().equals("R") || getMatch_progress().equals("AB");
    }

    public boolean isAbondentMatch() {
        return getMatch_progress().equals("AB");
    }

    public boolean isCompletedMatch() {
        return getMatch_progress().equals("R");
    }

    public boolean isInReviewMatch() {
        return getMatch_progress().equals("IR");
    }

//soccer data model

    List<PlayerModel>  Goalkeepers;
    List<PlayerModel>  Defenders;
    List<PlayerModel>  Midfielders;
    List<PlayerModel>  Forwards;
    public List<PlayerModel> getGoalkeepers() {
        return Goalkeepers ;
    }

    public void setGoalkeepers(List<PlayerModel> goalkeepers) {
        this.Goalkeepers = goalkeepers;
    }

    public List<PlayerModel> getDefenders() {
        return Defenders;
    }

    public void setDefenders(List<PlayerModel> defenders) {
        this.Defenders = defenders;
    }

    public List<PlayerModel> getMidfielders() {
        return Midfielders;
    }

    public void setMidfielders(List<PlayerModel> midfielders) {
        this.Midfielders = midfielders;
    }

    public List<PlayerModel> getForwards() {
        return Forwards;
    }

    public void setForwards(List<PlayerModel> forwards) {
        this.Forwards = forwards;
    }

}
