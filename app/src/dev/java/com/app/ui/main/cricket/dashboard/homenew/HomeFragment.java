package com.app.ui.main.cricket.dashboard.homenew;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.ui.main.cricket.dashboard.homenew.fixture.FixtureFragment;
import com.app.ui.main.cricket.dashboard.homenew.live.LiveFragment;
import com.app.ui.main.cricket.dashboard.homenew.result.ResultFragment;
import com.app.ui.slider.SliderFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.utilities.DeviceScreenUtil;

/**
 * Created by Vishnu Gupta on 10/1/19.
 */
public class HomeFragment extends AppBaseFragment {

    ViewPager viewpager_match;
    ViewPagerAdapter adapter;
    TabLayout home_tabs;
    CoordinatorLayout coordinatorLayout;
    AppBarLayout app_bar_layout;


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
        return R.layout.fragment_home_new;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        viewpager_match = getView().findViewById(R.id.viewpager_match);
        home_tabs = getView().findViewById(R.id.home_tabs);
        coordinatorLayout = getView().findViewById(R.id.coordinatorLayout);
        app_bar_layout = getView().findViewById(R.id.app_bar_layout);

        setupUi();
        addSlider();
        setupViewPager();

    }


    private void setupUi() {
        coordinatorLayout.post(new Runnable() {
            @Override
            public void run() {
                int width = app_bar_layout.getWidth();
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) app_bar_layout.getLayoutParams();
                layoutParams.height = Math.round(width * 0.30f) + DeviceScreenUtil.getInstance()
                        .convertDpToPixel(20.0f);
                app_bar_layout.setLayoutParams(layoutParams);
            }
        });

    }

    private void addSlider() {
        SliderFragment sliderFragment = new SliderFragment();
        sliderFragment.setMainContainer(app_bar_layout);
        FragmentTransaction transaction = getChildFm().beginTransaction();
        transaction.add(R.id.container, sliderFragment);
        transaction.commit();
    }


    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFm());
        adapter.addFragment(new FixtureFragment(), "FIXTURE"); // UPCOMING
        adapter.addFragment(new LiveFragment(), "LIVE");
        adapter.addFragment(new ResultFragment(), "COMPLETED");
        viewpager_match.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                adapter.getItem(position).onPageSelected();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewpager_match.setAdapter(adapter);
        home_tabs.setupWithViewPager(viewpager_match);

    }


}
