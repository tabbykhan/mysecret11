#include <jni.h>
#include <string>
#include "include/main.h"

extern "C" {


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateDeviceToken(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "update_token";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CheckAppVersion(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "check_app_version";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAppCustomIcons(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_app_custom_icons";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetQuotations(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_quotations";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetGames(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_games";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_NewUser(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "newuser";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_VerifyOtp(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "verifyotp";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ForgotPassword(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "forgotpassword_email";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CheckUser(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "check_user";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_SocialLogin(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "social_login";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_Login(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "login";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSuggestedTeamName(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_suggested_team_name";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetStates(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "states/%s";//country_id
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateProfile(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "update_profile";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateVerifyEmail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "update_verify_email";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_SendOtpMobile(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "send_otp_mobile";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateVerifyMobile(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "update_verify_mobile";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetProfilePictures(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_profile_pictures";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ChangeProfilePictures(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "change_profile_picture";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ChangePassword(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "change_password";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_Logout(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "logout";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetUpcomingMatches(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_matches/%s";//match_progress(F,L,R)
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerUpcomingMatches(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_matches/F";//match_progress(F,L,R)
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerLiveMatches(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_matches/L";//match_progress(F,L,R)
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerPastMatches(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_matches/R";//match_progress(F,L,R)
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchPlayers(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_players/%s";//matchid
    return env->NewStringUTF(data.c_str());
}



JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchPlayersStats(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_players_stats/%s";//match_uniqueid
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchContests(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_contest/%s/%s";//matchid/match_uniqueid
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchContestsDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data =
            getBaseUrl(env) + "get_match_contest_detail/%s/%s";//match_contest_id,match_unique_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchContestPdf(
        JNIEnv *env,
        jobject obj) {
    std::string data =
            getBaseUrl(env) + "get_match_contest_pdf/%s/%s";//match_contest_id,match_unique_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetContestTeams(
        JNIEnv *env,
        jobject obj) {
    std::string data =
            getBaseUrl(env) + "get_contest_teams/%s/%s/%s";//match_unique_id,match_contest_id,page_no
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchContests(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_match_contest/%s/%s";//matchid/uniqueid
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetContestWinnerBreakUp(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_contest_winner_breakup/%s";//contestid
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreateCustomerTeam(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "create_customer_team";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchTeams(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_match_teams/%s";//match_unique_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchTeamDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_match_team_detail/%s";//customer_team_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchTeamStats(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_match_team_stats/%s";//customer_team_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchDreamTeamDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_dream_team_detail/%s";//match_unique_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchDreamTeamStats(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_dream_team_stats/%s";//match_unique_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateCustomerTeam(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "update_customer_team";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSeriesByPlayerStatistics(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) +
                       "get_series_by_player_statistics/%s/%s";//match_unique_id,player_unique_id
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAlreadyCreatedTeamCount(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_already_created_team_count/%s";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivateContestSettings(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_private_contest_settings";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivateContestEntryFee(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_private_contest_entry_fee";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivateContestWinningBreakUp(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_private_contest_winning_breakup";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreatePrivateContest(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "create_private_contest";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchPrivateContestDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_private_contest_detail/%s";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ShareContest(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_contest_share_detail/%s";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerPreJoinContest(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "customer_pre_join_contest";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerJoinContest(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "customer_join_contest";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerSwitchTeam(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "customer_switch_team";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchScore(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_score/%s";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerDepositAmount(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "customer_deposit_amount";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_WalletRecharge(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "wallet_recharge";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWithdrawAmount(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "customer_withdraw_amount";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWalletHistory(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_wallet_history_filter/%s/%s";//page_no,type
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWithdrawHistory(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_withdraw_history/%s";//page_no
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWalletDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_wallet_detail";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerTeamNameUpdate(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "customer_team_name_update";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetReferralSettings(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_referral_settings";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSlider(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_slider";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_AddPancard(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "add_pancard";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_AddBankDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "add_bankdetail";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetProfile(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_profile";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetReferEarn(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_refer_earn";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetReferEarnDetail(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_refer_earn_detail";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetNotifications(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_notifications/%s";//:page_no
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPlayingHistory(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_playing_history";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetTnc(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "tnc.html";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivacyPolicy(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "privacy-policy.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetStaggeredCashbonus(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "staggered-cashbonus.html";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetFantasyPoints(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "fantasy-point-system.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetHowToPlay(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "how-to-play.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAboutUs(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "about-us.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetLegality(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "legality.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetWithdrawPolicy(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "withdrawal_policy.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetContact(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "contact_us.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetInviteCode(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "invite-code.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetInviteFriends(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "invite-friends.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ReferHowItWorks(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "refer-how-it-works.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ReferFairPlay(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "refer-fair-play.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetFaq(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "faq.html";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetHelpDesk(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "helpdesk.html";
    return env->NewStringUTF(data.c_str());
}


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerProfile(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customer_profile";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_getCustomers(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_customers";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreateSharePost(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "create_post";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSerieLeaderboardCustomerMatches(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_series_leaderboard_customer_matches";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetApplyPromoCode(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "apply_promocode";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreateCustomerEnquiry(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "create_customer_enquiry";
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivatePolicy(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBasePageUrl(env) + "privacy-policy.html";
    return env->NewStringUTF(data.c_str());
}

//-----------------new service----
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_cashfree_1wallet(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "cashfree_wallet";//payment gateway
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_wallet_1recharge(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "wallet_recharge";//payment gateway
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_get_1matches_15aside(JNIEnv *env,jobject obj){
   std::string data = getBaseUrl(env) + "get_matches_5aside/F";//get5player match(F,L,R)
    return env->NewStringUTF(data.c_str());
}

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_get_1match_1players_15aside(
        JNIEnv *env,
        jobject obj) {
    std::string data = getBaseUrl(env) + "get_match_players_5aside/%s";
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_create_1customer_1team_1five_1players(JNIEnv
*env,jobject obj){
 std::string data = getBaseUrl(env) + "create_customer_team_five_players";// 5 player team create
    return env->NewStringUTF(data.c_str());
}
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_customer_1withdraw_1amount(JNIEnv
*env,jobject obj){
 std::string data = getBaseUrl(env) + "customer_withdraw_amount";// customer withdraw amount
    return env->NewStringUTF(data.c_str());
}
}



