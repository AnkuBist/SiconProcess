package com.hgil.siconprocess.activity.fragments.customerInvoice;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.dbModels.CrateDetailModel;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.utils.UtilNetworkLocation;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.utilPermission.UtilIMEI;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CratesManagementFragment extends BaseFragment {

    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.etOpeningCrates)
    EditText etOpeningCrates;
    @BindView(R.id.etIssuedCrates)
    EditText etIssuedCrates;
    @BindView(R.id.etReceivedCrates)
    EditText etReceivedCrates;
    @BindView(R.id.etBalanceCrates)
    EditText etBalanceCrates;

    private PaymentTable paymentTable;
    private int crateOpening, issuedCrate, receivedCrate, balanceCrate;

    public CratesManagementFragment() {
        // Required empty public constructor
    }

    public static CratesManagementFragment newInstance(String customer_id, String customer_name) {
        CratesManagementFragment fragment = new CratesManagementFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CUSTOMER_ID, customer_id);
        bundle.putString(CUSTOMER_NAME, customer_name);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            customer_id = getArguments().getString(CUSTOMER_ID);
            customer_name = getArguments().getString(CUSTOMER_NAME);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_crates_management;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        // do the rest stuff here only
        paymentTable = new PaymentTable(getContext());
        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(getContext());
        crateOpening = routeCustomerView.custCreditCrates(customer_id);

        CrateDetailModel crateInfo = paymentTable.getCustomerCrateInfo(customer_id);
        issuedCrate = crateInfo.getIssuedCrates();
        receivedCrate = crateInfo.getReceivedCrates();

        if (issuedCrate == 0)
            issuedCrate = routeCustomerView.custLoadingCrates(customer_id);

        balanceCrate = crateOpening - issuedCrate + receivedCrate;

        etOpeningCrates.setText(String.valueOf(crateOpening));
        etIssuedCrates.setText(String.valueOf(issuedCrate));

        if (receivedCrate == 0)
            etReceivedCrates.setText("");
        else
            etReceivedCrates.setText(String.valueOf(receivedCrate));

        etBalanceCrates.setText(String.valueOf(balanceCrate));

       /* etIssuedCrates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                issuedCrate = Utility.getInteger(etIssuedCrates.getText().toString());
                etBalanceCrates.setText(String.valueOf(crateOpening + issuedCrate - receivedCrate));
            }
        });*/

        etReceivedCrates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    etBalanceCrates.setText(String.valueOf(balanceCrate));
                } else {
                    int tempReceive = Utility.getInteger(etReceivedCrates.getText().toString());
                    etBalanceCrates.setText(String.valueOf(crateOpening - issuedCrate + tempReceive));
                }
            }
        });

        setTitle("Crates Management");
        showSaveButton();
        updateSaveIcon();
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save the crate details to the database
                CrateDetailModel crateDetailModel = new CrateDetailModel();
                crateDetailModel.setCustomer_id(customer_id);
                crateDetailModel.setIssuedCrates(Utility.getInteger(etIssuedCrates.getText().toString()));
                crateDetailModel.setReceivedCrates(Utility.getInteger(etReceivedCrates.getText().toString()));

                // update device imei_no, location and login_id
                // time_stamp will be updated automatically;
                crateDetailModel.setImei_no(UtilIMEI.getIMEINumber(getContext()));
                crateDetailModel.setLat_lng(UtilNetworkLocation.getLatLng(UtilNetworkLocation.getLocation(getContext())));
                crateDetailModel.setLogin_id(getLoginId());

                paymentTable.insertCustomerCrates(crateDetailModel);

                // show snackbar message
                showSnackbar(getView(), "Crates details saved successfully.");

                //move to next fragment
                //CustOtherMarketProductDetailsFragment fragment = CustOtherMarketProductDetailsFragment.newInstance(customer_id, customer_name);
                FinalInvoiceFragment fragment = FinalInvoiceFragment.newInstance(customer_id, customer_name);
                launchInvoiceFragment(fragment);
            }
        });
    }
}
