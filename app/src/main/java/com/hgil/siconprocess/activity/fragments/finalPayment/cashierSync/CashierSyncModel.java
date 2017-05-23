package com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync;

import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.CashCheckModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.CrateStockCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.ItemStockCheck;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mohan.giri on 10-03-2017.
 */

public class CashierSyncModel implements Serializable {

    //updated models
    private CrateStockCheck crateStockCheck;
    private ArrayList<ItemStockCheck> arrItemStock;
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

    public CashCheckModel getCashCheckModel() {
        return cashCheckModel;
    }

    public void setCashCheckModel(CashCheckModel cashCheckModel) {
        this.cashCheckModel = cashCheckModel;
    }
}
