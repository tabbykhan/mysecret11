package com.app.ui.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.base.BaseFragment;
import com.utilities.DeviceScreenUtil;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class ConfirmationLocationDialog extends AppBaseDialogFragment {

    TextView tv_message;
    TextView tv_cancel;
    TextView tv_ok;
    DialogInterface.OnClickListener onClickListener;

    public static ConfirmationLocationDialog getInstance(Bundle bundle) {
        ConfirmationLocationDialog messageDialog = new ConfirmationLocationDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_confirmation_location;
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
        wlmp.width = DeviceScreenUtil.getInstance().getWidth(0.90f);
        wlmp.dimAmount = 0.8f;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

    private String getMessage() {
        String message = null;
        if (getArguments() != null) {
            message = getArguments().getString(MESSAGE);
        }
        return message == null ? "" : message;
    }

    private String getPositiveBtnText() {
        String message = null;
        if (getArguments() != null) {
            message = getArguments().getString(POS_BTN);
        }
        return message == null ? "" : message;
    }


    private String getNegativeBtnText() {
        String message = null;
        if (getArguments() != null) {
            message = getArguments().getString(NEG_BTN);
        }
        return message == null ? "" : message;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        tv_message = getView().findViewById(R.id.tv_message);

        tv_message.setText(Html.fromHtml(getMessage()));


        tv_cancel = getView().findViewById(R.id.tv_cancel);
        String negativeBtnText = getNegativeBtnText();

        tv_cancel.setText(negativeBtnText);
        tv_cancel.setOnClickListener(this);

        tv_ok = getView().findViewById(R.id.tv_ok);
        String positiveBtnText = getPositiveBtnText();
        tv_ok.setText(positiveBtnText);
        tv_ok.setOnClickListener(this);


        if(!isValidString(negativeBtnText)){
            updateViewVisibitity(tv_cancel,View.GONE);

            setCancelable(false);
            getDialog().setCanceledOnTouchOutside(false);
        }

        if(!isValidString(positiveBtnText)){
            updateViewVisibitity(tv_ok,View.GONE);
        }
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
