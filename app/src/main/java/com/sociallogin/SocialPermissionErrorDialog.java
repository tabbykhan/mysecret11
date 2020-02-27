package com.sociallogin;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.app.ui.dialogs.ConfirmationDialog;
import com.base.BaseFragment;

/**
 * @author Manish Kumar
 * @since 8/9/17
 */


public class SocialPermissionErrorDialog extends AppBaseDialogFragment {
    TextView tv_cancel;
    TextView tv_ok;
    DialogInterface.OnClickListener onClickListener;

    public static ConfirmationDialog getInstance(Bundle bundle) {
        ConfirmationDialog messageDialog = new ConfirmationDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_social_permission;
    }

    @Override
    public int getFragmentContainerResourceId(BaseFragment baseFragment) {
        return 0;
    }


    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.CENTER;
        wlmp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wlmp.dimAmount = 0.8f;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        tv_cancel = getView().findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(this);

        tv_ok = getView().findViewById(R.id.tv_ok);
        tv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (onClickListener != null) {
            onClickListener.onClick(this.getDialog(),
                    v.getId() == R.id.tv_ok ?
                            DialogInterface.BUTTON_POSITIVE : DialogInterface.BUTTON_NEGATIVE);
        } else {
            dismiss();
        }
    }


}
