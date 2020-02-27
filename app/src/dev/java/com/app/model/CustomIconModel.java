package com.app.model;

import com.app.appbase.AppBaseModel;

import java.util.Objects;

public class CustomIconModel extends AppBaseModel {

    IconModel tab_home;
    IconModel tab_my_contest;
    IconModel tab_feeds;
    IconModel tab_account;
    IconModel tab_more;
    IconModel more_invite_friends;
    IconModel more_contest_invite_code;
    IconModel more_fantasy_point_system;
    IconModel more_how_to_play;
    IconModel more_helpdesk;
    IconModel more_aboutus;
    IconModel more_legality;
    IconModel more_withdraw_policy;
    IconModel more_terms_of_services;
    IconModel more_contact;
    IconModel more_faq;

    @Override
    public int hashCode() {
        return Objects.hash(getTab_home(),getTab_my_contest(),getTab_feeds(),getTab_account(),getTab_more(),
                getMore_invite_friends(),getMore_contest_invite_code(),getMore_fantasy_point_system(),
                getMore_how_to_play(),getMore_helpdesk(),getMore_aboutus(),getMore_legality(),
                getMore_withdraw_policy(),getMore_terms_of_services(),getMore_contact(),getMore_faq());
    }

    public IconModel getTab_home() {
        return tab_home;
    }

    public void setTab_home(IconModel tab_home) {
        this.tab_home = tab_home;
    }

    public IconModel getTab_my_contest() {
        return tab_my_contest;
    }

    public void setTab_my_contest(IconModel tab_my_contest) {
        this.tab_my_contest = tab_my_contest;
    }

    public IconModel getTab_feeds() {
        return tab_feeds;
    }

    public void setTab_feeds(IconModel tab_feeds) {
        this.tab_feeds = tab_feeds;
    }

    public IconModel getTab_account() {
        return tab_account;
    }

    public void setTab_account(IconModel tab_account) {
        this.tab_account = tab_account;
    }

    public IconModel getTab_more() {
        return tab_more;
    }

    public void setTab_more(IconModel tab_more) {
        this.tab_more = tab_more;
    }

    public IconModel getMore_invite_friends() {
        return more_invite_friends;
    }

    public void setMore_invite_friends(IconModel more_invite_friends) {
        this.more_invite_friends = more_invite_friends;
    }

    public IconModel getMore_contest_invite_code() {
        return more_contest_invite_code;
    }

    public void setMore_contest_invite_code(IconModel more_contest_invite_code) {
        this.more_contest_invite_code = more_contest_invite_code;
    }

    public IconModel getMore_fantasy_point_system() {
        return more_fantasy_point_system;
    }

    public void setMore_fantasy_point_system(IconModel more_fantasy_point_system) {
        this.more_fantasy_point_system = more_fantasy_point_system;
    }

    public IconModel getMore_how_to_play() {
        return more_how_to_play;
    }

    public void setMore_how_to_play(IconModel more_how_to_play) {
        this.more_how_to_play = more_how_to_play;
    }

    public IconModel getMore_helpdesk() {
        return more_helpdesk;
    }

    public void setMore_helpdesk(IconModel more_helpdesk) {
        this.more_helpdesk = more_helpdesk;
    }

    public IconModel getMore_aboutus() {
        return more_aboutus;
    }

    public void setMore_aboutus(IconModel more_aboutus) {
        this.more_aboutus = more_aboutus;
    }

    public IconModel getMore_legality() {
        return more_legality;
    }

    public void setMore_legality(IconModel more_legality) {
        this.more_legality = more_legality;
    }

    public IconModel getMore_withdraw_policy() {
        return more_withdraw_policy;
    }

    public void setMore_withdraw_policy(IconModel more_withdraw_policy) {
        this.more_withdraw_policy = more_withdraw_policy;
    }

    public IconModel getMore_terms_of_services() {
        return more_terms_of_services;
    }

    public void setMore_terms_of_services(IconModel more_terms_of_services) {
        this.more_terms_of_services = more_terms_of_services;
    }

    public IconModel getMore_contact() {
        return more_contact;
    }

    public void setMore_contact(IconModel more_contact) {
        this.more_contact = more_contact;
    }

    public IconModel getMore_faq() {
        return more_faq;
    }

    public void setMore_faq(IconModel more_faq) {
        this.more_faq = more_faq;
    }

    public static class IconModel extends AppBaseModel{
        String tag;
        String name;
        String image;

        @Override
        public int hashCode() {
            return Objects.hash(getTag(),getName(),getImage());
        }

        public String getTag() {
            return getValidString(tag);
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getName() {
            return getValidString(name);
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return getValidString(image);
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
