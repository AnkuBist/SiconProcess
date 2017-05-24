package com.hgil.siconprocess.activity.fragments.finalPayment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.CashierSyncModel;
import com.hgil.siconprocess.syncPOJO.supervisorSyncModel.CrateStockCheck;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.tables.PaymentTable;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CrateCheckFragment extends BaseFragment {

    @BindView(R.id.etCrateOpening)
    EditText etCrateOpening;
    @BindView(R.id.etCrateIssued)
    EditText etCrateIssued;
    @BindView(R.id.etCrateReceived)
    EditText etCrateReceived;
    @BindView(R.id.etCrateBalance)
    EditText etCrateBalance;

    private CashierSyncModel cashierSyncModel;
    private CrateStockCheck crateStockCheck;

    public CrateCheckFragment() {
        // Required empty public constructor
    }

    public static CrateCheckFragment newInstance() {
        CrateCheckFragment fragment = new CrateCheckFragment();
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_crate_check;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cashierSyncModel = new CashierSyncModel();
        showSaveButton();

        // crate stock verifying
        crateStockCheck = new PaymentTable(getContext()).syncCrateStock(getRouteId());

        etCrateOpening.setText(String.valueOf(crateStockCheck.getOpening()));
        etCrateIssued.setText(String.valueOf(crateStockCheck.getIssued()));
        etCrateReceived.setText(String.valueOf(crateStockCheck.getReceived()));
        etCrateBalance.setText(String.valueOf(crateStockCheck.getBalance()));

        etCrateReceived.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    crateStockCheck.setReceived(Integer.parseInt(etCrateReceived.getText().toString()));
                    etCrateBalance.setText(String.valueOf(crateStockCheck.getOpening() - crateStockCheck.getIssued() + crateStockCheck.getReceived()));
                } else {
                    etCrateBalance.setText(String.valueOf(crateStockCheck.getOpening() - crateStockCheck.getIssued()));
                }
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crateStockCheck.setReceived(Integer.parseInt(etCrateReceived.getText().toString()));
                crateStockCheck.setBalance(Integer.parseInt(etCrateBalance.getText().toString()));

                // crate stock verifying
                cashierSyncModel.setCrateStockCheck(crateStockCheck);

                ItemCheckFragment fragment = ItemCheckFragment.newInstance(cashierSyncModel);
                launchNavFragment(fragment);
            }
        });
    }

}
