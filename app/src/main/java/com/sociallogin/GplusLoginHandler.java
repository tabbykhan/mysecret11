package com.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


/**
 * Created by ubuntu on 12/5/16.
 */
public class GplusLoginHandler {

    public static final int RC_SIGN_IN = 1004;

    GoogleSignInClient mGoogleSignInClient;

    SocialLoginListener socialLoginListner;

    Context context;


    public GplusLoginHandler(Context context) {
        this.context = context;
        gPlusInit();
    }


    public void setSocialLoginListner(SocialLoginListener socialLoginListner) {
        this.socialLoginListner = socialLoginListner;
    }


    private void gPlusInit() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

    }

    public void destroy() {

    }

    public void gPlusSignIn(Context context) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        ((Activity) context).startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void gPlusSignIn(Fragment fragment) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void gPlusSignIn(android.app.Fragment fragment) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void callLogout() {
        if (mGoogleSignInClient != null)
            mGoogleSignInClient.signOut();
    }


    public void onActivityResult(Intent data) {
        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
        handleSignInResult(task);
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                SocialData socialData = new SocialData();
                socialData.setEmail(account.getEmail());
                socialData.setFirstName(account.getGivenName());
                socialData.setLastName(account.getFamilyName());
                socialData.setId(account.getId());
                socialData.setLoginFrom("G");
                printLog("SocialData== " + socialData.toString());
                if (socialLoginListner != null) {
                    socialLoginListner.socialLoginSuccess(socialData);
                }
            } else {
                int statusCode = 200;
                String message = "Something went wrong";
                showToast("GoogleSignInAccount error" + "\n" + statusCode + " - " + message);
            }
        } catch (ApiException e) {
            int statusCode = e.getStatusCode();
            String message = e.getMessage();
            if (message == null) {
                message = "Something went wrong";
            }
            showToast("GoogleSignInAccount error" + "\n" + statusCode + " - " + message);
        }
    }

    private void printLog(String msg) {
        if (msg == null) return;
        Log.e(getClass().getSimpleName(), msg);
    }

    private void showToast(String msg) {
        if (msg == null || msg.trim().isEmpty()) return;
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
