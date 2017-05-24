package com.hgil.siconprocess.activity.navFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.customerTomorrowOrder.RouteCustomerList;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.utils.Constant;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerOrderFragment extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.etPSMpass)
    EditText etPSMpass;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    public CustomerOrderFragment() {
        // Required empty public constructor
    }

    public static CustomerOrderFragment newInstance() {
        CustomerOrderFragment fragment = new CustomerOrderFragment();
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
        return R.layout.fragment_customer_tomorrow_order;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // set route name to the route
        tvRouteName.setText(getRouteName());
        setTitle("PSM Login");
        hideSaveButton();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psm_pass = etPSMpass.getText().toString();
                if (psm_pass.isEmpty()) {
                    showSnackbar(getView(), "Please enter head cashier code");
                } else if (psm_pass.matches(Constant.PSM_CODE)) {
                    // start here customer list
                    RouteCustomerList fragment = RouteCustomerList.newInstance();
                    launchNavFragment(fragment);
                } else {
                    showSnackbar(getView(), "Please enter a valid PSM password");
                }
            }
        });

    }


}
