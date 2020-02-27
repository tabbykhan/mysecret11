package com.rest;



public final class WebServices {
    static {
        System.loadLibrary("rest");
    }


    public static native String UpdateDeviceToken();

    public static native String CheckAppVersion();

    public static native String GetAppCustomIcons();

    public static native String GetQuotations();

    public static native String GetGames();

    public static native String NewUser();

    public static native String VerifyOtp();

    public static native String ForgotPassword();

    public static native String CheckUser();

    public static native String SocialLogin();

    public static native String Login();

    public static native String Logout();
    public static native String GetSuggestedTeamName();

    public static native String GetUpcomingMatches();
    public static native String GetCustomerUpcomingMatches();
    public static native String GetCustomerLiveMatches();
    public static native String GetCustomerPastMatches();
    public static native String GetMatchPlayers();
    public static native String GetMatchPlayersStats();
    public static native String GetMatchContests();
    public static native String GetMatchContestsDetail();
    public static native String GetMatchContestPdf();
    public static native String GetContestTeams();
    public static native String GetCustomerMatchContests();
    public static native String GetContestWinnerBreakUp();
    public static native String CreateCustomerTeam();
    public static native String GetCustomerMatchTeams();
    public static native String GetCustomerMatchTeamDetail();
    public static native String GetCustomerMatchTeamStats();
    public static native String GetMatchDreamTeamDetail();
    public static native String GetMatchDreamTeamStats();
    public static native String UpdateCustomerTeam();
    public static native String GetSeriesByPlayerStatistics();

    public static native String GetAlreadyCreatedTeamCount();
    public static native String GetPrivateContestSettings();
    public static native String GetPrivateContestEntryFee();
    public static native String GetPrivateContestWinningBreakUp();
    public static native String CreatePrivateContest();
    public static native String GetMatchPrivateContestDetail();
    public static native String ShareContest();

    public static native String CustomerPreJoinContest();
    public static native String CustomerJoinContest();
    public static native String CustomerSwitchTeam();
    public static native String GetMatchScore();


    public static native String UpdateProfile();

    public static native String UpdateVerifyEmail();

    public static native String SendOtpMobile();

    public static native String UpdateVerifyMobile();

    public static native String GetStates();

    public static native String GetProfilePictures();

    public static native String ChangeProfilePictures();

    public static native String ChangePassword();

    public static native String CustomerDepositAmount();

    public static native String WalletRecharge();

    public static native String CustomerWithdrawAmount();

    public static native String CustomerWalletHistory();

    public static native String CustomerWithdrawHistory();

    public static native String CustomerWalletDetail();

    public static native String CustomerTeamNameUpdate();

    public static native String GetSlider();

    public static native String GetReferralSettings();

    public static native String AddPancard();

    public static native String AddBankDetail();

    public static native String GetProfile();

    public static native String GetTnc();

    public static native String GetPrivacyPolicy();

    public static native String GetStaggeredCashbonus();

    public static native String GetFantasyPoints();

    public static native String GetHowToPlay();

    public static native String GetHelpDesk();

    public static native String GetAboutUs();

    public static native String GetLegality();

    public static native String GetWithdrawPolicy();

    public static native String GetContact();

    public static native String GetFaq();

    public static native String ReferHowItWorks();

    public static native String ReferFairPlay();

    public static native String GetReferEarn();

    public static native String GetReferEarnDetail();

    public static native String GetNotifications();

    public static native String GetPlayingHistory();

    public static native String GetCustomerProfile();

    public static native String getCustomers();

    public static native String GetApplyPromoCode();


    public static native String CreateCustomerEnquiry();


}
