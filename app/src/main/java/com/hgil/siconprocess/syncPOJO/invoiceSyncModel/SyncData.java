package com.hgil.siconprocess.syncPOJO.invoiceSyncModel;

import com.hgil.siconprocess.database.dbModels.MarketProductModel;
import com.hgil.siconprocess.database.dbModels.NextDayOrderModel;

import java.util.ArrayList;

/**
 * Created by mohan.giri on 20-02-2017.
 */

public class SyncData {

    private ArrayList<SyncInvoiceDetailModel> syncInvoice;
    private ArrayList<SyncInvoiceDetailModel> syncInvoiceRejection;
    private ArrayList<RejectionDetailModel> syncRejDetails;
    private ArrayList<CollectionCashModel> cashCollection;
    private ArrayList<CollectionCrateModel> crateCollection;
    private ArrayList<CollectionChequeModel> chequeCollection;
    //private ArrayList<NextDayOrderModel> arrNextDayOrder;
    //private ArrayList<MarketProductModel> arrMarketProductsSummary;

    private ArrayList<SyncInvoiceDetailModel> syncInvoiceSaleRej;

    public ArrayList<SyncInvoiceDetailModel> getSyncInvoiceSaleRej() {
        return syncInvoiceSaleRej;
    }

    public void setSyncInvoiceSaleRej(ArrayList<SyncInvoiceDetailModel> syncInvoiceSaleRej) {
        this.syncInvoiceSaleRej = syncInvoiceSaleRej;
    }

    /*public ArrayList<MarketProductModel> getArrMarketProductsSummary() {
        return arrMarketProductsSummary;
    }

    public void setArrMarketProductsSummary(ArrayList<MarketProductModel> arrMarketProductsSummary) {
        this.arrMarketProductsSummary = arrMarketProductsSummary;
    }*/

    public ArrayList<CollectionChequeModel> getChequeCollection() {
        return chequeCollection;
    }

    public void setChequeCollection(ArrayList<CollectionChequeModel> chequeCollection) {
        this.chequeCollection = chequeCollection;
    }

   /* public ArrayList<NextDayOrderModel> getArrNextDayOrder() {
        return arrNextDayOrder;
    }

    public void setArrNextDayOrder(ArrayList<NextDayOrderModel> arrNextDayOrder) {
        this.arrNextDayOrder = arrNextDayOrder;
    }*/

    public ArrayList<SyncInvoiceDetailModel> getSyncInvoice() {
        return syncInvoice;
    }

    public void setSyncInvoice(ArrayList<SyncInvoiceDetailModel> syncInvoice) {
        this.syncInvoice = syncInvoice;
    }

    public ArrayList<SyncInvoiceDetailModel> getSyncInvoiceRejection() {
        return syncInvoiceRejection;
    }

    public void setSyncInvoiceRejection(ArrayList<SyncInvoiceDetailModel> syncInvoiceRejection) {
        this.syncInvoiceRejection = syncInvoiceRejection;
    }

    public ArrayList<RejectionDetailModel> getSyncRejDetails() {
        return syncRejDetails;
    }

    public void setSyncRejDetails(ArrayList<RejectionDetailModel> syncRejDetails) {
        this.syncRejDetails = syncRejDetails;
    }

    public ArrayList<CollectionCashModel> getCashCollection() {
        return cashCollection;
    }

    public void setCashCollection(ArrayList<CollectionCashModel> cashCollection) {
        this.cashCollection = cashCollection;
    }

    public ArrayList<CollectionCrateModel> getCrateCollection() {
        return crateCollection;
    }

    public void setCrateCollection(ArrayList<CollectionCrateModel> crateCollection) {
        this.crateCollection = crateCollection;
    }
}
