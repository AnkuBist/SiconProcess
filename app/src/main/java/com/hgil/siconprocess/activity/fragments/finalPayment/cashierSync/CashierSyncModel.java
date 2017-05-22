package com.hgil.siconprocess.activity.fragments.finalPayment.cashierSync;

import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CashCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.CrateStockCheck;
import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.VanStockCheck;

import java.io.Serializable;

/**
 * Created by mohan.giri on 10-03-2017.
 */

public class CashierSyncModel implements Serializable {

    private CashCheck cashCheck;
    private CrateCheck crateCheck;
    private VanStockCheck vanStockCheck;
    private CrateStockCheck crateStockCheck;

    public CrateStockCheck getCrateStockCheck() {
        return crateStockCheck;
    }

    public void setCrateStockCheck(CrateStockCheck crateStockCheck) {
        this.crateStockCheck = crateStockCheck;
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
