package com.hgil.siconprocess.syncPOJO.autoSaleUpdate;

import java.util.ArrayList;

/**
 * Created by mohan.giri on 03-06-2017.
 */

public class SyncInvoice {

    private ArrayList<InvoiceSaleModel> arrInvSale;
    private ArrayList<InvoiceRejectionModel> arrInvRej;

    public ArrayList<InvoiceSaleModel> getArrInvSale() {
        return arrInvSale;
    }

    public void setArrInvSale(ArrayList<InvoiceSaleModel> arrInvSale) {
        this.arrInvSale = arrInvSale;
    }

    public ArrayList<InvoiceRejectionModel> getArrInvRej() {
        return arrInvRej;
    }

    public void setArrInvRej(ArrayList<InvoiceRejectionModel> arrInvRej) {
        this.arrInvRej = arrInvRej;
    }
}