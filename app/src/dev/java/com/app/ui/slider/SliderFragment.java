package com.app.ui.slider;

import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.model.GameModel;
import com.app.model.SliderModel;
import com.app.model.webresponsemodel.SliderResponseModel;
import com.app.ui.MyApplication;
import com.google.android.material.appbar.AppBarLayout;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.DeviceScreenUtil;

import java.util.ArrayList;
import java.util.List;

public class SliderFragment extends AppBaseFragment {

    private static int NUM_PAGES = 0;
    private TextView tv_no_item_slider;
    private ProgressBar pb_data;
    private ViewPager view_pager_slider;
    private LinearLayout pager_indicator;

    private ViewPagerAdapter viewPagerAdapter;
    private Handler handler;
    private Runnable Update;
    private List<SliderModel> sliderModels = new ArrayList<>();

    private ViewPager.OnPageChangeListener onPageChangeListener;

    AppBarLayout appBarLayout;


    public void setMainContainer(AppBarLayout appBarLayout) {
        this.appBarLayout = appBarLayout;
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_slider;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        tv_no_item_slider = getView().findViewById(R.id.tv_no_item_slider);
        pb_data = getView().findViewById(R.id.pb_data);
        view_pager_slider = getView().findViewById(R.id.view_pager_slider);
        pager_indicator = getView().findViewById(R.id.viewPagerCountDots);

        handler = new Handler();
        Update = new Runnable() {
            public void run() {
                if (NUM_PAGES > 0 && getActivity() != null && isVisible()) {
                    int currentPage = view_pager_slider.getCurrentItem();
                    if (currentPage == (NUM_PAGES - 1)) {
                        currentPage = -1;
                    }
                    currentPage++;
                    view_pager_slider.setCurrentItem(currentPage, true);
                }
            }
        };

        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                stopTimer();
                if (getActivity() != null && isVisible()) {
                    int currentPage = view_pager_slider.getCurrentItem();
                    for (int i = 0; i < pager_indicator.getChildCount(); i++) {
                        boolean activated = pager_indicator.getChildAt(i).isActivated();
                        if (i == currentPage) {
                            if (!activated) {
                                pager_indicator.getChildAt(i).setActivated(true);
                            }
                        } else {
                            if (activated) {
                                pager_indicator.getChildAt(i).setActivated(false);
                            }
                        }

                    }
                    startTimer();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };


        getSlider();
    }


    private void setupViewPager() {
        NUM_PAGES = sliderModels.size();


        viewPagerAdapter = new ViewPagerAdapter(getChildFm());
        for (SliderModel sliderModel : sliderModels) {
            SliderItemFragment sliderItemFragment = new SliderItemFragment();
            sliderItemFragment.setSliderModel(sliderModel);
            viewPagerAdapter.addFragment(sliderItemFragment, "");
        }
        setupViewPagerIndicator();
        view_pager_slider.setAdapter(viewPagerAdapter);
        view_pager_slider.setCurrentItem(0);
        view_pager_slider.removeOnPageChangeListener(onPageChangeListener);
        view_pager_slider.addOnPageChangeListener(onPageChangeListener);
    }


    private void setupViewPagerIndicator() {

        int margin = DeviceScreenUtil.getInstance().convertDpToPixel(3.0f);

        int dotsCount = viewPagerAdapter.getCount();

        for (int i = 0; i < dotsCount; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageDrawable(getResources().getDrawable(R.drawable.selector_pager_indicator_bg));
            if (i == 0) {
                imageView.setActivated(true);
            } else {
                imageView.setActivated(false);
            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(margin, 0, margin, 0);

            pager_indicator.addView(imageView, params);
        }

    }

    public void startTimer() {
        if (handler != null && Update != null) {
            handler.removeCallbacks(Update);
            handler.postDelayed(Update, 3000);
        }
    }

    public void stopTimer() {
        if (handler != null && Update != null) {
            handler.removeCallbacks(Update);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    private void getSlider() {
        stopTimer();
        GameModel currentGame = MyApplication.getInstance().getCurrentGame();
        if(currentGame==null)return;
        updateViewVisibitity(tv_no_item_slider, View.GONE);
        updateViewVisibitity(pb_data, View.VISIBLE);
        if(currentGame.isCricket()) {
            getWebRequestHelper().getSilder(this);
        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        updateViewVisibitity(pb_data, View.GONE);
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_SLIDER:
                handleSliderResponse(webRequest);
                break;
        }
    }

    private void handleSliderResponse(WebRequest webRequest) {
        SliderResponseModel responsePojo = webRequest.getResponsePojo(SliderResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            List<SliderModel> data = responsePojo.getData();
            sliderModels.clear();
            if (data != null && data.size() > 0) {
                sliderModels.addAll(data);
            }
            if (isFinishing()) return;
            if (appBarLayout != null) {
                if (sliderModels.size() == 0) {
                    tv_no_item_slider.setText(Html.fromHtml(responsePojo.getMessage()));
                    updateViewVisibitity(tv_no_item_slider, View.VISIBLE);
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                    layoutParams.height = 0;
                    appBarLayout.setLayoutParams(layoutParams);
                } else {
                    int width = appBarLayout.getWidth();
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                    layoutParams.height = Math.round(width * 0.34f) + DeviceScreenUtil.getInstance()
                            .convertDpToPixel(20.0f);
                    appBarLayout.setLayoutParams(layoutParams);
                    updateViewVisibitity(tv_no_item_slider, View.GONE);
                    setupViewPager();
                    startTimer();
                }
            }

        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }
    }
}
