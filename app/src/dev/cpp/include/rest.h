//
// Created by bitu on 2/9/18.
//
#include <jni.h>

#ifndef REST_NATIVE_LIB_H
#define REST_NATIVE_LIB_H

#endif //REST_NATIVE_LIB_H
extern "C" {


JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateDeviceToken(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CheckAppVersion(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAppCustomIcons(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetQuotations(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetGames(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_NewUser(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_VerifyOtp(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ForgotPassword(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CheckUser(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_SocialLogin(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_Login(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_Logout(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSuggestedTeamName(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetUpcomingMatches(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerUpcomingMatches(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerLiveMatches(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerPastMatches(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchPlayers(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchPlayersStats(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchContests(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchContestsDetail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchContestPdf(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetContestTeams(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchContests(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetContestWinnerBreakUp(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreateCustomerTeam(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchTeams(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchTeamDetail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetCustomerMatchTeamStats(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchDreamTeamDetail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchDreamTeamStats(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateCustomerTeam(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSeriesByPlayerStatistics(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerPreJoinContest(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerJoinContest(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerSwitchTeam(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchScore(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateProfile(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateVerifyEmail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_SendOtpMobile(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_UpdateVerifyMobile(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetStates(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetProfilePictures(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ChangeProfilePictures(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ChangePassword(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerDepositAmount(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_WalletRecharge(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWithdrawAmount(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWalletHistory(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWithdrawHistory(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerWalletDetail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerTeamNameUpdate(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetReferralSettings(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSlider(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_AddPancard(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_AddBankDetail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetProfile(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetTnc(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivacyPolicy(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetStaggeredCashbonus(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetFantasyPoints(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetHowToPlay(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAboutUs(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetLegality(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetInviteCode(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetInviteFriends(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ReferHowItWorks(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ReferFairPlay(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetReferEarn(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetReferEarnDetail(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetNotifications(
        JNIEnv *env,
        jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPlayingHistory(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_JoinPrivateContest(
        JNIEnv *env,
        jobject obj);

JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ShareContest(
        JNIEnv *env,
        jobject obj);




JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSliderKb(
        JNIEnv *env,
        jobject obj);

       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSliderSoccer(
               JNIEnv *env,
               jobject obj);

       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAlreadyCreatedTeamCountSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivateContestSettingsSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivateContestEntryFeeSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetPrivateContestWinningBreakUpSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreatePrivateContestSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_JoinPrivateContestSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_ShareContestSoccer(
               JNIEnv *env,
               jobject obj);//new
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerPreJoinContestSoccer(
                       JNIEnv *env,
                       jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerJoinContestSoccer(
                       JNIEnv *env,
                       jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CustomerSwitchTeamSoccer(
                                       JNIEnv *env,
                                       jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetMatchScoreSoccer(
               JNIEnv *env,
               jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_CreateSharePostSoccer(
               JNIEnv *env,
               jobject obj);

       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSeriesSoccer(
               JNIEnv *env,
               jobject obj);

    JNIEXPORT jstring JNICALL Java_com_rest_WebServices_getCustomerProfileSoccer(
               JNIEnv *env,
               jobject obj);


       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_getCustomerProfile(
               JNIEnv *env,
               jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_getCustomers(
               JNIEnv *env,
               jobject obj);

       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSeries(
               JNIEnv *env,
               jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSerieLeaderboard(
               JNIEnv *env,
               jobject obj);
       JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetSerieLeaderboardCustomerMatches(
               JNIEnv *env,
               jobject obj);

 //page
  JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetFantasyPoints(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetHowToPlay(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetAboutUs(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetLegality(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetWithdrawPolicy(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetTnc(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetContact(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetFaq(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetHelpDesk(
                JNIEnv *env,
                jobject obj);
JNIEXPORT jstring JNICALL Java_com_rest_WebServices_GetApplyPromoCode(
                JNIEnv *env,
                jobject obj);


}

