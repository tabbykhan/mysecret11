package com.app.model.webresponsemodel;

import com.app.appbase.AppBaseModel;
import com.app.model.PlayingHistoryModel;

public class CustomerProfileModel extends AppBaseModel {

    private long id;
    private String is_follow;
    private String is_following;
    private String firstname;
    private String lastname;
    private String email;
    private String country_mobile_code;
    private String phone;
    private String follower_count;
    private String following_count;
    private String post_count;
    private String image;
    private String team_name;
    private PlayingHistoryModel playing_history;




    public PlayingHistoryModel getPlaying_history() {
        return playing_history;
    }

    public void setPlaying_history(PlayingHistoryModel playing_history) {
        this.playing_history = playing_history;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIs_follow() {
        return is_follow;
    }

    public void setIs_follow(String is_follow) {
        this.is_follow = is_follow;
    }

    public String getIs_following() {
        return is_following;
    }

    public void setIs_following(String is_following) {
        this.is_following = is_following;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry_mobile_code() {
        return country_mobile_code;
    }

    public void setCountry_mobile_code(String country_mobile_code) {
        this.country_mobile_code = country_mobile_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFollower_count() {
        return follower_count;
    }

    public void setFollower_count(String follower_count) {
        this.follower_count = follower_count;
    }

    public String getFollowing_count() {
        return following_count;
    }

    public void setFollowing_count(String following_count) {
        this.following_count = following_count;
    }

    public String getPost_count() {
        return post_count;
    }

    public void setPost_count(String post_count) {
        this.post_count = post_count;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    public boolean getisFollowing(){
        if(getIs_follow().equalsIgnoreCase("Y")){
            return true;
        }
        return false;
    }

    public String getTeam_name() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getFullName(){
        if(getLastname() != null){
            return getFirstname() +" "+getLastname();
        }
        return getFirstname();
    }
}
