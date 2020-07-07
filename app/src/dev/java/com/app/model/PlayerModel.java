package com.app.model;

import android.util.Log;

import com.app.appbase.AppBaseModel;

public class PlayerModel extends AppBaseModel {

    String player_id;
    long team_id;
    String position;
    float credits;
    float total_points;
    float points;
    String name;
    String is_in_playing_squad;
    String playing_squad_updated;
    String bat_type;
    String bowl_type;
    String country;
    String dob;
    String selected_by;
    String selected_as_caption;
    String selected_as_vccaption;
    String selected_as_mpp;
    String image;
    String dream_team_player;
    long player_pos;
    float player_multiplier=1;
    boolean selected;
    int playerType;
    String is_mpp;

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void resetPlayer(){
        setIs_in_playing_squad("N");
        setPlayer_id(null);
        setTeam_id(0);
        setName(null);
        setBat_type(null);
        setBowl_type(null);
        setCountry(null);
        setDob(null);
        setImage(null);
    }


    public String getPlayer_id() {
        return getValidString(player_id);
    }

    public void setPlayer_id(String player_id) {
        this.player_id = player_id;
    }

    public long getTeam_id() {
        return team_id;
    }

    public void setTeam_id(long team_id) {
        this.team_id = team_id;
    }

    public String getPosition() {
        return getValidString(position);
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public float getCredits() {
        return credits;
    }

    public String getCreditText() {
        String s = getValidDecimalFormat(getCredits());
        return s.replaceAll("\\.00", "");
    }

    public void setCredits(float credits) {
        this.credits = credits;
    }

    public float getTotal_points() {
        return total_points;
    }

    public String getTotalPointsText() {
        String s = getValidDecimalFormat(getTotal_points());
        return s.replaceAll("\\.00", "");
    }

    public void setTotal_points(float total_points) {
        this.total_points = total_points;
    }

    public float getPoints() {
        return points;
    }

    public void setPoints(float points) {
        this.points = points;
    }

    public String getPointsText() {
        String s = getValidDecimalFormat(getPoints() * getPlayer_multiplier());
        return s.replaceAll("\\.00", "");
    }

    public String getName() {
        return getValidString(name).trim();
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInPlayingSquad() {
        return getIs_in_playing_squad().equals("Y");
    }

    public String getIs_in_playing_squad() {
        return getValidString(is_in_playing_squad);
    }

    public void setIs_in_playing_squad(String is_in_playing_squad) {
        this.is_in_playing_squad = is_in_playing_squad;
    }

    public boolean isInPlayingSquadUpdated() {
        return getPlaying_squad_updated().equals("Y");
    }

    public String getPlaying_squad_updated() {
        return getValidString(playing_squad_updated);
    }

    public void setPlaying_squad_updated(String playing_squad_updated) {
        this.playing_squad_updated = playing_squad_updated;
    }

    public String getBat_type() {
        return getValidString(bat_type);
    }

    public void setBat_type(String bat_type) {
        this.bat_type = bat_type;
    }

    public String getBowl_type() {
        return getValidString(bowl_type);
    }

    public void setBowl_type(String bowl_type) {
        this.bowl_type = bowl_type;
    }

    public String getCountry() {
        return getValidString(country);
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return getValidString(dob);
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSelected_by() {
        return getValidString(selected_by);
    }

    public void setSelected_by(String selected_by) {
        this.selected_by = selected_by;
    }

    public String getSelected_as_caption() {
        return getValidString(selected_as_caption);
    }

    public void setSelected_as_caption(String selected_as_caption) {
        this.selected_as_caption = selected_as_caption;
    }

    public String getSelected_as_vccaption() {
        return getValidString(selected_as_vccaption);
    }

    public void setSelected_as_vccaption(String selected_as_vccaption) {
        this.selected_as_vccaption = selected_as_vccaption;
    }

    public String getSelected_as_mpp() {
        return selected_as_mpp;
    }

    public void setSelected_as_mpp(String selected_as_mpp) {
        this.selected_as_mpp = selected_as_mpp;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
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

    public long getPlayer_pos() {
        return player_pos;
    }

    public void setPlayer_pos(long player_pos) {
        this.player_pos = player_pos;
    }

    public float getPlayer_multiplier() {
        return player_multiplier;
    }

    public void setPlayer_multiplier(float player_multiplier) {
        this.player_multiplier = player_multiplier;
    }

    public String getPlayerMultiplierText() {
        return String.valueOf(((int) getPlayer_multiplier()));
    }

    public String getSelected_byText() {
        String s = getValidDecimalFormat(getSelected_by());
        Log.d("selected", s);
        return s.replaceAll("\\.00", "");

    }

    public String getCaptionSelected_byText() {
        String s = getValidDecimalFormat(getSelected_as_caption());
        return s.replaceAll("\\.00", "");
    }

    public String getViceCaptionSelected_byText() {
        String s = getValidDecimalFormat(getSelected_as_vccaption());
        return s.replaceAll("\\.00", "");
    }
    public String getMppSelected_byText() {
        String s = getValidDecimalFormat(getSelected_as_mpp());
        return s.replaceAll("\\.00", "");
    }
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PlayerModel)) return false;
        return getPlayer_id().equals(((PlayerModel) obj).getPlayer_id());
    }

    public float getFinalPoint() {
        return getPoints() * getPlayer_multiplier();
    }

    public String getFinalPointText() {
        return String.valueOf(((int) getFinalPoint()));
    }

    public String getIs_mpp() {
        return is_mpp;
    }

    public void setIs_mpp(String is_mpp) {
        this.is_mpp = is_mpp;
    }
}
