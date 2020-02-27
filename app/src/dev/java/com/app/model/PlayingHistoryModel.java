package com.app.model;

import com.app.appbase.AppBaseModel;

public class PlayingHistoryModel extends AppBaseModel {

    long contests;
    long matches;
    long series;
    long wins;

    public long getContests() {
        return contests;
    }

    public void setContests(long contests) {
        this.contests = contests;
    }

    public long getMatches() {
        return matches;
    }

    public void setMatches(long matches) {
        this.matches = matches;
    }

    public long getSeries() {
        return series;
    }

    public void setSeries(long series) {
        this.series = series;
    }

    public long getWins() {
        return wins;
    }

    public void setWins(long wins) {
        this.wins = wins;
    }
}
