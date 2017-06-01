package com.hgil.siconprocess.activity.fragments.customerInvoice;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.HomeInvoiceActivity;
import com.hgil.siconprocess.activity.NavBaseActivity;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.dbModels.PaymentModel;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.MarketProductTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.ui.SampleDialog;
import com.hgil.siconprocess.utils.utilPermission.UtilsSms;

import butterknife.BindView;
import butterknife.OnClick;

import static com.hgil.siconprocess.utils.utilPermission.UtilsSms.SEND_SMS;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinalInvoiceFragment extends BaseFragment {

    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvOpeningBalance)
    TextView tvOpeningBalance;
    @BindView(R.id.tvTodaySale)
    TextView tvTodaySale;
    @BindView(R.id.tvAmountCollected)
    TextView tvAmountCollected;
    @BindView(R.id.tvOsBalance)
    TextView tvOsBalance;

    @BindView(R.id.btnInvoiceCancel)
    Button btnInvoiceCancel;
    @BindView(R.id.btnSendSms)
    Button btnSendSms;

    //TODO
    // for now values are static
    private String mobile = "9023503384";

    private CustomerRejectionTable customerRejectionTable;
    private InvoiceOutTable invoiceOutTable;
    private PaymentTable paymentTable;
    private MarketProductTable marketProductTable;

    private int qtyPcs;
    private double creditOs, todaySale, amountCollected, osBalance;
    private String customerInvoiceBillNo, date, harvestCareNo;

    public FinalInvoiceFragment() {
        // Required empty public constructor
    }

    public static FinalInvoiceFragment newInstance(String customer_id, String customer_name) {
        FinalInvoiceFragment fragment = new FinalInvoiceFragment();
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
        return R.layout.fragment_final_invoice;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer_name != null) {
            tvCustomerName.setText(customer_name);
        }

        customerRejectionTable = new CustomerRejectionTable(getContext());
        invoiceOutTable = new InvoiceOutTable(getContext());
        paymentTable = new PaymentTable(getContext());
        marketProductTable = new MarketProductTable(getContext());

        //TODO
        // get customer contact details
        //mobile = new CustomerInfoView(getContext()).getCustomerContact(customer_id);

        // get customer credit outstanding
        CustomerRouteMappingView routeCustomerView = new CustomerRouteMappingView(getContext());
        creditOs = Utility.roundTwoDecimals(routeCustomerView.custCreditAmount(customer_id));
        tvOpeningBalance.setText(strRupee + creditOs);

        PaymentTable paymentTable = new PaymentTable(getContext());
        PaymentModel paymentModel = paymentTable.getCustomerPaymentInfo(customer_id);
        todaySale = Utility.roundTwoDecimals(paymentModel.getSaleAmount());
        amountCollected = Utility.roundTwoDecimals(paymentModel.getTotalPaidAmount());
        osBalance = Utility.roundTwoDecimals(creditOs + todaySale - amountCollected);

        //message related info
        customerInvoiceBillNo = invoiceOutTable.returnCustomerBillNo(customer_id);
        date = Utility.getDate();
        // saleQty-marketRejQty
        qtyPcs = invoiceOutTable.custTotalSaleQty(customer_id) - customerRejectionTable.custTotalMRej(customer_id);
        harvestCareNo = getRouteModel().getCustomerCareNo();

        tvTodaySale.setText(strRupee + todaySale);
        tvAmountCollected.setText(strRupee + amountCollected);
        tvOsBalance.setText(strRupee + osBalance);

        setTitle("Invoice");
        hideSaveButton();
    }

    @OnClick(R.id.btnInvoiceCancel)
    public void onInvoiceCancel(View view) {
        // on press cancel button please erase all recent invoice update for the user stored at local and move to the main page
        // will erase the customer prepared all data
        customerRejectionTable.cancelInvoice(customer_id);
        invoiceOutTable.cancelInvoice(customer_id);
        paymentTable.cancelInvoice(customer_id);
        //marketProductTable.cancelInvoice(customer_id);

        /*customerRejectionTable.updateCustInvRejStatus(customer_id, Constant.STATUS_CANCELLED);
        invoiceOutTable.updateCustInvStatus(customer_id, Constant.STATUS_CANCELLED);
        paymentTable.updateCustPaymentStatus(customer_id, Constant.STATUS_CANCELLED);
        marketProductTable.updateCustMarketStatus(customer_id, Constant.STATUS_CANCELLED);*/

        // restart app or move back to the route map list
        getContext().startActivity(new Intent(getContext(), NavBaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        ((HomeInvoiceActivity) getContext()).finish();
        ((HomeInvoiceActivity) getContext()).overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
    }

    @OnClick(R.id.btnSendSms)
    public void onSendSms(View view) {
         /*after message send button press set the customer route to set to completed*/
        //activity is finished at
        CustomerRouteMappingView custRouteMap = new CustomerRouteMappingView(getContext());
        custRouteMap.updateCustomerStatus(customer_id, Constant.STATUS_COMPLETE);

        // update all inv prepared table status for the customer to completed
        customerRejectionTable.updateCustInvRejStatus(customer_id, Constant.STATUS_COMPLETE);
        invoiceOutTable.updateCustInvStatus(customer_id, Constant.STATUS_COMPLETE);
        paymentTable.updateCustPaymentStatus(customer_id, Constant.STATUS_COMPLETE);
        //marketProductTable.updateCustMarketStatus(customer_id, Constant.STATUS_COMPLETE);

        if (mobile != null && !mobile.matches("")) {
            String message = messageFormat(customerInvoiceBillNo, date, qtyPcs,
                    creditOs, amountCollected, todaySale,
                    osBalance, harvestCareNo);
            UtilsSms.checkAndroidVersionForSms(getContext(), mobile, message);
        } else {
            new SampleDialog().SampleMessageDialog("No Contact found with this customer", getContext());
            //showSnackbar(getView(), "No contact found with this customer");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    String message = messageFormat(customerInvoiceBillNo, date, qtyPcs,
                            creditOs, amountCollected, todaySale,
                            osBalance, harvestCareNo);
                    UtilsSms.sendSMS(getContext(), mobile, "");
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "SEND_SMS Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private String messageFormat(String billNo, String date, int qtyPcs,
                                 double openingAmt, double paidAmt, double saleAmt,
                                 double balAmt, String harvestCareNo) {
        String strMessage = String.format("Harvest Gold\n " +
                "B.NO:%s;" +
                "Dt:%s;" +
                "All Qty:%d Pcs;" +
                "Prev:%f/-;" +
                "Paid:%f/-;" +
                "Sale:%f/-;" +
                "Bal:%f/-;" +
                " Customer Care No.:%s", billNo, date, qtyPcs, openingAmt, paidAmt, saleAmt, balAmt, harvestCareNo);
        return strMessage;
    }

}
