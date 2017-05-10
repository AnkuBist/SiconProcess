package com.hgil.siconprocess.activity.fragments.finalPayment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync.CashierSyncModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateCheck;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrateCheckFragment extends BaseFragment {

    private static final String SYNC_OBJECT = "sync_object";
    @BindView(R.id.etCrateLoaded)
    EditText etCrateLoaded;
    @BindView(R.id.etCrateReceived)
    EditText etCrateReceived;
    private int crates_loaded_in_van, crates_delivered_by_cashier;
    private CashierSyncModel cashierSyncModel;

    public CrateCheckFragment() {
        // Required empty public constructor
    }

    public static CrateCheckFragment newInstance(CashierSyncModel cashierSyncModel) {
        CrateCheckFragment fragment = new CrateCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SYNC_OBJECT, cashierSyncModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_crate_check;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cashierSyncModel = (CashierSyncModel) getArguments().getSerializable(SYNC_OBJECT);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showSaveButton();

        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(getContext());

         /*get items loaded and received stock*/
        crates_loaded_in_van = routeCustomerView.vanTotalCrate();
        etCrateLoaded.setText(String.valueOf(crates_loaded_in_van));
        etCrateReceived.setText(String.valueOf(crates_delivered_by_cashier));

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                crates_delivered_by_cashier = Utility.getInteger(etCrateReceived.getText().toString());

                CrateCheck crateCheck = new CrateCheck();
                crateCheck.setCrates_loaded(crates_loaded_in_van);
                crateCheck.setCrate_delivered(crates_delivered_by_cashier);

                cashierSyncModel.setCrateCheck(crateCheck);

                ItemCheckFragment fragment = ItemCheckFragment.newInstance(cashierSyncModel);
                launchNavFragment(fragment);
            }
        });
    }

}
