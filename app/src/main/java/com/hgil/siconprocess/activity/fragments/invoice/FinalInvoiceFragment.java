package com.hgil.siconprocess.activity.fragments.invoice;

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
import com.hgil.siconprocess.database.masterTables.CreditOpeningTable;
import com.hgil.siconprocess.database.masterTables.CustomerInfoView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.NextDayOrderTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.utils.Utility;
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
    private String message = "Hello, \nthis is a test message.";

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

        //TODO
        // get customer contact details
        mobile = new CustomerInfoView(getContext()).getCustomerContact(customer_id);

        // get customer credit outstanding
        CreditOpeningTable creditOpeningTable = new CreditOpeningTable(getContext());
        double creditOs = creditOpeningTable.custCreditAmount(customer_id);
        tvOpeningBalance.setText(strRupee + Utility.roundTwoDecimals(creditOs));

        PaymentTable paymentTable = new PaymentTable(getContext());
        PaymentModel paymentModel = paymentTable.getCustomerPaymentInfo(customer_id);
        double todaySale = paymentModel.getSaleAmount();
        double amountCollected = paymentModel.getTotalPaidAmount();
        double osBalance = creditOs + todaySale - amountCollected;
        tvTodaySale.setText(strRupee + Utility.roundTwoDecimals(todaySale));
        tvAmountCollected.setText(strRupee + Utility.roundTwoDecimals(amountCollected));
        tvOsBalance.setText(strRupee + Utility.roundTwoDecimals(osBalance));

        setTitle("Invoice");
        hideSaveButton();
    }

    @OnClick(R.id.btnInvoiceCancel)
    public void onInvoiceCancel(View view) {
        // on press cancel button please erase all recent invoice update for the user stored at local and move to the main page
        CustomerRejectionTable customerRejectionTable = new CustomerRejectionTable(getContext());
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(getContext());
        NextDayOrderTable nextDayOrderTable = new NextDayOrderTable(getContext());
        PaymentTable paymentTable = new PaymentTable(getContext());

        customerRejectionTable.cancelInvoice(customer_id);
        invoiceOutTable.cancelInvoice(customer_id);
        nextDayOrderTable.cancelInvoice(customer_id);
        paymentTable.cancelInvoice(customer_id);

        // restart app or move back to the route map list
        getContext().startActivity(new Intent(getContext(), NavBaseActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        ((HomeInvoiceActivity) getContext()).finish();
        ((HomeInvoiceActivity) getContext()).overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);


    }

    @OnClick(R.id.btnSendSms)
    public void onSendSms(View view) {
        if (mobile != null && mobile.matches(""))
            UtilsSms.checkAndroidVersionForSms(getContext(), mobile, message);
        else
            showSnackbar(getView(), "No contact found with this customer");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case SEND_SMS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    UtilsSms.sendSMS(getContext(), mobile, message);
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
}
