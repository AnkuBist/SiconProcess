package com.hgil.siconprocess.activity.fragments.customerInvoice;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.base.BaseToolbarActivity;
import com.hgil.siconprocess.database.dbModels.ChequeDetailsModel;
import com.hgil.siconprocess.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChequeDetailsActivity extends BaseToolbarActivity {

    @Nullable
    @BindView(R.id.etChequeNumber)
    EditText etChequeNumber;
    @Nullable
    @BindView(R.id.etChequeDate)
    EditText etChequeDate;
    @Nullable
    @BindView(R.id.etChequeAmount)
    EditText etChequeAmount;
    @Nullable
    @BindView(R.id.etBankName)
    EditText etBankName;
    @Nullable
    @BindView(R.id.etBankBranch)
    EditText etBankBranch;
    @Nullable
    @BindView(R.id.etInvoiceId)
    EditText etInvoiceId;

    @Nullable
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @Nullable
    @BindView(R.id.btnCancel)
    Button btnCancel;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_details);

        ButterKnife.bind(this);

        setNavTitle("Cheque Details");
        hideSaveBtn();

        ChequeDetailsModel chequeDetailsModel = (ChequeDetailsModel) getIntent().getSerializableExtra("saved_cheque_details");
        if (chequeDetailsModel != null) {
            etChequeNumber.setText(Utility.getString(chequeDetailsModel.getChequeNumber()));
            etChequeDate.setText(Utility.getString(chequeDetailsModel.getChequeDate()));
            if (chequeDetailsModel.getChequeAmount() > 0)
                etChequeAmount.setText(String.valueOf(chequeDetailsModel.getChequeAmount()));
            etBankName.setText(Utility.getString(chequeDetailsModel.getBankName()));
            etBankBranch.setText(Utility.getString(chequeDetailsModel.getBankBranch()));
            etInvoiceId.setText(Utility.getString(chequeDetailsModel.getInvoiceId()));
        }

        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        //etChequeDate.setEnabled(false);
        etChequeDate.setOnTouchListener(new View.OnTouchListener()

        {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_UP == event.getAction()) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(ChequeDetailsActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH));//.show();
                    datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());
                    datePickerDialog.show();
                }
                return false;
            }
        });
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etChequeDate.setText(sdf.format(myCalendar.getTime()));
    }

    // @OnClick(R.id.btnSubmit)
    public void onSubmit(View view) {
        //Perform some action
        String chequeNumber = etChequeNumber.getText().toString().trim();
        String chequeDate = etChequeDate.getText().toString().trim();
        String chequeAmount = etChequeAmount.getText().toString().trim();
        String bankName = etBankName.getText().toString().trim();
        String bankBranch = etBankBranch.getText().toString().trim();
        String invoiceId = etInvoiceId.getText().toString().trim();

        // first mandate all fields are required to fill
        if (chequeNumber == null || chequeNumber.isEmpty()) {
            etChequeNumber.requestFocus();
            Toast.makeText(this, "Enter cheque number", Toast.LENGTH_SHORT).show();
        } else if (chequeDate == null || chequeDate.isEmpty()) {
            etChequeDate.requestFocus();
            Toast.makeText(this, "Enter date on cheque", Toast.LENGTH_SHORT).show();
        } else if (chequeAmount == null || chequeAmount.isEmpty()) {
            etChequeAmount.requestFocus();
            Toast.makeText(this, "Enter date on cheque", Toast.LENGTH_SHORT).show();
        } else if (bankName == null || bankName.isEmpty()) {
            etBankName.requestFocus();
            Toast.makeText(this, "Enter bank name on cheque", Toast.LENGTH_SHORT).show();
        } else if (bankBranch == null || bankBranch.isEmpty()) {
            etBankBranch.requestFocus();
            Toast.makeText(this, "Enter bank branch", Toast.LENGTH_SHORT).show();
        } else if (invoiceId == null || invoiceId.isEmpty()) {
            etInvoiceId.requestFocus();
            Toast.makeText(this, "Enter invoice ID", Toast.LENGTH_SHORT).show();
        } else {
            Intent resultIntent = new Intent();
            ChequeDetailsModel chequeDetail = new ChequeDetailsModel();
            // convert amount to double before setting into model
            double amount = Utility.getDouble(chequeAmount);

            chequeDetail.setChequeNumber(chequeNumber);
            chequeDetail.setChequeDate(chequeDate);
            chequeDetail.setChequeAmount(amount);
            chequeDetail.setBankName(bankName);
            chequeDetail.setBankBranch(bankBranch);
            chequeDetail.setInvoiceId(invoiceId);

            Bundle bundle = new Bundle();
            bundle.putSerializable("cheque_detail", chequeDetail);
            resultIntent.putExtras(bundle);
            setResult(Activity.RESULT_OK, resultIntent);
            finish();
            overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
        }
    }

    //
    // @OnClick(R.id.btnCancel)
    public void onCancel(View view) {
        //Perform some action
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_right);
    }


}
