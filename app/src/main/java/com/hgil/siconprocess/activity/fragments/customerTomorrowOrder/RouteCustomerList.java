package com.hgil.siconprocess.activity.fragments.customerTomorrowOrder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.customerOrder.CustomerListAdapter;
import com.hgil.siconprocess.adapter.routeMap.RouteCustomerModel;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RouteCustomerList extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;

    @BindView(R.id.rvCustomerList)
    RecyclerView rvCustomerList;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    private CustomerListAdapter customersAdapter;
    private CustomerRouteMappingView routeMap;
    private ArrayList<RouteCustomerModel> arrCustomers;

    public RouteCustomerList() {
        // Required empty public constructor
    }

    public static RouteCustomerList newInstance() {
        RouteCustomerList fragment = new RouteCustomerList();
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
        return R.layout.fragment_route_customer_list;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        tvRouteName.setText(getRouteName());
        setTitle("Route Customer's");
        hideSaveButton();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerList.setLayoutManager(linearLayoutManager);

        arrCustomers = new ArrayList<>();
        routeMap = new CustomerRouteMappingView(getActivity());
        arrCustomers.addAll(routeMap.getRouteCustomers());
        customersAdapter = new CustomerListAdapter(getActivity(), arrCustomers);
        rvCustomerList.setAdapter(customersAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrCustomers.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvCustomerList.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvCustomerList.setVisibility(View.VISIBLE);
        }
    }
}
