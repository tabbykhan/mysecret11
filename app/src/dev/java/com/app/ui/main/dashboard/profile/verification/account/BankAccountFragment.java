package com.app.ui.main.dashboard.profile.verification.account;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.AwsModel;
import com.app.model.BankDetailModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.UpdateBankRequestModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.awss3.S3BucketHelper;
import com.imagePicker.FileInformation;
import com.imagePicker.ProfilePicDialog;
import com.medy.retrofitwrapper.WebRequest;

public class BankAccountFragment extends AppBaseFragment {


    CardView cv_data_pending;
    CardView cv_bank_detail;
    TextView tv_verify_title;
    ImageView iv_bank_proof;
    TextView tv_upload_account_proof;
    TextView tv_reason;
    EditText et_bank_account_number;
    EditText et_re_bank_account_number;
    EditText et_bank_ac_name;
    EditText et_ifsc;
    TextView tv_proceed;


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_bank_account;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        cv_data_pending = getView().findViewById(R.id.cv_data_pending);
        cv_bank_detail = getView().findViewById(R.id.cv_bank_detail);
        tv_verify_title = getView().findViewById(R.id.tv_verify_title);
        iv_bank_proof = getView().findViewById(R.id.iv_bank_proof);
        tv_upload_account_proof = getView().findViewById(R.id.tv_upload_account_proof);
        tv_reason = getView().findViewById(R.id.tv_reason);
        et_bank_account_number = getView().findViewById(R.id.et_bank_account_number);
        et_re_bank_account_number = getView().findViewById(R.id.et_re_bank_account_number);
        et_bank_ac_name = getView().findViewById(R.id.et_bank_ac_name);
        et_ifsc = getView().findViewById(R.id.et_ifsc);
        tv_proceed = getView().findViewById(R.id.tv_proceed);


        iv_bank_proof.post(new Runnable() {
            @Override
            public void run() {
                int width = iv_bank_proof.getWidth();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_bank_proof.getLayoutParams();
                layoutParams.height = Math.round(width * 0.6f);
                iv_bank_proof.setLayoutParams(layoutParams);
            }
        });

        setupData();


    }

    public void setupData() {
        UserModel userModel = getUserModel();
        if (!userModel.isEmailVerified() ||
                !userModel.isPhoneVerified() ||
                userModel.getPancard() == null || userModel.getPancard().isRejected()) {
            updateViewVisibitity(cv_data_pending, View.VISIBLE);
            updateViewVisibitity(cv_bank_detail, View.GONE);
           // return;
        }
        updateViewVisibitity(cv_data_pending, View.GONE);
        updateViewVisibitity(cv_bank_detail, View.VISIBLE);
        if (userModel.getBankdetail() != null) {
            BankDetailModel bankdetail = userModel.getBankdetail();
            et_bank_account_number.setText(bankdetail.getAccount_number());
            et_re_bank_account_number.setText(bankdetail.getAccount_number());
            et_bank_ac_name.setText(bankdetail.getAccount_holder_name());
            et_ifsc.setText(bankdetail.getIfsc());
            tv_reason.setText(bankdetail.getReason());

            ((AppBaseActivity) getActivity()).loadImage(getActivity(),
                    iv_bank_proof, null, bankdetail.getImage(), R.drawable.icon_bank);
            if (bankdetail.isInReview() || bankdetail.isApproved()) {
                if (bankdetail.isInReview()) {
                    tv_verify_title.setText("Bank details are under review");
                    tv_verify_title.setTextColor(getResources().getColor(R.color.colorOrange));
                    cv_bank_detail.setCardBackgroundColor(getResources().getColor(R.color.colorOrange));
                } else {
                    tv_verify_title.setText("Bank details verified");
                    tv_verify_title.setTextColor(getResources().getColor(R.color.colorActivateGreen));
                    cv_bank_detail.setCardBackgroundColor(getResources().getColor(R.color.colorActivateGreen));
                }

                updateViewVisibitity(tv_upload_account_proof, View.GONE);
                updateViewVisibitity(tv_proceed, View.GONE);
                updateViewVisibitity(tv_reason, View.GONE);

//                et_bank_account_number.setEnabled(false);
//                et_re_bank_account_number.setEnabled(false);
//                et_bank_ac_name.setEnabled(false);
//                et_ifsc.setEnabled(false);
                et_bank_account_number.setFocusable(false);
                et_re_bank_account_number.setFocusable(false);
                et_bank_ac_name.setFocusable(false);
                et_ifsc.setFocusable(false);

                iv_bank_proof.setOnClickListener(null);
                tv_proceed.setOnClickListener(null);

            } else if (bankdetail.isRejected()) {
                tv_verify_title.setText("Bank details rejected");
                tv_verify_title.setTextColor(getResources().getColor(R.color.colorRed));
                cv_bank_detail.setCardBackgroundColor(getResources().getColor(R.color.colorRed));
                updateViewVisibitity(tv_upload_account_proof, View.VISIBLE);
                updateViewVisibitity(tv_proceed, View.VISIBLE);
                updateViewVisibitity(tv_reason, View.VISIBLE);

//                et_bank_account_number.setEnabled(true);
//                et_re_bank_account_number.setEnabled(true);
//                et_bank_ac_name.setEnabled(true);
//                et_ifsc.setEnabled(true);
                et_bank_account_number.setFocusable(true);
                et_re_bank_account_number.setFocusable(true);
                et_bank_ac_name.setFocusable(true);
                et_ifsc.setFocusable(true);

                iv_bank_proof.setOnClickListener(this);
                tv_proceed.setOnClickListener(this);
            }

        } else {
            tv_verify_title.setText("VERIFY BANK ACCOUNT");
            tv_verify_title.setTextColor(getResources().getColor(R.color.colorWhite));
            cv_bank_detail.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
            updateViewVisibitity(tv_upload_account_proof, View.VISIBLE);
            updateViewVisibitity(tv_proceed, View.VISIBLE);
            updateViewVisibitity(tv_reason, View.GONE);

//            et_bank_account_number.setEnabled(true);
//            et_re_bank_account_number.setEnabled(true);
//            et_bank_ac_name.setEnabled(true);
//            et_ifsc.setEnabled(true);
            et_bank_account_number.setFocusable(true);
            et_re_bank_account_number.setFocusable(true);
            et_bank_ac_name.setFocusable(true);
            et_ifsc.setFocusable(true);

            iv_bank_proof.setOnClickListener(this);
            tv_proceed.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_proceed:
                onProceed();
                break;
            case R.id.iv_bank_proof:
                showImagePickerDialog();
                break;
        }
    }

    private void onProceed() {

        Object image = iv_bank_proof.getTag(R.id.image_path_tag);
        if (image == null) {
            showErrorMsg("Please add bank proof image");
            return;
        }


        String accountNumber = et_bank_account_number.getText().toString().trim();
        String reAccountNumber = et_re_bank_account_number.getText().toString().trim();
        String accountName = et_bank_ac_name.getText().toString().trim();
        String ifsc = et_ifsc.getText().toString().trim();


        if (!isValidString(accountNumber)) {
            showErrorMsg("Please enter account number");
            return;
        }
        if (!isValidString(reAccountNumber)) {
            showErrorMsg("Please retype account number");
            return;
        }

        if (!accountNumber.equals(reAccountNumber)) {
            showErrorMsg("Account number not match");
            return;
        }

        if (!isValidString(accountName)) {
            showErrorMsg("Please enter account holder name");
            return;
        }

        if (!isValidString(ifsc)) {
            showErrorMsg("Please enter IFSC code");
            return;
        }


        UpdateBankRequestModel requestModel = new UpdateBankRequestModel();
        requestModel.name = accountName;
        requestModel.account_number = accountNumber;
        requestModel.ifsc = ifsc;
        hideKeyboard();
        uploadImageToS3(requestModel);
    }

    private void showImagePickerDialog() {
        final ProfilePicDialog dialog = ProfilePicDialog.getNewInstance(false);
        dialog.setProfilePicDialogListner(new ProfilePicDialog.ProfilePicDialogListner() {
            @Override
            public void onProfilePicSelected(FileInformation fileInformation) {
                dialog.dismiss();

                String imageName = "bankproof_image_" + System.currentTimeMillis();

                String large_file_path = fileInformation.getBitmapPathForUpload(getContext(),
                        FileInformation.IMAGE_SIZE_LARGE, FileInformation.IMAGE_SIZE_LARGE,
                        "large/" + imageName);

                String thumb_file_path = fileInformation.getBitmapPathForUpload(getContext(),
                        FileInformation.IMAGE_SIZE_THUMB, FileInformation.IMAGE_SIZE_THUMB,
                        "thumb/" + imageName);

                iv_bank_proof.setTag(R.id.image_path_tag, large_file_path);
                iv_bank_proof.setTag(R.id.image_path_thumb_tag, thumb_file_path);
                ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_bank_proof, null,
                        large_file_path, R.drawable.icon_bank);
            }

            @Override
            public void onProfilePicRemoved() {

            }
        });
        dialog.showDialog(getContext(), getChildFm());
    }

    private void uploadImageToS3(final UpdateBankRequestModel requestModel) {
        S3BucketHelper s3Helper = getS3Helper();
        if (s3Helper == null) {
            showErrorMsg("Something went wrong please login again");
            return;
        }
        UserModel userModel = getUserModel();
        AwsModel aws = userModel.getAws();

        String large_img = (String) iv_bank_proof.getTag(R.id.image_path_tag);
        String thumb_img = (String) iv_bank_proof.getTag(R.id.image_path_thumb_tag);
        if (large_img == null || thumb_img == null) return;

        displayProgressBar(false);

        S3BucketHelper.S3BucketFile s3BucketFile = new S3BucketHelper.
                S3BucketFile(aws.getFullBucketPath(aws.getBANK_IMAGE_PATH()), thumb_img, large_img);
        s3BucketFile.setOnS3BucketHelperListener(new S3BucketHelper.OnS3BucketHelperListener() {
            @Override
            public void onUploadComplete(S3BucketHelper.S3BucketFile s3BucketFile) {
                printLog("uploadImageToS3", s3BucketFile.toString());
                dismissProgressBar();
                if (s3BucketFile.isSuccess()) {
                    requestModel.image = s3BucketFile.getBucket_large_url();
                    callAddBankDetail(requestModel);
                } else {
                    dismissProgressBar();
                    showErrorMsg("Error in file upload. Please try again.");
                }
            }
        });
        s3Helper.uploadFile(s3BucketFile);

    }

    private void callAddBankDetail(UpdateBankRequestModel requestModel) {
        getWebRequestHelper().addBankDetail(requestModel, this);
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_ADD_BANK_DETAIL:
                handleAddBankDetailResponse(webRequest);
                break;
        }

    }

    private void handleAddBankDetailResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            getUserPrefs().updateLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            setupData();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

}
