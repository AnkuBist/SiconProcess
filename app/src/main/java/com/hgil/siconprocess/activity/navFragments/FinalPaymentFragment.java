package com.hgil.siconprocess.activity.navFragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.CrateCheckFragment;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.utils.Constant;

import butterknife.BindView;

public class FinalPaymentFragment extends BaseFragment {

    @BindView(R.id.tvRouteName)
    TextView tvRouteName;
    @BindView(R.id.etCashierCode)
    EditText etCashierCode;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String head_cashier_code = etCashierCode.getText().toString();
                if (head_cashier_code.isEmpty()) {
                    showSnackbar(getView(), "Please enter head cashier code");
                } else if (head_cashier_code.matches(Constant.HEAD_CASHIER_CODE)) {
                    CrateCheckFragment fragment = CrateCheckFragment.newInstance();
                    launchNavFragment(fragment);
                } else {
                    showSnackbar(getView(), "Please enter a valid head cashier code");
                }
            }
        });
    }

}
