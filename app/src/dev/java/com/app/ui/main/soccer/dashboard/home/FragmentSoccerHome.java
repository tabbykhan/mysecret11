package com.app.ui.main.soccer.dashboard.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.ui.main.soccer.dashboard.home.fixture.FragmentSoccerFixtureMatch;
import com.app.ui.main.soccer.dashboard.home.live.LiveSocerFragment;
import com.app.ui.main.soccer.dashboard.home.result.ResultSoccerMatchFragment;
import com.app.ui.main.soccer.slider.SoccerSliderFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.utilities.DeviceScreenUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSoccerHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSoccerHome extends AppBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ViewPager viewpager_match;
    ViewPagerAdapter adapter;
    TabLayout home_tabs;
    CoordinatorLayout coordinatorLayout;
    AppBarLayout app_bar_layout;


    public FragmentSoccerHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSoccerHome.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSoccerHome newInstance(String param1, String param2) {
        FragmentSoccerHome fragment = new FragmentSoccerHome();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getView() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return getView();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_soccer_home;
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

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(getChildFm());
        adapter.addFragment(new FragmentSoccerFixtureMatch(), "FIXTURE"); // UPCOMING
        // adapter.addFragment(new FivePlayerMatchFragment(), "5 A Side");// five player selection
        // match
        adapter.addFragment(new LiveSocerFragment(), "LIVE");
        adapter.addFragment(new ResultSoccerMatchFragment(), "COMPLETED");
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
        SoccerSliderFragment sliderFragment = new SoccerSliderFragment();
        sliderFragment.setMainContainer(app_bar_layout);
        FragmentTransaction transaction = getChildFm().beginTransaction();
        transaction.add(R.id.container, sliderFragment);
        transaction.commit();
    }
}
