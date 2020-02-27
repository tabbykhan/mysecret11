package com.app.ui.dialogs.quotation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseDialogFragment;
import com.app.model.QuotationModel;
import com.base.BaseFragment;
import com.utilities.DeviceScreenUtil;


/**
 * @author Manish Kumar
 * @since 20/10/18
 */

public class QuotationDialog extends AppBaseDialogFragment {

    QuotationDialogListener quotationDialogListener;

    public void setQuotationDialogListener(QuotationDialogListener quotationDialogListener) {
        this.quotationDialogListener = quotationDialogListener;
    }

    public QuotationDialogListener getQuotationDialogListener() {
        return quotationDialogListener;
    }

    ImageView iv_quotation;
    ImageView iv_close;
    TextView tv_dont_show;
    ProgressBar pb_quotation;
    DialogInterface.OnClickListener onClickListener;

    QuotationModel quotationModel;

    public void setQuotationModel(QuotationModel quotationModel) {
        this.quotationModel = quotationModel;
    }

    public static QuotationDialog getInstance(Bundle bundle) {
        QuotationDialog messageDialog = new QuotationDialog();
        messageDialog.setArguments(bundle);
        return messageDialog;
    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.dialog_quotation;
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
        wlmp.height = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlmp.dimAmount = 0.7f;
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();
        tv_dont_show = getView().findViewById(R.id.tv_dont_show);
        iv_quotation = getView().findViewById(R.id.iv_quotation);
        iv_close = getView().findViewById(R.id.iv_close);
        pb_quotation = getView().findViewById(R.id.pb_quotation);
        updateViewVisibitity(pb_quotation, View.GONE);
        iv_close.setOnClickListener(this);
        tv_dont_show.setOnClickListener(this);
        iv_quotation.setOnClickListener(this);
        setupImage();
    }

    private void setupImage() {
        if (quotationModel == null) return;
        int deviceWidth = DeviceScreenUtil.getInstance().getWidth(0.90f);
        int deviceHeight = DeviceScreenUtil.getInstance().getHeight(0.60f);

        int imageWidth = deviceWidth;
        int imageHeight = deviceHeight;

        long width = quotationModel.getWidth();
        long height = quotationModel.getHeight();

        if (width > height) {
            float ratio = ((float) height) / ((float) width);

            imageHeight = Math.round(imageWidth * ratio);

        } else if (height > width) {
            float ratio = ((float) width) / ((float) height);

            imageWidth = Math.round(imageHeight * ratio);
        } else {
            imageHeight = imageWidth;
        }

        ViewGroup.LayoutParams layoutParams = iv_quotation.getLayoutParams();
        layoutParams.width = imageWidth;
        layoutParams.height = imageHeight;
        iv_quotation.setLayoutParams(layoutParams);
        if (getActivity() == null) return;
        ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_quotation, pb_quotation, quotationModel.getImage(),
                0, -1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                dismiss();
                break;

            case R.id.tv_dont_show:
                if (getQuotationDialogListener() != null) {
                    getQuotationDialogListener().onDontShowClick();
                }
                break;
            case R.id.iv_quotation:
                if (isValidString(quotationModel.getLink())) {
                    Intent i2 = new Intent(Intent.ACTION_VIEW, Uri.parse(quotationModel.getLink()));
                    startActivity(i2);
                }
                break;
        }
    }

    public interface QuotationDialogListener {
        void onDontShowClick();
    }
}
