package com.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.base.BaseApplication;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.CallbackManagerImpl;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

/**
 * Created by ubuntu on 12/5/16.
 */
public class FacebookLoginHandler {
    static CallbackManager callbackManager;

    static {
        callbackManager = CallbackManager.Factory.create();
    }

    Context context;
    SocialLoginListener socialLoginListner;
    FacebookCallback<LoginResult> loginResultFacebookCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            // App code
            handleFacebookLogin(loginResult);

        }

        @Override
        public void onCancel() {
            // App code
            printLog("login cancel by user");
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
            printLog("Error in login " + exception.getMessage());
            showToast("FacebookSignInAccount error in login.");

        }
    };

    public static int fbLoginRequestCode() {
        return CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode();
    }

    public FacebookLoginHandler(Context context) {
        this.context = context;
        generateKeyHash(context);
        facebookInit();
    }

    private void generateKeyHash(Context context) {
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                printLog("KeyHash = " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (PackageManager.NameNotFoundException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void setSocialLoginListner(SocialLoginListener socialLoginListner) {
        this.socialLoginListner = socialLoginListner;
    }


    private void facebookInit() {
        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(context.getApplicationContext(), new FacebookSdk.InitializeCallback() {
                @Override
                public void onInitialized() {
                    LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);
                }
            });
        } else {
            LoginManager.getInstance().registerCallback(callbackManager, loginResultFacebookCallback);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookLogin(LoginResult loginResult) {

        AccessToken accessToken = loginResult.getAccessToken();

        Set<String> d_premission = accessToken.getDeclinedPermissions();
        String[] d_prems = d_premission.toArray(new String[d_premission.size()]);
        if (TextUtils.join(",", d_prems).contains("email")) {
            if (socialLoginListner != null) {
                socialLoginListner.fbLoginPermissionError();
            }
        } else {
            GraphRequest.Callback graphCallback = new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    if (response.getError() != null) {
                        FacebookRequestError facebookRequestError = response.getError();
                        String msg = facebookRequestError.getErrorMessage();
                        showToast("Facebook Error\n" + msg);
                    } else {
                        JSONObject jsonObject = response.getJSONObject();
                        Log.d("json obj 1","--"+jsonObject.toString());
                        if (jsonObject != null) {
                            SocialData socialData = new SocialData();
                            socialData.setId(jsonObject.optString("id"));
                            socialData.setEmail(jsonObject.optString("email"));
                            socialData.setName(jsonObject.optString("name"));
                            socialData.setFirstName(jsonObject.optString("first_name"));
                            socialData.setLastName(jsonObject.optString("last_name"));
                            socialData.setLoginFrom("F");
                            printLog("SocialData== " + socialData.toString());
                            if (socialLoginListner != null) {
                                socialLoginListner.socialLoginSuccess(socialData);
                            }
                        } else {
                            String msg = "Something went wrong";
                            showToast("Facebook Error\n" + msg);
                        }
                    }
                }
            };
            GraphRequest graphRequest = getGraphMeRequestWithCache(accessToken.getToken());
            graphRequest.setCallback(graphCallback);
            graphRequest.executeAsync();
        }
    }

    public void callLoginWithRead(Activity activity, List<String> premission) {
        if (premission != null) {
            LoginManager.getInstance().logInWithReadPermissions(
                    activity, premission);
        }
    }

    public void callLoginWithRead(Fragment fragment, List<String> premission) {
        if (premission != null) {
            LoginManager.getInstance().logInWithReadPermissions(
                    fragment, premission);
        }
    }

    public void callLoginWithRead(android.app.Fragment fragment, List<String> premission) {
        if (premission != null) {
            LoginManager.getInstance().logInWithReadPermissions(
                    fragment, premission);
        }
    }

    public void callLogout() {
        LoginManager.getInstance().logOut();
    }


    private static GraphRequest getGraphMeRequestWithCache(
            final String accessToken) {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,first_name,middle_name,last_name,link,email");
        parameters.putString("access_token", accessToken);
        GraphRequest graphRequest = new GraphRequest(
                null,
                "me",
                parameters,
                HttpMethod.GET,
                null);
        return graphRequest;
    }

    private void printLog(String msg) {
        if (msg == null) return;
        if (BaseApplication.instance.isDebugBuild()) {
            Log.e(getClass().getSimpleName(), msg);
        }
    }

    private void showToast(String msg) {
        if (msg == null || msg.trim().isEmpty()) return;
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

}
