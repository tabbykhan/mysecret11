package com.MLM;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;

import com.MLM.Adapter.DrawerExpandableListAdapter;
import com.MLM.model.ExpandableListModel;
import com.R;
import com.app.appbase.AppBaseActivity;
import com.app.ui.main.ToolbarFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class MLMDashboard extends AppBaseActivity {

    ToolbarFragment toolbarFragment;
    ArrayList<ExpandableListModel> expandableTitleList;
    HashMap<String, ArrayList<String>> expandListData;
    private ExpandableListView mExpandableListView;

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_m_l_m_dashboard;
    }

    public void teamView(View view) {
        startActivity(new Intent(this, TeamView.class));
    }

    @Override
    public void initializeComponent() {
        super.initializeComponent();
       /* toolbarFragment = findViewById(R.id.topBar);
        Fragment toolbar = getA().findFragmentById(R.id.topBar);
        if (toolbar != null && toolbar instanceof AppBaseFragment) {
            toolbarFragment = (ToolbarFragment) toolbar;
            toolbarFragment.initializeComponent();
            toolbarFragment.setToolbarView(this);
        }*/
        mExpandableListView = findViewById(R.id.navList);
        setExpandableList();
        expandableListListener();

    }

    public void treeView(View view) {
        startActivity(new Intent(this, TeamView.class));
    }

    public void makePayment(View view) {
        startActivity(new Intent(this, MakePayment.class));
    }

    public void backBtn(View view) {
        onBackPressed();
    }

    public void leadBoard(View view) {
        startActivity(new Intent(this, SeriesLeaderBoard.class));
    }

    private void setExpandableList() {
        expandableTitleList = new ArrayList<>();
        expandListData = new HashMap<>();

        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Dashboard"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Profile"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Packages"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Referrals"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Income"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Withdrawal"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "New Registration"));
        //        expandableTitleList.add(new ExpandableListModel(R.drawable.person, "Logout"));

        expandableTitleList.add(new ExpandableListModel("Home"));
        expandableTitleList.add(new ExpandableListModel("Network"));
        expandableTitleList.add(new ExpandableListModel("Join Mega Contest"));
        expandableTitleList.add(new ExpandableListModel("Fund"));
        expandableTitleList.add(new ExpandableListModel("Income Report"));
        expandableTitleList.add(new ExpandableListModel("Transfer Balance"));
        expandableTitleList.add(new ExpandableListModel("Series LeaderBoard"));


        ArrayList<String> allTransactions = new ArrayList<String>(); // dashboard
        expandListData.put(expandableTitleList.get(0).title, allTransactions);

        ArrayList<String> network_array = new ArrayList<>();
        network_array.add("Tree View");
        network_array.add("Team View");
        network_array.add("Direct View");
        expandListData.put(expandableTitleList.get(1).title, network_array);

        ArrayList<String> fund_array = new ArrayList<>();
        fund_array.add("Add Fund");
        fund_array.add("Pending Fund Report");
        expandListData.put(expandableTitleList.get(3).title, fund_array);

        ArrayList<String> topup_array = new ArrayList<>();
        topup_array.add("Topup");
        topup_array.add("Topup Report");
        topup_array.add("Downline Topup Report");
        expandListData.put(expandableTitleList.get(4).title, topup_array);


        ArrayList<String> income_array = new ArrayList<>();
        income_array.add("Invitee Bonus");
        income_array.add("Retention Points");
        income_array.add("Team Bonus");
        expandListData.put(expandableTitleList.get(4).title, income_array);

        ArrayList<String> transfer_bal_array = new ArrayList<>();
        transfer_bal_array.add("Transfer Balance Report");
        expandListData.put(expandableTitleList.get(5).title, transfer_bal_array);

        expandListData.put(expandableTitleList.get(6).title, allTransactions);// new registration


        DrawerExpandableListAdapter drawerExpandableListAdapter = new DrawerExpandableListAdapter(getBaseContext(), expandListData, expandableTitleList);
        mExpandableListView.setAdapter(drawerExpandableListAdapter);
    }


    private void expandableListListener() {

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                int index = expandableListView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(i, i1));
                expandableListView.setItemChecked(index, true);

                return true;
            }
        });


        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                CheckGroupItem(i);
                return false;
            }
        });

        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                gorupChildClick(i, i1);
                return false;
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                for (int i = 0; i < mExpandableListView.getCount(); i++) {
                    if (i != groupPosition) {
                        mExpandableListView.collapseGroup(i);
                    }
                }
            }
        });

    }

    private void gorupChildClick(int i, int i1) {
        switch (i) {
            case 0:

                break;
            case 1: //network
                switch (i1) {
                    case 0://
                            treeView(null);
                        break;
                    case 1:
                            teamView(null);
                        break;
                    case 2: // direct list

                        break;
                }
                break;
            case 2: //
                switch (i1) {
                    case 0://

                        break;
                    case 1:

                        break;
                    case 2: // direct list

                        break;
                }
                break;
            case 3: // fund
                switch (i1) {
                    case 0:

                        break;
                    case 1:

                        break;
                    case 2:

                        break;

                }
                break;
            case 4:
                switch (i1) {
                    case 0:

                        break;
                    case 1:

                        break;

                }
                break;
            case 5:
                switch (i1) {
                    case 0:

                        break;
                    case 1:

                        break;

                }
                break;
        }
    }

    private void CheckGroupItem(int i) {
        switch (i) {
            case 0:


                break;


            case 6:

                break;
            case 7:

                break;

        }

    }
}
