package com.hgil.siconprocess.activity.fragments.customerTomorrowOrder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.NavBaseActivity;
import com.hgil.siconprocess.adapter.nextDayOrder.NextDayOrderAdapter;
import com.hgil.siconprocess.adapter.productSelection.ProductSelectModel;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.dbModels.NextDayOrderModel;
import com.hgil.siconprocess.database.tables.NextDayOrderTable;
import com.hgil.siconprocess.utils.UtilNetworkLocation;
import com.hgil.siconprocess.utils.utilPermission.UtilIMEI;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrepareCustomerTomorrowOrderFragment extends BaseFragment {

    private static int PRODUCT_LIST = 121;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.rvCustomerOrder)
    RecyclerView rvCustomerOrder;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.btnAddItems)
    Button btnAddItems;
    private NextDayOrderAdapter orderAdapter;
    private NextDayOrderTable orderTable;
    private ArrayList<NextDayOrderModel> arrOrder = new ArrayList<>();

    public PrepareCustomerTomorrowOrderFragment() {
        // Required empty public constructor
    }

    public static PrepareCustomerTomorrowOrderFragment newInstance(String customer_id, String customer_name) {
        PrepareCustomerTomorrowOrderFragment fragment = new PrepareCustomerTomorrowOrderFragment();
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
        return R.layout.fragment_prepare_customer_tomorrow_order;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer_name != null)
            tvCustomerName.setText(customer_name);

        // do the rest stuff here only
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomerOrder.setLayoutManager(linearLayoutManager);

        orderTable = new NextDayOrderTable(getContext());
        arrOrder = orderTable.getCustomerOrder(customer_id);

        orderAdapter = new NextDayOrderAdapter(getActivity(), arrOrder);
        rvCustomerOrder.setAdapter(orderAdapter);

        setTitle("Tomorrow's Order");
        showSaveButton();
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<NextDayOrderModel> reviewOrderData = new ArrayList<NextDayOrderModel>();
                for (int i = 0; i < arrOrder.size(); i++) {
                    NextDayOrderModel nextDayOrderModel = arrOrder.get(i);
                    if (nextDayOrderModel.getQuantity() > 0) {
                        // update device imei_no, location and login_id
                        // time_stamp will be updated automatically;
                        nextDayOrderModel.setImei_no(UtilIMEI.getIMEINumber(getContext()));
                        nextDayOrderModel.setLat_lng(UtilNetworkLocation.getLatLng(UtilNetworkLocation.getLocation(getContext())));
                        nextDayOrderModel.setLogin_id(getLoginId());

                        reviewOrderData.add(nextDayOrderModel);
                    }
                }

                // insert the above data to local database
                // save the crate details to the database
                orderTable.insertNextOrder(reviewOrderData, customer_id);

                // show snackbar message
                showSnackbar(getView(), "Tomorrow's Invoice order saved successfully.");

                //now finish this fragment
                getActivity().getSupportFragmentManager().popBackStackImmediate();

                //move to next fragment
               /* DisplayTomorrowOrderFragment fragment = DisplayTomorrowOrderFragment.newInstance(customer_id, customer_name, reviewOrderData);
                launchInvoiceFragment(fragment);*/
            }
        });

        btnAddItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TomorrowOrderProductListActivity.class);
                intent.putExtra("customer_id", customer_id);
                intent.putExtra("customer_name", customer_name);
                ArrayList<String> arrItems = new ArrayList<String>();
                for (int i = 0; i < arrOrder.size(); i++) {
                    arrItems.add(arrOrder.get(i).getItemId());
                }
                intent.putStringArrayListExtra("existing_items", arrItems);
                startActivityForResult(intent, PRODUCT_LIST);
                ((NavBaseActivity) getContext()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
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
            rvCustomerOrder.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            rvCustomerOrder.setVisibility(View.VISIBLE);
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
                    NextDayOrderModel orderModel = new NextDayOrderModel();
                    orderModel.setItemId(pModel.getItem_id());
                    orderModel.setItemName(pModel.getItem_name());
                    orderModel.setCustomerId(customer_id);
                    orderModel.setCustomerName(customer_name);
                    if (pModel.isSelected())
                        arrOrder.add(orderModel);
                }
                orderAdapter.notifyDataSetChanged();
                refreshScreen();
            }
        }
    }
}
