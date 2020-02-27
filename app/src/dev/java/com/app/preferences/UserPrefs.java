package com.app.preferences;

import android.content.Context;

import com.app.model.CustomIconModel;
import com.app.model.PlayingHistoryModel;
import com.app.model.QuotationDontShowModel;
import com.app.model.ReferEarnModel;
import com.app.model.UserModel;
import com.app.ui.MyApplication;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.preferences.BasePrefs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Manish Kumar
 * @since 31/8/17
 */


public class UserPrefs extends BasePrefs {

    static final String Prefsname = "prefs_user";

    static String KEY_LOGGEDIN_USER = "logged_in_user";
    static String KEY_REFER_EARN = "refer_earn";
    static String KEY_PLAYING_HISTORY = "playing_history";
    static String KEY_CUSTOM_ICONS = "custom_ions";
    static String KEY_SELECTED_GAME = "selected_game_id";
    static String KEY_QUOTATION_DONT_SHOW = "quotation_dont_show";

    Context context;

    List<UserPrefsListener> userPrefsListenerList = new ArrayList<>();

    public UserPrefs(Context context) {
        this.context = context;
    }


    public void addListener(UserPrefsListener userPrefsListener) {
        userPrefsListenerList.add(userPrefsListener);
    }

    public void removeListener(UserPrefsListener userPrefsListener) {
        userPrefsListenerList.remove(userPrefsListener);
    }

    public void clearListeners() {
        userPrefsListenerList.clear();
    }

    @Override
    public String getPrefsName() {
        return Prefsname;
    }

    @Override
    public Context getContext() {
        return context;
    }

    public UserModel getLoggedInUser() {
        UserModel userModel = null;
        try {
            String userDetail = getStringKeyValuePrefs(KEY_LOGGEDIN_USER);
            if (isValidString(userDetail)) {
                userModel = new Gson().fromJson(userDetail, UserModel.class);
            }
        } catch (JsonSyntaxException e) {

        }
        return userModel;
    }

    public ReferEarnModel getReferEarnModel() {
        ReferEarnModel referEarnModel = null;
        try {
            String data = getStringKeyValuePrefs(KEY_REFER_EARN);
            if (isValidString(data)) {
                referEarnModel = new Gson().fromJson(data, ReferEarnModel.class);
            }
        } catch (JsonSyntaxException e) {

        }
        return referEarnModel;
    }

    public PlayingHistoryModel getPlayingHistoryModel() {
        PlayingHistoryModel playingHistoryModel = null;
        try {
            String data = getStringKeyValuePrefs(KEY_PLAYING_HISTORY);
            if (isValidString(data)) {
                playingHistoryModel = new Gson().fromJson(data, PlayingHistoryModel.class);
            }
        } catch (JsonSyntaxException e) {

        }
        return playingHistoryModel;
    }

    public CustomIconModel getAppCustomIcons() {
        CustomIconModel customIconModel = null;
        try {
            String data = getStringKeyValuePrefs(KEY_CUSTOM_ICONS);
            if (isValidString(data)) {
                customIconModel = new Gson().fromJson(data, CustomIconModel.class);
            }
        } catch (JsonSyntaxException e) {

        }
        return customIconModel;
    }

    public long getSelectedGameId() {
        return getLongKeyValuePrefs(KEY_SELECTED_GAME,-1);
    }

    public QuotationDontShowModel getQuotationDoNot(){
        QuotationDontShowModel quotationDontShowModel = new QuotationDontShowModel();
        try {
            String data = getStringKeyValuePrefs(KEY_QUOTATION_DONT_SHOW);
            if (isValidString(data)) {
                quotationDontShowModel = new Gson().fromJson(data, QuotationDontShowModel.class);
            }
        } catch (JsonSyntaxException e) {

        }
        return quotationDontShowModel;

    }

    public void saveLoggedInUser(UserModel userModel) {
        if (userModel == null) return;
        String userDetail = new Gson().toJson(userModel);
        if (setStringKeyValuePrefs(KEY_LOGGEDIN_USER, userDetail)) {
            triggerUserLoggedIn(userModel);
        }
    }

    public void saveReferEarn(ReferEarnModel referEarnModel) {
        if (referEarnModel == null) return;
        String data = new Gson().toJson(referEarnModel);
        if (setStringKeyValuePrefs(KEY_REFER_EARN, data)) {

        }
    }

    public void savePlayingHistory(PlayingHistoryModel playingHistoryModel) {
        if (playingHistoryModel == null) return;
        String data = new Gson().toJson(playingHistoryModel);
        if (setStringKeyValuePrefs(KEY_PLAYING_HISTORY, data)) {

        }
    }

    public void saveAppCustomIcons(CustomIconModel customIconModel) {
        if (customIconModel == null) return;
        String data = new Gson().toJson(customIconModel);
        if (setStringKeyValuePrefs(KEY_CUSTOM_ICONS, data)) {
            triggerCustomIcon(customIconModel);
        }
    }


    public void saveQuotationDonotShow(QuotationDontShowModel quotationDontShowModel) {
        if (quotationDontShowModel == null) return;
        String data = new Gson().toJson(quotationDontShowModel);
        if (setStringKeyValuePrefs(KEY_QUOTATION_DONT_SHOW, data)) {

        }
    }

    public void saveSelectedGame(long gameId) {
        if (setLongKeyValuePrefs(KEY_SELECTED_GAME, gameId)) {
        }
    }

    private void triggerCustomIcon(CustomIconModel customIconModel) {
        for (UserPrefsListener userPrefsListener : userPrefsListenerList) {
            userPrefsListener.customIconUpdate(customIconModel);
        }
    }


    private void triggerUserLoggedIn(UserModel userModel) {
        for (UserPrefsListener userPrefsListener : userPrefsListenerList) {
            userPrefsListener.userLoggedIn(userModel);
        }
    }

    public void updateLoggedInUser(UserModel userModel) {
        if (userModel == null) return;
        String userdetail = new Gson().toJson(userModel);
        if (setStringKeyValuePrefs(KEY_LOGGEDIN_USER, userdetail)) {
            triggerLoggedInUserUpdate(userModel);
        }
    }

    private void triggerLoggedInUserUpdate(UserModel userModel) {
        for (UserPrefsListener userPrefsListener : userPrefsListenerList) {
            userPrefsListener.loggedInUserUpdate(userModel);
        }
    }


    public void clearLoggedInUser() {
        QuotationDontShowModel quotationDontShowModel = MyApplication.getInstance().getQuotationDontShowModel();
        setStringKeyValuePrefs(KEY_QUOTATION_DONT_SHOW,"");
        quotationDontShowModel.addGameRemove();

        if (setStringKeyValuePrefs(KEY_LOGGEDIN_USER, "")) {
            triggerLoggedInUserClear();
        }
    }

    private void triggerLoggedInUserClear() {
        for (UserPrefsListener userPrefsListener : userPrefsListenerList) {
            userPrefsListener.loggedInUserClear();
        }
    }


    public interface UserPrefsListener {

        void userLoggedIn(UserModel userModel);

        void loggedInUserUpdate(UserModel userModel);

        void customIconUpdate(CustomIconModel customIconModel);

        void loggedInUserClear();
    }
}
