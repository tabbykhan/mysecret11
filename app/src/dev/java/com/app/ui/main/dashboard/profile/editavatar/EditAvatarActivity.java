package com.app.ui.main.dashboard.profile.editavatar;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.UserModel;
import com.app.model.webrequestmodel.UpdateProfileRequestModel;
import com.app.model.webresponsemodel.ProfilePicturesResponseModel;
import com.app.model.webresponsemodel.VerifyOtpResponseModel;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.dashboard.profile.editavatar.adapter.AvatarAdapter;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vishnu Gupta on 7/1/19.
 */
public class EditAvatarActivity extends AppBaseActivity {


    ToolbarFragment toolbarFragment;

    RecyclerView recycler_view;
    ProgressBar pb_data;

    LinearLayout ll_update;
    TextView tv_update;


    AvatarAdapter adapter;


    List<String> avatarModels = new ArrayList<>();



    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_edit_avatar;
    }


    @Override
    public void initializeComponent() {
        super.initializeComponent();

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }

        ll_update = findViewById(R.id.ll_update);
        tv_update = findViewById(R.id.tv_update);

        tv_update.setOnClickListener(this);

        recycler_view = findViewById(R.id.recycler_view);
        pb_data = findViewById(R.id.pb_data);
        updateViewVisibitity(pb_data, View.GONE);
        initializeRecyclerView();
        getAvatars();


    }


    private void initializeRecyclerView() {
        adapter = new AvatarAdapter(this, avatarModels) {

            @Override
            public int getItemSize() {
                return Math.round(((float) recycler_view.getWidth()) / 4);
            }
        };
        recycler_view.setLayoutManager(getGridLayoutManager(4));
        recycler_view.setAdapter(adapter);

        new ItemClickSupport(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                adapter.setSelectedItem(position);
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update: {
                String selectedTeam = adapter.getSelectedItem();
                if (selectedTeam == null || selectedTeam.trim().isEmpty()) {
                    showErrorMsg("Please select avatar");
                    return;
                }
                updateAvatar();

            }
            break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void getAvatars() {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            updateViewVisibitity(pb_data, View.VISIBLE);
            getWebRequestHelper().getProfilePictures(this);
        }
    }

    private void updateAvatar() {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            displayProgressBar(false);
            UpdateProfileRequestModel requestModel = new UpdateProfileRequestModel();
            requestModel.image = adapter.getSelectedItem();
            requestModel.fb_image = "";
            getWebRequestHelper().changeProfilePicture(requestModel, this);
        }
    }
    private void updateAvatarFacebook(String imageURL) {
        UserModel userModel = getUserModel();
        if (userModel != null) {
            displayProgressBar(false);
            UpdateProfileRequestModel requestModel = new UpdateProfileRequestModel();
            requestModel.image = "";
            requestModel.fb_image = imageURL;
            getWebRequestHelper().changeProfilePicture(requestModel, this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);
        if (webRequest.getWebRequestId() == ID_CHANGE_PROFILE_PICTURE) {
            dismissProgressBar();
        }
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_GET_PROFILE_PICTURES:
                handleGetProfilePicturesResponse(webRequest);
                break;
            case ID_CHANGE_PROFILE_PICTURE:
                handleChangeProfilePictureResponse(webRequest);
                break;
        }

    }

    private void handleGetProfilePicturesResponse(WebRequest webRequest) {
        ProfilePicturesResponseModel responsePojo = webRequest.getResponsePojo(ProfilePicturesResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<String> data = responsePojo.getData();
            avatarModels.clear();
            if (data != null && data.size() > 0) {
                avatarModels.addAll(data);
            }
            if (isFinishing()) return;
            UserModel userModel = getUserModel();
            if(userModel!=null){
                int i = avatarModels.indexOf(userModel.getImage());
                if(i>=0){
                    adapter.setSelectedItem(i);
                }else{
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleChangeProfilePictureResponse(WebRequest webRequest) {
        VerifyOtpResponseModel responsePojo = webRequest.getResponsePojo(VerifyOtpResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            UserModel data = responsePojo.getData();
            if (data == null) return;
            if (isFinishing()) return;
            getUserPrefs().updateLoggedInUser(data);
            showCustomToast(responsePojo.getMessage());
            setResult();
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }


    private void setResult() {
        setResult(Activity.RESULT_OK);
        supportFinishAfterTransition();
    }



}
