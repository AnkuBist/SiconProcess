package com.hgil.siconprocess.activity.fragments.dashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.routeTarget.RouteTargetAdapter;
import com.hgil.siconprocess.adapter.routeTarget.RouteTargetModel;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TargetsFragment extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.rvTargets)
    RecyclerView rvTargets;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    private RouteTargetAdapter routeTargetAdapter;
    private ArrayList<RouteTargetModel> arrRouteTarget;

    public TargetsFragment() {
        // Required empty public constructor
    }

    public static TargetsFragment newInstance() {
        TargetsFragment fragment = new TargetsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_targets;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        String routeName = getRouteName();
        if (routeName != null && !routeName.isEmpty())
            tvRouteName.setText(routeName);

        setTitle("Targets");
        hideSaveButton();

        // handling recycler data and adapter working pending
        //TODO
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvTargets.setLayoutManager(linearLayoutManager);

        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(getContext());

        arrRouteTarget = new ArrayList<>();
        arrRouteTarget.addAll(itemPriceTable.getDashboardTargets());
        routeTargetAdapter = new RouteTargetAdapter(getContext(), arrRouteTarget);
        rvTargets.setAdapter(routeTargetAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrRouteTarget.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvTargets.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvTargets.setVisibility(View.VISIBLE);
        }
    }

}
