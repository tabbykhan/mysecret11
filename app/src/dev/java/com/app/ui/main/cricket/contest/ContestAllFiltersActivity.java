package com.app.ui.main.cricket.contest;

import android.view.View;
import android.widget.TextView;

import com.R;
import com.app.appbase.AppBaseActivity;

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
//
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

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_filter_contest;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        entry1_50=findViewById(R.id.entry1_50);
        entry51_100=findViewById(R.id.entry51_100);
        entry101_1000=findViewById(R.id.entry101_1000);
        entry1001_above=findViewById(R.id.entry1001_above);
        team_2=findViewById(R.id.team_2);
        team_3_10=findViewById(R.id.team_3_10);
        team_11_100=findViewById(R.id.team_11_100);
        team_101_1000=findViewById(R.id.team_101_1000);
        team_1001_above=findViewById(R.id.team_1001_above);
        prize1_10000=findViewById(R.id.prize1_10000);


        entry1_50.setOnClickListener(this);
        entry51_100.setOnClickListener(this);
        entry101_1000.setOnClickListener(this);

        team_2.setOnClickListener(this);
        team_3_10.setOnClickListener(this);
        team_11_100.setOnClickListener(this);
        team_101_1000.setOnClickListener(this);
        team_1001_above.setOnClickListener(this);

        prize1_10000.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.entry1_50:
                if(entry1_50.isSelected()){
                    entry1_50.setSelected(false);
                }else{
                    entry1_50.setSelected(true);
                }
                break;
            case R.id.entry51_100:
                if(entry51_100.isSelected()){
                    entry51_100.setSelected(false);
                }else{
                    entry51_100.setSelected(true);
                }
                break;
            case R.id.entry101_1000:
                if(entry101_1000.isSelected()){
                    entry101_1000.setSelected(false);
                }else{
                    entry101_1000.setSelected(true);
                }
                break;
            case R.id.entry1001_above:
                if(entry1001_above.isSelected()){
                    entry1001_above.setSelected(false);
                }else{
                    entry1001_above.setSelected(true);
                }
                break;
            case R.id.team_2:
                if (team_2.isSelected()){
                    team_2.setSelected(false);
                }else{
                    team_2.setSelected(true);
                }
                break;
            case R.id.team_3_10:
                if (team_3_10.isSelected()){
                    team_3_10.setSelected(false);
                }else{
                    team_3_10.setSelected(true);
                }
                break;
            case R.id.team_11_100:
                if (team_11_100.isSelected()){
                    team_11_100.setSelected(false);
                }else{
                    team_11_100.setSelected(true);
                }
                break;
            case R.id.team_101_1000:
                if (team_101_1000.isSelected()){
                    team_101_1000.setSelected(false);
                }else{
                    team_101_1000.setSelected(true);
                }
                break;
            case R.id.team_1001_above:
                if (team_1001_above.isSelected()){
                    team_1001_above.setSelected(false);
                }else{
                    team_1001_above.setSelected(true);
                }
                break;
            case R.id.prize1_10000:
                if (prize1_10000.isSelected()){
                    prize1_10000.setSelected(false);
                }else{
                    prize1_10000.setSelected(true);
                }
                break;
        }

    }
}
