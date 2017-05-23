package com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync;

import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CashCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.CashCheckModel;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.cashierSync.CrateStockCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.VanStockCheck;

import java.io.Serializable;

/**
 * Created by mohan.giri on 10-03-2017.
 */

public class CashierSyncModel implements Serializable {

    //updated models
    private CrateStockCheck crateStockCheck;
    private CashCheckModel cashCheckModel;

    public CashCheckModel getCashCheckModel() {
        return cashCheckModel;
    }

    public void setCashCheckModel(CashCheckModel cashCheckModel) {
        this.cashCheckModel = cashCheckModel;
    }

    public CrateStockCheck getCrateStockCheck() {
        return crateStockCheck;
    }

    public void setCrateStockCheck(CrateStockCheck crateStockCheck) {
        this.crateStockCheck = crateStockCheck;
    }
}
