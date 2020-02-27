package com.app.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.fcm.AppNotificationMessagingService;
import com.app.preferences.UserPrefs;
import com.app.ui.checkuser.CheckUserActivity;
import com.app.ui.main.dashboard.DashboardActivityNew;
import com.permissions.PermissionHelperNew;
import com.utilities.DeviceScreenUtil;

import static com.app.appbase.AppBaseApplication.PLAY_SERVICES_RESOLUTION_REQUEST;

/**
 * @author Manish Kumar
 * @since 29/9/18
 */

public class SplashActivity extends AppBaseActivity implements PermissionHelperNew.OnSpecificPermissionGranted {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private static final long SPLASH_TIME_MS = 2000;
    private Handler handler = new Handler();

    private boolean callOnResume = true;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            UserPrefs userPrefs = getUserPrefs();
            if (userPrefs.getLoggedInUser() == null) {
                goToCheckUserActivity(getIntent().getExtras());
                return;
            }
            goToDashboardActivity(getIntent().getExtras());
        }
    };


    ImageView iv_logo;
    ImageView iv_bottom;


    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_splash_screen;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        iv_logo = findViewById(R.id.iv_logo);
        iv_bottom = findViewById(R.id.iv_bottom);

        int width = DeviceScreenUtil.getInstance().getWidth();
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_bottom.getLayoutParams();
        layoutParams.height=Math.round(width*0.335f);
        iv_bottom.setLayoutParams(layoutParams);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppNotificationMessagingService.generateLatestToken();
        if (callOnResume) {
            goToForward();
        } else {
            callOnResume = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissions.length > 0) {
            callOnResume = false;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelperNew.onSpecificRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGranted(boolean isGranted, boolean withNeverAsk, String permission, int requestCode) {
        if (requestCode == PermissionHelperNew.STORAGE_PERMISSION_REQUEST_CODE) {
            if (isGranted) {
                goToForward();
            } else {
//                if (withNeverAsk) {
//                    PermissionHelperNew.showNeverAskAlert(this, true, requestCode);
//                } else {
//                    PermissionHelperNew.showSpecificDenyAlert(this, permission, requestCode, true);
//                }
                supportFinishAfterTransition();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST) {
            if (resultCode == RESULT_OK) {
                goToForward();
            } else {
                showCustomToast("Please update Google play services.");
                finish();
            }
        }
    }


    private void goToForward() {
        if (isPlayServiceUpdated()) {
            if (PermissionHelperNew.needStoragePermission(this, this)) return;

            handler.postDelayed(runnable, SPLASH_TIME_MS);

        } else {
            if (!isPlayServiceErrorUserResolvable()) {
                showCustomToast("This device is not supported.");
                finish();
            }
        }

    }

    private void goToCheckUserActivity(Bundle bundle) {
        Intent intent = new Intent(this, CheckUserActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent, null);
        supportFinishAfterTransition();
    }

    private void goToDashboardActivity(Bundle bundle) {
        Intent intent = new Intent(this, DashboardActivityNew.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        supportFinishAfterTransition();
    }
}
