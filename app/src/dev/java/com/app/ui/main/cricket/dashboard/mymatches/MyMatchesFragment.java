package com.app.ui.main.cricket.dashboard.mymatches;

import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.cricket.dashboard.mymatches.fixture.MyFixtureFragment;
import com.app.ui.main.cricket.dashboard.mymatches.live.MyLiveFragment;
import com.app.ui.main.cricket.dashboard.mymatches.result.MyResultFragment;
import com.google.android.material.tabs.TabLayout;


/**
 * Created by Vishnu Gupta on 16/1/19.
 */
public class MyMatchesFragment extends AppBaseFragment implements MatchTimerListener {

    ViewPager viewpager_match;
    ViewPagerAdapter adapter;
    TabLayout mymatches_tabs;

    @Override
    public void onResume() {
        super.onResume();
        MyApplication.getInstance().addMatchTimerListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MyApplication.getInstance().removeMatchTimerListener(this);
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fargment_mymatches;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        viewpager_match = getView().findViewById(R.id.viewpager_match);
        mymatches_tabs = getView().findViewById(R.id.mymatches_tabs);
        setupViewPager();
    }


    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyFixtureFragment(), "FIXTURE");
        adapter.addFragment(new MyLiveFragment(), "LIVE");
        adapter.addFragment(new MyResultFragment(), "RESULT");
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
        mymatches_tabs.setupWithViewPager(viewpager_match);

    }

    @Override
    public void onMatchTimeUpdate() {
        if (isFinishing()) return;
        if (viewpager_match.getCurrentItem() == 0) {
            AppBaseFragment item = adapter.getItem(0);
            ((MyFixtureFragment) item).onMatchTimeUpdate();
        }

    }


}
