package com.app.appupdater;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.R;
import com.app.model.AppVersionModel;


/**
 * Created by ubuntu on 29/5/17.
 */

public class DialogAppUpdater extends Dialog implements View.OnClickListener

{

    private Context context;
    private TextView tv_update, tv_later;
    private TextView tv_message_data;
    private TextView tv_title;
    private View view_seperate;
    private AppVersionModel appVersionModel;

    public DialogAppUpdater(Context context, AppVersionModel appVersionModel) {
        super(context);
        this.context = context;
        this.appVersionModel = appVersionModel;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflate.inflate(R.layout.dialog_app_updater, null);
        setContentView(layout);

        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams wlmp = getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;

        setTitle(null);
        if (appVersionModel.isForceUpdate()) {
            setCancelable(false);
            setCanceledOnTouchOutside(false);
        } else {
            setCancelable(true);
            setCanceledOnTouchOutside(true);
        }

        setOnCancelListener(null);
        setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                tv_message_data.setText(Html.fromHtml(appVersionModel.getVersion_desc()));
                tv_title.setText(appVersionModel.getTitle());
            }
        });


        tv_update = findViewById(R.id.tv_update);
        tv_later = findViewById(R.id.tv_later);
        tv_title = findViewById(R.id.tv_title);
        view_seperate = findViewById(R.id.view_seperate);
        tv_message_data = findViewById(R.id.tv_message_data);
        tv_update.setOnClickListener(this);
        tv_later.setOnClickListener(this);

        if (appVersionModel.isForceUpdate()) {
            tv_later.setVisibility(View.GONE);
            view_seperate.setVisibility(View.GONE);
        } else {
            tv_later.setVisibility(View.VISIBLE);
            view_seperate.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_later) {
            this.dismiss();
        } else if (view.getId() == R.id.tv_update) {
            openBrowser(appVersionModel.getApp_link());
            this.dismiss();
        }

    }

    private void openBrowser(String url) {
        try {
            final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }
}
