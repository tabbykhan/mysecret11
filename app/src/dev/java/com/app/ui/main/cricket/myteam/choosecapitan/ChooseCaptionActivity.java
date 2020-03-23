package com.app.ui.main.cricket.myteam.choosecapitan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.appbase.AppBaseFragment;
import com.app.model.CustomerTeamModel;
import com.app.model.MatchModel;
import com.app.model.PlayerModel;
import com.app.model.TeamModel;
import com.app.model.webrequestmodel.CreateTeamRequestModel;
import com.app.model.webrequestmodel.UpdateTeamRequestModel;
import com.app.model.webresponsemodel.CreateTeamResponseModel;
import com.app.model.webresponsemodel.UpdateTeamResponseModel;
import com.app.ui.MatchTimerListener;
import com.app.ui.MyApplication;
import com.app.ui.main.ToolbarFragment;
import com.app.ui.main.cricket.myteam.choosecapitan.adapter.ChooseCaptionAdapter;
import com.google.gson.Gson;
import com.medy.retrofitwrapper.WebRequest;
import com.utilities.ItemClickSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ChooseCaptionActivity extends AppBaseActivity implements MatchTimerListener {

    ToolbarFragment toolbarFragment;

    LinearLayout ll_sort_players;
    ImageView iv_sort_players;

    LinearLayout ll_sort_points;
    ImageView iv_sort_points;

    View currentSortBy = null;
    int currentSortType = 0;//0 mean asyc 1 mean desc

    TextView tv_save;
    RecyclerView recycler_view;

    ChooseCaptionAdapter adapter;

    CustomerTeamModel customerExistTeam;
    TextView tv_timer_time;
    TextView tv_match_name;
    TextView tv_caption_name;
    TextView tv_vc_caption_name;
    ImageView iv_caption_image;
    ImageView iv_vc_caption_image;

    List<PlayerModel> selectedPlayers = new ArrayList<>();

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Collections.sort(selectedPlayers, listSorter);
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    };

    Comparator listSorter = new Comparator() {
        @Override
        public int compare(Object o1, Object o2) {
            if (currentSortBy != null) {
                PlayerModel player1 = (PlayerModel) o1;
                PlayerModel player2 = (PlayerModel) o2;
                switch (currentSortBy.getId()) {
                    case R.id.iv_sort_players: {
                        if (currentSortType == 1) {
                            return player2.getName().compareToIgnoreCase(player1.getName());
                        } else {
                            return player1.getName().compareToIgnoreCase(player2.getName());
                        }
                    }

                    case R.id.iv_sort_points: {
                        if (currentSortType == 1) {
                           // return Float.compare(player2.getTotal_points(),player1.getTotal_points());
                            return Float.compare(Float.parseFloat(player2.getCaptionSelected_byText()), Float.parseFloat(player1.getCaptionSelected_byText()));
                        } else {
                            //return Float.compare(player1.getTotal_points(),player2.getTotal_points());
                            return Float.compare(Float.parseFloat(player1.getCaptionSelected_byText()), Float.parseFloat(player2.getCaptionSelected_byText()));
                        }

                    }
                    case R.id.iv_sort_points_vc:{
                        if (currentSortType == 1) {
                            return Float.compare(Float.parseFloat(player2.getViceCaptionSelected_byText()),
                                    Float.parseFloat(player1.getViceCaptionSelected_byText()));
                        } else {
                            return Float.compare(Float.parseFloat(player1.getViceCaptionSelected_byText()),
                                    Float.parseFloat(player2.getViceCaptionSelected_byText()));
                        }
                    }
                }
            }
            return 0;
        }

    };
    private LinearLayout ll_sort_points_vc;
    private ImageView iv_sort_points_vc;

    private MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    public long getMatchContestId() {
        Bundle extras = getIntent().getExtras();
        return (extras == null) ? -1 : extras.getLong(DATA2, -1);
    }

    public long getCustomerTeamId() {
        Bundle extras = getIntent().getExtras();
        return extras == null ? -1 : extras.getLong(DATA1, -1);
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
        return R.layout.activity_choose_caption;
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        customerExistTeam = getCustomerTeam();
        selectedPlayers.addAll(customerExistTeam.getWicketkeapers());
        selectedPlayers.addAll(customerExistTeam.getBatsmans());
        selectedPlayers.addAll(customerExistTeam.getAllrounders());
        selectedPlayers.addAll(customerExistTeam.getBowlers());

        Fragment toolbar = getFm().findFragmentById(R.id.fragment_toolbar);
        if (toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }
        tv_save = findViewById(R.id.tv_save);
        tv_save.setOnClickListener(this);

        ll_sort_players = findViewById(R.id.ll_sort_players);
        iv_sort_players = findViewById(R.id.iv_sort_players);
        ll_sort_players.setOnClickListener(this);

        ll_sort_points = findViewById(R.id.ll_sort_points);
        iv_sort_points = findViewById(R.id.iv_sort_points);
        ll_sort_points.setOnClickListener(this);

        ll_sort_points_vc = findViewById(R.id.ll_sort_points_vc);
        iv_sort_points_vc = findViewById(R.id.iv_sort_points_vc);
        ll_sort_points_vc.setOnClickListener(this);

        tv_timer_time = findViewById(R.id.tv_timer_time);
        tv_match_name = findViewById(R.id.tv_match_name);
        tv_caption_name = findViewById(R.id.tv_caption_name);
        tv_vc_caption_name = findViewById(R.id.tv_vc_caption_name);
        iv_caption_image = findViewById(R.id.iv_caption_image);
        iv_vc_caption_image = findViewById(R.id.iv_vc_caption_image);

        updateSortArrow();
        initializeRecyclerView();
        setupUi();
    }

    private void setMatchData() {
        if (getMatchModel() != null) {
            toolbarFragment.setLeftTitle(/*getMatchModel().getRemainTimeText() + " Left"*/"CHOOSE C & VC");
            tv_match_name.setText(getMatchModel().getShortName());
            tv_timer_time.setText(getMatchModel().getRemainTimeText());
            tv_timer_time.setTextColor(getResources().getColor(getMatchModel()
                    .getTimerColor()));

            boolean matchTimeExpire = getMatchModel().isMatchTimeExpire();
            if(matchTimeExpire){
                showMatchExpireDialog();
            }
        }
    }


    private String getPlayerTypeShortName(int type) {
        switch (type) {
            case 1:
                return "WKT";
            case 2:
                return "BAT";
            case 3:
                return "ALL";
            case 4:
                return "BOWL";
            default:
                return "";
        }
    }

    private void initializeRecyclerView() {
        recycler_view = findViewById(R.id.recycler_view);
        adapter = new ChooseCaptionAdapter(selectedPlayers) {

            @Override
            public Filter getFilter() {
                return filter;
            }

            @Override
            public String getPlayerTypeName(long team_id) {
                TeamModel team1 = customerExistTeam.getTeam1();
                TeamModel team2 = customerExistTeam.getTeam2();

                String correctColor = "#"+ Integer.toHexString(ContextCompat.getColor(ChooseCaptionActivity.this, R.color.colorWhite)).substring(2);
                String correctColor2 = "#"+ Integer.toHexString(ContextCompat.getColor(ChooseCaptionActivity.this, R.color.colorWhite)).substring(2);

                if (team1.getId() == team_id) {
                    return "<font color='" +correctColor + "'>" + team1.getName(1) + "</font>";
                } else {
                    return "<font color='" + correctColor2 + "'>" + team2.getName(1) + "</font>";
                }
            }

            @Override
            public String getPlayerTypeShortName(int playerType){
                return ChooseCaptionActivity.this.getPlayerTypeShortName(playerType);
            }

            @Override
            public int getPlayerTeamType(long team_id) {
                TeamModel team1 = customerExistTeam.getTeam1();
                if (team1.getId() == team_id) {
                    return 1;
                } else {
                    return 2;
                }
            }
        };
        if (customerExistTeam.getCaptain() != null) {
            adapter.setCaption(customerExistTeam.getCaptain().getPlayer_id());
            adapter.setCaptionModel(customerExistTeam.getCaptain());
        }
        if (customerExistTeam.getVise_captain() != null) {
            adapter.setVcCaption(customerExistTeam.getVise_captain().getPlayer_id());
            adapter.setVccaptionModel(customerExistTeam.getVise_captain());
        }
        if (customerExistTeam.getTrump() != null) {
            adapter.setTrumpPlayer(customerExistTeam.getTrump().getPlayer_id());
            adapter.setTreamplaerModel(customerExistTeam.getTrump());
        }

        recycler_view.setLayoutManager(getFullHeightLinearLayoutManager());
        recycler_view.setAdapter(adapter);
        recycler_view.setNestedScrollingEnabled(true);
        ItemClickSupport.addTo(recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                switch (v.getId()) {
                    case R.id.tv_captain:
                        adapter.updateCaption(position);
                        setupUi();
                        setupCaptionViceCaptionHeader();

                        break;

                    case R.id.tv_vice_captain:
                        adapter.updateVcCaption(position);
                        setupUi();
                        setupCaptionViceCaptionHeader();

                        break;
                    case R.id.tv_trump:
                        adapter.updateTrumperPlayer(position);
                        setupUi();
                        setupCaptionViceCaptionHeader();

                        break;
                }

            }
        });

        setupCaptionViceCaptionHeader();
    }

    private void setupCaptionViceCaptionHeader(){
        PlayerModel captionModel = adapter.getCaptionModel();
        PlayerModel vccaptionModel = adapter.getVccaptionModel();
        PlayerModel treamPlayerModel = adapter.getTreamplaerModel();

        if(captionModel==null){
            iv_caption_image.setImageResource(R.drawable.no_image);
            tv_caption_name.setText("Choose Captain");
        }else{
            loadImage(ChooseCaptionActivity.this, iv_caption_image, null, captionModel.getImage(),
                    R.drawable.no_image);
            tv_caption_name.setText(captionModel.getName());
        }

        if(vccaptionModel==null){
            iv_vc_caption_image.setImageResource(R.drawable.no_image);
            tv_vc_caption_name.setText("Choose Vice Captain");
        }else{
            loadImage(ChooseCaptionActivity.this, iv_vc_caption_image, null, vccaptionModel.getImage(),
                    R.drawable.no_image);
            tv_vc_caption_name.setText(vccaptionModel.getName());
        }


    }

    private void setupUi() {
        if (adapter != null) {
            if (adapter.getCaption() == null || adapter.getVcCaption() == null || adapter.getTrumpPlayer() == null) {
                tv_save.setActivated(false);
            } else {
                tv_save.setActivated(true);
            }
        } else {
            tv_save.setActivated(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                callSaveTeam();
                break;

            case R.id.ll_sort_players: {
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_players.getId()) {
                        currentSortBy = iv_sort_players;
                        currentSortType = 0;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_players;
                    currentSortType = 0;
                }
                updateSortArrow();
            }
            break;
            case R.id.ll_sort_points: {
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_points.getId()) {
                        currentSortBy = iv_sort_points;
                        currentSortType = 0;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_points;
                    currentSortType = 0;
                }
                updateSortArrow();
            }

            break;
            case R.id.ll_sort_points_vc:{
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_points_vc.getId()) {
                        currentSortBy = iv_sort_points_vc;
                        currentSortType = 0;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_points_vc;
                    currentSortType = 0;
                }
                updateSortArrow();
            }
        }
    }

    private void updateSortArrow() {
        if (currentSortBy == null) {
            updateViewVisibitity(iv_sort_players, View.INVISIBLE);
            updateViewVisibitity(iv_sort_points, View.INVISIBLE);
            updateViewVisibitity(iv_sort_points_vc, View.INVISIBLE);
        } else {
            switch (currentSortBy.getId()) {
                case R.id.iv_sort_players:
                    updateViewVisibitity(iv_sort_players, View.VISIBLE);
                    updateViewVisibitity(iv_sort_points, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_points_vc, View.INVISIBLE);
                    break;
                case R.id.iv_sort_points:
                    updateViewVisibitity(iv_sort_players, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_points, View.VISIBLE);
                    updateViewVisibitity(iv_sort_points_vc, View.INVISIBLE);
                    break;
                case R.id.iv_sort_points_vc:
                    updateViewVisibitity(iv_sort_players, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_points, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_points_vc, View.VISIBLE);
                    break;
            }

            if (currentSortType == 0) {
                currentSortBy.setRotation(180);
            } else {
                currentSortBy.setRotation(0);
            }
            adapter.getFilter().filter(String.valueOf(currentSortBy.getId()));
        }
    }

    private void callSaveTeam() {
        if (getMatchModel() != null && getUserModel() != null) {
            if (adapter.getCaption() == null) {
                showErrorMsg("please select captain");
                return;
            }

            if (adapter.getVcCaption() == null) {
                showErrorMsg("please select vise captain");
                return;
            }

            /*if (adapter.getTrumpPlayer() == null) {
                showErrorMsg("please select most Preferred player");
                return;
            }*/

            displayProgressBar(false);
            if (getCustomerTeamId() == -1) {
                CreateTeamRequestModel requestModel = new CreateTeamRequestModel();
                requestModel.match_unique_id = getMatchModel().getMatch_id();
                for (PlayerModel playerModel : selectedPlayers) {
                    if (playerModel.getPlayer_id().equals(adapter.getCaption())) {
                        playerModel.setPlayer_multiplier(2);
                    } else if (playerModel.getPlayer_id().equals(adapter.getVcCaption())) {
                        playerModel.setPlayer_multiplier(1.5f);
                    } else {
                        playerModel.setPlayer_multiplier(1);
                    }

                }
                Collections.sort(selectedPlayers, new Comparator<PlayerModel>() {
                    @Override
                    public int compare(PlayerModel o1, PlayerModel o2) {
                        if (o1.getPlayer_multiplier() > o2.getPlayer_multiplier()) {
                            return -1;
                        } else if (o1.getPlayer_multiplier() < o2.getPlayer_multiplier()) {
                            return 1;
                        }
                        return 0;
                    }
                });

                for (int i = 0; i < selectedPlayers.size(); i++) {
                    selectedPlayers.get(i).setPlayer_pos(i+1);
                }

                requestModel.player_json.clear();
                requestModel.player_json.addAll(selectedPlayers);
                getWebRequestHelper().createCustomerTeam(requestModel, this);
            } else {
                UpdateTeamRequestModel requestModel = new UpdateTeamRequestModel();
                requestModel.customer_team_id = getCustomerTeamId();
                requestModel.match_unique_id = getMatchModel().getMatch_id();
                for (PlayerModel playerModel : selectedPlayers) {
                    if (playerModel.getPlayer_id().equals(adapter.getCaption())) {
                        playerModel.setPlayer_multiplier(2);
                    } else if (playerModel.getPlayer_id().equals(adapter.getVcCaption())) {
                        playerModel.setPlayer_multiplier(1.5f);
                    } else {
                        playerModel.setPlayer_multiplier(1);
                    }
                }
                Collections.sort(selectedPlayers, new Comparator<PlayerModel>() {
                    @Override
                    public int compare(PlayerModel o1, PlayerModel o2) {
                        if (o1.getPlayer_multiplier() > o2.getPlayer_multiplier()) {
                            return -1;
                        } else if (o1.getPlayer_multiplier() < o2.getPlayer_multiplier()) {
                            return 1;
                        }
                        return 0;
                    }
                });
                for (int i = 0; i < selectedPlayers.size(); i++) {
                    selectedPlayers.get(i).setPlayer_pos(i+1);
                }
                requestModel.player_json.clear();
                requestModel.player_json.addAll(selectedPlayers);
                getWebRequestHelper().updateCustomerTeam(requestModel, this);
            }

        }
    }


    @Override
    public void onWebRequestResponse(WebRequest webRequest) {
        dismissProgressBar();
        super.onWebRequestResponse(webRequest);
        if (webRequest.getResponseCode() == 401 || webRequest.getResponseCode() == 412) return;
        switch (webRequest.getWebRequestId()) {
            case ID_CREATE_CUSTOMER_TEAM:
                handleCreateCustomerTeamResponse(webRequest);
                break;
            case ID_UPDATE_CUSTOMER_TEAM:
                handleUpdateCustomerTeamResponse(webRequest);
                break;
        }

    }

    private void handleCreateCustomerTeamResponse(WebRequest webRequest) {
        CreateTeamResponseModel responsePojo = webRequest.getResponsePojo(CreateTeamResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            setResult(responsePojo.getData());
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void handleUpdateCustomerTeamResponse(WebRequest webRequest) {
        UpdateTeamResponseModel responsePojo = webRequest.getResponsePojo(UpdateTeamResponseModel.class);
        if (responsePojo == null) return;
        if (!responsePojo.isError()) {
            if (isFinishing()) return;
            showCustomToast(responsePojo.getMessage());
            setResult(responsePojo.getData());
        } else {
            if (isFinishing()) return;
            showErrorMsg(responsePojo.getMessage());
        }

    }

    private void setResult(CustomerTeamModel data) {
        Intent intent = null;
        long matchContestId = getMatchContestId();
        if (matchContestId > 0) {
            Bundle extras = getIntent().getExtras();
            extras.putLong(DATA1, data.getId());
            intent = new Intent();
            intent.putExtras(extras);
        }
        setResult(RESULT_OK, intent);
        supportFinishAfterTransition();
    }


    @Override
    public void onMatchTimeUpdate() {
        if (isFinishing()) return;

        setMatchData();
    }
}
