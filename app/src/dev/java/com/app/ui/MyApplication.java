package com.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.BuildConfig;
import com.app.appbase.AppBaseApplication;
import com.app.appbase.AppBaseResponseModel;
import com.app.model.GameModel;
import com.app.model.MatchModel;
import com.app.model.webrequestmodel.UpdateDeviceTokenRequestModel;
import com.app.ui.checkuser.CheckUserActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.fcm.NotificationPrefs;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.sociallogin.FacebookLoginHandler;
import com.sociallogin.GplusLoginHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author Manish Kumar
 * @since 1/10/18
 */

public class MyApplication extends AppBaseApplication {

    FacebookLoginHandler facebookLoginHandler;
    GplusLoginHandler gplusLoginHandler;


    private final Object lock = new Object();
    private Handler handler;
    private Runnable runnable;
    private long serverDate=0;
    private List<MatchModel> upcomingMatches = new ArrayList<>();
    private List<MatchModel> liveMatches = new ArrayList<>();
    private List<MatchModel> pastMatches = new ArrayList<>();
    private List<MatchModel> myUpcomingMatches = new ArrayList<>();
    private List<MatchModel> myLiveMatches = new ArrayList<>();
    private List<MatchModel> myPastMatches = new ArrayList<>();
    private List<MatchTimerListener> matchTimerListeners = new ArrayList<>();

    private MatchModel selectedMatch;
    private MatchModel matchModelWithPlayers;
    private GameModel currentGame;


    public void setServerDate(long serverDate) {
        this.serverDate = serverDate;
    }

    public long getServerDate() {
        return serverDate;
    }

    public void setCurrentGame(GameModel currentGame) {
        this.currentGame = currentGame;
        if(getUserPrefs()!=null){
            getUserPrefs().saveSelectedGame(currentGame.getId());
        }
    }

    public GameModel getCurrentGame() {
        return currentGame;
    }

    public long getPreviousSelectedGameId(){
        if(getUserPrefs()!=null){
            return getUserPrefs().getSelectedGameId();
        }
        return -1;
    }

    public MatchModel getSelectedMatch() {
        return selectedMatch;
    }

    public void setSelectedMatch(MatchModel selectedMatch) {
        this.selectedMatch = selectedMatch;
    }

    public MatchModel getMatchModelWithPlayers() {
        return matchModelWithPlayers;
    }

    public void setMatchModelWithPlayers(MatchModel matchModelWithPlayers) {
        this.matchModelWithPlayers = matchModelWithPlayers;
    }

    public static MyApplication getInstance() {
        return (MyApplication) instance;
    }

    public FacebookLoginHandler getFacebookLoginHandler() {
        return facebookLoginHandler;
    }

    public GplusLoginHandler getGplusLoginHandler() {
        return gplusLoginHandler;
    }


    public void unAuthorizedResponse(AppBaseResponseModel modelResponse) {
        getUserPrefs().clearLoggedInUser();
        moveToLoginActivity(modelResponse);
    }

    public void moveToLoginActivity(AppBaseResponseModel modelResponse) {
        Intent intent = new Intent(getApplicationContext(), CheckUserActivity.class);
        if (modelResponse != null) {
            Bundle bundle = new Bundle();
            bundle.putString("unAuth", new Gson().toJson(modelResponse));
            intent.putExtras(bundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) {
            AppBaseResponseModel appBaseResponseModel = null;
            try {
                appBaseResponseModel = webRequest.getResponsePojo(AppBaseResponseModel.class);
            } catch (Exception ignore) {

            }
            unAuthorizedResponse(appBaseResponseModel);
        }
    }

    public void updateDeviceTokenToServer() {
        String notificationToken = NotificationPrefs.getInstance(this).getNotificationToken();
        if (notificationToken != null && notificationToken.trim().length() > 0) {
            UpdateDeviceTokenRequestModel requestModel = new UpdateDeviceTokenRequestModel();
            requestModel.device_token = notificationToken;
            getWebRequestHelper().updateDeviceToken(requestModel, this);
        }
    }

    public void showOtp(String otp) {
        if (otp != null && otp.trim().length() > 0) {
            if (BuildConfig.APPLICATION_ID.equals("com.imuons")) {
                Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        setupTimer();
        Fresco.initialize(this);
        facebookLoginHandler = new FacebookLoginHandler(this);
        gplusLoginHandler = new GplusLoginHandler(this);

        new AppCustomIconsHelper().start();

    }

    public Object getLock() {
        return lock;
    }

    public List<MatchModel> getUpcomingMatches() {
        return upcomingMatches;
    }

    public List<MatchModel> getLiveMatches() {
        return liveMatches;
    }

    public List<MatchModel> getPastMatches() {
        return pastMatches;
    }


    public List<MatchModel> getMyUpcomingMatches() {
        return myUpcomingMatches;
    }

    public List<MatchModel> getMyLiveMatches() {
        return myLiveMatches;
    }

    public List<MatchModel> getMyPastMatches() {
        return myPastMatches;
    }

    public void addMatchTimerListener(MatchTimerListener matchTimerListener) {
        if (!this.matchTimerListeners.contains(matchTimerListener)) {
            this.matchTimerListeners.add(matchTimerListener);
        }
    }

    public void removeMatchTimerListener(MatchTimerListener matchTimerListener) {
        if (this.matchTimerListeners.contains(matchTimerListener)) {
            this.matchTimerListeners.remove(matchTimerListener);
        }
    }

    private void setupTimer() {
        matchTimerListeners.clear();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    serverDate++;
                    if (matchTimerListeners != null && matchTimerListeners.size() > 0) {
                        for (MatchTimerListener matchTimerListener : matchTimerListeners) {
                            matchTimerListener.onMatchTimeUpdate();
                        }
                    }
                    handler.postDelayed(runnable, 1000);
                }
            }
        };
    }

    public void startTimer() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    public void stopTimer() {
        handler.removeCallbacks(runnable);
    }

    public synchronized static long convertToDateOnlyTime(long timestamp) {

        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(timestamp);
        d.set(Calendar.HOUR_OF_DAY, 0);
        d.set(Calendar.MINUTE, 0);
        d.set(Calendar.SECOND, 0);
        d.set(Calendar.MILLISECOND, 0);
        return d.getTimeInMillis();
    }
}
