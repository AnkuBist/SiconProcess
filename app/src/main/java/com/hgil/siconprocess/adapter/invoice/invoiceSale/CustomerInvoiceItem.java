package com.hgil.siconprocess.adapter.invoice.invoiceSale;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.activity.fragments.customerInvoice.makeSaleInvoice.CustomerInvoiceFragment;
import com.hgil.siconprocess.adapter.invoice.InvoiceModel;
import com.hgil.siconprocess.utils.Utility;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 25-01-2017.
 */

public class CustomerInvoiceItem {
    @BindView(R.id.tvItemName)
    TextView tvItemName;
    @BindView(R.id.tvStock)
    TextView tvStock;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvTarget)
    TextView tvTarget;
    @BindView(R.id.etQty)
    EditText etQty;
    @BindView(R.id.etSample)
    EditText etSample;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindString(R.string.strRupee)
    String strRupee;
    private Context mContext;
    private int stockAvail, tempStock;

    public CustomerInvoiceItem(Context mContext, View v) {
        this.mContext = mContext;
        ButterKnife.bind(this, v);
    }

    public void updateInvoiceItem(final CustomerInvoiceAdapter.ViewHolder holder, final InvoiceModel itemInvoice, final int position) {
        final String itemName = itemInvoice.getItemName();
        final float price = itemInvoice.getItemRate();
        float orderQty = itemInvoice.getInvQtyPs();
        final double orderAmount = itemInvoice.getOrderAmount();

        // get product stock
        stockAvail = itemInvoice.getStockAvail();
        tempStock = itemInvoice.getTempStock();

        tvItemName.setText(itemName);
        tvStock.setText("Stock : " + tempStock);
        tvRate.setText("Rate : " + strRupee + price);
        tvTarget.setText("TGT : ");
        tvTarget.setVisibility(View.GONE);
        if (orderQty > 0) {
            etQty.setText(String.valueOf((int) orderQty));
        } else {
            // let it be empty
        }
        etSample.setText(String.valueOf(itemInvoice.getFixedSample()));
        etAmount.setText(strRupee + String.valueOf(orderAmount));

        // now calculate the total price of the entered quantity
        etQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // qty cant exceed the available stock
                // amount to be calculated on basis of qty and price
                double oldOrderAmount = itemInvoice.getOrderAmount();
                if (s.length() != 0) {
                    int enteredQty = Integer.valueOf(etQty.getText().toString());
                    if (stockAvail >= enteredQty) {
                        int updateStock = stockAvail - enteredQty;
                        double orderAmount = Utility.roundTwoDecimals(enteredQty * price);
                        itemInvoice.setInvQtyPs(enteredQty);
                        itemInvoice.setTempStock(updateStock);
                        itemInvoice.setOrderAmount(orderAmount);
                        tvStock.setText("Stock : " + itemInvoice.getTempStock());
                        etAmount.setText(strRupee + String.valueOf(orderAmount));

                    } else {
                        Toast.makeText(mContext, "Can't enter quantity more than available quantity", Toast.LENGTH_SHORT).show();

                        itemInvoice.setInvQtyPs(0);
                        itemInvoice.setTempStock(stockAvail);
                        itemInvoice.setOrderAmount(0);
                        tvStock.setText("Stock : " + stockAvail);
                        etQty.setText("");
                        etAmount.setText(strRupee + "0.0");

                    }
                } else if (s.length() == 0) {
                    etAmount.setText(strRupee + "0.0");
                    tvStock.setText("Stock : " + stockAvail);

                    itemInvoice.setInvQtyPs(0);
                    itemInvoice.setTempStock(stockAvail);
                    itemInvoice.setOrderAmount(0);
                }

                double updated_amount = itemInvoice.getOrderAmount();
                CustomerInvoiceFragment.grandTotal += (-oldOrderAmount + updated_amount);
                String customerTotalInvoiceAmount = "Total\n" + strRupee + Utility.roundTwoDecimals(CustomerInvoiceFragment.grandTotal);
                CustomerInvoiceFragment.tvInvoiceTotal.setText(customerTotalInvoiceAmount);
            }
        });
    }


}
