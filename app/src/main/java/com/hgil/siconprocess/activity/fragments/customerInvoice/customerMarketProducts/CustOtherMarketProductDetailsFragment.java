package com.hgil.siconprocess.activity.fragments.customerInvoice.customerMarketProducts;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.HomeInvoiceActivity;
import com.hgil.siconprocess.activity.fragments.customerInvoice.FinalInvoiceFragment;
import com.hgil.siconprocess.adapter.MarketProductAdapter;
import com.hgil.siconprocess.adapter.productSelection.ProductSelectModel;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.dbModels.MarketProductModel;
import com.hgil.siconprocess.database.tables.MarketProductTable;
import com.hgil.siconprocess.utils.UtilNetworkLocation;
import com.hgil.siconprocess.utils.utilPermission.UtilIMEI;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustOtherMarketProductDetailsFragment extends BaseFragment {

    private static int PRODUCT_LIST = 121;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.rvMarketProducts)
    RecyclerView rvMarketProducts;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.btnAddItems)
    Button btnAddItems;
    private MarketProductAdapter marketProductAdapter;
    private MarketProductTable marketProductTable;
    private ArrayList<MarketProductModel> arrOrder = new ArrayList<>();

    public CustOtherMarketProductDetailsFragment() {
        // Required empty public constructor
    }

    public static CustOtherMarketProductDetailsFragment newInstance(String customer_id, String customer_name) {
        CustOtherMarketProductDetailsFragment fragment = new CustOtherMarketProductDetailsFragment();
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
        return R.layout.fragment_cust_other_market_product_details;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        // do the rest stuff here only
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMarketProducts.setLayoutManager(linearLayoutManager);

        marketProductTable = new MarketProductTable(getContext());
        arrOrder = marketProductTable.getCustMarketProducts(customer_id);

        marketProductAdapter = new MarketProductAdapter(getActivity(), arrOrder);
        rvMarketProducts.setAdapter(marketProductAdapter);

        setTitle("Client's Market Products");
        showSaveButton();
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MarketProductModel> reviewOrderData = new ArrayList<MarketProductModel>();
                for (int i = 0; i < arrOrder.size(); i++) {
                    MarketProductModel marketProductModel = arrOrder.get(i);
                    if (marketProductModel.getQuantity() > 0) {
                        // update device imei_no, location and login_id
                        // time_stamp will be updated automatically;
                        marketProductModel.setImei_no(UtilIMEI.getIMEINumber(getContext()));
                        marketProductModel.setLat_lng(UtilNetworkLocation.getLatLng(UtilNetworkLocation.getLocation(getContext())));
                        marketProductModel.setLogin_id(getLoginId());

                        reviewOrderData.add(marketProductModel);
                    }
                }

                // insert the above data to local database
                // save the crate details to the database
                marketProductTable.insertMarketProducts(reviewOrderData, customer_id);

                // show snackbar message
                showSnackbar(getView(), "Market Products Details saved successfully.");

                //now finish this fragment
                //getActivity().getSupportFragmentManager().popBackStackImmediate();

                //move to next fragment
                FinalInvoiceFragment fragment = FinalInvoiceFragment.newInstance(customer_id, customer_name);
                launchInvoiceFragment(fragment);
            }
        });

        btnAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MarketProductListActivity.class);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("customer_name", customer_name);
                ArrayList<String> arrItems = new ArrayList<String>();
                for (int i = 0; i < arrOrder.size(); i++) {
                    arrItems.add(arrOrder.get(i).getItemId());
                }
                intent.putStringArrayListExtra("existing_items", arrItems);
                startActivityForResult(intent, PRODUCT_LIST);
                ((HomeInvoiceActivity) getContext()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshScreen();
    }

    // refresh screen
    private void refreshScreen() {
        if (arrOrder.size() == 0) {
            tvEmpty.setVisibility(View.VISIBLE);
            rvMarketProducts.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvMarketProducts.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (requestCode == PRODUCT_LIST) {
                ArrayList<ProductSelectModel> arrProduct = (ArrayList<ProductSelectModel>) bundle.getSerializable("selected_products");
                for (int i = 0; i < arrProduct.size(); i++) {
                    ProductSelectModel pModel = arrProduct.get(i);
                    MarketProductModel marketProductModel = new MarketProductModel();
                    marketProductModel.setItemId(pModel.getItem_id());
                    marketProductModel.setItemName(pModel.getItem_name());
                    marketProductModel.setCustomerId(customer_id);
                    marketProductModel.setCustomerName(customer_name);
                    if (pModel.isSelected())
                        arrOrder.add(marketProductModel);
                }
                marketProductAdapter.notifyDataSetChanged();
                refreshScreen();
            }
        }
    }
}

