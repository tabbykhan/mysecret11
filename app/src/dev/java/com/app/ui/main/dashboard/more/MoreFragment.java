package com.app.ui.main.dashboard.more;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appupdater.AppUpdateUtils;
import com.app.model.CustomIconModel;
import com.app.ui.AppCustomIconsHelper;
import com.app.ui.MyApplication;
import com.rest.WebServices;

/**
 * Created by Vishnu Gupta on 10/1/19.
 */
public class MoreFragment extends AppBaseFragment {

    RelativeLayout rl_invite, rl_contest_invite_code, rl_point_system,
            rl_how_to_play, rl_helpdesk, rl_about_us, rl_legality, rl_withdraw_policy, rl_terms_services,
            rl_contact, rl_faq, rl_account;

    TextView tv_app_version;

    ImageView img_invite;
    TextView txt_invite;
    ImageView img_contest;
    TextView txt_invite_code;
    ImageView img_graph;
    TextView tv_graph;
    ImageView img_play;
    TextView tv_play;
    ImageView img_helpdesk;
    TextView tv_helpdesk;
    ImageView img_about;
    TextView tv_about;
    ImageView img_ligality;
    TextView tv_ligality;
    ImageView img_withdraw_policy;
    TextView tv_withdraw_policy;
    ImageView img_terms_services;
    TextView tv_terms_services;
    ImageView img_contect;
    TextView tv_contect;
    ImageView img_faq;
    TextView tv_faq;


    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_more;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        img_invite = getView().findViewById(R.id.img_invite);
        txt_invite = getView().findViewById(R.id.txt_invite);
        img_contest = getView().findViewById(R.id.img_contest);
        txt_invite_code = getView().findViewById(R.id.txt_invite_code);
        img_graph = getView().findViewById(R.id.img_graph);
        tv_graph = getView().findViewById(R.id.tv_graph);
        img_play = getView().findViewById(R.id.img_play);
        tv_play = getView().findViewById(R.id.tv_play);
        img_helpdesk = getView().findViewById(R.id.img_helpdesk);
        tv_helpdesk = getView().findViewById(R.id.tv_helpdesk);
        img_about = getView().findViewById(R.id.img_about);
        tv_about = getView().findViewById(R.id.tv_about);
        img_ligality = getView().findViewById(R.id.img_ligality);
        tv_ligality = getView().findViewById(R.id.tv_ligality);
        img_withdraw_policy = getView().findViewById(R.id.img_withdraw_policy);
        tv_withdraw_policy = getView().findViewById(R.id.tv_withdraw_policy);
        img_terms_services = getView().findViewById(R.id.img_terms_services);
        tv_terms_services = getView().findViewById(R.id.tv_terms_services);
        img_contect = getView().findViewById(R.id.img_contect);
        tv_contect = getView().findViewById(R.id.tv_contect);
        img_faq = getView().findViewById(R.id.img_faq);
        tv_faq = getView().findViewById(R.id.tv_faq);


      rl_invite = getView().findViewById(R.id.rl_invite);
        rl_contest_invite_code = getView().findViewById(R.id.rl_contest_invite_code);
        rl_point_system = getView().findViewById(R.id.rl_point_system);
        rl_how_to_play = getView().findViewById(R.id.rl_how_to_play);
        rl_helpdesk = getView().findViewById(R.id.rl_helpdesk);
        rl_about_us = getView().findViewById(R.id.rl_about_us);
        rl_legality = getView().findViewById(R.id.rl_legality);
        rl_withdraw_policy = getView().findViewById(R.id.rl_withdraw_policy);
        rl_terms_services = getView().findViewById(R.id.rl_terms_services);
        rl_contact = getView().findViewById(R.id.rl_contact);
        rl_faq = getView().findViewById(R.id.rl_faq);
        rl_account = getView().findViewById(R.id.rl_account);
        tv_app_version = getView().findViewById(R.id.tv_app_version);

        tv_app_version.setText("Version " + AppUpdateUtils.getAppInstalledVersionName(getActivity()));


       rl_invite.setOnClickListener(this);
        rl_contest_invite_code.setOnClickListener(this);
        rl_point_system.setOnClickListener(this);
        rl_how_to_play.setOnClickListener(this);
        rl_helpdesk.setOnClickListener(this);
        rl_about_us.setOnClickListener(this);
        rl_legality.setOnClickListener(this);
        rl_withdraw_policy.setOnClickListener(this);
        rl_terms_services.setOnClickListener(this);
        rl_contact.setOnClickListener(this);
        rl_faq.setOnClickListener(this);
        rl_account.setOnClickListener(this);

        setupCustomIcons();

        new AppCustomIconsHelper().start();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserPrefs() != null) {
            getUserPrefs().addListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserPrefs() != null) {
            getUserPrefs().removeListener(this);
        }
    }

    @Override
    public void customIconUpdate(CustomIconModel customIconModel) {
        super.customIconUpdate(customIconModel);
        setupCustomIcons();
    }

    private void setupCustomIcons() {
        if (img_invite == null || isFinishing() || getActivity() == null) return;
        CustomIconModel customIconModel = MyApplication.getInstance().getCustomIconModel();
        if (customIconModel != null) {
            CustomIconModel.IconModel more_invite_friends = customIconModel.getMore_invite_friends();
            if (more_invite_friends != null) {
                txt_invite.setText(more_invite_friends.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_invite, null, more_invite_friends.getImage(), R.drawable.invite_3x, -1);
                updateViewVisibitity(rl_invite, View.VISIBLE);
            } else {
                updateViewVisibitity(rl_invite, View.GONE);
                txt_invite.setText("");
                img_invite.setImageResource(R.drawable.invite_3x);
            }

            CustomIconModel.IconModel more_contest_invite_code = customIconModel.getMore_contest_invite_code();
            if (more_contest_invite_code != null) {
                updateViewVisibitity(rl_contest_invite_code, View.VISIBLE);
                txt_invite_code.setText(more_contest_invite_code.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_contest, null, more_contest_invite_code.getImage(), R.drawable.invite_code_3x, -1);
            } else {
                updateViewVisibitity(rl_contest_invite_code, View.GONE);
                txt_invite_code.setText("");
                img_contest.setImageResource(R.drawable.invite_code_3x);
            }

            CustomIconModel.IconModel more_fantasy_point_system = customIconModel.getMore_fantasy_point_system();
            if (more_fantasy_point_system != null) {
                updateViewVisibitity(rl_point_system, View.VISIBLE);
                tv_graph.setText(more_fantasy_point_system.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_graph, null, more_fantasy_point_system.getImage(), R.drawable.analysis, -1);
            } else {
                updateViewVisibitity(rl_point_system, View.GONE);
                tv_graph.setText("");
                img_graph.setImageResource(R.drawable.analysis);
            }


            CustomIconModel.IconModel more_how_to_play = customIconModel.getMore_how_to_play();
            if (more_how_to_play != null) {
                updateViewVisibitity(rl_how_to_play, View.VISIBLE);
                tv_play.setText(more_how_to_play.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_play, null, more_how_to_play.getImage(), R.drawable.howplay, -1);
            } else {
                updateViewVisibitity(rl_how_to_play, View.GONE);
                tv_play.setText("");
                img_play.setImageResource(R.drawable.howplay);
            }


            CustomIconModel.IconModel more_helpdesk = customIconModel.getMore_helpdesk();
            if (more_helpdesk != null) {
                updateViewVisibitity(rl_helpdesk, View.VISIBLE);
                tv_helpdesk.setText(more_helpdesk.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_helpdesk, null, more_helpdesk.getImage(), R.drawable.question, -1);
            } else {
                updateViewVisibitity(rl_helpdesk, View.GONE);
                tv_helpdesk.setText("");
                img_helpdesk.setImageResource(R.drawable.question);
            }


            CustomIconModel.IconModel more_aboutus = customIconModel.getMore_aboutus();
            if (more_aboutus != null) {
                updateViewVisibitity(rl_about_us, View.VISIBLE);
                tv_about.setText(more_aboutus.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_about, null, more_aboutus.getImage(), R.drawable.about_us_3x, -1);
            } else {
                updateViewVisibitity(rl_about_us, View.GONE);
                tv_about.setText("");
                img_about.setImageResource(R.drawable.about_us_3x);
            }


            CustomIconModel.IconModel more_legality = customIconModel.getMore_legality();
            if (more_legality != null) {
                updateViewVisibitity(rl_legality, View.VISIBLE);
                tv_ligality.setText(more_legality.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_ligality, null, more_legality.getImage(), R.drawable.legality_3x, -1);
            } else {
                updateViewVisibitity(rl_legality, View.GONE);
                tv_ligality.setText("");
                img_ligality.setImageResource(R.drawable.legality_3x);
            }


            CustomIconModel.IconModel more_withdraw_policy = customIconModel.getMore_withdraw_policy();
            if (more_withdraw_policy != null) {
                updateViewVisibitity(rl_withdraw_policy, View.VISIBLE);
                tv_withdraw_policy.setText(more_withdraw_policy.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_withdraw_policy, null, more_withdraw_policy.getImage(), R.drawable.withdraw_icon, -1);
            } else {
                updateViewVisibitity(rl_withdraw_policy, View.GONE);
                tv_withdraw_policy.setText("");
                img_withdraw_policy.setImageResource(R.drawable.withdraw_icon);
            }


            CustomIconModel.IconModel more_terms_of_services = customIconModel.getMore_terms_of_services();
            if (more_terms_of_services != null) {
                updateViewVisibitity(rl_terms_services, View.VISIBLE);
                tv_terms_services.setText(more_terms_of_services.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_terms_services, null, more_terms_of_services.getImage(), R.drawable.terms, -1);
            } else {
                updateViewVisibitity(rl_terms_services, View.GONE);
                tv_terms_services.setText("");
                img_terms_services.setImageResource(R.drawable.terms);
            }


            CustomIconModel.IconModel more_contact = customIconModel.getMore_contact();
            if (more_contact != null) {
                updateViewVisibitity(rl_contact, View.VISIBLE);
                tv_contect.setText(more_contact.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_contect, null, more_contact.getImage(), R.drawable.contact_icon, -1);
            } else {
                updateViewVisibitity(rl_contact, View.GONE);
                tv_contect.setText("");
                img_contect.setImageResource(R.drawable.contact_icon);
            }

            CustomIconModel.IconModel more_faq = customIconModel.getMore_faq();
            if (more_faq != null) {
                updateViewVisibitity(rl_faq, View.VISIBLE);
                tv_faq.setText(more_faq.getName());
                ((AppBaseActivity) getActivity()).loadImage(this, img_faq, null, more_faq.getImage(), R.drawable.faq_icon, -1);
            } else {
                updateViewVisibitity(rl_faq, View.GONE);
                tv_faq.setText("");
                img_faq.setImageResource(R.drawable.faq_icon);
            }
        }

    }


    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {

            case R.id.rl_invite:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToInviteActivity(null);
                break;

            case R.id.rl_contest_invite_code:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToContestInviteActivity(null);
                break;

            case R.id.rl_account:
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToMyAccountMLMActivity(null);
                break;
            case R.id.rl_point_system:
                bundle.putString(DATA1, tv_graph.getText().toString());
                bundle.putString(DATA, WebServices.GetFantasyPoints());
                goToWebViewActivity(bundle);
                break;
            case R.id.rl_how_to_play:
                bundle.putString(DATA1, tv_play.getText().toString());
                bundle.putString(DATA, WebServices.GetHowToPlay());
                goToWebViewActivity(bundle);
                break;

            case R.id.rl_helpdesk:
                bundle.putString(DATA1, tv_helpdesk.getText().toString());
                if (getActivity() == null) return;
                ((AppBaseActivity) getActivity()).goToHelpDeskActivity(bundle);
                break;
            case R.id.rl_about_us:
                bundle.putString(DATA1, tv_about.getText().toString());
                bundle.putString(DATA, WebServices.GetAboutUs());
                goToWebViewActivity(bundle);
                break;
            case R.id.rl_legality:
                bundle.putString(DATA1, tv_ligality.getText().toString());
                bundle.putString(DATA, WebServices.GetLegality());
                goToWebViewActivity(bundle);
                break;
            case R.id.rl_withdraw_policy:
                bundle.putString(DATA1, tv_withdraw_policy.getText().toString());
                bundle.putString(DATA, WebServices.GetWithdrawPolicy());
                goToWebViewActivity(bundle);
                break;
            case R.id.rl_terms_services:
                bundle.putString(DATA1, tv_terms_services.getText().toString());
                bundle.putString(DATA, WebServices.GetTnc());
                goToWebViewActivity(bundle);
                break;
            case R.id.rl_contact:
                bundle.putString(DATA1, tv_contect.getText().toString());
                bundle.putString(DATA, WebServices.GetContact());
                goToWebViewActivity(bundle);
                break;
            case R.id.rl_faq:
                bundle.putString(DATA1, tv_faq.getText().toString());
                bundle.putString(DATA, WebServices.GetFaq());
                goToWebViewActivity(bundle);
                break;
        }
    }

}
