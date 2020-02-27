package com.app.model;

import com.app.appbase.AppBaseModel;
import com.utilities.DeviceScreenUtil;

import java.util.List;

public class ContestModel extends AppBaseModel {

    long id;
    long match_contest_id;
    int total_team;
    int total_team_left;
    long per_user_team_allowed;
    long total_winners;
    double total_price;
    double entry_fees;
    double more_entry_fees;
    double actual_entry_fees;
    String joined_teams;
    String joined_teams_name;
    String confirm_win;
    String contest_pdf;
    double entry_fee_multiplier;
    String is_beat_the_expert;//"Y","N"
    String multi_team_allowed;//"Y","N"
    String max_entry_fees;

    List<String> entry_fees_suggest;

    List<ContestTeamModel> myteams;
    float highest_rank_points = -1;
    long highest_rank = -1;
    float highest_rank_win_amount = -1;
    float highest_rank_refund_amount = -1;


    String discount_image;
    int discount_image_width;
    int discount_image_height;
    String cash_bonus_used_type;//"F = fixd and P = %,
    String cash_bonus_used_value;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMatch_contest_id() {
        return match_contest_id;
    }

    public void setMatch_contest_id(long match_contest_id) {
        this.match_contest_id = match_contest_id;
    }

    public int getTotal_team() {
        return total_team;
    }

    public void setTotal_team(int total_team) {
        this.total_team = total_team;
    }

    public int getTotal_team_left() {
        return total_team_left;
    }

    public void setTotal_team_left(int total_team_left) {
        this.total_team_left = total_team_left;
    }

    public long getPer_user_team_allowed() {
        return per_user_team_allowed;
    }

    public void setPer_user_team_allowed(long per_user_team_allowed) {
        this.per_user_team_allowed = per_user_team_allowed;
    }

    public long getTotal_winners() {
        return total_winners;
    }

    public void setTotal_winners(long total_winners) {
        this.total_winners = total_winners;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getEntry_fees() {
        return entry_fees;
    }

    public void setEntry_fees(double entry_fees) {
        this.entry_fees = entry_fees;
    }

    public double getMore_entry_fees() {
        return more_entry_fees;
    }

    public void setMore_entry_fees(double more_entry_fees) {
        this.more_entry_fees = more_entry_fees;
    }

    public double getActual_entry_fees() {
        return actual_entry_fees;
    }

    public void setActual_entry_fees(double actual_entry_fees) {
        this.actual_entry_fees = actual_entry_fees;
    }

    public String getJoined_teams() {
        return getValidString(joined_teams);
    }

    public void setJoined_teams(String joined_teams) {
        this.joined_teams = joined_teams;
    }

    public String getJoined_teams_name() {
        return getValidString(joined_teams_name);
    }

    public void setJoined_teams_name(String joined_teams_name) {
        this.joined_teams_name = joined_teams_name;
    }

    public String getConfirm_win() {
        return getValidString(confirm_win);
    }


    public void setConfirm_win(String confirm_win) {
        this.confirm_win = confirm_win;
    }

    public boolean isConfirmWin() {
        return getConfirm_win().equals("Y");
    }

    public String getContest_pdf() {
        return getValidString(contest_pdf);
    }

    public void setContest_pdf(String contest_pdf) {
        this.contest_pdf = contest_pdf;
    }

    public double getEntry_fee_multiplier() {
        return entry_fee_multiplier;
    }

    public void setEntry_fee_multiplier(double entry_fee_multiplier) {
        this.entry_fee_multiplier = entry_fee_multiplier;
    }

    public String getIs_beat_the_expert() {
        return getValidString(is_beat_the_expert);
    }

    public void setIs_beat_the_expert(String is_beat_the_expert) {
        this.is_beat_the_expert = is_beat_the_expert;
    }

    public boolean isBetTheExpert(){
        return getIs_beat_the_expert().equals("Y");
    }

    public String getMulti_team_allowed() {
        return getValidString(multi_team_allowed);
    }

    public void setMulti_team_allowed(String multi_team_allowed) {
        this.multi_team_allowed = multi_team_allowed;
    }

    public boolean isMultiTeamAllowed(){
        return getMulti_team_allowed().equals("Y");
    }


    public String getMax_entry_fees() {
        return max_entry_fees;
    }

    public void setMax_entry_fees(String max_entry_fees) {
        this.max_entry_fees = max_entry_fees;
    }

    public List<String> getEntry_fees_suggest() {
        return entry_fees_suggest;
    }

    public void setEntry_fees_suggest(List<String> entry_fees_suggest) {
        this.entry_fees_suggest = entry_fees_suggest;
    }

    public float getHighest_rank_points() {
        return highest_rank_points;
    }

    public void setHighest_rank_points(float highest_rank_points) {
        this.highest_rank_points = highest_rank_points;
    }

    public long getHighest_rank() {
        return highest_rank;
    }

    public void setHighest_rank(long highest_rank) {
        this.highest_rank = highest_rank;
    }

    public float getHighest_rank_win_amount() {
        return highest_rank_win_amount;
    }

    public float getHighest_rank_refund_amount() {
        return highest_rank_refund_amount;
    }

    public List<ContestTeamModel> getMyteams() {
        return myteams;
    }

    public void setMyteams(List<ContestTeamModel> myteams) {
        this.myteams = myteams;
    }

    public boolean isMoreJoinAvailable() {
        int joinTeams = 0;
        if (getJoined_teams().trim().length() > 0) {
            String[] split = getJoined_teams().split(",");
            joinTeams = split.length;
        }

        return per_user_team_allowed > joinTeams;
    }

    public int getJoinedTeamCount(){
        int joinTeams = 0;
        if (getJoined_teams().trim().length() > 0) {
            String[] split = getJoined_teams().split(",");
            joinTeams = split.length;
        }
        return joinTeams;
    }

    public boolean isContestFull() {
        return getTotal_team_left() <= 0;
    }

    public boolean isAtleastOneTeamJoin() {
        return getJoined_teams().trim().length() > 0;
    }

    public boolean isMultiJoinContest() {
        return per_user_team_allowed > 1;
    }

    public String getJoinedWithText() {
        if (isValidString(getJoined_teams_name())) {
            String[] split = getJoined_teams_name().split(",");
            if (split.length > 1) {
                return split.length + " Teams";
            } else {
                return "Team " + getJoined_teams_name();
            }
        }
        return "Team " + "";

    }

    public void calculateData() {
        if (highest_rank == -1 || highest_rank_points == -1 || highest_rank_win_amount == -1|| highest_rank_refund_amount == -1) {
            if (getMyteams() != null && getMyteams().size() > 0) {
                long previousRank = Long.MAX_VALUE;
                int position = 0;
                float totalWinAmount = 0;
                float totalRefundAmount = 0;
                for (int i = 0; i < getMyteams().size(); i++) {
                    ContestTeamModel contestTeamModel = getMyteams().get(i);
                    totalWinAmount += contestTeamModel.getWin_amount();
                    totalRefundAmount += contestTeamModel.getRefund_amount();
                    if (contestTeamModel.getNew_rank() < previousRank) {
                        position = i;
                        previousRank = contestTeamModel.getNew_rank();
                    }
                }
                highest_rank = getMyteams().get(position).getNew_rank();
                highest_rank_points = getMyteams().get(position).getTotal_points();
                highest_rank_win_amount = totalWinAmount;
                highest_rank_refund_amount = totalRefundAmount;
            } else {
                highest_rank = 0;
                highest_rank_points = 0;
                highest_rank_win_amount = 0;
                highest_rank_refund_amount = 0;
            }
        }
    }

    public String getNewRankText() {
        return getHighest_rank() == 0 ? "-" : "#" + getHighest_rank();
    }

    public String getTotalPointstext() {
        String s = getValidDecimalFormat(getHighest_rank_points());
        return s.replaceAll("\\.00", "");
    }

    public String getWinAmountText() {
        String s = getValidDecimalFormat(getHighest_rank_win_amount());
        return s.replaceAll("\\.00", "");
    }

    public String getRefundAmountText() {
        String s = getValidDecimalFormat(getHighest_rank_refund_amount());
        return s.replaceAll("\\.00", "");
    }

    public String getTotalPriceText() {
        String s = getValidDecimalFormat(getTotal_price());
        return s.replaceAll("\\.00", "");
    }

    public String getEntryFeesText() {
        String s = getValidDecimalFormat(getEntry_fees());
        return s.replaceAll("\\.00", "");
    }
    public String getMoreEntryFeesText() {
        if(getMore_entry_fees() == 0)
            return "";
        //double more_entry = (getEntry_fees() * 100 )/ (100 - getMore_entry_fees());
        double more_entry = getActual_entry_fees();
        String s = getValidDecimalFormat(more_entry);
        return s.replaceAll("\\.00", "");
    }
    public String getEntryFeesMultiplierText() {
        String s = getValidDecimalFormat(getEntry_fee_multiplier());
        return s.replaceAll("\\.00", "");
    }


    public String getTotalMultiplierText() {
        double total_multipal_amount = getEntry_fee_multiplier() * getEntry_fees();
        String s = getValidDecimalFormat(total_multipal_amount);
        return s.replaceAll("\\.00", "");
    }

    public String getTotalDiscountText() {
        double total_discount_amount = Double.parseDouble(getCash_bonus_used_value());
        String s = getValidDecimalFormat(total_discount_amount);
        return s.replaceAll("\\.00", "");
    }

    public String getDiscount_image() {
        return getValidString(discount_image);
    }

    public void setDiscount_image(String discount_image) {
        this.discount_image = discount_image;
    }

    public int getDiscount_image_width() {
        return discount_image_width;
    }

    public void setDiscount_image_width(int discount_image_width) {
        this.discount_image_width = discount_image_width;
    }

    public int getDiscount_image_height() {
        return discount_image_height;
    }

    public void setDiscount_image_height(int discount_image_height) {
        this.discount_image_height = discount_image_height;
    }


    public String getCash_bonus_used_type() {
        return cash_bonus_used_type;
    }

    public void setCash_bonus_used_type(String cash_bonus_used_type) {
        this.cash_bonus_used_type = cash_bonus_used_type;
    }

    public String getCash_bonus_used_value() {
        return cash_bonus_used_value;
    }

    public void setCash_bonus_used_value(String cash_bonus_used_value) {
        this.cash_bonus_used_value = cash_bonus_used_value;
    }

    public int[] getDiscountImageSizeForContest(){
        int deviceWidth = DeviceScreenUtil.getInstance().convertDpToPixel(70.0f);
        int deviceHeight = DeviceScreenUtil.getInstance().convertDpToPixel(25.0f);

        int imageWidth = deviceWidth;
        int imageHeight = deviceHeight;

        long width = getDiscount_image_width();
        long height = getDiscount_image_height();

        if (width > height) {
            float ratio = ((float) height) / ((float) width);

            imageHeight = Math.round(imageWidth * ratio);

            if(imageHeight>deviceHeight){
                imageHeight=deviceHeight;
                ratio = ((float) width) / ((float) height);
                imageWidth = Math.round(imageHeight * ratio);
            }

        } else if (height > width) {
            float ratio = ((float) width) / ((float) height);

            imageWidth = Math.round(imageHeight * ratio);

            if(imageWidth>deviceWidth){
                imageWidth=deviceWidth;
                ratio = ((float) height) / ((float) width);
                imageHeight = Math.round(imageWidth * ratio);
            }
        } else {
            imageWidth=Math.min(imageWidth,imageHeight);
            imageHeight = imageWidth;
        }
        return new int[]{imageWidth,imageHeight};
    }

    public int[] getDiscountImageSizeForContestSmall(){
        int deviceWidth = DeviceScreenUtil.getInstance().convertDpToPixel(40.0f);
        int deviceHeight = DeviceScreenUtil.getInstance().convertDpToPixel(20.0f);

        int imageWidth = deviceWidth;
        int imageHeight = deviceHeight;

        long width = getDiscount_image_width();
        long height = getDiscount_image_height();

        if (width > height) {
            float ratio = ((float) height) / ((float) width);

            imageHeight = Math.round(imageWidth * ratio);

            if(imageHeight>deviceHeight){
                imageHeight=deviceHeight;
                ratio = ((float) width) / ((float) height);
                imageWidth = Math.round(imageHeight * ratio);
            }

        } else if (height > width) {
            float ratio = ((float) width) / ((float) height);

            imageWidth = Math.round(imageHeight * ratio);

            if(imageWidth>deviceWidth){
                imageWidth=deviceWidth;
                ratio = ((float) height) / ((float) width);
                imageHeight = Math.round(imageWidth * ratio);
            }
        } else {
            imageWidth=Math.min(imageWidth,imageHeight);
            imageHeight = imageWidth;
        }
        return new int[]{imageWidth,imageHeight};
    }
}
