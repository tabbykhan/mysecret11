package com.app.ui.main.soccer.team.createteam;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.appbase.ViewPagerAdapter;
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.PlayerModel;
import com.app.model.TeamModel;
import com.app.model.TeamSettingModel;
import com.app.model.webrequestmodel.CreateTeamRequestModel;
import com.app.model.webrequestmodel.UpdateTeamRequestModel;
import com.app.model.webresponsemodel.CreateTeamResponseModel;
import com.app.model.webresponsemodel.MatchPlayersResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.soccer.team.playerpreview.SoccerTeamPreviewDialog;
import com.customviews.ProgressBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.medy.retrofitwrapper.WebRequest;

import java.util.ArrayList;
import java.util.List;

public class CreateSoccerTeamActivity extends AppBaseActivity implements MatchTimerListener {

    public float totalSelectedPlayerPoints = 0;
    public int totalTeam1Players = 0;
    public int totalTeam2Players = 0;
    public int currentSortBy = -1;
    public int currentSortType = 0;
    ToolbarFragment toolbarFragment;
    ImageView iv_man;
    TabLayout create_team_tabs;
    ViewPager view_pager;
    ViewPagerAdapter adapter;
    LinearLayout ll_bottom;
    LinearLayout ll_team_preview;
    TextView tv_continue;
    int totalSelectedWckitKeeper = 0;
    int totalSelectedBetsman = 0;
    int totalSelectedAllrounder = 0;
    int totalSelectedBowler = 0;
    CustomerTeamModel customerExistTeam;
    MatchModel matchModelWithPlayers;
    private TextView tv_max_player_in_a_team;
    private LinearLayout ll_team_detail;
    private TextView tv_selected_player;
    private TextView tv_team1_name;
    private ImageView iv_team_one;
    private TextView tv_team1players;
    private TextView tv_team2players;
    private TextView tv_team2_name;
    private ImageView iv_team_two;
    private TextView tv_credit_limit;
    private ProgressBarView progressBar_team;
    SoccerPlayersFragment.UpdateSelectedPlayer updateSelectedPlayer =
            new SoccerPlayersFragment.UpdateSelectedPlayer() {
        @Override
        public void updatePlayer(PlayerModel playerModel) {
            calculateData();
        }

        @Override
        public int getTotalTeam1Players() {
            return totalTeam1Players;
        }

        @Override
        public int getTotalTeam2Players() {
            return totalTeam2Players;
        }

        @Override
        public int getPlayerCountByType(int type) {
            return getPlayerCount(type);
        }

        @Override
        public float getSelectedPlayerTotalPoints() {
            return totalSelectedPlayerPoints;
        }

        @Override
        public void reset() {
            checkselectedplayerType();

        }

        @Override
        public int getSelectedPlayer() {
            return getTotalSelectedPlayers();
        }

        @Override
        public TeamSettingModel getTeamSetting() {
            return CreateSoccerTeamActivity.this.getTeamSetting();
        }

        @Override
        public TeamModel getTeam1() {
            return matchModelWithPlayers.getTeam1();
        }

        @Override
        public TeamModel getTeam2() {
            return matchModelWithPlayers.getTeam2();
        }
    };
    private int selected_player_type = 0;
    private SoccerPlayersFragment playersFragment2;

    private void checkselectedplayerType() {
        List<PlayerModel> allPlayers;
        TeamModel team1 = matchModelWithPlayers.getTeam1();
        if (selected_player_type == 0) {
             allPlayers = matchModelWithPlayers.getGoalkeepers();
            for (PlayerModel playerModel : allPlayers) {
                if (playerModel.isSelected()) {
                    playerModel.setSelected(false);
                    totalSelectedWckitKeeper--;
                    totalSelectedPlayerPoints -= playerModel.getCredits();
                    if (playerModel.getTeam_id() == team1.getId()) {
                        totalTeam1Players--;
                    } else {
                        totalTeam2Players--;
                    }
                }
            }
            updateUi();
        } else if (selected_player_type == 1) {
            allPlayers = matchModelWithPlayers.getDefenders();
            for (PlayerModel playerModel : allPlayers) {
                if (playerModel.isSelected()) {
                    totalSelectedBetsman--;
                    playerModel.setSelected(false);
                    totalSelectedPlayerPoints -= playerModel.getCredits();
                    if (playerModel.getTeam_id() == team1.getId()) {
                        totalTeam1Players--;
                    } else {
                        totalTeam2Players--;
                    }
                }
            }
            updateUi();
        } else if (selected_player_type == 2) {
            allPlayers = matchModelWithPlayers.getMidfielders();
            for (PlayerModel playerModel : allPlayers) {
                if (playerModel.isSelected()) {
                    totalSelectedAllrounder--;
                    playerModel.setSelected(false);
                    totalSelectedPlayerPoints -= playerModel.getCredits();
                    if (playerModel.getTeam_id() == team1.getId()) {
                        totalTeam1Players--;
                    } else {
                        totalTeam2Players--;
                    }
                }
            }
            updateUi();
        } else if (selected_player_type == 3) {
            allPlayers = matchModelWithPlayers.getForwards();
            for (PlayerModel playerModel : allPlayers) {
                if (playerModel.isSelected()) {
                    totalSelectedBowler--;
                    playerModel.setSelected(false);
                    totalSelectedPlayerPoints -= playerModel.getCredits();
                    if (playerModel.getTeam_id() == team1.getId()) {
                        totalTeam1Players--;
                    } else {
                        totalTeam2Players--;
                    }
                }
            }
            updateUi();
        }

    }


    public CustomerTeamModel getCustomerTeam() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String string = getIntent().getExtras().getString(DATA);
            if (isValidString(string)) {
                return new Gson().fromJson(string, CustomerTeamModel.class);
            }
        }
        return null;
    }

    public long getCustomerTeamId() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? -1 : extras.getLong(DATA1, -1);
    }

    public long getMatchContestId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null) ? -1 : extras.getLong(DATA2, -1);
    }

    public String getEntryFeeSuggestString() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA4, ""));
    }

    public List<String> getEntryFeeSuggest() {
        Bundle extras = getIntent().getExtras();
        String Siggest = (extras == null ? "" : extras.getString(DATA4, ""));
        if (isValidString(Siggest)) {
            return new Gson().fromJson(Siggest, new TypeToken<List<String>>() {
            }.getType());
        }
        return new ArrayList<>();
    }

    public String getEntryFee() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA5, ""));
    }

    public String getMaxEntryFee() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA6, ""));
    }

    public String getMultiplier() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA7, ""));
    }

    public String getMoreEntryFree() {
        Bundle extras = getIntent().getExtras();
        return (extras == null ? "" : extras.getString(DATA8, ""));
    }


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public TeamSettingModel getTeamSetting() {
        if (matchModelWithPlayers != null)
            return matchModelWithPlayers.getTeam_settings();
        return null;
    }


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
        return R.layout.activity_soccer_create_team;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();

        customerExistTeam = getCustomerTeam();
        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }
        iv_man = findViewById(R.id.iv_man);
        updateViewVisibitity(iv_man, View.INVISIBLE);
        tv_max_player_in_a_team = findViewById(R.id.tv_max_player_in_a_team);
        ll_team_detail = findViewById(R.id.ll_team_detail);
        tv_selected_player = findViewById(R.id.tv_selected_player);
        tv_team1_name = findViewById(R.id.tv_team1_name);
        iv_team_one = findViewById(R.id.iv_team_one);
        tv_team1players = findViewById(R.id.tv_team1players);
        tv_team2players = findViewById(R.id.tv_team2players);
        tv_team2_name = findViewById(R.id.tv_team2_name);
        iv_team_two = findViewById(R.id.iv_team_two);
        tv_credit_limit = findViewById(R.id.tv_credit_limit);

        progressBar_team = findViewById(R.id.progressBar_team);
        progressBar_team.initialize();


        ll_bottom = findViewById(R.id.ll_bottom);
        ll_team_preview = findViewById(R.id.ll_team_preview);
        tv_continue = findViewById(R.id.tv_continue);
//        if(MyApplication.getInstance().isIs_5_player_match()){
//            tv_continue.setText("SAVE TEAM");
//        }
        ll_team_preview.setOnClickListener(this);
        tv_continue.setOnClickListener(this);

        updateTitle();
        setMatchData();
        getMatchPlayers();
    }

    private void updateTitle() {
        if (customerExistTeam == null) {
            toolbarFragment.setLeftTitle("CREATE YOUR SQUAD");
        } else {
            if (getCustomerTeamId() == -1) {
                toolbarFragment.setLeftTitle("CLONE YOUR SQUAD");
            } else {
                toolbarFragment.setLeftTitle("UPDATE YOUR SQUAD");
            }
        }
    }

    private void setMatchData() {
        if (getMatchModel() != null) {
            toolbarFragment.setLeftTitle(getMatchModel().getRemainTimeText() + " Left");

            boolean matchTimeExpire = getMatchModel().isMatchTimeExpire();
            if (matchTimeExpire) {
                showMatchExpireDialog();
            }
        }
    }

    private void updateMan() {

        float selectedPositionX = progressBar_team.getSelectedPositionX();
        float v = iv_man.getWidth() * 0.5f;
        if (selectedPositionX == 0) {
            updateViewVisibitity(iv_man, View.INVISIBLE);
            iv_man.clearAnimation();
            ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_man, "translationX", 0f);
            translationX.setDuration(100);
            translationX.start();

        } else {
            updateViewVisibitity(iv_man, View.VISIBLE);
            iv_man.clearAnimation();
            ObjectAnimator translationX = ObjectAnimator.ofFloat(iv_man, "translationX", selectedPositionX - v);
            translationX.setDuration(100);
            translationX.start();
        }
    }


    private void setupData() {
        if (matchModelWithPlayers == null)
            return;
        CustomerTeamModel customerTeam = customerExistTeam;
        if (customerTeam != null) {
            List<PlayerModel> selectedPlayers = customerTeam.getGoalkeepers();
            List<PlayerModel> allPlayers = matchModelWithPlayers.getGoalkeepers();
            if (allPlayers != null && allPlayers.size() > 0 && selectedPlayers != null && selectedPlayers.size() > 0) {
                for (PlayerModel player : allPlayers) {
                    if (selectedPlayers.indexOf(player) >= 0) {
                        player.setSelected(true);
                    }
                }
            }
            selectedPlayers = customerTeam.getDefenders();
            allPlayers = matchModelWithPlayers.getDefenders();
            if (allPlayers != null && allPlayers.size() > 0 && selectedPlayers != null && selectedPlayers.size() > 0) {
                for (PlayerModel player : allPlayers) {
                    if (selectedPlayers.indexOf(player) >= 0) {
                        player.setSelected(true);
                    }
                }
            }
            selectedPlayers = customerTeam.getMidfielders();
            allPlayers = matchModelWithPlayers.getMidfielders();
            if (allPlayers != null && allPlayers.size() > 0 && selectedPlayers != null && selectedPlayers.size() > 0) {
                for (PlayerModel player : allPlayers) {
                    if (selectedPlayers.indexOf(player) >= 0) {
                        player.setSelected(true);
                    }
                }
            }
            selectedPlayers = customerTeam.getForwards();
            allPlayers = matchModelWithPlayers.getForwards();
            if (allPlayers != null && allPlayers.size() > 0 && selectedPlayers != null && selectedPlayers.size() > 0) {
                for (PlayerModel player : allPlayers) {
                    if (selectedPlayers.indexOf(player) >= 0) {
                        player.setSelected(true);
                    }
                }
            }
            calculateData();
        }
        setHeaderData();
        setupViewPager();
        updateUi();
    }

    private void setHeaderData() {

        TeamModel team1 = matchModelWithPlayers.getTeam1();
        TeamModel team2 = matchModelWithPlayers.getTeam2();
        tv_team1_name.setText(team1.getName(1));
        loadImage(this, iv_team_one, null, team1.getImage(), R.drawable.no_image);
        tv_team2_name.setText(team2.getName(1));
        loadImage(this, iv_team_two, null, team2.getImage(), R.drawable.no_image);
        String max_from_one_team = String.format("Max %s players from a team", getTeamSetting().getMAX_PLAYERS_PER_TEAM());
        tv_max_player_in_a_team.setText(max_from_one_team);

    }

    private void setupViewPager() {

        create_team_tabs = findViewById(R.id.create_team_tabs);
        view_pager = findViewById(R.id.view_pager);
        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                //adapter.getItem(i).onPageSelected();
                selected_player_type = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        adapter = new ViewPagerAdapter(getFm());

        playersFragment2 = new SoccerPlayersFragment();
        playersFragment2.setPlayersList(matchModelWithPlayers.getGoalkeepers());
        playersFragment2.setType(1);
        playersFragment2.setMin(getTeamSetting().getMINGOALKEEPER());
        playersFragment2.setMax(getTeamSetting().getMAXGOALKEEPER());
        playersFragment2.setUpdateSelectedPlayer(updateSelectedPlayer);
        adapter.addFragment(playersFragment2, "GK (0)");


        SoccerPlayersFragment playersFragment1 = new SoccerPlayersFragment();
        playersFragment1.setPlayersList(matchModelWithPlayers.getDefenders());
        playersFragment1.setType(2);
        playersFragment1.setMin(getTeamSetting().getMINDEFENDER());
        playersFragment1.setMax(getTeamSetting().getMAXDEFENDER());
        playersFragment1.setUpdateSelectedPlayer(updateSelectedPlayer);
        adapter.addFragment(playersFragment1, "DEF (0)");

        SoccerPlayersFragment playersFragment3 = new SoccerPlayersFragment();
        playersFragment3.setPlayersList(matchModelWithPlayers.getMidfielders());
        playersFragment3.setType(3);
        playersFragment3.setMin(getTeamSetting().getMINMIDFIELDER());
        playersFragment3.setMax(getTeamSetting().getMAXMIDFIELDER());
        playersFragment3.setUpdateSelectedPlayer(updateSelectedPlayer);
        adapter.addFragment(playersFragment3, "MID (0)");


        SoccerPlayersFragment playersFragment = new SoccerPlayersFragment();
        playersFragment.setPlayersList(matchModelWithPlayers.getForwards());
        playersFragment.setType(4);
        playersFragment.setMin(getTeamSetting().getMINFORWARD());
        playersFragment.setMax(getTeamSetting().getMAXFORWARD());
        playersFragment.setUpdateSelectedPlayer(updateSelectedPlayer);
        adapter.addFragment(playersFragment, "ST (0)");


        view_pager.setAdapter(adapter);
        create_team_tabs.setupWithViewPager(view_pager);
        createTabs();

        int currentItem = view_pager.getCurrentItem();
        AppBaseFragment item = adapter.getItem(currentItem);
        ((SoccerPlayersFragment) item).performDefaultSort();


    }

    public int getPlayerCount(int type) {
        if (type == 1) {
            return totalSelectedWckitKeeper;
        } else if (type == 2) {
            return totalSelectedBetsman;
        } else if (type == 3) {
            return totalSelectedAllrounder;
        } else if (type == 4) {
            return totalSelectedBowler;
        }
        return 0;
    }


    private void calculateData() {
        if (matchModelWithPlayers == null)
            return;

        totalSelectedPlayerPoints = 0;
        totalSelectedWckitKeeper = 0;
        totalSelectedBetsman = 0;
        totalSelectedAllrounder = 0;
        totalSelectedBowler = 0;
        totalTeam1Players = 0;
        totalTeam2Players = 0;

        TeamModel team1 = matchModelWithPlayers.getTeam1();
        List<PlayerModel> allPlayers = matchModelWithPlayers.getGoalkeepers();
        for (PlayerModel playerModel : allPlayers) {
            if (playerModel.isSelected()) {
                totalSelectedWckitKeeper++;
                totalSelectedPlayerPoints += playerModel.getCredits();
                if (playerModel.getTeam_id() == team1.getId()) {
                    totalTeam1Players++;
                } else {
                    totalTeam2Players++;
                }
            } else {
                Log.i("De selected--", "--" + playerModel.isSelected());
            }
        }

        allPlayers = matchModelWithPlayers.getDefenders();
        for (PlayerModel playerModel : allPlayers) {
            if (playerModel.isSelected()) {
                totalSelectedBetsman++;
                totalSelectedPlayerPoints += playerModel.getCredits();
                if (playerModel.getTeam_id() == team1.getId()) {
                    totalTeam1Players++;
                } else {
                    totalTeam2Players++;
                }
            }
        }

        allPlayers = matchModelWithPlayers.getMidfielders();
        for (PlayerModel playerModel : allPlayers) {
            if (playerModel.isSelected()) {
                totalSelectedAllrounder++;
                totalSelectedPlayerPoints += playerModel.getCredits();
                if (playerModel.getTeam_id() == team1.getId()) {
                    totalTeam1Players++;
                } else {
                    totalTeam2Players++;
                }
            }
        }

        allPlayers = matchModelWithPlayers.getForwards();
        for (PlayerModel playerModel : allPlayers) {
            if (playerModel.isSelected()) {
                totalSelectedBowler++;
                totalSelectedPlayerPoints += playerModel.getCredits();
                if (playerModel.getTeam_id() == team1.getId()) {
                    totalTeam1Players++;
                } else {
                    totalTeam2Players++;
                }
            }
        }


        updateUi();
    }

    private int getTotalSelectedPlayers() {
        return totalSelectedWckitKeeper + totalSelectedBetsman + totalSelectedAllrounder + totalSelectedBowler;
    }

    private void createTabs() {
        if (create_team_tabs == null)
            return;
        TabLayout.Tab tabAt = create_team_tabs.getTabAt(0);
        View inflate = LayoutInflater.from(this).inflate(R.layout.include_create_soccer_team_tab, null);
        inflate.setActivated(true);
        TextView tv_player_type = inflate.findViewById(R.id.tv_player_type);
        TextView tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        ImageView iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("GT");
        tv_player_type_count.setText(totalSelectedWckitKeeper + "");
        iv_player_type.setImageResource(R.drawable.ic_gk1);
        tabAt.setCustomView(inflate);

        Log.i("player size check--", "" + totalSelectedWckitKeeper);
        tabAt = create_team_tabs.getTabAt(1);
        inflate = LayoutInflater.from(this).inflate(R.layout.include_create_soccer_team_tab, null);
        tv_player_type = inflate.findViewById(R.id.tv_player_type);
        tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("DEF");
        tv_player_type_count.setText(totalSelectedBetsman + "");
        iv_player_type.setImageResource(R.drawable.def);
        tabAt.setCustomView(inflate);

        tabAt = create_team_tabs.getTabAt(2);
        inflate = LayoutInflater.from(this).inflate(R.layout.include_create_soccer_team_tab, null);
        tv_player_type = inflate.findViewById(R.id.tv_player_type);
        tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("MID");
        tv_player_type_count.setText(totalSelectedAllrounder + "");
        iv_player_type.setImageResource(R.drawable.def);
        tabAt.setCustomView(inflate);


        tabAt = create_team_tabs.getTabAt(3);
        inflate = LayoutInflater.from(this).inflate(R.layout.include_create_soccer_team_tab, null);
        tv_player_type = inflate.findViewById(R.id.tv_player_type);
        tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("ST");
        tv_player_type_count.setText(totalSelectedBowler + "");
        iv_player_type.setImageResource(R.drawable.def);
        tabAt.setCustomView(inflate);


    }

    private void updateUi() {
        if (create_team_tabs == null)
            return;

        TabLayout.Tab tabAt = create_team_tabs.getTabAt(0);
        View inflate = tabAt.getCustomView();
        TextView tv_player_type = inflate.findViewById(R.id.tv_player_type);
        TextView tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        ImageView iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("GT");
        tv_player_type_count.setText(totalSelectedWckitKeeper + "");
        iv_player_type.setImageResource(R.drawable.ic_gk1);


        tabAt = create_team_tabs.getTabAt(1);
        inflate = tabAt.getCustomView();
        tv_player_type = inflate.findViewById(R.id.tv_player_type);
        tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("DEF");
        tv_player_type_count.setText(totalSelectedBetsman + "");
        iv_player_type.setImageResource(R.drawable.def);



        tabAt = create_team_tabs.getTabAt(2);
        inflate = tabAt.getCustomView();
        tv_player_type = inflate.findViewById(R.id.tv_player_type);
        tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("MID");
        tv_player_type_count.setText(totalSelectedAllrounder + "");
        iv_player_type.setImageResource(R.drawable.def);


        tabAt = create_team_tabs.getTabAt(3);
        inflate = tabAt.getCustomView();
        tv_player_type = inflate.findViewById(R.id.tv_player_type);
        tv_player_type_count = inflate.findViewById(R.id.tv_player_type_count);
        iv_player_type = inflate.findViewById(R.id.iv_player_type);
        tv_player_type.setText("ST");
        tv_player_type_count.setText(totalSelectedBowler + "");
        iv_player_type.setImageResource(R.drawable.def);


        tv_team1players.setText(String.valueOf(totalTeam1Players));
        tv_team2players.setText(String.valueOf(totalTeam2Players));
        float v = getTeamSetting().getMAX_CREDITS() - totalSelectedPlayerPoints;
        String s = getValidDecimalFormat(v);
        s = s.replaceAll("\\.00", "");
        tv_credit_limit.setText(s);
        tv_selected_player.setText(getTotalSelectedPlayers() + "/" + getTeamSetting().getMAX_PLAYERS());

        progressBar_team.changeColor(getTotalSelectedPlayers());
        progressBar_team.setMaxPlayer(getTotalSelectedPlayers());
        //updateMan();

        if (getTotalSelectedPlayers() == getTeamSetting().getMAX_PLAYERS()) {
            tv_continue.setActivated(true);
        } else {
            tv_continue.setActivated(false);
        }
    }


    private CustomerTeamModel generateLatestCustomerTeam() {
        CustomerTeamModel customerTeamModel = new CustomerTeamModel();
        customerTeamModel.setTeam1(matchModelWithPlayers.getTeam1());
        customerTeamModel.setTeam2(matchModelWithPlayers.getTeam2());
        customerTeamModel.setId(customerExistTeam != null ? customerTeamModel.getId() : 0);
        customerTeamModel.setName(customerExistTeam != null ? customerTeamModel.getName() : "");

        PlayerModel alreadyCaption = null;
        PlayerModel alreadyVCCaption = null;
        PlayerModel alreadyMpp = null;
        if (customerExistTeam != null) {
            alreadyCaption = customerExistTeam.getCaptain();
            alreadyVCCaption = customerExistTeam.getVise_captain();
            alreadyMpp = customerExistTeam.getTrump();
        }
        customerTeamModel.setGoalkeepers(new ArrayList<PlayerModel>());
        customerTeamModel.setDefenders(new ArrayList<PlayerModel>());
        customerTeamModel.setMidfielders(new ArrayList<PlayerModel>());
        customerTeamModel.setForwards(new ArrayList<PlayerModel>());

        List<PlayerModel> allPlayers = matchModelWithPlayers.getGoalkeepers();
        if (allPlayers != null && allPlayers.size() > 0) {
            for (PlayerModel player : allPlayers) {
                player.setPlayerType(1);
                if (player.isSelected()) {
                    customerTeamModel.getGoalkeepers().add(player);

                    if (alreadyCaption != null && player.getPlayer_id().equals(alreadyCaption.getPlayer_id())) {
                        customerTeamModel.setCaptain(customerExistTeam.getCaptain());
                    }
                    if (alreadyVCCaption != null && player.getPlayer_id().equals(alreadyVCCaption.getPlayer_id())) {
                        customerTeamModel.setVise_captain(customerExistTeam.getVise_captain());
                    }
                    if (alreadyMpp != null && player.getPlayer_id().equals(alreadyMpp.getPlayer_id())) {
                        customerTeamModel.setTrump(customerExistTeam.getTrump());
                    }


                }
            }
        }

        allPlayers = matchModelWithPlayers.getDefenders();

        if (allPlayers != null && allPlayers.size() > 0) {
            for (PlayerModel player : allPlayers) {
                player.setPlayerType(2);
                if (player.isSelected()) {
                    customerTeamModel.getDefenders().add(player);

                    if (alreadyCaption != null && player.getPlayer_id().equals(alreadyCaption.getPlayer_id())) {
                        customerTeamModel.setCaptain(customerExistTeam.getCaptain());
                    }
                    if (alreadyVCCaption != null && player.getPlayer_id().equals(alreadyVCCaption.getPlayer_id())) {
                        customerTeamModel.setVise_captain(customerExistTeam.getVise_captain());
                    }
                    if (alreadyMpp != null && player.getPlayer_id().equals(alreadyMpp.getPlayer_id())) {
                        customerTeamModel.setTrump(customerExistTeam.getTrump());
                    }
                }
            }
        }

        allPlayers = matchModelWithPlayers.getMidfielders();
        if (allPlayers != null && allPlayers.size() > 0) {
            for (PlayerModel player : allPlayers) {
                player.setPlayerType(3);
                if (player.isSelected()) {
                    customerTeamModel.getMidfielders().add(player);

                    if (alreadyCaption != null && player.getPlayer_id().equals(alreadyCaption.getPlayer_id())) {
                        customerTeamModel.setCaptain(customerExistTeam.getCaptain());
                    }
                    if (alreadyVCCaption != null && player.getPlayer_id().equals(alreadyVCCaption.getPlayer_id())) {
                        customerTeamModel.setVise_captain(customerExistTeam.getVise_captain());
                    }
                    if (alreadyMpp != null && player.getPlayer_id().equals(alreadyMpp.getPlayer_id())) {
                        customerTeamModel.setTrump(customerExistTeam.getTrump());
                    }
                }
            }
        }

        allPlayers = matchModelWithPlayers.getForwards();
        if (allPlayers != null && allPlayers.size() > 0) {
            for (PlayerModel player : allPlayers) {
                player.setPlayerType(4);
                if (player.isSelected()) {
                    customerTeamModel.getForwards().add(player);

                    if (alreadyCaption != null && player.getPlayer_id().equals(alreadyCaption.getPlayer_id())) {
                        customerTeamModel.setCaptain(customerExistTeam.getCaptain());
                    }
                    if (alreadyVCCaption != null && player.getPlayer_id().equals(alreadyVCCaption.getPlayer_id())) {
                        customerTeamModel.setVise_captain(customerExistTeam.getVise_captain());
                    }
                    if (alreadyMpp != null && player.getPlayer_id().equals(alreadyMpp.getPlayer_id())) {
                        customerTeamModel.setTrump(customerExistTeam.getTrump());
                    }
                }
            }
        }
        return customerTeamModel;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_team_preview:
                openTeamPreviewDialog();
                break;

            case R.id.tv_continue:
                if (getTeamSetting() == null) {
                    showErrorMsg("Something went wrong.");
                    return;
                }
                if (getTotalSelectedPlayers() == getTeamSetting().getMAX_PLAYERS()) {
//                    if(MyApplication.getInstance().isIs_5_player_match()){
//                        callSaveTeam();
//                    }else {
//
//                    }
                    CustomerTeamModel customerTeamModel = generateLatestCustomerTeam();
                    Log.d("player obj", "--"+new Gson().toJson(customerTeamModel));
                    Bundle bundle = new Bundle();
                    bundle.putString(DATA, new Gson().toJson(customerTeamModel));
                    bundle.putLong(DATA1, getCustomerTeamId());
                    bundle.putLong(DATA2, getMatchContestId());
                    bundle.putString((DATA4), getEntryFeeSuggestString());
                    bundle.putString((DATA5), getEntryFee());
                    bundle.putString((DATA6), getMaxEntryFee());
                    bundle.putString((DATA7), getMultiplier());
                    bundle.putString((DATA8), getMoreEntryFree());
                    goToChooseCaptionActivity(bundle);
                } else {
                    int count = getTeamSetting().getMAX_PLAYERS() - getTotalSelectedPlayers();
                    String format = String.format("Pick %s more players to complete your team", count);
                    showErrorMsg(format);
                }
                break;
        }
    }

    private void goToChooseCaptionActivity(Bundle bundle) {
        Intent intent = new Intent(this, ChooseSoccerCaptionActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, ContestActivity.REQUEST_CREATE_TEAM);
        overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha);
    }


    private void openTeamPreviewDialog() {

        CustomerTeamModel customerTeamModel = generateLatestCustomerTeam();
        SoccerTeamPreviewDialog instance = SoccerTeamPreviewDialog.getInstance(null);
        instance.setCustomerTeamModel(customerTeamModel);
        instance.show(getFm(), instance.getClass().getSimpleName());

    }


    private void getMatchPlayers() {
        if (getMatchModel() != null) {
            displayProgressBar(false);
            if(MyApplication.getInstance().isIs_5_player_match()){
                getWebRequestHelper().getFiveMatchPlayers(getMatchModel().getId(), this);
            }else{
                getWebRequestHelper().getSoccerMatchPlayer(getMatchModel().getId(), this);
            }

        }
    }

    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412)
            return;
        switch (webRequest.getWebRequestId()) {
            case ID_SOCCER_MATCH_PLAYERS:
                handleMatchPlayersResponse(webRequest);
                break;
            case ID_5_PLAYER_TEAM_LIST:
                handleMatchPlayersResponse(webRequest);
                break;
            case ID_CREATE_5_PLAYER_TEAM:
                handleCreateCustomerTeamResponse(webRequest);
                break;
            case ID_UPDATE_CUSTOMER_TEAM:
                break;

        }

    }

    private void handleMatchPlayersResponse(WebRequest webRequest) {
        MatchPlayersResponseModel responsePojo = webRequest.getResponsePojo(MatchPlayersResponseModel.class);
        if (responsePojo == null)
            return;
        if (!responsePojo.isError()) {
            matchModelWithPlayers = responsePojo.getData();
            if (isFinishing())
                return;
            Log.i("activity result",
                    "-----goalkeep------"+matchModelWithPlayers.getGoalkeepers().size());
            setupData();
        } else {
            if (isFinishing())
                return;
            Log.i("activity result", "-----6-----------");
            showErrorMsg(responsePojo.getMessage());
        }

    }


    @Override
    public void onMatchTimeUpdate() {
        if (isFinishing()) return;

        setMatchData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContestActivity.REQUEST_CREATE_TEAM) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK, data);
                supportFinishAfterTransition();
                Log.i("activity result", "---1--------------");
            }else{
                Log.i("activity result", "-----2------------");
            }
        }
    }

    public void refreshOtherFragmentData() {
        int currentItem = view_pager.getCurrentItem();
        for (int i = 0; i < 4; i++) {
            if (currentItem != i) {
                adapter.getItem(i).onPageSelected();
            }
        }
    }

    public void performFilterOnOtherFragment(int currentSortBy, int currentSortType) {
        this.currentSortBy = currentSortBy;
        this.currentSortType = currentSortType;
        int currentItem = view_pager.getCurrentItem();
        for (int i = 0; i < 4; i++) {
            if (currentItem != i) {
                AppBaseFragment item = adapter.getItem(i);
                ((SoccerPlayersFragment) item).perFormFilter(currentSortBy, currentSortType);
            }
        }
    }



    private void callSaveTeam() {
        if (getMatchModel() != null && getUserModel() != null) {

            displayProgressBar(false);
            if (getCustomerTeamId() == -1) {
                CreateTeamRequestModel requestModel = new CreateTeamRequestModel();
                requestModel.match_unique_id = getMatchModel().getMatch_id();
                CustomerTeamModel customerTeamModel=generateLatestCustomerTeam();
                List<PlayerModel> selectedPlayers = new ArrayList<>();
                selectedPlayers.addAll(customerTeamModel.getGoalkeepers());
                selectedPlayers.addAll(customerTeamModel.getDefenders());
                selectedPlayers.addAll(customerTeamModel.getMidfielders());
                selectedPlayers.addAll(customerTeamModel.getForwards());
                requestModel.player_json.addAll(selectedPlayers);
                Log.i("Player create", new Gson().toJson(requestModel));
                getWebRequestHelper().createfivePlayerTeam(requestModel, this);
            } else {
                UpdateTeamRequestModel requestModel = new UpdateTeamRequestModel();
                requestModel.customer_team_id = getCustomerTeamId();
                requestModel.match_unique_id = getMatchModel().getMatch_id();
                CustomerTeamModel customerTeamModel=generateLatestCustomerTeam();
                List<PlayerModel> selectedPlayers = new ArrayList<>();
                selectedPlayers.addAll(customerTeamModel.getGoalkeepers());
                selectedPlayers.addAll(customerTeamModel.getDefenders());
                selectedPlayers.addAll(customerTeamModel.getMidfielders());
                selectedPlayers.addAll(customerTeamModel.getForwards());
                requestModel.player_json.addAll(selectedPlayers);

             //   getWebRequestHelper().updateCustomerTeam(requestModel, this); update customer
                //   team
            }

        }
    }


    private void handleCreateCustomerTeamResponse(WebRequest webRequest) {
        CreateTeamResponseModel responsePojo = webRequest.getResponsePojo(CreateTeamResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            Log.i("activity result", "-----3------------");
          //  setResult(responsePojo.getData());
        } else {
            Log.i("activity result", "-----4------------");
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

}
