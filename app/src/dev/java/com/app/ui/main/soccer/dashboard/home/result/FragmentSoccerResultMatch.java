package com.app.ui.main.soccer.dashboard.home.result;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.R;
import com.app.appbase.AppBaseFragment;

public class FragmentSoccerResultMatch extends AppBaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() != null) {
            return getView();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_soccer_result_match;
    }
}
