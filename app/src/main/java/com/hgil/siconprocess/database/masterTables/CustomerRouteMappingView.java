package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.routeMap.RouteCustomerModel;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerRouteMapModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class CustomerRouteMappingView extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_route_map";
    private static final String TABLE_NAME = "V_SD_Customer_Route_Mapping";

    private static final String ROUTE_ID = "Route_id";
    private static final String ROUTE_NAME = "Route_Name";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String CUSTOMER_NAME = "Customer_Name";
    private static final String PRICEGROUP = "PRICEGROUP";
    private static final String LINEDISC = "LINEDISC";
    private static final String C_TYPE = "C_Type";
    private static final String CUSTCLASSIFICATIONID = "CUSTCLASSIFICATIONID";

    // customer status
    private static final String CUST_STATUS = "Customer_status";

    private Context mContext;

    public CustomerRouteMappingView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ROUTE_ID + " TEXT NULL, "
                + ROUTE_NAME + " TEXT NULL, "
                + CUSTOMER_ID + " TEXT NULL, " + CUSTOMER_NAME + " TEXT NOT NULL, " + PRICEGROUP + " TEXT NOT NULL, "
                + LINEDISC + " TEXT NOT NULL, " + C_TYPE + " TEXT NOT NULL, " + CUSTCLASSIFICATIONID + " TEXT NULL, "
                + CUST_STATUS + " TEXT NULL)");
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

    //update route customer status
    public void updateCustomerStatus(String customer_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CUST_STATUS, status);
        db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    // multiple insert
    //insert single
    public boolean insertCustomerRouteMap(List<CustomerRouteMapModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            CustomerRouteMapModel customerRouteMapModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROUTE_ID, customerRouteMapModel.getRouteId());
            contentValues.put(ROUTE_NAME, customerRouteMapModel.getRouteName());
            contentValues.put(CUSTOMER_ID, customerRouteMapModel.getCustomerId());
            contentValues.put(CUSTOMER_NAME, customerRouteMapModel.getCustomerName());
            contentValues.put(PRICEGROUP, customerRouteMapModel.getPRICEGROUP());
            contentValues.put(LINEDISC, customerRouteMapModel.getLINEDISC());
            contentValues.put(C_TYPE, customerRouteMapModel.getCType());
            contentValues.put(CUSTCLASSIFICATIONID, customerRouteMapModel.getCUSTCLASSIFICATIONID());
            contentValues.put(CUST_STATUS, customerRouteMapModel.getCustStatus());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    //insert single
    public boolean insertCustomerRouteMap(CustomerRouteMapModel customerRouteMapModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ROUTE_ID, customerRouteMapModel.getRouteId());
        contentValues.put(ROUTE_NAME, customerRouteMapModel.getRouteName());
        contentValues.put(CUSTOMER_ID, customerRouteMapModel.getCustomerId());
        contentValues.put(CUSTOMER_NAME, customerRouteMapModel.getCustomerName());
        contentValues.put(PRICEGROUP, customerRouteMapModel.getPRICEGROUP());
        contentValues.put(LINEDISC, customerRouteMapModel.getLINEDISC());
        contentValues.put(C_TYPE, customerRouteMapModel.getCType());
        contentValues.put(CUSTCLASSIFICATIONID, customerRouteMapModel.getCUSTCLASSIFICATIONID());
        contentValues.put(CUST_STATUS, customerRouteMapModel.getCustStatus());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public CustomerRouteMapModel getCustomerRouteMapByCustomer(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "='" + customer_id + "'", null);

        CustomerRouteMapModel customerRouteMapModel = new CustomerRouteMapModel();
        if (res.moveToFirst()) {
            customerRouteMapModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            customerRouteMapModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
            customerRouteMapModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            customerRouteMapModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            customerRouteMapModel.setPRICEGROUP(res.getString(res.getColumnIndex(PRICEGROUP)));
            customerRouteMapModel.setLINEDISC(res.getString(res.getColumnIndex(LINEDISC)));
            customerRouteMapModel.setCType(res.getString(res.getColumnIndex(C_TYPE)));
            customerRouteMapModel.setCUSTCLASSIFICATIONID(res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID)));
            customerRouteMapModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
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
                customerRouteMapModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                customerRouteMapModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                customerRouteMapModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                customerRouteMapModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                customerRouteMapModel.setPRICEGROUP(res.getString(res.getColumnIndex(PRICEGROUP)));
                customerRouteMapModel.setLINEDISC(res.getString(res.getColumnIndex(LINEDISC)));
                customerRouteMapModel.setCType(res.getString(res.getColumnIndex(C_TYPE)));
                customerRouteMapModel.setCUSTCLASSIFICATIONID(res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID)));
                customerRouteMapModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));

                array_list.add(customerRouteMapModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    public ArrayList<RouteCustomerModel> getRouteCustomers() {
        ArrayList<RouteCustomerModel> array_list = new ArrayList<RouteCustomerModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteCustomerModel routeCustomerModel = new RouteCustomerModel();
                routeCustomerModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                routeCustomerModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
                routeCustomerModel.setSaleAmount(invoiceOutTable.custInvoiceTotalAmount(routeCustomerModel.getCustomerId()));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*route pending customers list*/
    public ArrayList<RouteCustomerModel> getRoutePendingCustomers() {
        ArrayList<RouteCustomerModel> array_list = new ArrayList<RouteCustomerModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUST_STATUS + "=?", new String[]{"PENDING"});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteCustomerModel routeCustomerModel = new RouteCustomerModel();
                routeCustomerModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                routeCustomerModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
                routeCustomerModel.setSaleAmount(invoiceOutTable.custInvoiceTotalAmount(routeCustomerModel.getCustomerId()));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*route completed customers list*/
    public ArrayList<RouteCustomerModel> getRouteCompletedCustomers() {
        ArrayList<RouteCustomerModel> array_list = new ArrayList<RouteCustomerModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUST_STATUS + "=?", new String[]{"COMPLETED"});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RouteCustomerModel routeCustomerModel = new RouteCustomerModel();
                routeCustomerModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                routeCustomerModel.setRouteName(res.getString(res.getColumnIndex(ROUTE_NAME)));
                routeCustomerModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                routeCustomerModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                routeCustomerModel.setCustStatus(res.getString(res.getColumnIndex(CUST_STATUS)));
                routeCustomerModel.setSaleAmount(invoiceOutTable.custInvoiceTotalAmount(routeCustomerModel.getCustomerId()));

                array_list.add(routeCustomerModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*get customer classification id*/
    public String getCustomerClassification(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + CUSTCLASSIFICATIONID + " FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});

        String classification_id = "";
        if (res.moveToFirst()) {
            classification_id = res.getString(res.getColumnIndex(CUSTCLASSIFICATIONID));
        }
        res.close();
        db.close();
        return classification_id;
    }

}