package com.hgil.siconprocess.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerRouteMapModel;
import com.hgil.siconprocess.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class CustomerRouteMappingView extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Sicon_route_map";
    private static final String TABLE_NAME = "V_SD_Customer_Route_Mapping";
    private static final String REC_ID = "Rec_id";
    private static final String SUB_COMPANY_ID = "Sub_Company_id";
    private static final String DEPOT = "Depot";
    private static final String ROUTE_ID = "Route_id";
    private static final String ROUTE_NAME = "Route_Name";
    private static final String SALE_DATE_PARAMETER = "Sale_Date_Parameter";
    private static final String PSMID = "PSMID";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String CUSTOMER_NAME = "Customer_Name";
    private static final String PRICEGROUP = "PRICEGROUP";
    private static final String LINEDISC = "LINEDISC";
    private static final String C_TYPE = "C_Type";
    private static final String COMMISSIONGROUP = "COMMISSIONGROUP";
    private static final String SALESGROUP = "SALESGROUP";
    private static final String SUBDEPOT_ID = "SubDepot_id";
    private static final String CUSTCLASSIFICATIONID = "CUSTCLASSIFICATIONID";
    private static final String FLAG = "Flag";
    private static final String RFLAG = "RFlag";
    private static final String ACCOUNTNUM = "ACCOUNTNUM";
    private static final String MANDT = "Mandt";

    public CustomerRouteMappingView(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_NAME+" (" + REC_ID + " NUMERIC NOT NULL, "
                + SUB_COMPANY_ID + " TEXT NULL, " + DEPOT + " TEXT NULL, " + ROUTE_ID + " TEXT NULL, "
                + ROUTE_NAME + " TEXT NULL, " + SALE_DATE_PARAMETER + " TEXT NULL, " + PSMID + " TEXT NULL, "
                + CUSTOMER_ID + " TEXT NULL, " + CUSTOMER_NAME + " TEXT NOT NULL, " + PRICEGROUP + " TEXT NOT NULL, "
                + LINEDISC + " TEXT NOT NULL, " + C_TYPE + " TEXT NOT NULL, " + COMMISSIONGROUP + " TEXT NOT NULL, "
                + SALESGROUP + " TEXT NOT NULL, " + SUBDEPOT_ID + " TEXT NULL, " + CUSTCLASSIFICATIONID + " TEXT NULL, "
                + FLAG + " INTEGER NULL, " + RFLAG + " INTEGER NULL, " + ACCOUNTNUM + " TEXT NOT NULL, "
                + MANDT + " INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // multiple insert
    //insert single
    public boolean insertCustomerRouteMap(List<CustomerRouteMapModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            CustomerRouteMapModel customerRouteMapModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(REC_ID, customerRouteMapModel.getRecId());
            contentValues.put(SUB_COMPANY_ID, customerRouteMapModel.getSubCompanyId());
            contentValues.put(DEPOT, customerRouteMapModel.getDepot());
            contentValues.put(ROUTE_ID, customerRouteMapModel.getRouteId());
            contentValues.put(ROUTE_NAME, customerRouteMapModel.getRouteName());
            contentValues.put(SALE_DATE_PARAMETER, customerRouteMapModel.getSaleDateParameter());
            contentValues.put(PSMID, customerRouteMapModel.getPSMID());
            contentValues.put(CUSTOMER_ID, customerRouteMapModel.getCustomerId());
            contentValues.put(CUSTOMER_NAME, customerRouteMapModel.getCustomerName());
            contentValues.put(PRICEGROUP, customerRouteMapModel.getPRICEGROUP());
            contentValues.put(LINEDISC, customerRouteMapModel.getLINEDISC());
            contentValues.put(C_TYPE, customerRouteMapModel.getCType());
            contentValues.put(COMMISSIONGROUP, customerRouteMapModel.getCOMMISSIONGROUP());
            contentValues.put(SALESGROUP, customerRouteMapModel.getSALESGROUP());
            contentValues.put(SUBDEPOT_ID, customerRouteMapModel.getSubDepotId());
            contentValues.put(CUSTCLASSIFICATIONID, customerRouteMapModel.getCUSTCLASSIFICATIONID());
            contentValues.put(FLAG, customerRouteMapModel.getFlag());
            contentValues.put(RFLAG, customerRouteMapModel.getRFlag());
            contentValues.put(ACCOUNTNUM, customerRouteMapModel.getACCOUNTNUM());
            contentValues.put(MANDT, customerRouteMapModel.getMandt());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    //insert single
    public boolean insertCustomerRouteMap(CustomerRouteMapModel customerRouteMapModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(REC_ID, customerRouteMapModel.getRecId());
        contentValues.put(SUB_COMPANY_ID, customerRouteMapModel.getSubCompanyId());
        contentValues.put(DEPOT, customerRouteMapModel.getDepot());
        contentValues.put(ROUTE_ID, customerRouteMapModel.getRouteId());
        contentValues.put(ROUTE_NAME, customerRouteMapModel.getRouteName());
        contentValues.put(SALE_DATE_PARAMETER, customerRouteMapModel.getSaleDateParameter());
        contentValues.put(PSMID, customerRouteMapModel.getPSMID());
        contentValues.put(CUSTOMER_ID, customerRouteMapModel.getCustomerId());
        contentValues.put(CUSTOMER_NAME, customerRouteMapModel.getCustomerName());
        contentValues.put(PRICEGROUP, customerRouteMapModel.getPRICEGROUP());
        contentValues.put(LINEDISC, customerRouteMapModel.getLINEDISC());
        contentValues.put(C_TYPE, customerRouteMapModel.getCType());
        contentValues.put(COMMISSIONGROUP, customerRouteMapModel.getCOMMISSIONGROUP());
        contentValues.put(SALESGROUP, customerRouteMapModel.getSALESGROUP());
        contentValues.put(SUBDEPOT_ID, customerRouteMapModel.getSubDepotId());
        contentValues.put(CUSTCLASSIFICATIONID, customerRouteMapModel.getCUSTCLASSIFICATIONID());
        contentValues.put(FLAG, customerRouteMapModel.getFlag());
        contentValues.put(RFLAG, customerRouteMapModel.getRFlag());
        contentValues.put(ACCOUNTNUM, customerRouteMapModel.getACCOUNTNUM());
        contentValues.put(MANDT, customerRouteMapModel.getMandt());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public CustomerRouteMapModel getCustomerRouteMapByRoute(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ROUTE_ID + "='" + route_id + "'", null);

        CustomerRouteMapModel customerRouteMapModel = new CustomerRouteMapModel();
        if (res.moveToFirst()) {
            customerRouteMapModel.setRecId(res.getLong(res.getColumnIndex(REC_ID)));
            customerRouteMapModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
            customerRouteMapModel.setDepot(res.getString(res.getColumnIndex(DEPOT)));
            customerRouteMapModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            customerRouteMapModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            customerRouteMapModel.setSaleDateParameter(res.getString(res.getColumnIndex(SALE_DATE_PARAMETER)));
            customerRouteMapModel.setPSMID(res.getString(res.getColumnIndex(PSMID)));
            customerRouteMapModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            customerRouteMapModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            customerRouteMapModel.setPRICEGROUP(res.getString(res.getColumnIndex(PRICEGROUP)));
            customerRouteMapModel.setLINEDISC(res.getString(res.getColumnIndex(LINEDISC)));
            customerRouteMapModel.setCType(res.getString(res.getColumnIndex(C_TYPE)));
            customerRouteMapModel.setCOMMISSIONGROUP(res.getString(res.getColumnIndex(COMMISSIONGROUP)));
            customerRouteMapModel.setSALESGROUP(res.getString(res.getColumnIndex(SALESGROUP)));
            customerRouteMapModel.setSubDepotId(res.getString(res.getColumnIndex(SUBDEPOT_ID)));
            customerRouteMapModel.setCUSTCLASSIFICATIONID(res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID)));
            customerRouteMapModel.setFlag(res.getInt(res.getColumnIndex(FLAG)));
            customerRouteMapModel.setRFlag(res.getInt(res.getColumnIndex(RFLAG)));
            customerRouteMapModel.setACCOUNTNUM(res.getString(res.getColumnIndex(ACCOUNTNUM)));
            customerRouteMapModel.setMandt(res.getInt(res.getColumnIndex(MANDT)));
        }
        res.close();
        db.close();
        return customerRouteMapModel;
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

    public ArrayList<CustomerRouteMapModel> getAllCustomerRouteMap() {
        ArrayList<CustomerRouteMapModel> array_list = new ArrayList<CustomerRouteMapModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                CustomerRouteMapModel customerRouteMapModel = new CustomerRouteMapModel();
                customerRouteMapModel.setRecId(res.getLong(res.getColumnIndex(REC_ID)));
                customerRouteMapModel.setSubCompanyId(res.getString(res.getColumnIndex(SUB_COMPANY_ID)));
                customerRouteMapModel.setDepot(res.getString(res.getColumnIndex(DEPOT)));
                customerRouteMapModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                customerRouteMapModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                customerRouteMapModel.setSaleDateParameter(res.getString(res.getColumnIndex(SALE_DATE_PARAMETER)));
                customerRouteMapModel.setPSMID(res.getString(res.getColumnIndex(PSMID)));
                customerRouteMapModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                customerRouteMapModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                customerRouteMapModel.setPRICEGROUP(res.getString(res.getColumnIndex(PRICEGROUP)));
                customerRouteMapModel.setLINEDISC(res.getString(res.getColumnIndex(LINEDISC)));
                customerRouteMapModel.setCType(res.getString(res.getColumnIndex(C_TYPE)));
                customerRouteMapModel.setCOMMISSIONGROUP(res.getString(res.getColumnIndex(COMMISSIONGROUP)));
                customerRouteMapModel.setSALESGROUP(res.getString(res.getColumnIndex(SALESGROUP)));
                customerRouteMapModel.setSubDepotId(res.getString(res.getColumnIndex(SUBDEPOT_ID)));
                customerRouteMapModel.setCUSTCLASSIFICATIONID(res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID)));
                customerRouteMapModel.setFlag(res.getInt(res.getColumnIndex(FLAG)));
                customerRouteMapModel.setRFlag(res.getInt(res.getColumnIndex(RFLAG)));
                customerRouteMapModel.setACCOUNTNUM(res.getString(res.getColumnIndex(ACCOUNTNUM)));
                customerRouteMapModel.setMandt(res.getInt(res.getColumnIndex(MANDT)));

                array_list.add(customerRouteMapModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }
}