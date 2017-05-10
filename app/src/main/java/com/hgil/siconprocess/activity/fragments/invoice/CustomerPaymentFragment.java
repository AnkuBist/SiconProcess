package com.hgil.siconprocess.activity.fragments.invoice;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.HomeInvoiceActivity;
import com.hgil.siconprocess.base.BaseFragment;
import com.hgil.siconprocess.database.dbModels.ChequeDetailsModel;
import com.hgil.siconprocess.database.dbModels.PaymentModel;
import com.hgil.siconprocess.database.dbModels.UpiPaymentModel;
import com.hgil.siconprocess.database.masterTables.CustomerRouteMappingView;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.database.tables.PaymentTable;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.UtilNetworkLocation;
import com.hgil.siconprocess.utils.Utility;
import com.hgil.siconprocess.utils.utilPermission.UtilIMEI;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerPaymentFragment extends BaseFragment {

    public static final int CHEQUE_DETAILS = 100;
    @BindView(R.id.tvCustomerName)
    TextView tvCustomerName;
    @BindView(R.id.tvCreditOs)
    TextView tvCreditOs;
    @BindView(R.id.etCash)
    EditText etCash;
    @BindView(R.id.etUpi)
    EditText etUpi;
    @BindView(R.id.etCheque)
    EditText etCheque;
    @BindView(R.id.tvSaleAmount)
    TextView tvSaleAmount;
    @BindView(R.id.tvTotalOs)
    TextView tvTotalOs;
    @BindView(R.id.tvCustomerTotal)
    TextView tvCustomerTotal;

    /*base layouts*/
    @BindView(R.id.layoutPayment)
    LinearLayout layoutPayment;
    @BindView(R.id.layoutTotal)
    LinearLayout layoutTotal;

    private PaymentTable paymentTable;
    private UpiPaymentModel upiDetails;
    private ChequeDetailsModel chequeDetailsModel;

    public CustomerPaymentFragment() {
        // Required empty public constructor
    }

    public static CustomerPaymentFragment newInstance(String customer_id, String customer_name) {
        CustomerPaymentFragment fragment = new CustomerPaymentFragment();
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
        return R.layout.fragment_customer_payment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (customer_name != null) {
            tvCustomerName.setText(customer_name);
        }

        // check customer CUSTCLASSIFICATIONID
        CustomerRouteMappingView customerRouteMappingView = new CustomerRouteMappingView(getContext());
        String custClassificationId = customerRouteMappingView.getCustomerClassification(customer_id);
        //if customer is institutional customer than hide the payment steps
        if (custClassificationId.matches(Constant.CUSTCLASSIFICATIONID)) {
            layoutPayment.setVisibility(View.GONE);
            layoutTotal.setVisibility(View.GONE);
        } else {
            layoutPayment.setVisibility(View.VISIBLE);
            layoutTotal.setVisibility(View.VISIBLE);
        }

        // get customer credit outstanding
        double creditOs = customerRouteMappingView.custCreditAmount(customer_id);
        tvCreditOs.setText(strRupee + creditOs);

        //get customer sale amount.....rejection is also substracted from this
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(getContext());
        CustomerRejectionTable rejectionTable = new CustomerRejectionTable(getContext());
        double invoiceTotal = invoiceOutTable.custInvoiceTotalAmount(customer_id);
        double rejectionTotal = rejectionTable.custRejTotalAmount(customer_id);
        final double saleTotal = invoiceTotal - rejectionTotal;
        tvSaleAmount.setText(strRupee + Utility.roundTwoDecimals(saleTotal));

        // calculate payable amount
        final double payable_amount = saleTotal + creditOs;
        tvTotalOs.setText(strRupee + Utility.roundOff(payable_amount));

        paymentTable = new PaymentTable(getContext());
        PaymentModel paymentModel = paymentTable.getCustomerPaymentInfo(customer_id);
        chequeDetailsModel = paymentModel.getChequeDetail();
        upiDetails = paymentModel.getUpiDetail();

        if (upiDetails == null)
            upiDetails = new UpiPaymentModel();

        if (chequeDetailsModel == null)
            chequeDetailsModel = new ChequeDetailsModel();


        // payment details exists then display the existing values to et and respective data
        if (paymentModel != null) {
            etCash.setText(String.valueOf(paymentModel.getCashPaid()));
            etUpi.setText(String.valueOf(upiDetails.getPaidAmount()));
            etCheque.setText(String.valueOf(chequeDetailsModel.getChequeAmount()));
            tvCustomerTotal.setText(strRupee + paymentModel.getTotalPaidAmount());
        }

        setTitle("Payment");
        showSaveButton();
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save some more info
                PaymentModel paymentModel = new PaymentModel();
                paymentModel.setCustomerId(customer_id);
                paymentModel.setCustomerName(customer_name);
                paymentModel.setSaleAmount(saleTotal);
                paymentModel.setUpiDetail(upiDetails);
                paymentModel.setChequeDetail(chequeDetailsModel);
                paymentModel.setCashPaid(Utility.getDouble(etCash.getText().toString().trim()));
                paymentModel.setTotalPaidAmount(Utility.getDouble(String.valueOf(paymentModel.getCashPaid() + paymentModel.getUpiDetail().getPaidAmount()))); //+ paymentModel.getChequeDetail().getChequeAmount())));

                // update device imei_no, location and login_id
                // time_stamp will be updated automatically;
                paymentModel.setImei_no(UtilIMEI.getIMEINumber(getContext()));
                paymentModel.setLat_lng(UtilNetworkLocation.getLatLng(UtilNetworkLocation.getLocation(getContext())));
                paymentModel.setLogin_id(getLoginId());

                paymentTable.insertCustPayment(paymentModel);

                //move to next fragment
                CratesManagementFragment fragment = CratesManagementFragment.newInstance(customer_id, customer_name);
                launchInvoiceFragment(fragment);
            }
        });

        etCash.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalAmount();
                //double chequeAmount = Utility.roundTwoDecimals(Utility.getDouble(etCheque.getText().toString()));
                //tvCustomerTotal.setText(strRupee + (chequeAmount + Utility.roundTwoDecimals(Utility.getDouble(etCash.getText().toString()))));
            }
        });

        etUpi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {

                    etCash.requestFocus();

                    // start dialog here
                    UpiPaymentDialog cdd = new UpiPaymentDialog(getActivity(), upiDetails);
                    cdd.setCancelable(false);
                    cdd.show();

                    //Grab the window of the dialog, and change the width
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = cdd.getWindow();
                    lp.copyFrom(window.getAttributes());
                    //This makes the dialog take up the full width
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    window.setAttributes(lp);
                }
                return false;
            }
        });

        // etCheque.setEnabled(false);
        etCheque.setOnTouchListener(new View.OnTouchListener()

        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    Intent intent = new Intent(getContext(), ChequeDetailsActivity.class);
                    intent.putExtra("saved_cheque_details", chequeDetailsModel);
                    startActivityForResult(intent, CHEQUE_DETAILS);
                    ((HomeInvoiceActivity) getContext()).overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                }
                return false;
            }
        });
    }

    public void setUpiDetails(UpiPaymentModel upiPaymentModel) {
        this.upiDetails = upiPaymentModel;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();

            if (requestCode == CHEQUE_DETAILS) {
                chequeDetailsModel = (ChequeDetailsModel) bundle.getSerializable("cheque_detail");
                chequeDetailsModel.setCustomer_name(customer_name);
                chequeDetailsModel.setCustomer_id(customer_id);

                updateTotalAmount();
                //tvCustomerTotal.setText(strRupee + Utility.roundTwoDecimals(chequeDetailsModel.getChequeAmount() + Utility.roundTwoDecimals(Utility.getDouble(etCash.getText().toString()))));
                etCheque.setText(String.valueOf(Utility.roundTwoDecimals(chequeDetailsModel.getChequeAmount())));
            }
        }
    }


    /*customize dialog for UPI payment*/
    /*private void upiPaymentDialog() {

    }*/

    private void updateTotalAmount() {
        //(double cash_amount, double cheque_amount, double upi_amount) {
        tvCustomerTotal.setText(strRupee + Utility.roundTwoDecimals(Utility.getDouble(etCash.getText().toString()) + upiDetails.getPaidAmount())); //+ chequeDetailsModel.getChequeAmount()));
    }

    public class UpiPaymentDialog extends Dialog implements
            android.view.View.OnClickListener {

        public Activity c;
        public Dialog d;
        public UpiPaymentModel upiDetails;
        public Button yes, no;
        public EditText etUpiReferenceId, etUpiAmount;

        public UpiPaymentDialog(Activity a, UpiPaymentModel upiDetails) {
            super(a);
            this.c = a;
            this.upiDetails = upiDetails;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_upi_payment_details);
            yes = (Button) findViewById(R.id.btnCancel);
            no = (Button) findViewById(R.id.btnSubmit);
            etUpiReferenceId = (EditText) findViewById(R.id.etUpiReferenceId);
            etUpiAmount = (EditText) findViewById(R.id.etUpiAmount);

            if (upiDetails != null) {
                etUpiReferenceId.setText(upiDetails.getPaymentReferenceId());
                etUpiAmount.setText(String.valueOf(upiDetails.getPaidAmount()));
            }

            yes.setOnClickListener(this);
            no.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCancel:
                    dismiss();
                    break;
                case R.id.btnSubmit:
                    String reference_id = etUpiReferenceId.getText().toString();
                    String amount = etUpiAmount.getText().toString();
                    if (reference_id.isEmpty()) {
                        etUpiReferenceId.requestFocus();
                        etUpiReferenceId.setError("Enter reference id");
                    } else if (amount.isEmpty() || Utility.getDouble(amount) <= 0) {
                        etUpiAmount.requestFocus();
                        etUpiAmount.setError("Enter some payment amount");
                    } else {
                        // now submit and update upi payment detail
                        upiDetails.setPaymentReferenceId(reference_id);
                        upiDetails.setPaidAmount(Utility.getDouble(amount));
                        setUpiDetails(upiDetails);
                        etUpi.setText(amount);
                        updateTotalAmount();
                        dismiss();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
