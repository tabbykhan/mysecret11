package com.app.ui.main.cricket.myteam.createteam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.R;
import com.app.appbase.AppBaseDialogFragment;
import com.app.appbase.AppBaseFragment;
import com.app.model.MatchModel;
import com.app.model.PlayerModel;
import com.app.model.TeamModel;
import com.app.model.TeamSettingModel;
import com.app.ui.MyApplication;
import com.app.ui.main.cricket.contest.ContestActivity;
import com.app.ui.main.cricket.dialogs.playerdetaildialog.PlayerDetailDialog;
import com.app.ui.main.cricket.myteam.createteam.adapter.PlayersAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.utilities.ItemClickSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PlayersFragment extends AppBaseFragment {

    LinearLayout ll_sort_players;
    ImageView iv_sort_players;

    LinearLayout ll_sort_points;
    ImageView iv_sort_points;

    LinearLayout ll_sort_credits;
    ImageView iv_sort_credits;

    View currentSortBy = null;
    int currentSortType = 0;//0 mean asyc 1 mean desc

    TextView tv_player_title;
    List<PlayerModel> players;
    RecyclerView player_recycler_view;
    PlayersAdapter adapter;
    UpdateSelectedPlayer updateSelectedPlayer;
    int type;
    int min;
    int max;
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
//                            return Float.compare(player2.getTotal_points(), player1.getTotal_points());
                            return Float.compare(Float.parseFloat(player2.getSelected_byText()),
                                    Float.parseFloat(player1.getSelected_byText()));

                        } else {
//                            return Float.compare(player1.getTotal_points(), player2.getTotal_points());
                            return Float.compare(Float.parseFloat(player1.getSelected_byText()),
                                    Float.parseFloat(player2.getSelected_byText()));
                        }

                    }
                    case R.id.iv_sort_credits: {
                        if (currentSortType == 1) {
                            return Float.compare(player2.getCredits(), player1.getCredits());
                        } else {
                            return Float.compare(player1.getCredits(), player2.getCredits());
                        }
                    }
                }
            }
            return 0;
        }

    };
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Collections.sort(players, listSorter);
            return null;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (adapter != null && getActivity() != null) {
                adapter.notifyDataSetChanged();
            }
        }
    };
    private LinearLayout ll_reset_list;

    public void setPlayersList(List<PlayerModel> players) {
        this.players = players;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setUpdateSelectedPlayer(UpdateSelectedPlayer updateSelectedPlayer) {
        this.updateSelectedPlayer = updateSelectedPlayer;
    }


    public MatchModel getMatchModel() {
        return MyApplication.getInstance().getSelectedMatch();
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.fragment_players;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getView() == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }

        return getView();
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
        ll_sort_players = getView().findViewById(R.id.ll_sort_players);
        iv_sort_players = getView().findViewById(R.id.iv_sort_players);
        ll_sort_players.setOnClickListener(this);

        ll_sort_points = getView().findViewById(R.id.ll_sort_points);
        iv_sort_points = getView().findViewById(R.id.iv_sort_points);
        ll_sort_points.setOnClickListener(this);

        ll_sort_credits = getView().findViewById(R.id.ll_sort_credits);
        iv_sort_credits = getView().findViewById(R.id.iv_sort_credits);
        ll_sort_credits.setOnClickListener(this);

        ll_reset_list=getView().findViewById(R.id.fragment_player_list_reset);
        ll_reset_list.setOnClickListener(this);
        updateSortArrow();

        tv_player_title = getView().findViewById(R.id.tv_player_title);
        initializeRecyclerView();
        tv_player_title.setText(fragmentTitle(type));
        if (getActivity() != null)
            perFormFilter(((CreateTeamActivity) getActivity()).currentSortBy, ((CreateTeamActivity) getActivity()).currentSortType);
    }

    public void performDefaultSort() {
        if (ll_sort_credits != null) {
            ll_sort_credits.performClick();
        }
    }

    @Override
    public void onPageSelected() {
        super.onPageSelected();
        if (adapter != null && getView() != null) {
            if (adapter != null && getActivity() != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    public String fragmentTitle(int type) {
        String playerTypeFullName = getPlayerTypeFullName(type);
        if (isValidString(playerTypeFullName)) {
            String title = "Pick %s " + playerTypeFullName;
            if (min == max) {
                title = String.format(title, max);
            } else {
                title = String.format(title, (min + "-" + max));
            }
            return title;
        }
        return "";

    }

    private String getPlayerTypeFullName(int type) {
        switch (type) {
            case 1:
                return "wicketkeeper";
            case 2:
                return "batsman";
            case 3:
                return "allrounder";
            case 4:
                return "bowler";
            default:
                return "";
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                if (getActivity() != null)
                    ((CreateTeamActivity) getActivity()).performFilterOnOtherFragment(0, currentSortType);
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
                if (getActivity() != null)
                    ((CreateTeamActivity) getActivity()).performFilterOnOtherFragment(1, currentSortType);
            }

            break;
            case R.id.ll_sort_credits: {
                if (currentSortBy != null) {
                    if (currentSortBy.getId() != iv_sort_credits.getId()) {
                        currentSortBy = iv_sort_credits;
                        currentSortType = 0;
                    } else {
                        currentSortType = (currentSortType == 0) ? 1 : 0;
                    }
                } else {
                    currentSortBy = iv_sort_credits;
                    currentSortType = 1;
                }
                updateSortArrow();
                if (getActivity() != null)
                    ((CreateTeamActivity) getActivity()).performFilterOnOtherFragment(2, currentSortType);
            }

            break;
            case R.id.fragment_player_list_reset:{
               adapter.notifyDataSetChanged();
                updateSelectedPlayer.reset();

            }
            break;

        }
    }

    public void perFormFilter(int currentSortBy, int currentSortType) {
        if (getView() == null || adapter == null || getActivity() == null)
            return;
        switch (currentSortBy) {
            case 0: {
                this.currentSortBy = iv_sort_players;
                this.currentSortType = currentSortType;
                updateSortArrow();
            }
            break;
            case 1: {
                this.currentSortBy = iv_sort_points;
                this.currentSortType = currentSortType;
                updateSortArrow();
            }
            break;
            case 2: {
                this.currentSortBy = iv_sort_credits;
                this.currentSortType = currentSortType;
                updateSortArrow();
            }
            break;
        }
    }

    private void updateSortArrow() {
        if (currentSortBy == null) {
            updateViewVisibitity(iv_sort_players, View.INVISIBLE);
            updateViewVisibitity(iv_sort_points, View.INVISIBLE);
            updateViewVisibitity(iv_sort_credits, View.INVISIBLE);
        } else {
            switch (currentSortBy.getId()) {
                case R.id.iv_sort_players:
                    updateViewVisibitity(iv_sort_players, View.VISIBLE);
                    updateViewVisibitity(iv_sort_points, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_credits, View.INVISIBLE);
                    break;
                case R.id.iv_sort_points:
                    updateViewVisibitity(iv_sort_players, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_points, View.VISIBLE);
                    updateViewVisibitity(iv_sort_credits, View.INVISIBLE);
                    break;
                case R.id.iv_sort_credits:
                    updateViewVisibitity(iv_sort_players, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_points, View.INVISIBLE);
                    updateViewVisibitity(iv_sort_credits, View.VISIBLE);
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

    public String getMinPlayerMessage(int min, String playerType) {
        return String.format("Every team needs at least %s %s", min, playerType);
    }

    private void initializeRecyclerView() {
        player_recycler_view = getView().findViewById(R.id.player_recycler_view);
        adapter = new PlayersAdapter(getActivity(), players) {

            @Override
            public Filter getFilter() {
                return filter;
            }

            @Override
            public boolean isMaxPlayerSelected() {
                return updateSelectedPlayer.getSelectedPlayer() >= updateSelectedPlayer.getTeamSetting().getMAX_PLAYERS();
            }

            @Override
            public boolean isMaxFrom1Team(long teamId) {
                TeamModel team1 = updateSelectedPlayer.getTeam1();
                long playerCount = 0;
                if (team1.getId() == teamId) {
                    playerCount = updateSelectedPlayer.getTotalTeam1Players();
                } else {
                    playerCount = updateSelectedPlayer.getTotalTeam2Players();
                }
                return playerCount >= updateSelectedPlayer.getTeamSetting().getMAX_PLAYERS_PER_TEAM();
            }

            @Override
            public boolean isMaxPlayerType() {
                return updateSelectedPlayer.getPlayerCountByType(type) >= max;
            }

            @Override
            public boolean isMinPlayerFailed() {
                int previousSelectedPlayer = updateSelectedPlayer.getPlayerCountByType(type);
                previousSelectedPlayer++;
                int requiredSelectedPlayer = 0;
                for (int i = 1; i < 5; i++) {
                    int playerCount1 = updateSelectedPlayer.getPlayerCountByType(i);
                    int minPlayer = updateSelectedPlayer.getTeamSetting().getMinPlayer(i);
                    if (i != type) {
                        requiredSelectedPlayer += Math.max(playerCount1, minPlayer);
                    }
                }

                int totalMinPlayer = requiredSelectedPlayer;
                return totalMinPlayer + previousSelectedPlayer > updateSelectedPlayer.getTeamSetting().getMAX_PLAYERS();
            }

            @Override
            public boolean isCreditExceed(float playerCredit) {
                float totalCredit = updateSelectedPlayer.getSelectedPlayerTotalPoints() + playerCredit;
                return totalCredit > updateSelectedPlayer.getTeamSetting().getMAX_CREDITS();
            }

            @Override
            public String getPlayerTypeName(long team_id) {
                TeamModel team1 = updateSelectedPlayer.getTeam1();
                TeamModel team2 = updateSelectedPlayer.getTeam2();
                String correctColor = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorWhite)).substring(2);
                String correctColor2 = "#" + Integer.toHexString(ContextCompat.getColor(getActivity(), R.color.colorWhite)).substring(2);

                if (team1.getId() == team_id) {
                    return "<font color='" + correctColor + "'>" + team1.getName(1) + "</font>";
                } else {
                    return "<font color='" + correctColor2 + "'>" + team2.getName(1) + "</font>";
                }
            }

            @Override
            public int getPlayerTeamType(long team_id) {
                TeamModel team1 = updateSelectedPlayer.getTeam1();
                if (team1.getId() == team_id) {
                    return 1;
                } else {
                    return 2;
                }
            }
        };
        player_recycler_view.setLayoutManager(getVerticalLinearLayoutManager());
        player_recycler_view.setAdapter(adapter);
        player_recycler_view.setNestedScrollingEnabled(false);
        ItemClickSupport.addTo(player_recycler_view).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                PlayerModel playerModel = adapter.getItem(position);
                switch (v.getId()) {
                    case R.id.rl_player_image:
                        openPlayerDetailDialog(playerModel);
                        break;
                    default:
                        boolean b = onPlayerSelected(playerModel);
                        if (b) {
                            if (getActivity() != null)
                                ((CreateTeamActivity) getActivity()).refreshOtherFragmentData();
                        }
                        break;
                }
            }
        });
    }

    public boolean onPlayerSelected(PlayerModel playerModel) {
        if (playerModel.isSelected()) {
            Log.i("is player select", "--00--");
            playerModel.setSelected(false);
            updateSelectedPlayer.updatePlayer(playerModel);
            adapter.notifyDataSetChanged();
            return true;
        } else if (!adapter.isMaxPlayerSelected()) {
            Log.i("is player not select", "--11111--");
            if (!adapter.isMaxFrom1Team(playerModel.getTeam_id())) {
                if (!adapter.isMaxPlayerType()) {
                    if (adapter.isMinPlayerFailed()) {
                        for (int i = 1; i < 5; i++) {
                            int playerCount1 = updateSelectedPlayer.getPlayerCountByType(i);
                            if (playerCount1 < updateSelectedPlayer.getTeamSetting().getMinPlayer(i) && i != type) {
                                String minPlayerMessage = getMinPlayerMessage(updateSelectedPlayer.getTeamSetting().getMinPlayer(i), getPlayerTypeFullName(i));
                                showErrorMsg(minPlayerMessage);
                                adapter.notifyDataSetChanged();
                                return false;
                            }
                        }
                    }

                    if (!adapter.isCreditExceed(playerModel.getCredits())) {
                        playerModel.setSelected(true);
                        updateSelectedPlayer.updatePlayer(playerModel);
                        adapter.notifyDataSetChanged();
                        return true;
                    } else {
                        double leftCredit = updateSelectedPlayer.getTeamSetting().getMAX_CREDITS() - updateSelectedPlayer.getSelectedPlayerTotalPoints();
                        showErrorMsg(String.format("Only %s credit left", leftCredit));
                    }

                } else {
                    showErrorMsg(String.format("Only %s allowed", max + " " + getPlayerTypeFullName(type)));
                }
            } else {
                showErrorMsg(String.format("Max %s players from 1 team", updateSelectedPlayer.getTeamSetting().getMAX_PLAYERS_PER_TEAM()));
            }
        } else {
            showErrorMsg(String.format("you already selected %s players ", updateSelectedPlayer.getTeamSetting().getMAX_PLAYERS()));
        }
        return false;
    }


    private void openPlayerDetailDialog(final PlayerModel playerModel) {
        PlayerDetailDialog playerDetailDialog = PlayerDetailDialog.getInstance(null);
        playerDetailDialog.setPlayerModel(playerModel);
        playerDetailDialog.setFromCreateTeam(true);
        playerDetailDialog.setPlayerDetailDialogListener(new PlayerDetailDialog.PlayerDetailDialogListener() {
            @Override
            public void onPlayerSelected(AppBaseDialogFragment appBaseDialogFragment) {
                boolean b = PlayersFragment.this.onPlayerSelected(playerModel);
                if (b) {
                    appBaseDialogFragment.dismiss();
                    if (getActivity() != null)
                        ((CreateTeamActivity) getActivity()).refreshOtherFragmentData();
                }
            }
        });
        playerDetailDialog.show(getChildFm(), playerDetailDialog.getClass().getSimpleName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ContestActivity.REQUEST_CHOOSE_PLAYER) {
            if (resultCode == Activity.RESULT_OK) {
                Bundle extras = data.getExtras();
                try {
                    PlayerModel playerModel = new Gson().fromJson(extras.getString(DATA), PlayerModel.class);
                } catch (JsonSyntaxException ignore) {

                }

            }
        }
    }

    public interface UpdateSelectedPlayer {
        void updatePlayer(PlayerModel selectedPlayer);

        int getTotalTeam1Players();

        int getTotalTeam2Players();

        int getPlayerCountByType(int type);

        float getSelectedPlayerTotalPoints();

        void reset();

        int getSelectedPlayer();

        TeamSettingModel getTeamSetting();

        TeamModel getTeam1();

        TeamModel getTeam2();
    }
}
