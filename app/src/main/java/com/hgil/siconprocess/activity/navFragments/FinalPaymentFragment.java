package com.hgil.siconprocess.activity.navFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.CashCheckFragment;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.masterTables.RouteView;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.ui.SampleDialog;

import butterknife.BindView;

public class FinalPaymentFragment extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.etCashierCode)
    EditText etCashierCode;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private RouteView routeView;
    private CustomerRouteMappingView customerRouteMappingView;

    public FinalPaymentFragment() {
        // Required empty public constructor
    }

    public static FinalPaymentFragment newInstance() {
        FinalPaymentFragment fragment = new FinalPaymentFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_final_payment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        tvRouteName.setText(getRouteName());
        hideSaveButton();
        setTitle("Final Payment");

        routeView = new RouteView(getContext());

        final int finalPaymentStatus = routeView.finalPaymentStatus(getRouteId());
        final int vanCloseStatus = routeView.vanCloseStatus(getRouteId());

        customerRouteMappingView = new CustomerRouteMappingView(getContext());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head_cashier_code = etCashierCode.getText().toString();
                if (head_cashier_code.isEmpty()) {
                    showSnackbar(getView(), "Please enter head cashier code");
                } else if (head_cashier_code.matches(Constant.HEAD_CASHIER_CODE)) {
                    if (vanCloseStatus != 1) {
                        new SampleDialog("Please Close Van before making final payment.", getContext());
                    } else if (finalPaymentStatus == 1) {
                        new SampleDialog("Route is already closed.", getContext());
                    } else {
                        CashCheckFragment fragment = CashCheckFragment.newInstance();
                        launchNavFragment(fragment);
                    }
                } else {
                    showTopSnackbar(getView(), "Please enter a valid head cashier code");
                }
            }
        });
    }

}
