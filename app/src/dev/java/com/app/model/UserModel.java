package com.app.model;

import com.app.appbase.AppBaseModel;

/**
 * @author Manish Kumar
 * @since 29/9/18
 */

public class UserModel extends AppBaseModel {

    String slug;
    long id;
    String firstname;
    String lastname;
    String email;
    String is_social;
    String social_type;
    String social_id;
    String country_mobile_code;
    String phone;
    String referral_code;
    String is_phone_verified;
    String is_email_verified;
    String image;
    long dob;
    String addressline1;
    String addressline2;
    String pincode;
    String team_name;
    String team_change;
    CountryModel country;
    StateModel state;
    CityModel city;
    WalletModel wallet;

    PanCardModel pancard;
    BankDetailModel bankdetail;

    AwsModel aws;

    SettingsModel settings;

    long notification_counter;

    float used_refferal_amount;
    float received_referral_amount;

    String follower_count;
    String following_count;
    String post_count;

    public float getUsed_refferal_amount() {
        return used_refferal_amount;
    }

    public String getUserReferralAmountText() {
        String validDecimalFormat = getValidDecimalFormat(getUsed_refferal_amount());
        return validDecimalFormat.replaceAll("\\.00", "");
    }

    public void setUsed_refferal_amount(float used_refferal_amount) {
        this.used_refferal_amount = used_refferal_amount;
    }

    public float getReceived_referral_amount() {
        return received_referral_amount;
    }

    public String getReceivedReferralAmountText() {
        String validDecimalFormat = getValidDecimalFormat(getReceived_referral_amount());
        return validDecimalFormat.replaceAll("\\.00", "");
    }

    public void setReceived_referral_amount(float received_referral_amount) {
        this.received_referral_amount = received_referral_amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSlug() {
        return getValidString(slug);
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getFirstname() {
        return getValidString(firstname);
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return getValidString(lastname);
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return getValidString(email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIs_social() {
        return getValidString(is_social);
    }

    public boolean isSocial() {
        return getIs_social().equals("Y");
    }

    public void setIs_social(String is_social) {
        this.is_social = is_social;
    }

    public String getSocial_type() {
        return getValidString(social_type);
    }

    public boolean isFacebookLogin() {
        return getSocial_type().equals("F");
    }

    public boolean isGplusLogin() {
        return getSocial_type().equals("G");
    }

    public void setSocial_type(String social_type) {
        this.social_type = social_type;
    }

    public String getSocial_id() {
        return getValidString(social_id);
    }

    public void setSocial_id(String social_id) {
        this.social_id = social_id;
    }

    public String getCountry_mobile_code() {
        return getValidString(country_mobile_code);
    }

    public void setCountry_mobile_code(String country_mobile_code) {
        this.country_mobile_code = country_mobile_code;
    }

    public String getPhone() {
        return getValidString(phone);
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReferral_code() {
        return getValidString(referral_code);
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    public boolean isPhoneVerified() {
        return getIs_phone_verified().equals("Y");
    }

    public String getIs_phone_verified() {
        return getValidString(is_phone_verified);
    }

    public void setIs_phone_verified(String is_phone_verified) {
        this.is_phone_verified = is_phone_verified;
    }

    public boolean isEmailVerified() {
        return getIs_email_verified().equals("Y");
    }

    public String getIs_email_verified() {
        return getValidString(is_email_verified);
    }

    public void setIs_email_verified(String is_email_verified) {
        this.is_email_verified = is_email_verified;
    }

    public String getImage() {
        return getValidString(image);
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getDob() {
        return dob;
    }

    public void setDob(long dob) {
        this.dob = dob;
    }

    public String getDobText() {
        if (getDob() == 0) return "";
        return getFormatedDateString(DATE_DDMMYYYY, getDob());

    }

    public String getAddressline1() {
        return getValidString(addressline1);
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return getValidString(addressline2);
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }

    public String getPincode() {
        return getValidString(pincode);
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getTeam_name() {
        return getValidString(team_name);
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public String getTeam_change() {
        return getValidString(team_change);
    }

    public void setTeam_change(String team_change) {
        this.team_change = team_change;
    }

    public boolean isTeamChange() {
        return getTeam_change().equals("Y");
    }

    public CountryModel getCountry() {
        return country;
    }

    public void setCountry(CountryModel country) {
        this.country = country;
    }

    public StateModel getState() {
        return state;
    }

    public void setState(StateModel state) {
        this.state = state;
    }

    public CityModel getCity() {
        return city;
    }

    public void setCity(CityModel city) {
        this.city = city;
    }

    public WalletModel getWallet() {
        return wallet;
    }

    public void setWallet(WalletModel wallet) {
        this.wallet = wallet;
    }

    public long getNotification_counter() {
        return notification_counter;
    }

    public String getNotificationCounterText(){
        return getNotification_counter()>99?"99+":String.valueOf(getNotification_counter());
    }

    public void setNotification_counter(long notification_counter) {
        this.notification_counter = notification_counter;
    }

    public String getFullName() {
        if (isValidString(getLastname())) {
            return getFirstname() + " " + getLastname();
        }
        return getFirstname();
    }


    public String getFullMobile() {
        if (isValidString(getCountry_mobile_code())) {
            return getCountry_mobile_code() + "-" + getPhone();
        }
        return getPhone();
    }


    public PanCardModel getPancard() {
        return pancard;
    }

    public void setPancard(PanCardModel pancard) {
        this.pancard = pancard;
    }

    public BankDetailModel getBankdetail() {
        return bankdetail;
    }

    public void setBankdetail(BankDetailModel bankdetail) {
        this.bankdetail = bankdetail;
    }

    public AwsModel getAws() {
        return aws;
    }


    public SettingsModel getSettings() {
        return settings;
    }

    public boolean isWithdrawAvailable() {
        boolean phoneVerified = isPhoneVerified();
        if (!phoneVerified) return false;
        boolean emailVerified = isEmailVerified();
        if (!isEmailVerified()) return false;
        PanCardModel pancard = getPancard();
        if (pancard == null || !pancard.isApproved()) return false;
        BankDetailModel bankdetail = getBankdetail();
        if (bankdetail == null || !bankdetail.isApproved()) return false;

        return true;

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
}
