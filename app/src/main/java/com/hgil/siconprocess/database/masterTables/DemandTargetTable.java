package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.routeTarget.RouteTargetModel;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.DemandTargetModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class DemandTargetTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_demand_target";
    private static final String TABLE_NAME = "SD_DemandTarget_Master";

    private static final String DDATE = "DDate";
    private static final String ROUTE_ID = "Route_ID";
    private static final String CUSTOMER_ID = "Customer_ID";
    private static final String ITEM_ID = "Item_id";
    private static final String TARGET_QTY = "Target_Qty";

    private final Context mContext;

    public DemandTargetTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + DDATE + " TEXT NOT NULL, "
                + ROUTE_ID + " TEXT NOT NULL, " + CUSTOMER_ID + " TEXT NULL, "
                + ITEM_ID + " TEXT NOT NULL, " + TARGET_QTY + " REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void eraseTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME); //delete all rows in a table
        db.close();
    }

    // insert multiple
    public boolean insertDemandTarget(List<DemandTargetModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            DemandTargetModel demandTargetModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DDATE, demandTargetModel.getDDate());
            contentValues.put(ROUTE_ID, demandTargetModel.getRouteID());
            contentValues.put(CUSTOMER_ID, demandTargetModel.getCustomerID());
            contentValues.put(ITEM_ID, demandTargetModel.getItemId());
            contentValues.put(TARGET_QTY, demandTargetModel.getTargetQty());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public DemandTargetModel getDemandTargetByItem(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        DemandTargetModel demandTargetModel = new DemandTargetModel();
        if (res.moveToFirst()) {
            demandTargetModel.setDDate(res.getString(res.getColumnIndex(DDATE)));
            demandTargetModel.setRouteID(res.getString(res.getColumnIndex(ROUTE_ID)));
            demandTargetModel.setCustomerID(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            demandTargetModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            demandTargetModel.setTargetQty(res.getFloat(res.getColumnIndex(TARGET_QTY)));
        }
        res.close();
        db.close();
        return demandTargetModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    /*public boolean updateUserRoleMap(UserRoleMapModel userRoleMapModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL, userRoleMapModel.getEmail());
        contentValues.put(ROLE_ID, userRoleMapModel.getRole_id());
        contentValues.put(IP, userRoleMapModel.getIp());
        contentValues.put(U_TS, userRoleMapModel.getU_ts());
        db.update(TABLE_NAME, contentValues, USER_ROLE_ID + "= ? ", new String[]{Integer.toString(userRoleMapModel.getUser_role_id())});
        db.close();
        return true;
    }*/

 /*   public Integer deleteUserRoleMapById(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, USER_ROLE_ID + "= ? ", new String[]{Integer.toString(id)});
    }*/

    public ArrayList<DemandTargetModel> getAllDemandTarget() {
        ArrayList<DemandTargetModel> array_list = new ArrayList<DemandTargetModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                DemandTargetModel demandTargetModel = new DemandTargetModel();
                demandTargetModel.setDDate(res.getString(res.getColumnIndex(DDATE)));
                demandTargetModel.setRouteID(res.getString(res.getColumnIndex(ROUTE_ID)));
                demandTargetModel.setCustomerID(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                demandTargetModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                demandTargetModel.setTargetQty(res.getFloat(res.getColumnIndex(TARGET_QTY)));

                array_list.add(demandTargetModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }


    /* get demand target for dashboard target screen*/
    public ArrayList<RouteTargetModel> getDashboardTargets() {
        ArrayList<RouteTargetModel> array_list = new ArrayList<RouteTargetModel>();

        ProductView productView = new ProductView(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT DISTINCT " + ITEM_ID + ", sum(" + TARGET_QTY + ") as target_qty FROM " + TABLE_NAME + " GROUP BY " + ITEM_ID, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteTargetModel routeTargetModel = new RouteTargetModel();
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                routeTargetModel.setItemId(item_id);
                routeTargetModel.setTarget(res.getInt(res.getColumnIndex("target_qty")));

                // get product name from the product list table
                productView.productName(item_id);
                routeTargetModel.setItem_name(productView.productName(item_id));

                // get the product invoice count from the local table
                routeTargetModel.setAchieved(invoiceOutTable.soldItemTargetCount(item_id));
                routeTargetModel.setVariance(routeTargetModel.getTarget() - routeTargetModel.getAchieved());

                array_list.add(routeTargetModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*get total of target amount*/
    /* get demand target for dashboard target screen*/
    public double customerTargetSale(String customer_id) {
        double target_sale_amount = 0;

        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(mContext);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + ITEM_ID + ", " + TARGET_QTY + " FROM " + TABLE_NAME + " where " + CUSTOMER_ID + "=?", new String[]{customer_id});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                int target = res.getInt(res.getColumnIndex(TARGET_QTY));

                // get item price for customer
                double amount = itemPriceTable.getItemPriceById(item_id, customer_id);

                target_sale_amount += (amount * target);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return target_sale_amount;
    }
}