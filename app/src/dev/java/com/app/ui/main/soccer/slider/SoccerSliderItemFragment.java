package com.app.ui.main.soccer.slider;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.SliderModel;
import com.app.ui.MyApplication;
import com.app.ui.dialogs.webviewdialog.WebViewDialog;
import com.app.ui.main.soccer.contest.mycontest.SoccerMyContestActivity;
import com.app.ui.main.soccer.contest.view.SoccerContestActivity;

public class SoccerSliderItemFragment extends AppBaseFragment {

    ImageView image;
    ProgressBar pb_image;

    SliderModel sliderModel;

    public void setSliderModel(SliderModel sliderModel) {
        this.sliderModel = sliderModel;
    }

    public SliderModel getSliderModel() {
        return sliderModel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return getView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_slider_item;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        image = getView().findViewById(R.id.image);
        pb_image = getView().findViewById(R.id.pb_image);

        setupData();

        image.setOnClickListener(this);
    }

    private void setupData() {
        SliderModel sliderModel = getSliderModel();
        if (sliderModel == null) {
            updateViewVisibitity(pb_image, View.GONE);
            image.setImageResource(R.drawable.bg_transparent);
            return;
        }
        ((AppBaseActivity) getContext()).loadImage(getContext(), image, pb_image,
                sliderModel.getImage_large(),
                R.drawable.bg_transparent, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                MatchModel match = sliderModel.getMatch();
                if (match != null) {
                    if (match.isFixtureMatch()) {
                        MyApplication.getInstance().setSelectedMatch(match);
                        MyApplication.getInstance().setIs_5_player_match(false);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, match.getMatch_id());
                        goToContestActivity(bundle);

                    } else {
                        MyApplication.getInstance().setSelectedMatch(match);
                        MyApplication.getInstance().setIs_5_player_match(false);
                        Bundle bundle = new Bundle();
                        bundle.putString(DATA, match.getMatch_id());
                        bundle.putString(DATA1, SoccerSliderItemFragment.this.getClass().getSimpleName());
                        goToMyContestActivity(bundle);
                    }
                    return;
                }
                String content = sliderModel.getContent();
                if (isValidString(content)) {
                    WebViewDialog instance = WebViewDialog.getInstance(null);
                    instance.setData(content);
                    instance.show(getChildFm(), instance.getClass().getSimpleName());
                }

                break;
        }
    }

    private void goToContestActivity(Bundle bundle) {
        if (isFinishing()) return;
        Intent intent = new Intent(getActivity(), SoccerContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (isFinishing()) return;
        startActivity(intent);
        if (getActivity() == null) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }

    private void goToMyContestActivity(Bundle bundle) {
        Intent intent = new Intent(getActivity(), SoccerMyContestActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isFinishing()) return;
        getActivity().overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }
}
