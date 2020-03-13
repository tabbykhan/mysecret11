package com.app.ui.main.dashboard.profile.verification.panCard;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.AppBaseModel;
import com.app.model.AwsModel;
import com.app.model.PanCardModel;
import com.app.model.StateModel;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.UpdatePanRequestModel;
import com.app.model.webresponsemodel.StatesResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.dialogs.MyDatePickerDialog;
import com.app.ui.dialogs.SelectStateDialog;
import com.app.ui.dialogs.selection.DataDialog;
import com.app.ui.main.dashboard.profile.verification.VerificationActivity;
import com.awss3.S3BucketHelper;
import com.imagePicker.FileInformation;
import com.imagePicker.ProfilePicDialog;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DatePickerUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PanCardFragment extends AppBaseFragment {

    private CardView cv_data;
    private TextView tv_verify_title;
    private ImageView iv_pan_image;
    private TextView tv_upload_pan_card;
    private TextView tv_reason;
    private EditText et_pan_name;
    private EditText et_pan_number;
    private TextView tv_dob;
    private TextView tv_state;
    private TextView tv_proceed;


    List<StateModel> stateModels = new ArrayList<>();


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_pan_card;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        cv_data = getView().findViewById(R.id.cv_data);
        tv_verify_title = getView().findViewById(R.id.tv_verify_title);
        iv_pan_image = getView().findViewById(R.id.iv_pan_image);
        tv_upload_pan_card = getView().findViewById(R.id.tv_upload_pan_card);
        tv_reason = getView().findViewById(R.id.tv_reason);
        et_pan_name = getView().findViewById(R.id.et_pan_name);
        et_pan_number = getView().findViewById(R.id.et_pan_number);
        tv_dob = getView().findViewById(R.id.tv_dob);
        tv_state = getView().findViewById(R.id.tv_state);
        tv_proceed = getView().findViewById(R.id.tv_proceed);


        iv_pan_image.post(new Runnable() {
            @Override
            public void run() {
                int width = iv_pan_image.getWidth();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_pan_image.getLayoutParams();
                layoutParams.height = Math.round(width * 0.6f);
                iv_pan_image.setLayoutParams(layoutParams);
            }
        });

        setupData();


    }

    public void setupData() {
        UserModel userModel = getUserModel();
        if (userModel != null && userModel.getPancard() != null) {
            PanCardModel pancard = userModel.getPancard();
            et_pan_name.setText(pancard.getName());
            et_pan_number.setText(pancard.getNumber());
            tv_state.setTag(pancard.getState());
            tv_state.setText(pancard.getState().getName());
            tv_reason.setText(pancard.getReason());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppBaseModel.DATE_DDMMYYYY);
            try {
                Date date = simpleDateFormat.parse(pancard.getDob());
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                tv_dob.setTag(calendar);

                simpleDateFormat = new SimpleDateFormat(AppBaseModel.DATE_DDMMYYYY);
                String sel_date = simpleDateFormat.format(date);
                tv_dob.setText(sel_date);

            } catch (ParseException e) {
                e.printStackTrace();
                tv_dob.setText("");
                tv_dob.setTag(null);
            }

            ((AppBaseActivity) getActivity()).loadImage(getActivity(),
                    iv_pan_image, null, pancard.getImage(), R.drawable.icon_pancard);
            if (pancard.isInReview() || pancard.isApproved()) {
                if (pancard.isInReview()) {
                    tv_verify_title.setText("PAN details are under review");
                    tv_verify_title.setTextColor(getResources().getColor(R.color.colorWhite));
                    cv_data.setCardBackgroundColor(getResources().getColor(R.color.colorWhite));
                } else {
                    tv_verify_title.setText("PAN details verified");
                    tv_verify_title.setTextColor(getResources().getColor(R.color.colorActivateGreen));
                    cv_data.setCardBackgroundColor(getResources().getColor(R.color.colorActivateGreen));
                }

                updateViewVisibitity(tv_reason, View.GONE);
                updateViewVisibitity(tv_upload_pan_card, View.GONE);
                updateViewVisibitity(tv_proceed, View.GONE);
//                et_pan_name.setEnabled(false);
//                et_pan_number.setEnabled(false);
                et_pan_name.setFocusable(false);
                et_pan_number.setFocusable(false);

                iv_pan_image.setOnClickListener(null);
                tv_dob.setOnClickListener(null);
                tv_state.setOnClickListener(null);
                tv_proceed.setOnClickListener(null);

            } else if (pancard.isRejected()) {
                tv_verify_title.setText("PAN details rejected");
                tv_verify_title.setTextColor(getResources().getColor(R.color.colorRedLight));
                cv_data.setCardBackgroundColor(getResources().getColor(R.color.colorRedLight));
                updateViewVisibitity(tv_upload_pan_card, View.VISIBLE);
                updateViewVisibitity(tv_proceed, View.VISIBLE);
                updateViewVisibitity(tv_reason, View.VISIBLE);

//                et_pan_name.setEnabled(true);
//                et_pan_number.setEnabled(true);
                et_pan_name.setFocusable(true);
                et_pan_number.setFocusable(true);

                iv_pan_image.setOnClickListener(this);
                tv_dob.setOnClickListener(this);
                tv_state.setOnClickListener(this);
                tv_proceed.setOnClickListener(this);
            }

        } else {
//            et_pan_name.setEnabled(true);
//            et_pan_number.setEnabled(true);
            et_pan_name.setFocusable(true);
            et_pan_number.setFocusable(true);

            cv_data.setCardBackgroundColor(getResources().getColor(R.color.transparent_color));
            tv_verify_title.setText("UPLOAD PAN CARD");
            tv_verify_title.setTextColor(getResources().getColor(R.color.colorWhite));
            updateViewVisibitity(tv_upload_pan_card, View.VISIBLE);
            updateViewVisibitity(tv_proceed, View.VISIBLE);
            updateViewVisibitity(tv_reason, View.GONE);


            iv_pan_image.setOnClickListener(this);
            tv_dob.setOnClickListener(this);
            tv_state.setOnClickListener(this);
            tv_proceed.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_proceed:
                onProceed();
                break;

            case R.id.iv_pan_image:
                showImagePickerDialog();
                break;

            case R.id.tv_dob:
                showDobPicker();
                break;

            case R.id.tv_state:
                if (stateModels.size() == 0) {
                    callGetStates();
                } else {
                    showStatesDialog();
                }

                break;

        }
    }

    private void onProceed() {

        Object image = iv_pan_image.getTag(R.id.image_path_tag);
        if (image == null) {
            showErrorMsg("Please add PAN card image");
            return;
        }


        String panName = et_pan_name.getText().toString().trim();
        String panNumber = et_pan_number.getText().toString().trim();
        String dob = "";
        if (tv_dob.getTag() != null) {
            Calendar calendar = (Calendar) tv_dob.getTag();
            Date date = calendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppBaseModel.YYYY_MM_DD);
            dob = simpleDateFormat.format(date);
        }
        long state = 0;
        if (tv_state.getTag() != null) {
            state = ((StateModel) tv_state.getTag()).getId();
        }

        if (!isValidString(panName)) {
            showErrorMsg("Please enter name");
            return;
        }
        if (!isValidString(panNumber)) {
            showErrorMsg("Please enter PAN number");
            return;
        }

        Pattern pattern = Pattern.compile("[A-Z]{5}[0-9]{4}[A-Z]{1}");
        Matcher matcher = pattern.matcher(panNumber);
        if (!matcher.matches()) {
            showErrorMsg("Please enter valid PAN number");
            return;
        }

        if (!isValidString(dob)) {
            showErrorMsg("Please enter date of birth");
            return;
        }
        if (state == 0) {
            showErrorMsg("Please select state");
            return;
        }


        UpdatePanRequestModel requestModel = new UpdatePanRequestModel();
        requestModel.name = panName;
        requestModel.number = panNumber;
        requestModel.dob = dob;
        requestModel.state = state;
        hideKeyboard();
        uploadImageToS3(requestModel);

    }


    private void showImagePickerDialog() {
        final ProfilePicDialog dialog = ProfilePicDialog.getNewInstance(false);
        dialog.setProfilePicDialogListner(new ProfilePicDialog.ProfilePicDialogListner() {
            @Override
            public void onProfilePicSelected(FileInformation fileInformation) {
                dialog.dismiss();

                String imageName = "pan_image_" + System.currentTimeMillis();

                String large_file_path = fileInformation.getBitmapPathForUpload(getContext(),
                        FileInformation.IMAGE_SIZE_LARGE, FileInformation.IMAGE_SIZE_LARGE,
                        "large/" + imageName);

                String thumb_file_path = fileInformation.getBitmapPathForUpload(getContext(),
                        FileInformation.IMAGE_SIZE_THUMB, FileInformation.IMAGE_SIZE_THUMB,
                        "thumb/" + imageName);

                iv_pan_image.setTag(R.id.image_path_tag, large_file_path);
                iv_pan_image.setTag(R.id.image_path_thumb_tag, thumb_file_path);
                ((AppBaseActivity) getActivity()).loadImage(getActivity(), iv_pan_image, null,
                        large_file_path, R.drawable.icon_pancard);
            }

            @Override
            public void onProfilePicRemoved() {

            }
        });
        dialog.showDialog(getContext(), getChildFm());
    }

    private void showDobPicker() {
        hideKeyboard();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.YEAR, -18);

//        DatePickerDialog datePicker = DatePickerUtil.getDatePicker(getActivity(), tv_dob, new DatePickerUtil.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(Calendar calendar) {
//                tv_dob.setTag(calendar);
//
//                Date date = calendar.getTime();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppBaseModel.DATE_MMMDDYYYY);
//                String sel_date = simpleDateFormat.format(date);
//                tv_dob.setText(sel_date);
//
//            }
//        });
//        datePicker.getDatePicker().setMaxDate(now.getTimeInMillis());
//        datePicker.show();

        MyDatePickerDialog datePicker = DatePickerUtil.getDatePicker("SELECT DATE",getActivity(), tv_dob, new DatePickerUtil.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Calendar calendar) {
                tv_dob.setTag(calendar);

                Date date = calendar.getTime();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(AppBaseModel.DATE_DDMMYYYY);
                String sel_date = simpleDateFormat.format(date);
                tv_dob.setText(sel_date);

            }
        });
        datePicker.setMaxDate(now.getTimeInMillis());
        datePicker.show(getChildFm(),datePicker.getClass().getSimpleName());
    }


    private void showStatesDialog() {
        hideKeyboard();
        final SelectStateDialog selectStateDialog = new SelectStateDialog();
        selectStateDialog.setDataList(stateModels);
        selectStateDialog.setOnItemSelectedListeners(new DataDialog.OnItemSelectedListener() {
            @Override
            public void onItemSelectedListener(int position) {
                selectStateDialog.dismiss();
                tv_state.setTag(stateModels.get(position));
                tv_state.setText(stateModels.get(position).getName());
            }
        });
        selectStateDialog.show(getChildFm(), selectStateDialog.getClass().getSimpleName());

    }

    private void uploadImageToS3(final UpdatePanRequestModel requestModel) {
        S3BucketHelper s3Helper = getS3Helper();
        if (s3Helper == null) {
            showErrorMsg("Something went wrong please login again");
            return;
        }
        UserModel userModel = getUserModel();
        AwsModel aws = userModel.getAws();

        String large_img = (String) iv_pan_image.getTag(R.id.image_path_tag);
        String thumb_img = (String) iv_pan_image.getTag(R.id.image_path_thumb_tag);
        if (large_img == null || thumb_img == null) return;

        displayProgressBar(false);

        S3BucketHelper.S3BucketFile s3BucketFile = new S3BucketHelper.
                S3BucketFile(aws.getFullBucketPath(aws.getPANCARD_IMAGE_PATH()), thumb_img, large_img);
        s3BucketFile.setOnS3BucketHelperListener(new S3BucketHelper.OnS3BucketHelperListener() {
            @Override
            public void onUploadComplete(S3BucketHelper.S3BucketFile s3BucketFile) {
                printLog("uploadImageToS3", s3BucketFile.toString());
                dismissProgressBar();
                if (s3BucketFile.isSuccess()) {
                    requestModel.image = s3BucketFile.getBucket_large_url();
                    callAddPanCard(requestModel);
                } else {
                    dismissProgressBar();
                    showErrorMsg("Error in file upload. Please try again.");
                }
            }
        });
        s3Helper.uploadFile(s3BucketFile);

    }

    private void callAddPanCard(UpdatePanRequestModel requestModel) {
        getWebRequestHelper().addPanCard(requestModel, this);
    }


    private void callGetStates() {
        displayProgressBar(false, "Wait...");
        getWebRequestHelper().getStates(COUNTRY_ID_VALUE, this);
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_STATES:
                handleStatesResponse(webRequest);
                break;
            case ID_ADD_PAN_CARD:
                handleAddPanCardResponse(webRequest);
                break;
        }

    }


    private void handleStatesResponse(WebRequest webRequest) {
        StatesResponseModel responsePojo = webRequest.getResponsePojo(StatesResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<StateModel> data = responsePojo.getData();
            stateModels.clear();
            if (data != null && data.size() > 0) {
                stateModels.addAll(data);
            }
            if (isFinishing()) return;
            showStatesDialog();

        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleAddPanCardResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            getUserPrefs().updateLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            setupData();
            if (getActivity() == null) return;
            ((VerificationActivity) getActivity()).panCardUpdated();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }

}
