package com.hgil.siconprocess.syncPOJO.supervisorSyncModel;

import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncInvoiceDetailModel;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohan.giri on 10-03-2017.
 */

public class CashierSyncModel implements Serializable {

    //updated models
    private CrateStockCheck crateStockCheck;
    private ArrayList<ItemStockCheck> arrItemStock;
    private ArrayList<SyncInvoiceDetailModel> arrRetailSale;
    private CashCheckModel cashCheckModel;

    public CrateStockCheck getCrateStockCheck() {
        return crateStockCheck;
    }

    public void setCrateStockCheck(CrateStockCheck crateStockCheck) {
        this.crateStockCheck = crateStockCheck;
    }

    public ArrayList<ItemStockCheck> getArrItemStock() {
        return arrItemStock;
    }

    public void setArrItemStock(ArrayList<ItemStockCheck> arrItemStock) {
        this.arrItemStock = arrItemStock;
    }

    public ArrayList<SyncInvoiceDetailModel> getArrRetailSale() {
        return arrRetailSale;
    }

    public void setArrRetailSale(ArrayList<SyncInvoiceDetailModel> arrRetailSale) {
        this.arrRetailSale = arrRetailSale;
    }

    public CashCheckModel getCashCheckModel() {
        return cashCheckModel;
    }

    public void setCashCheckModel(CashCheckModel cashCheckModel) {
        this.cashCheckModel = cashCheckModel;
    }
}
