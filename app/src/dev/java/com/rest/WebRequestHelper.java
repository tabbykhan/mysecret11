package com.rest;

import android.content.Context;

import com.app.model.webrequestmodel.CheckAppVersionRequestModel;
import com.app.model.webrequestmodel.CheckUserRequestModel;
import com.app.model.webrequestmodel.CreatePrivateContestRequestModel;
import com.app.model.webrequestmodel.CreateTeamRequestModel;
import com.app.model.webrequestmodel.CustomerEnquiryRequestModel;
import com.app.model.webrequestmodel.CustomerJoinContestRequestModel;
import com.app.model.webrequestmodel.CustomerRequestModel;
import com.app.model.webrequestmodel.DepositAmountRequestModel;
import com.app.model.webrequestmodel.ForgotPasswordRequestModel;
import com.app.model.webrequestmodel.LoginRequestModel;
import com.app.model.webrequestmodel.LogoutRequestModel;
import com.app.model.webrequestmodel.NewUserRequestModel;
import com.app.model.webrequestmodel.PromoCodeRequestModel;
import com.app.model.webrequestmodel.SocialLoginRequestModel;
import com.app.model.webrequestmodel.SwitchTeamRequestModel;
import com.app.model.webrequestmodel.TeamNameRequestModel;
import com.app.model.webrequestmodel.TeamNameUpdateRequestModel;
import com.app.model.webrequestmodel.UpdateBankRequestModel;
import com.app.model.webrequestmodel.UpdateDeviceTokenRequestModel;
import com.app.model.webrequestmodel.UpdatePanRequestModel;
import com.app.model.webrequestmodel.UpdateProfileRequestModel;
import com.app.model.webrequestmodel.UpdateTeamRequestModel;
import com.app.model.webrequestmodel.VerifyOtpRequestModel;
import com.app.model.webrequestmodel.WithdrawAmountRequestModel;
import com.medy.retrofitwrapper.WebRequest;
import com.medy.retrofitwrapper.WebServiceResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

/**
 * Created by ubuntu on 27/3/18.
 */

public class WebRequestHelper implements WebRequestConstants {

    private static final String TAG = WebRequestHelper.class.getSimpleName();
    private Context context;

    public WebRequestHelper(Context context) {
        this.context = context;
    }

    public boolean isValidString(String data) {
        return data != null && !data.trim().isEmpty();
    }


    public void updateDeviceToken(UpdateDeviceTokenRequestModel requestModel,
                                  WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_DEVICE_TOKEN, WebServices.UpdateDeviceToken(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void checkAppVersion(CheckAppVersionRequestModel requestModel,
                                WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CHECK_APP_VERSION, WebServices.CheckAppVersion(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getAppCustomIcons(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_APP_CUSTOM_ICONS, WebServices.GetAppCustomIcons(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getQuotations(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_QUOTATIONS, WebServices.GetQuotations(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getGames(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_GAMES, WebServices.GetGames(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void newUser(NewUserRequestModel requestModel,
                        WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_NEW_USER, WebServices.NewUser(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.addExtra(DATA, requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void verifyOtp(VerifyOtpRequestModel requestModel,
                          WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_VERIFY_OTP, WebServices.VerifyOtp(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void forgotPassword(ForgotPasswordRequestModel requestModel,
                               WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_FORGOT_PASSWORD, WebServices.ForgotPassword(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void checkUser(CheckUserRequestModel requestModel,
                          WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CHECK_USER, WebServices.CheckUser(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.addExtra(DATA, requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void socialLogin(SocialLoginRequestModel requestModel,
                            WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_SOCIAL_LOGIN, WebServices.SocialLogin(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void login(LoginRequestModel requestModel,
                      WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_LOGIN, WebServices.Login(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void logout(LogoutRequestModel requestModel,
                       WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_LOGOUT, WebServices.Logout(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public WebRequest searchSuggesName(TeamNameRequestModel requestModel, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_TEAM_NAME, WebServices.GetSuggestedTeamName(), WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        return webRequest;
    }

    public void getUpcomingMatches(String matchProgress,
                                   WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetUpcomingMatches(), matchProgress);
        WebRequest webRequest = new WebRequest(ID_UPCOMING_MATCHES, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getUpcomingSoccerMatches(String matchProgress,
                                   WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetSoccerMatch(), matchProgress);
        WebRequest webRequest = new WebRequest(ID_UPCOMING_SOCCER_MATCH, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getCustomerUpcomingMatches(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_UPCOMING_MATCHES, WebServices.GetCustomerUpcomingMatches(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getCustomerSoccerUpcomingMatches(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_UPCOMING_MATCHES, WebServices.GetCustomerUpcomingMatchesSoccer(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getCustomerLiveMatches(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_LIVE_MATCHES, WebServices.GetCustomerLiveMatches(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getCustomerSoccerLiveMatches(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_LIVE_MATCHES,
                WebServices.GetCustomerLiveMatchesSoccer(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getCustomerResultLiveMatches(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_RESULT_MATCHES,
                WebServices.GetCustomerResultMatchesSoccer(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getCustomerPastMatches(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_PAST_MATCHES, WebServices.GetCustomerPastMatches(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchContest(long id, String uniqueId,
                                  JSONObject jsonObject,
                                  WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchContests(), id, uniqueId);
        WebRequest webRequest = new WebRequest(ID_MATCH_CONTESTS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        if(jsonObject!=null){
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = null;
                try {
                    value = (String) jsonObject.get(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webRequest.addParam(key, value);
            }
        }
        webRequest.send(context, webServiceResponseListener);
    }

    public void getSoccerMatchContest(long id, String uniqueId,
                                JSONObject jsonObject,
                                WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetSoccerMatchContest(), id, uniqueId);
        WebRequest webRequest = new WebRequest(ID_SOCCER_MATCH_CONTEST, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        if(jsonObject!=null){
            Iterator<String> keysItr = jsonObject.keys();
            while (keysItr.hasNext()) {
                String key = keysItr.next();
                String value = null;
                try {
                    value = (String) jsonObject.get(key);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webRequest.addParam(key, value);
            }
        }
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchContest_withFilter(long id, String uniqueId, JSONObject jsonObject,
                                 WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchContests(), id, uniqueId);
        WebRequest webRequest = new WebRequest(ID_MATCH_CONTESTS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchContestDetail(long match_contest_id, String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchContestsDetail(), match_contest_id, match_unique_id);
        WebRequest webRequest = new WebRequest(ID_MATCH_CONTESTS_DETAIL, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getSoccerMatchContestDetail(long match_contest_id, String match_unique_id,
                                       WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchSoccerContestsDetail(), match_contest_id, match_unique_id);
        WebRequest webRequest = new WebRequest(ID_MATCH_SOCCER_CONTESTS_DETAIL, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }    public void getMatchContestPdf(long match_contest_id, String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchContestPdf(), match_contest_id, match_unique_id);
        WebRequest webRequest = new WebRequest(ID_CONTEST_PDF, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void getContestTeams(String match_unique_id, long match_contest_id, long page_no, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetContestTeams(), match_unique_id, match_contest_id, page_no);
        WebRequest webRequest = new WebRequest(ID_CONTEST_TEAMS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getSoccerContestTeams(String match_unique_id, long match_contest_id, long page_no,
                                 WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetSoccerContestTeams(), match_unique_id, match_contest_id, page_no);
        WebRequest webRequest = new WebRequest(ID_SOCCER_CONTEST_TEAMS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void getCustomerMatchContest(long id, String uniqueId, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomerMatchContests(), id, uniqueId);
        WebRequest webRequest = new WebRequest(ID_GET_CUSTOMER_MATCH_CONTEST, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getCustomerSoccerMatchContest(long id, String uniqueId,
                                         WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomerSoccerMatchContests(), id, uniqueId);
        WebRequest webRequest = new WebRequest(ID_GET_CUSTOMER_SOCCER_MATCH_CONTEST, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getContestWinnerBreakUp(long id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetContestWinnerBreakUp(), id);
        WebRequest webRequest = new WebRequest(ID_CONTEST_WINNER_BREAKUP, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getContestSoccerWinnerBreakUp(long id,
                                         WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetContestSoccerWinnerBreakUp(), id);
        WebRequest webRequest = new WebRequest(ID_CONTEST_SOCCER_WINNER_BREAKUP, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchPlayers(long id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchPlayers(), id);
        WebRequest webRequest = new WebRequest(ID_MATCH_PLAYERS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getSoccerMatchPlayer(long id,
                                    WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetSoccerMatchPlayer(), id);
        WebRequest webRequest = new WebRequest(ID_SOCCER_MATCH_PLAYERS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchPlayersStats(String match_uniqueid, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchPlayersStats(), match_uniqueid);
        WebRequest webRequest = new WebRequest(ID_MATCH_PLAYERS_STATS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

       public void createCustomerTeam(CreateTeamRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CREATE_CUSTOMER_TEAM, WebServices.CreateCustomerTeam(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }
    public void createSoccerCustomerTeam(CreateTeamRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CREATE_SOCCER_CUSTOMER_TEAM, WebServices.CreateCustomerSoccerTeam(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }


    public void getGetCustomerTeams(String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomerMatchTeams(), match_unique_id);
        WebRequest webRequest = new WebRequest(ID_GET_CUSTOMER_TEAMS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getGetSoccerCustomerTeams(String match_unique_id,
                                     WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomeSoccerrMatchTeams(), match_unique_id);
        WebRequest webRequest = new WebRequest(ID_GET_CUSTOMER_SOCCER_TEAMS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getGetCustomerTeamDetail(long customer_team_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomerMatchTeamDetail(), customer_team_id);
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_TEAM_DETAIL, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getGetCustomerSoccerTeamDetail(long customer_team_id,
                                          WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomerSoccerMatchTeamDetail(), customer_team_id);
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_TEAM_DETAIL, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getDreamTeamDetail(String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchDreamTeamDetail(), match_unique_id);
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_TEAM_DETAIL, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getGetCustomerTeamStats(long customer_team_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetCustomerMatchTeamStats(), customer_team_id);
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_TEAM_STATS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void getDreamTeamStats(String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchDreamTeamStats(), match_unique_id);
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_TEAM_STATS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void updateCustomerTeam(UpdateTeamRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_CUSTOMER_TEAM, WebServices.UpdateCustomerTeam(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }
    public void updateCustomerSoccerTeam(UpdateTeamRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_CUSTOMER_SOCCER_TEAM, WebServices.UpdateCustomerSoccerTeam(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getSeriesByPlayerStatistics(String match_unique_id, String player_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetSeriesByPlayerStatistics(), match_unique_id, player_unique_id);
        WebRequest webRequest = new WebRequest(ID_GET_PLAYER_STATS, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getAlreadyCreatedTeamCount(String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetAlreadyCreatedTeamCount(), match_unique_id);
        WebRequest webRequest = new WebRequest(ID_GET_ALREADY_CREATED_TEAM_COUNT, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getPrivateContestSettings(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_PRIVATE_CONTEST_SETTING, WebServices.GetPrivateContestSettings(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void getPrivateContestEntryFee(String contest_size, String prize_pool, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_PRIVATE_CONTEST_ENTRY_FEE, WebServices.GetPrivateContestEntryFee(),
                WebRequest.POST_REQ);
        webRequest.addParam("contest_size", contest_size);
        webRequest.addParam("prize_pool", prize_pool);
        webRequest.send(context, webServiceResponseListener);
    }


    public void getPrivateContestWinningBreakUp(String contest_size, String prize_pool, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_PRIVATE_CONTEST_WINNING_BREAKUP, WebServices.GetPrivateContestWinningBreakUp(),
                WebRequest.POST_REQ);
        webRequest.addParam("contest_size", contest_size);
        webRequest.addParam("prize_pool", prize_pool);
        webRequest.send(context, webServiceResponseListener);
    }

    public void createPrivateContest(CreatePrivateContestRequestModel requestModel, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CREATE_PRIVATE_CONTEST, WebServices.CreatePrivateContest(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchPrivateContestDetail(String slug,String match_unique_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchPrivateContestDetail(), slug,match_unique_id);
        WebRequest webRequest = new WebRequest(ID_GET_MATCH_PRIVATE_CONTEST_DETAIL, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void shareContest(String slug, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.ShareContest(), slug);
        WebRequest webRequest = new WebRequest(ID_SHARE_CONTEST, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void shareSoccerContest(String slug,
                                WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.ShareSoccerContest(), slug);
        WebRequest webRequest = new WebRequest(ID_SHARE_SOCCER_CONTEST, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void customerPreJoinContest(CustomerJoinContestRequestModel requestModel,
                                       WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_PRE_JOIN_CONTEST, WebServices.CustomerPreJoinContest(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }
    public void customerSoccerPreJoinContest(CustomerJoinContestRequestModel requestModel,
                                       WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_PRE_JOIN_CONTEST, WebServices.CustomerSoccerPreJoinContest(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void customerJoinContest(CustomerJoinContestRequestModel requestModel,
                                    WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_JOIN_CONTEST, WebServices.CustomerJoinContest(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }
    public void customerSoccerJoinContest(CustomerJoinContestRequestModel requestModel,
                                    WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_JOIN_CONTEST, WebServices.CustomerSoccerJoinContest(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void customerSwitchTeam(SwitchTeamRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SWITCH_TEAM, WebServices.CustomerSwitchTeam(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }
    public void customerSoccerSwitchTeam(SwitchTeamRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER_SOCCER_SWITCH_TEAM, WebServices.CustomerSoccerSwitchTeam(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getMatchScore(String match_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetMatchScore(), match_id);
        WebRequest webRequest = new WebRequest(ID_MATCH_SCORE, url, WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void getSoccerMatchScore(String match_id,
                               WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetSoccerMatchScore(), match_id);
        WebRequest webRequest = new WebRequest(ID_SOCCER_MATCH_SCORE, url, WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void updateProfile(UpdateProfileRequestModel requestModel,
                              WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_PROFILE, WebServices.UpdateProfile(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void addPanCard(UpdatePanRequestModel requestModel,
                           WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_ADD_PAN_CARD, WebServices.AddPancard(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void addBankDetail(UpdateBankRequestModel requestModel,
                              WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_ADD_BANK_DETAIL, WebServices.AddBankDetail(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void updateVerifyEmail(UpdateProfileRequestModel requestModel,
                                  WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_VERIFY_EMAIL, WebServices.UpdateVerifyEmail(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void sendOtpMobile(UpdateProfileRequestModel requestModel,
                              WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_SEND_OTP_MOBILE, WebServices.SendOtpMobile(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void updateVerifyMobile(VerifyOtpRequestModel requestModel,
                                   WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_VERIFY_MOBILE, WebServices.UpdateVerifyMobile(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getStates(String country_id, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetStates(), country_id);
        WebRequest webRequest = new WebRequest(ID_GET_STATES, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getProfilePictures(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_PROFILE_PICTURES, WebServices.GetProfilePictures(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void changeProfilePicture(UpdateProfileRequestModel requestModel,
                                     WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CHANGE_PROFILE_PICTURE, WebServices.ChangeProfilePictures(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void changePassword(UpdateProfileRequestModel requestModel,
                               WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CHANGE_PASSWORD, WebServices.ChangePassword(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }


    public void customerDepositAmount(DepositAmountRequestModel requestModel,
                                      WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_DEPOSIT_AMOUNT, WebServices.CustomerDepositAmount(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public WebRequest walletRecharge(DepositAmountRequestModel requestModel,
                                     WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_DEPOSIT_AMOUNT, WebServices.WalletRecharge(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
        return webRequest;
    }

    public WebRequest walletAddBalance(DepositAmountRequestModel requestModel,
                                     WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_DEPOSIT_AMOUNT,WebServices.wallet_recharge(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
        return webRequest;
    }



    public void customerWithdrawAmount(WithdrawAmountRequestModel requestModel,
                                       WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_WITHDRAW_AMOUNT, WebServices.CustomerWithdrawAmount(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void customerWalletDetail(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_WALLET_DETAIL, WebServices.CustomerWalletDetail(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void customerWalletHistory(long page_no,String transaction_type, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.CustomerWalletHistory(), page_no,transaction_type);
        WebRequest webRequest = new WebRequest(ID_WALLET_HISTORY, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void customerWithdrawHistory(long page_no, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.CustomerWithdrawHistory(), page_no);
        WebRequest webRequest = new WebRequest(ID_WITHDRAW_HISTORY, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void customerTeamNameUpdate(TeamNameUpdateRequestModel requestModel,
                                       WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_UPDATE_TEAM_NAME, WebServices.CustomerTeamNameUpdate(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getSilder(WebServiceResponseListener webServiceResponseListener) {

        WebRequest webRequest = new WebRequest(ID_SLIDER, WebServices.GetSlider(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getReferralSettings(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_REFERRAL_SETTINGS, WebServices.GetReferralSettings(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getProfile(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_GET_PROFILE, WebServices.GetProfile(),
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getReferEarn(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_REFER_EARN, WebServices.GetReferEarn(), WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getReferEarnDetail(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_REFER_EARN_DEATIL, WebServices.GetReferEarnDetail(), WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getNotifications(long page_no, WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.GetNotifications(), page_no);
        WebRequest webRequest = new WebRequest(ID_GET_NOTIFICATIONS, url, WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void getPlayingHistory(WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_PLAYING_HISTORY, WebServices.GetPlayingHistory(), WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }


    public void get_customers(CustomerRequestModel requestModel, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CUSTOMER, WebServices.getCustomers(), WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }


    public void applyPromoCode(PromoCodeRequestModel requestModel, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_APPLY_PROMO_CODE, WebServices.GetApplyPromoCode(), WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void createCustomerEnquiry(CustomerEnquiryRequestModel customerEnquiryRequestModel, WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CREATE_CUSTOMER_ENQUIRY, WebServices.CreateCustomerEnquiry(), WebRequest.POST_REQ);
        webRequest.setRequestModel(customerEnquiryRequestModel);
        webRequest.send(context, webServiceResponseListener);
    }


    public void getFivePlayerMatch(String matchProgress,
                                   WebServiceResponseListener webServiceResponseListener) {
        String url =String.format(WebServices.get_matches_5aside(), matchProgress);
        WebRequest webRequest = new WebRequest(ID_5_PLAYER_MATCH, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }
    public void createfivePlayerTeam(CreateTeamRequestModel requestModel,
                                     WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_CREATE_5_PLAYER_TEAM,
                WebServices.create_customer_team_five_players(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }

    public void getFiveMatchPlayers(long id,
                                    WebServiceResponseListener webServiceResponseListener) {
        String url = String.format(WebServices.get_match_players_5aside(), id);
        WebRequest webRequest = new WebRequest(ID_5_PLAYER_TEAM_LIST, url,
                WebRequest.POST_REQ);
        webRequest.addParam("hgh","fgj");
        webRequest.send(context, webServiceResponseListener);
    }

    public void customer_withdraw_amount(WithdrawAmountRequestModel requestModel,
                                       WebServiceResponseListener webServiceResponseListener) {
        WebRequest webRequest = new WebRequest(ID_WITHDRAW_AMOUNT, WebServices.customer_withdraw_amount(),
                WebRequest.POST_REQ);
        webRequest.setRequestModel(requestModel);
        webRequest.send(context, webServiceResponseListener);
    }
}

