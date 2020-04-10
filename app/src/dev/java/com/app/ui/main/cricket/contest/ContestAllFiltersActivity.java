package com.app.ui.main.cricket.contest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.google.gson.JsonIOException;

import org.json.JSONException;
import org.json.JSONObject;

//Link : http://172.105.49.123/admin/apis/mobile/v1/
//
//        Name : Get match Contest
//        Url : get_match_contest/:match_id/:match_unique_id
//        Method : map
//        Params : match_id(mandatory), match_unique_id(mandatory)
//        Filter-params(optional) : entry1_50:y, entry51_100:y, entry101_1000:y,
//        entry1001_abv:y, total_team2:y, total_team3_10:y, total_team11_100:y,
//        total_team101_1000:y, total_team_1001:y, total_price1_10k:y, total_price10k_10lac:y,
//        total_price1lac_10lac:y, total_price10lac_25lac:y, total_price25lac:y,
//        contest_type_single:y, contest_type_multi:y, confirmed:y,single_winner:y, multi_winner:y
//        Headers Params : lang(mandatory), device-id(mandatory), token(mandatory),
//        devicetype(mandatory), deviceinfo(mandatory), appinfo(mandatory)
public class ContestAllFiltersActivity extends AppBaseActivity {

    private TextView entry1_50;
    private TextView team_2;
    private TextView team_3_10;
    private TextView prize1_10000;
    private TextView team_11_100;
    private TextView team_101_1000;
    private TextView team_1001_above;
    private TextView entry51_100;
    private TextView entry101_1000;
    private TextView entry1001_above;
    private TextView prize10000_1_lakh;
    private TextView prize1_lakh_10_lakh;
    private TextView prize10_lakh_25_lakh;
    private TextView prize25_lakh_above;
    private TextView single_entry;
    private TextView multi_entry;
    private TextView single_winnner;
    private TextView multi_winner;
    private TextView confirmed;
    private JSONObject filter_json = new JSONObject();
    private TextView filter_apply;
    private Bundle bundle;
    private TextView tv_contest_clear_filter;
    private ImageView iv_back;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_filter_contest;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        iv_back = findViewById(R.id.iv_back);
        tv_contest_clear_filter = findViewById(R.id.tv_contest_clear_filter);
        entry1_50 = findViewById(R.id.entry1_50);
        entry51_100 = findViewById(R.id.entry51_100);
        entry101_1000 = findViewById(R.id.entry101_1000);
        entry1001_above = findViewById(R.id.entry1001_above);
        team_2 = findViewById(R.id.team_2);
        team_3_10 = findViewById(R.id.team_3_10);
        team_11_100 = findViewById(R.id.team_11_100);
        team_101_1000 = findViewById(R.id.team_101_1000);
        team_1001_above = findViewById(R.id.team_1001_above);
        prize1_10000 = findViewById(R.id.prize1_10000);
        prize10000_1_lakh = findViewById(R.id.prize10000_1_lakh);
        prize1_lakh_10_lakh = findViewById(R.id.prize1_lakh_10_lakh);
        prize10_lakh_25_lakh = findViewById(R.id.prize10_lakh_25_lakh);
        prize25_lakh_above = findViewById(R.id.prize25_lakh_above);

        single_entry = findViewById(R.id.single_entry);
        multi_entry = findViewById(R.id.multi_entry);
        single_winnner = findViewById(R.id.single_winnner);
        multi_winner = findViewById(R.id.multi_winner);

        confirmed = findViewById(R.id.confirmed);

        filter_apply = findViewById(R.id.filter_apply);
        filter_apply.setOnClickListener(this);

        entry1_50.setOnClickListener(this);
        entry51_100.setOnClickListener(this);
        entry101_1000.setOnClickListener(this);
        entry1001_above.setOnClickListener(this);

        team_2.setOnClickListener(this);
        team_3_10.setOnClickListener(this);
        team_11_100.setOnClickListener(this);
        team_101_1000.setOnClickListener(this);
        team_1001_above.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        prize1_10000.setOnClickListener(this);
        prize10000_1_lakh.setOnClickListener(this);
        prize1_lakh_10_lakh.setOnClickListener(this);
        prize10_lakh_25_lakh.setOnClickListener(this);
        prize25_lakh_above.setOnClickListener(this);

        single_entry.setOnClickListener(this);
        single_winnner.setOnClickListener(this);
        multi_entry.setOnClickListener(this);
        multi_winner.setOnClickListener(this);
        confirmed.setOnClickListener(this);
        tv_contest_clear_filter.setOnClickListener(this);
        try {
            setfilter();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString("data") != null) {
                try {
                    setSelectedFilter(new JSONObject(bundle.getString("data")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void backBtn(View view) {
        finish();
    }

    private void setSelectedFilter(JSONObject data) throws JSONException {
        if (data.getString("entry1_50").equals("y")) {
            filter_json.put("entry1_50", "y");
            entry1_50.setSelected(true);
        }
        if (data.getString("entry51_100").equals("y")) {
            filter_json.put("entry51_100", "y");
            entry51_100.setSelected(true);
        }
        if (data.getString("entry101_1000").equals("y")) {
            filter_json.put("entry101_1000", "y");
            entry101_1000.setSelected(true);
        }
        if (data.getString("entry1001_abv").equals("y")) {
            filter_json.put("entry1001_abv", "y");
            entry1001_above.setSelected(true);
        }


        if (data.getString("total_team2").equals("y")) {
            filter_json.put("total_team2", "y");
            team_2.setSelected(true);

        }
        if (data.getString("total_team3_10").equals("y")) {
            filter_json.put("total_team3_10", "y");
            team_3_10.setSelected(true);
        }
        if (data.getString("total_team11_100").equals("y")) {
            filter_json.put("total_team11_100", "y");
            team_11_100.setSelected(true);
        }
        if (data.getString("total_team101_1000").equals("y")) {
            filter_json.put("total_team101_1000", "y");
            team_101_1000.setSelected(true);
        }
        if (data.getString("total_team_1001").equals("y")) {
            filter_json.put("total_team_1001", "y");
            team_1001_above.setSelected(true);
        }


        if (data.getString("total_price1_10k").equals("y")) {
            filter_json.put("total_price1_10k", "y");
            prize1_10000.setSelected(true);
        }
        if (data.getString("total_price10k_10lac").equals("y")) {
            filter_json.put("total_price10k_10lac", "y");
            prize10000_1_lakh.setSelected(true);
        }
        if (data.getString("total_price1lac_10lac").equals("y")) {
            filter_json.put("total_price1lac_10lac", "y");
            prize1_lakh_10_lakh.setSelected(true);
        }
        if (data.getString("total_price10lac_25lac").equals("y")) {
            filter_json.put("total_price10lac_25lac", "y");
            prize10_lakh_25_lakh.setSelected(true);
        }
        if (data.getString("total_price25lac").equals("y")) {
            filter_json.put("total_price25lac", "y");
            prize25_lakh_above.setSelected(true);
        }


        if (data.getString("contest_type_single").equals("y")) {
            filter_json.put("contest_type_single", "y");
            single_entry.setSelected(true);

        }
        if (data.getString("contest_type_multi").equals("y")) {
            filter_json.put("contest_type_multi", "y");
            multi_entry.setSelected(true);

        }
        if (data.getString("confirmed").equals("y")) {
            filter_json.put("confirmed", "y");
            confirmed.setSelected(true);
        }
        if (data.getString("single_winner").equals("y")) {
            filter_json.put("single_winner", "y");
            single_winnner.setSelected(true);

        }
        if (data.getString("multi_winner").equals("y")) {
            filter_json.put("multi_winner", "y");
            multi_winner.setSelected(true);
        }


    }

    private void setfilter() throws JSONException {
        filter_json.put("entry1_50", "n");
        filter_json.put("entry51_100", "n");
        filter_json.put("entry101_1000", "n");
        filter_json.put("entry1001_abv", "n");

        filter_json.put("total_team2", "n");
        filter_json.put("total_team3_10", "n");
        filter_json.put("total_team11_100", "n");
        filter_json.put("total_team101_1000", "n");
        filter_json.put("total_team_1001", "n");

        filter_json.put("total_price1_10k", "n");
        filter_json.put("total_price10k_10lac", "n");
        filter_json.put("total_price1lac_10lac", "n");
        filter_json.put("total_price10lac_25lac", "n");
        filter_json.put("total_price25lac", "n");

        filter_json.put("contest_type_single", "n");
        filter_json.put("contest_type_multi", "n");
        filter_json.put("confirmed", "n");
        filter_json.put("single_winner", "n");
        filter_json.put("multi_winner", "n");
    }

    @Override
    public void onClick(View v) {

        try {
            switch (v.getId()) {
                case R.id.entry1_50:
                    if (entry1_50.isSelected()) {
                        entry1_50.setSelected(false);
                        filter_json.put("entry1_50", "n");
                    } else {
                        filter_json.put("entry1_50", "y");
                        entry1_50.setSelected(true);
                    }
                    break;
                case R.id.entry51_100:
                    if (entry51_100.isSelected()) {
                        entry51_100.setSelected(false);
                        filter_json.put("entry51_100", "n");
                    } else {
                        entry51_100.setSelected(true);
                        filter_json.put("entry51_100", "y");
                    }
                    break;
                case R.id.entry101_1000:
                    if (entry101_1000.isSelected()) {
                        entry101_1000.setSelected(false);
                        filter_json.put("entry101_1000", "n");
                    } else {
                        entry101_1000.setSelected(true);
                        filter_json.put("entry101_1000", "y");
                    }
                    break;
                case R.id.entry1001_above:
                    if (entry1001_above.isSelected()) {
                        entry1001_above.setSelected(false);
                        filter_json.put("entry1001_abv", "n");
                    } else {
                        entry1001_above.setSelected(true);
                        filter_json.put("entry1001_abv", "y");
                    }
                    break;
                case R.id.team_2:
                    if (team_2.isSelected()) {
                        team_2.setSelected(false);
                        filter_json.put("total_team2", "n");
                    } else {
                        team_2.setSelected(true);
                        filter_json.put("total_team2", "y");
                    }
                    break;
                case R.id.team_3_10:
                    if (team_3_10.isSelected()) {
                        team_3_10.setSelected(false);
                        filter_json.put("total_team3_10", "n");
                    } else {
                        team_3_10.setSelected(true);
                        filter_json.put("total_team3_10", "y");
                    }
                    break;
                case R.id.team_11_100:
                    if (team_11_100.isSelected()) {
                        team_11_100.setSelected(false);
                        filter_json.put("total_team11_100", "n");
                    } else {
                        team_11_100.setSelected(true);
                        filter_json.put("total_team11_100", "y");
                    }
                    break;
                case R.id.team_101_1000:
                    if (team_101_1000.isSelected()) {
                        team_101_1000.setSelected(false);
                        filter_json.put("total_team101_1000", "n");
                    } else {
                        team_101_1000.setSelected(true);
                        filter_json.put("total_team101_1000", "y");
                    }
                    break;
                case R.id.team_1001_above:
                    if (team_1001_above.isSelected()) {
                        team_1001_above.setSelected(false);
                        filter_json.put("total_team_1001", "n");
                    } else {
                        team_1001_above.setSelected(true);
                        filter_json.put("total_team_1001", "y");
                    }
                    break;
                case R.id.prize1_10000:
                    if (prize1_10000.isSelected()) {
                        prize1_10000.setSelected(false);
                        filter_json.put("total_price1_10k", "n");
                    } else {
                        prize1_10000.setSelected(true);
                        filter_json.put("total_price1_10k", "y");
                    }
                    break;
                case R.id.prize10000_1_lakh:
                    if (prize10000_1_lakh.isSelected()) {
                        prize10000_1_lakh.setSelected(false);
                        filter_json.put("total_price10k_10lac", "n");
                    } else {
                        prize10000_1_lakh.setSelected(true);
                        filter_json.put("total_price10k_10lac", "y");
                    }
                    break;
                case R.id.prize1_lakh_10_lakh:
                    if (prize1_lakh_10_lakh.isSelected()) {
                        prize1_lakh_10_lakh.setSelected(false);
                        filter_json.put("total_price1lac_10lac", "n");
                    } else {
                        filter_json.put("total_price1lac_10lac", "y");
                        prize1_lakh_10_lakh.setSelected(true);
                    }
                    break;
                case R.id.prize10_lakh_25_lakh:
                    if (prize10_lakh_25_lakh.isSelected()) {
                        prize10_lakh_25_lakh.setSelected(false);
                        filter_json.put("total_price10lac_25lac", "n");
                    } else {
                        filter_json.put("total_price10lac_25lac", "y");
                        prize10_lakh_25_lakh.setSelected(true);
                    }
                    break;
                case R.id.prize25_lakh_above:
                    if (prize25_lakh_above.isSelected()) {
                        prize25_lakh_above.setSelected(false);
                        filter_json.put("total_price25lac", "n");
                    } else {
                        prize25_lakh_above.setSelected(true);
                        filter_json.put("total_price25lac", "y");
                    }
                    break;
                case R.id.single_entry:
                    if (single_entry.isSelected()) {
                        single_entry.setSelected(false);
                        filter_json.put("contest_type_single", "n");
                    } else {
                        single_entry.setSelected(true);
                        filter_json.put("contest_type_single", "y");
                    }
                    break;
                case R.id.single_winnner:
                    if (single_winnner.isSelected()) {
                        single_winnner.setSelected(false);
                        filter_json.put("single_winner", "n");
                    } else {
                        single_winnner.setSelected(true);
                        filter_json.put("single_winner", "y");
                    }
                    break;
                case R.id.multi_entry:
                    if (multi_entry.isSelected()) {
                        multi_entry.setSelected(false);
                        filter_json.put("contest_type_multi", "n");
                    } else {
                        multi_entry.setSelected(true);
                        filter_json.put("contest_type_multi", "y");
                    }
                    break;
                case R.id.multi_winner:
                    if (multi_winner.isSelected()) {
                        multi_winner.setSelected(false);
                        filter_json.put("multi_winner", "n");
                    } else {
                        multi_winner.setSelected(true);
                        filter_json.put("multi_winner", "y");
                    }
                    break;

                case R.id.confirmed:
                    if (confirmed.isSelected()) {
                        confirmed.setSelected(false);
                        filter_json.put("confirmed", "n");
                    } else {
                        confirmed.setSelected(true);
                        filter_json.put("confirmed", "y");
                    }
                    break;
                case R.id.filter_apply:
                    applyFilter();
                    break;
                case R.id.tv_contest_clear_filter:
                    initializeComponent();
                    setfilter();
                    resetAll();
                    break;
                case R.id.iv_back:
                    backBtn(null);
                    break;

            }
        } catch (JsonIOException | JSONException e) {

        }

    }

    private void resetAll() {
        entry1_50.setSelected(false);
        entry51_100.setSelected(false);
        entry101_1000.setSelected(false);
        entry1001_above.setSelected(false);

        team_2.setSelected(false);
        team_3_10.setSelected(false);
        team_11_100.setSelected(false);
        team_1001_above.setSelected(false);
        team_101_1000.setSelected(false);

        prize1_10000.setSelected(false);
        prize10000_1_lakh.setSelected(false);
        prize1_lakh_10_lakh.setSelected(false);
        prize10_lakh_25_lakh.setSelected(false);
        prize25_lakh_above.setSelected(false);

        confirmed.setSelected(false);

        single_entry.setSelected(false);
        single_winnner.setSelected(false);
        multi_winner.setSelected(false);
        multi_entry.setSelected(false);
    }

    private void applyFilter() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(DATA, filter_json.toString());
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        supportFinishAfterTransition();
    }
}
