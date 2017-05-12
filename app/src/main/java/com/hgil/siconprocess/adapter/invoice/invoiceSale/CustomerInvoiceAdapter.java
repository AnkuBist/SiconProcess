package com.hgil.siconprocess.adapter.invoice.invoiceSale;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hgil.siconprocess.R;
import com.hgil.siconprocess.adapter.invoice.InvoiceModel;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * Created by mohan.giri on 25-01-2017.
 */

public class CustomerInvoiceAdapter extends RecyclerView.Adapter<CustomerInvoiceAdapter.ViewHolder> {
    public ArrayList<InvoiceModel> mDataset;
    private Context mContext;

    public CustomerInvoiceAdapter(Context mContext, ArrayList<InvoiceModel> myDataset) {
        this.mContext = mContext;
        this.mDataset = myDataset;
    }

    @Override
    public CustomerInvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_customer_invoice, null, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final InvoiceModel itemInvoice = mDataset.get(position);
        holder.invoiceItemView.updateInvoiceItem(holder, itemInvoice, position);
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CustomerInvoiceItem invoiceItemView;
        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
            invoiceItemView = new CustomerInvoiceItem(mContext, v);
        }
    }
}