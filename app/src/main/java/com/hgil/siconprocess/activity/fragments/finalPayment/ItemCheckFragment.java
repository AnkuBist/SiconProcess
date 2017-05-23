package com.hgil.siconprocess.activity.fragments.finalPayment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync.CashierSyncModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.ItemStockCheck;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.masterTables.ProductView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemCheckFragment extends BaseFragment {

    private static final String SYNC_OBJECT = "sync_object";
    @BindView(R.id.rvItemStockCheck)
    RecyclerView rvItemStockCheck;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    private CashierSyncModel cashierSyncModel;

    private ItemStockCheckAdapter itemStockCheckAdapter;
    private ProductView productView;
    private ArrayList<ItemStockCheck> arrItemStockCheck = new ArrayList<>();

    public ItemCheckFragment() {
        // Required empty public constructor
    }

    public static ItemCheckFragment newInstance(CashierSyncModel cashierSyncModel) {
        ItemCheckFragment fragment = new ItemCheckFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SYNC_OBJECT, cashierSyncModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cashierSyncModel = (CashierSyncModel) getArguments().getSerializable(SYNC_OBJECT);
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_item_check;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showSaveButton();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvItemStockCheck.setLayoutManager(linearLayoutManager);

        productView = new ProductView(getContext());
        arrItemStockCheck.addAll(productView.checkItemStock());
        itemStockCheckAdapter = new ItemStockCheckAdapter(getContext(), arrItemStockCheck);
        rvItemStockCheck.setAdapter(itemStockCheckAdapter);

        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cashierSyncModel.setArrItemStock(arrItemStockCheck);
                CashCheckFragment fragment = CashCheckFragment.newInstance(cashierSyncModel);
                launchNavFragment(fragment);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (arrItemStockCheck.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvItemStockCheck.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvItemStockCheck.setVisibility(View.VISIBLE);
        }
    }

}
