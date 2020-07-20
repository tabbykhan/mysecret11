package com.app.model;

import com.app.appbase.AppBaseModel;

import java.util.List;

public class CustomerTeamModel extends AppBaseModel {

    long customer_id;
    long id;
    String name;
    PlayerModel captain;
    PlayerModel vise_captain;
    PlayerModel mpp;


    TeamModel team1;
    TeamModel team2;

    List<PlayerModel> batsmans;
    List<PlayerModel> bowlers;
    List<PlayerModel> wicketkeapers;
    List<PlayerModel> allrounders;
    List<PlayerStatsModel> players_stats;
// soccer game player
    List<PlayerModel>  Goalkeepers;
    List<PlayerModel>  Defenders;
    List<PlayerModel>  Midfielders;
    List<PlayerModel>  Forwards;


    private long team1Players = -1;
    private long team2Players = -1;

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return getValidString(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerModel getCaptain() {
        return captain;
    }

    public void setCaptain(PlayerModel captain) {
        this.captain = captain;
    }

    public PlayerModel getVise_captain() {
        return vise_captain;
    }

    public void setVise_captain(PlayerModel vise_captain) {
        this.vise_captain = vise_captain;
    }

    public PlayerModel getTrump() {
        return mpp;
    }

    public void setTrump(PlayerModel mpp) {
        this.mpp = mpp;
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

    public List<PlayerStatsModel> getPlayers_stats() {
        return players_stats;
    }

    public void setPlayers_stats(List<PlayerStatsModel> players_stats) {
        this.players_stats = players_stats;
    }

    public long getTeam1Players() {
        return team1Players;
    }

    public void setTeam1Players(long team1Players) {
        this.team1Players = team1Players;
    }

    public long getTeam2Players() {
        return team2Players;
    }

    public void setTeam2Players(long team2Players) {
        this.team2Players = team2Players;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomerTeamModel)) return false;
        return getId() == ((CustomerTeamModel) obj).getId();
    }

    public boolean isMyTeam(long customerId) {
        return getCustomer_id()==customerId;
    }

    @Override
    public String toString() {
        return "TEAM " + getName();
    }


    public void updateTeamInfo() {
        if (team1Players == -1 || team2Players == -1) {
            team1Players = 0;
            team2Players = 0;
            if (getWicketkeapers() != null) {
                for (PlayerModel playerModel : getWicketkeapers()) {
                    if (isPlayerFromTeam1(playerModel)) {
                        team1Players++;
                    } else {
                        team2Players++;
                    }
                }

            }

            if (getBatsmans() != null) {
                for (PlayerModel playerModel : getBatsmans()) {
                    if (isPlayerFromTeam1(playerModel)) {
                        team1Players++;
                    } else {
                        team2Players++;
                    }
                }

            }


            if (getAllrounders() != null) {
                for (PlayerModel playerModel : getAllrounders()) {
                    if (isPlayerFromTeam1(playerModel)) {
                        team1Players++;
                    } else {
                        team2Players++;
                    }
                }

            }

            if (getBowlers() != null) {
                for (PlayerModel playerModel : getBowlers()) {
                    if (isPlayerFromTeam1(playerModel)) {
                        team1Players++;
                    } else {
                        team2Players++;
                    }
                }

            }
        }
    }

    public String getTeam1ShortName() {
        String name = getTeam1().getName(1);
        return name.toUpperCase();
    }


    public String getTeam2ShortName() {
        String name = getTeam2().getName(1);
        return name.toUpperCase();
    }

    public boolean isPlayerFromTeam1(PlayerModel playerModel) {
        return playerModel.getTeam_id() == getTeam1().getId();
    }

    public float getTotalPoints() {
        float totalPoints = 0.00f;
        if (getWicketkeapers() != null) {
            for (PlayerModel playerModel : getWicketkeapers()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }
        if (getBatsmans() != null) {
            for (PlayerModel playerModel : getBatsmans()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }
        if (getAllrounders() != null) {
            for (PlayerModel playerModel : getAllrounders()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }
        if (getBowlers() != null) {
            for (PlayerModel playerModel : getBowlers()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }

        return totalPoints;
    }
    public float getSoccerTotalPoints() {
        float totalPoints = 0.00f;
        if (getGoalkeepers() != null) {
            for (PlayerModel playerModel : getGoalkeepers()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }
        if (getDefenders() != null) {
            for (PlayerModel playerModel : getDefenders()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }
        if (getMidfielders() != null) {
            for (PlayerModel playerModel : getMidfielders()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }
        if (getForwards() != null) {
            for (PlayerModel playerModel : getForwards()) {
                totalPoints += playerModel.getPoints() * playerModel.getPlayer_multiplier();
            }

        }

        return totalPoints;
    }

    public String getTotalPointsText() {
        String s = getValidDecimalFormat(getTotalPoints());
        return s.replaceAll("\\.00", "");
    }

    public String getSoccerTotalPointsText() {
        String s = getValidDecimalFormat(getSoccerTotalPoints());
        return s.replaceAll("\\.00", "");
    }
    // soccer getter setter method
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
