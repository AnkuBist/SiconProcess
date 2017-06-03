package com.hgil.siconprocess.syncPOJO.autoSaleUpdate;

/**
 * Created by mohan.giri on 03-06-2017.
 */

public class InvoiceSaleModel {
    private String InvoiceId;
    private String CustomerId;
    private String ItemId;
    private int SaleQty;
    private String updatebydate;

    public String getInvoiceId() {
        return InvoiceId;
    }

    public void setInvoiceId(String InvoiceId) {
        this.InvoiceId = InvoiceId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String CustomerId) {
        this.CustomerId = CustomerId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String ItemId) {
        this.ItemId = ItemId;
    }

    public int getSaleQty() {
        return SaleQty;
    }

    public void setSaleQty(int SaleQty) {
        this.SaleQty = SaleQty;
    }

    public String getUpdatebydate() {
        return updatebydate;
    }

    public void setUpdatebydate(String updatebydate) {
        this.updatebydate = updatebydate;
    }


}