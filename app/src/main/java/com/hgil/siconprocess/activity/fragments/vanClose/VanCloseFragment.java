package com.hgil.siconprocess.activity.fragments.vanClose;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.RouteView;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VanCloseFragment extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.etCashierCode)
    EditText etCashierCode;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private CustomerRouteMappingView customerRouteMappingView;
    private RouteView routeView;

    public VanCloseFragment() {
        // Required empty public constructor
    }

    public static VanCloseFragment newInstance() {
        VanCloseFragment fragment = new VanCloseFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_van_close;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        tvRouteName.setText(getRouteName());
        hideSaveButton();

        routeView = new RouteView(getContext());
        final int vanCloseStatus = routeView.vanCloseStatus(getRouteId());

        setTitle("Van Close");

        customerRouteMappingView = new CustomerRouteMappingView(getContext());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head_cashier_code = etCashierCode.getText().toString();
                if (head_cashier_code.isEmpty()) {
                    showSnackbar(getView(), "Please enter head cashier code");
                } else if (head_cashier_code.matches(Constant.HEAD_CASHIER_CODE)) {
                    if (vanCloseStatus == 1) {
                        new SampleDialog("Van is already closed.", getContext());
                    } else if (customerRouteMappingView.numberPendingCustomers() > 0) {
                        new SampleDialog("Please Complete Sale on Pending Customers before closing route.", getContext());
                    } else {
                        ItemCheckFragment fragment = ItemCheckFragment.newInstance();
                        launchNavFragment(fragment);
                    }
                } else {
                    showTopSnackbar(getView(), "Please enter a valid head cashier code");
                }
            }
        });
    }

}