package com.hgil.siconprocess.activity.fragments.invoiceSyncModel;

import com.hgil.siconprocess.database.dbModels.MarketProductModel;
import com.hgil.siconprocess.database.dbModels.NextDayOrderModel;

import java.util.ArrayList;

/**
 * Created by mohan.giri on 20-02-2017.
 */

public class SyncData {

    private ArrayList<SyncInvoiceDetailModel> syncInvoice;
    private ArrayList<SyncInvoiceDetailModel> syncInvoiceRejection;
    private ArrayList<RejectionDetailModel> syncRejectDetails;
    private ArrayList<CollectionCashModel> cashCollection;
    private ArrayList<CollectionCrateModel> crateCollection;
    private CrateStockCheck crateStock;
    private CashCheck cashCheck;
    private CrateCheck crateCheck;
    private VanStockCheck vanStockCheck;
    private ArrayList<CollectionChequeModel> chequeCollection;
    private ArrayList<NextDayOrderModel> arrNextDayOrder;
    private ArrayList<MarketProductModel> arrMaketProductsSummary;

    public ArrayList<MarketProductModel> getArrMaketProductsSummary() {
        return arrMaketProductsSummary;
    }

    public void setArrMaketProductsSummary(ArrayList<MarketProductModel> arrMaketProductsSummary) {
        this.arrMaketProductsSummary = arrMaketProductsSummary;
    }

    public ArrayList<CollectionChequeModel> getChequeCollection() {
        return chequeCollection;
    }

    public void setChequeCollection(ArrayList<CollectionChequeModel> chequeCollection) {
        this.chequeCollection = chequeCollection;
    }

    public ArrayList<NextDayOrderModel> getArrNextDayOrder() {
        return arrNextDayOrder;
    }

    public void setArrNextDayOrder(ArrayList<NextDayOrderModel> arrNextDayOrder) {
        this.arrNextDayOrder = arrNextDayOrder;
    }

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

    public ArrayList<RejectionDetailModel> getSyncRejectDetails() {
        return syncRejectDetails;
    }

    public void setSyncRejectDetails(ArrayList<RejectionDetailModel> syncRejectDetails) {
        this.syncRejectDetails = syncRejectDetails;
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

    public CrateStockCheck getCrateStock() {
        return crateStock;
    }

    public void setCrateStock(CrateStockCheck crateStock) {
        this.crateStock = crateStock;
    }

    public CashCheck getCashCheck() {
        return cashCheck;
    }

    public void setCashCheck(CashCheck cashCheck) {
        this.cashCheck = cashCheck;
    }

    public CrateCheck getCrateCheck() {
        return crateCheck;
    }

    public void setCrateCheck(CrateCheck crateCheck) {
        this.crateCheck = crateCheck;
    }

    public VanStockCheck getVanStockCheck() {
        return vanStockCheck;
    }

    public void setVanStockCheck(VanStockCheck vanStockCheck) {
        this.vanStockCheck = vanStockCheck;
    }
}
