package com.app.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.R;
import com.app.ui.MyApplication;
import com.app.ui.splash.SplashActivity;
import com.base.BaseApplication;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fcm.NotificationMessagingService;
import com.fcm.NotificationPrefs;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.RemoteMessage;
import com.utilities.DeviceScreenUtil;

import java.util.concurrent.ExecutionException;


public class AppNotificationMessagingService extends NotificationMessagingService<AppNotificationModel>
        implements AppNotificationType {

    public static final String KEY_NOTIFICATION_TITLE = "title";
    public static final String KEY_NOTIFICATION_MESSAGE = "message";
    public static final String KEY_NOTIFICATION_TYPE = "noti_type";
    public static final String KEY_NOTIFICATION_TIME = "noti_time";
    public static final String KEY_JSON_DATA = "json_data";
    public static final String KEY_IMAGE_LARGE = "noti_large";
    public static final String KEY_IMAGE_SMALL = "noti_thumb";

    public static void generateLatestToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                if (instanceIdResult != null) {
                    NotificationPrefs.getInstance(BaseApplication.instance).saveNotificationToken(instanceIdResult.getToken());
                    if (BaseApplication.instance.isDebugBuild()) {
                        Log.e(NotificationMessagingService.class.getSimpleName(),
                                "generateLatestToken : " + instanceIdResult.getToken());
                    }
                    MyApplication.getInstance().updateDeviceTokenToServer();
                }
            }
        });
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (context == null) return;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            String name = context.getString(R.string.app_name);
            createNotificationChannel(context, name, name, importance, name);
        }

    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                MyApplication.getInstance().updateDeviceTokenToServer();
            }
        });

    }

    @Override
    public AppNotificationModel getNotificationModal(RemoteMessage remoteMessage) {
        return new AppNotificationModel(remoteMessage);
    }

    @Override
    public void pushMessageReceived(AppNotificationModel notificationModal) {
        if (notificationModal != null) {
            // if (isAppIsInBackground()) {
            generatePushNotification(notificationModal);
//            } else {
//                if (AppBaseApplication.instance != null) {
//                    AppBaseApplication.instance.getPushNotificationHelper().
//                            triggerNotificationListener(notificationModal);
//                }
//            }
        }
    }


    @Override
    public void generatePushNotification(AppNotificationModel notificationModal) {
        if (notificationModal.isAdminAlert()) {
            String imgUrl = notificationModal.getImageLarge();
            if (imgUrl != null && imgUrl.trim().length() > 0) {
                new GeneratePictureNotification(notificationModal).execute();
                return;
            }
        }


        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        long notificationId = System.currentTimeMillis();
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layout_text_notification_small);


        notificationLayout.setTextViewText(R.id.tv_title, notificationModal.getTitle());
        notificationLayout.setTextViewText(R.id.tv_message, notificationModal.getMessage());


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        notificationBuilder.setCustomContentView(notificationLayout)
                .setShowWhen(false);

        notificationBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        notificationManager.notify((int) notificationId, notification);

    }

    public void generatePictureNotification(AppNotificationModel notificationModal, Bitmap bitmap) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);


        long notificationId = 0;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.layout_image_notification_small);
        RemoteViews notificationLayoutBig = new RemoteViews(getPackageName(), R.layout.layout_image_notification_big);

        int minHeight = Math.round(DeviceScreenUtil.getInstance().getWidth() * 0.416f);
        notificationLayoutBig.setInt(R.id.iv_image, "setMinimumHeight", minHeight);


        notificationLayout.setTextViewText(R.id.tv_title, notificationModal.getTitle());
        notificationLayoutBig.setTextViewText(R.id.tv_title, notificationModal.getTitle());

        notificationLayout.setTextViewText(R.id.tv_message, notificationModal.getMessage());
        notificationLayoutBig.setTextViewText(R.id.tv_message, notificationModal.getMessage());

        if (bitmap != null) {
            notificationLayout.setImageViewBitmap(R.id.iv_image, bitmap);
            notificationLayoutBig.setImageViewBitmap(R.id.iv_image, bitmap);
            notificationLayout.setViewVisibility(R.id.ll_image_layout, View.VISIBLE);
        } else {
            notificationLayout.setViewVisibility(R.id.ll_image_layout, View.GONE);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                getString(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);

        notificationBuilder.setCustomContentView(notificationLayout)
                .setCustomBigContentView(notificationLayoutBig)
                .setShowWhen(false);


        notificationBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        notificationManager.notify((int) notificationId, notification);
    }

    public class GeneratePictureNotification extends AsyncTask<Void, Void, Bitmap> {
        AppNotificationModel appNotificationModel;

        public GeneratePictureNotification(AppNotificationModel appNotificationModel) {
            this.appNotificationModel = appNotificationModel;
        }


        @Override
        protected Bitmap doInBackground(Void... voids) {
            String imageUrl = appNotificationModel.getImageLarge();
            if (imageUrl != null && imageUrl.trim().length() > 0) {
                RequestManager requestManager = Glide.with(MyApplication.getInstance());
                RequestOptions options = new RequestOptions()
                        .override(400)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .priority(Priority.HIGH);

                try {
                    return requestManager.asBitmap().load(imageUrl).
                            apply(options).submit().get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            generatePictureNotification(appNotificationModel, bitmap);
        }
    }


    public static void cancelAllNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) return;
        notificationManager.cancelAll();
    }


}
