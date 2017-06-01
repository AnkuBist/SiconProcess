package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.invoice.InvoiceModel;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerItemPriceModel;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.InvoiceDetailModel;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.ProductModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class DepotInvoiceView extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "Sicon_depot_invoice";
    private static final String TABLE_NAME = "V_SD_DepotInvoice_Master";

    private static final String ROUTE_MANAGEMENT_DATE = "Route_management_Date";
    private static final String ROUTE_MANAGEMENT_ID = "Route_management_Id";
    private static final String INVOICE_NO = "Invoice_No";
    private static final String INVOICE_DATE = "Invoice_Date";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String ROUTE_ID = "Route_Id";
    private static final String VEHICLE_NO = "Vehicle_No";
    private static final String DRIVER_CODE = "Driver_Code";
    private static final String CASHIER_CODE = "Cashier_Code";
    private static final String ITEM_ID = "Item_id";
    private static final String CRATE_ID = "Crate_id";
    private static final String INVQTY_PS = "InvQty_ps";
    private static final String ITEM_RATE = "Item_Rate";
    private static final String ITEM_DISCOUNT = "Item_Discount";
    private static final String DISCOUNT_AMOUNT = "Discount_Amount";
    private static final String TOTAL_AMOUNT = "Total_Amount";

    private Context mContext;

    public DepotInvoiceView(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ROUTE_MANAGEMENT_DATE + " TEXT NULL, "
                + ROUTE_MANAGEMENT_ID + " TEXT NULL, " + INVOICE_NO + " TEXT NULL, "
                + INVOICE_DATE + " TEXT NULL, " + CUSTOMER_ID + " TEXT NULL, " + ROUTE_ID + " TEXT NULL, "
                + VEHICLE_NO + " TEXT NULL, " + DRIVER_CODE + " TEXT NULL, "
                + CASHIER_CODE + " TEXT NULL, " + ITEM_ID + " TEXT NULL, "
                + CRATE_ID + " TEXT NULL, " + INVQTY_PS + " REAL NULL, "
                + ITEM_RATE + " REAL NULL, " + ITEM_DISCOUNT + " REAL NULL, " + DISCOUNT_AMOUNT + " REAL NULL, "
                + " REAL NULL, " + TOTAL_AMOUNT + " REAL NULL)");
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
    public boolean insertDepotInvoice(List<InvoiceDetailModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            InvoiceDetailModel invoiceDetailModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROUTE_MANAGEMENT_DATE, invoiceDetailModel.getRouteManagemnetDate());
            contentValues.put(ROUTE_MANAGEMENT_ID, invoiceDetailModel.getRouteManagementId());
            contentValues.put(INVOICE_NO, invoiceDetailModel.getInvoiceNo());
            contentValues.put(INVOICE_DATE, invoiceDetailModel.getInvoiceDate());
            contentValues.put(CUSTOMER_ID, invoiceDetailModel.getCustomerId());
            contentValues.put(ROUTE_ID, invoiceDetailModel.getRouteId());
            contentValues.put(VEHICLE_NO, invoiceDetailModel.getVehicleNo());
            contentValues.put(DRIVER_CODE, invoiceDetailModel.getDriverCode());
            contentValues.put(CASHIER_CODE, invoiceDetailModel.getCashierCode());
            contentValues.put(ITEM_ID, invoiceDetailModel.getItemId());
            contentValues.put(CRATE_ID, invoiceDetailModel.getCrateId());
            contentValues.put(INVQTY_PS, invoiceDetailModel.getInvQtyPs());
            contentValues.put(ITEM_RATE, invoiceDetailModel.getItemRate());
            contentValues.put(ITEM_DISCOUNT, invoiceDetailModel.getItemDiscount());
            contentValues.put(DISCOUNT_AMOUNT, invoiceDetailModel.getDiscountAmount());
            contentValues.put(TOTAL_AMOUNT, invoiceDetailModel.getTotalAmount());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    // total van item loading count
    public int itemVanStockLoadingCount(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + INVQTY_PS + ") as loading_qty from " + TABLE_NAME + " where " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int total_item_count = 0;
        if (res.moveToFirst()) {
            total_item_count = res.getInt(res.getColumnIndex("loading_qty"));
        }
        res.close();
        db.close();
        return total_item_count;
    }

    // get van loading count
    public int totalItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + INVQTY_PS + ") as loading_qty from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total_item_count = 0;
        if (res.moveToFirst()) {
            total_item_count = res.getInt(res.getColumnIndex("loading_qty"));
        }
        res.close();
        db.close();
        return total_item_count;
    }

    /*required details by the application side in single pojo*/
    public ArrayList<InvoiceModel> getCustomerInvoice(String customer_id) {
        ArrayList<InvoiceModel> array_list = new ArrayList<InvoiceModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        ProductView dbItemDetails = new ProductView(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRejectionTable rejectionTable = new CustomerRejectionTable(mContext);
        CustomerItemPriceTable dbPriceTable = new CustomerItemPriceTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                InvoiceModel invoiceModel = new InvoiceModel();
                String item_id = (res.getString(res.getColumnIndex(ITEM_ID)));

                invoiceModel.setRouteManagemnetDate(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_DATE)));
                invoiceModel.setRouteManagementId(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
                invoiceModel.setInvoiceNo(res.getString(res.getColumnIndex(INVOICE_NO)));
                invoiceModel.setInvoiceDate(res.getString(res.getColumnIndex(INVOICE_DATE)));
                invoiceModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                invoiceModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                invoiceModel.setVehicleNo(res.getString(res.getColumnIndex(VEHICLE_NO)));
                invoiceModel.setDriverCode(res.getString(res.getColumnIndex(DRIVER_CODE)));
                invoiceModel.setCashierCode(res.getString(res.getColumnIndex(CASHIER_CODE)));
                invoiceModel.setItemId(item_id);
                invoiceModel.setCrateId(res.getString(res.getColumnIndex(CRATE_ID)));
                invoiceModel.setItemDiscount(res.getFloat(res.getColumnIndex(ITEM_DISCOUNT)));
                invoiceModel.setDiscountAmount(res.getFloat(res.getColumnIndex(DISCOUNT_AMOUNT)));
                invoiceModel.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));

                CustomerItemPriceModel itemPriceModel = dbPriceTable.custItemPriceNSample(item_id, customer_id);
                invoiceModel.setItemRate((float) (itemPriceModel.getDiscountedPrice()));

                //---------------if invoice exists-------------------//
                float custOrderQty = 0;

                // get item sample
                invoiceModel.setFixedSample(itemPriceModel.getSample_qty());

                // subtract ordered quantity from stock and also the sample collected
                int stock = itemVanStockLoadingCount(item_id)
                        - invoiceOutTable.totalItemOrderQtyOTSelf(customer_id, item_id)
                        - dbPriceTable.itemTotalSampleCount(item_id)
                        - rejectionTable.freshRejOtherThenCust(customer_id, item_id);

                invoiceModel.setStockAvail(stock);

                //first check whether invoice out table contains the user or not
                if (invoiceOutTable.checkUser(customer_id)) {
                    // get item order if invoice made lastly
                    custOrderQty = invoiceOutTable.customerInvOutItemQty(customer_id, item_id);
                } else {
                    custOrderQty = res.getFloat(res.getColumnIndex(INVQTY_PS));
                    if (stock < custOrderQty)
                        custOrderQty = 0;
                }

                invoiceModel.setTempStock(stock - (int) custOrderQty);
                invoiceModel.setInvQtyPs(custOrderQty);
                //invoiceModel.setDemandTargetQty(orderQty);
                invoiceModel.setOrderAmount(Utility.roundTwoDecimals(custOrderQty * invoiceModel.getItemRate()));

                if (dbItemDetails.checkProduct(invoiceModel.getItemId()) && stock > 0) {
                    ProductModel productInfo = dbItemDetails.getProductById(item_id);
                    invoiceModel.setItemName(productInfo.getItemName());
                    invoiceModel.setItemSequence(productInfo.getITEMSEQUENCE());
                    array_list.add(invoiceModel);
                }
                res.moveToNext();
            }
        }
        res.close();
        db.close();

        ArrayList<InvoiceModel> sortedArrayList = new ArrayList<>(array_list);
        Collections.sort(sortedArrayList, new Comparator<InvoiceModel>() {
            public int compare(InvoiceModel p1, InvoiceModel p2) {
                return Integer.valueOf(p1.getItemSequence()).compareTo(p2.getItemSequence());
            }
        });
        return sortedArrayList;
    }

    // this function is to display the invoice is there is not invoice exists for the customer.
    // this will calculate the available items in stock and display to the user.
    public ArrayList<InvoiceModel> getCustomerInvoiceOff(String customer_id) {
        ArrayList<InvoiceModel> array_list = new ArrayList<InvoiceModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        ProductView dbItemDetails = new ProductView(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRejectionTable rejectionTable = new CustomerRejectionTable(mContext);
        CustomerItemPriceTable dbPriceTable = new CustomerItemPriceTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=''", null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                InvoiceModel invoiceModel = new InvoiceModel();
                String item_id = (res.getString(res.getColumnIndex(ITEM_ID)));

                invoiceModel.setRouteManagemnetDate(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_DATE)));
                invoiceModel.setRouteManagementId(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
                invoiceModel.setInvoiceNo(res.getString(res.getColumnIndex(INVOICE_NO)));
                invoiceModel.setInvoiceDate(res.getString(res.getColumnIndex(INVOICE_DATE)));
                invoiceModel.setCustomerId(customer_id);
                invoiceModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                invoiceModel.setVehicleNo(res.getString(res.getColumnIndex(VEHICLE_NO)));
                invoiceModel.setDriverCode(res.getString(res.getColumnIndex(DRIVER_CODE)));
                invoiceModel.setCashierCode(res.getString(res.getColumnIndex(CASHIER_CODE)));
                invoiceModel.setItemId(item_id);
                invoiceModel.setCrateId(res.getString(res.getColumnIndex(CRATE_ID)));
                invoiceModel.setItemDiscount(res.getFloat(res.getColumnIndex(ITEM_DISCOUNT)));
                invoiceModel.setDiscountAmount(res.getFloat(res.getColumnIndex(DISCOUNT_AMOUNT)));
                invoiceModel.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));

                CustomerItemPriceModel itemPriceModel = dbPriceTable.custItemPriceNSample(item_id, customer_id);
                invoiceModel.setItemRate((float) (itemPriceModel.getDiscountedPrice()));

                //---------------if invoice exists-------------------//
                float custOrderQty = 0;

                // get item sample
                invoiceModel.setFixedSample(itemPriceModel.getSample_qty());

                // subtract ordered quantity from stock and also the sample collected
                int stock = itemVanStockLoadingCount(item_id)
                        - invoiceOutTable.totalItemOrderQtyOTSelf(customer_id, item_id)
                        - dbPriceTable.itemTotalSampleCount(item_id)
                        - rejectionTable.freshRejOtherThenCust(customer_id, item_id);

                invoiceModel.setStockAvail(stock);

                //first check whether invoice out table contains the user or not
                if (invoiceOutTable.checkUser(customer_id)) {
                    // get item order if invoice made lastly
                    custOrderQty = invoiceOutTable.customerInvOutItemQty(customer_id, item_id);
                } else {
                    //custOrderQty = res.getFloat(res.getColumnIndex(INVQTY_PS));
                    custOrderQty = 0;
                    if (stock < custOrderQty)
                        custOrderQty = 0;
                }

                invoiceModel.setTempStock(stock - (int) custOrderQty);
                invoiceModel.setInvQtyPs(custOrderQty);
                //invoiceModel.setDemandTargetQty(custOrderQty);
                invoiceModel.setOrderAmount(Utility.roundTwoDecimals(custOrderQty * invoiceModel.getItemRate()));

                if (dbItemDetails.checkProduct(invoiceModel.getItemId()) && stock > 0) {
                    ProductModel productInfo = dbItemDetails.getProductById(item_id);
                    invoiceModel.setItemName(productInfo.getItemName());
                    invoiceModel.setItemSequence(productInfo.getITEMSEQUENCE());
                    array_list.add(invoiceModel);
                }
                res.moveToNext();
            }
        }
        res.close();
        db.close();

        ArrayList<InvoiceModel> sortedArrayList = new ArrayList<>(array_list);
        Collections.sort(sortedArrayList, new Comparator<InvoiceModel>() {
            public int compare(InvoiceModel p1, InvoiceModel p2) {
                return Integer.valueOf(p1.getItemSequence()).compareTo(p2.getItemSequence());
            }
        });
        return sortedArrayList;
    }

    /*get invoice number for customer if exists*/
    public String customerInvoiceNumber(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + INVOICE_NO + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        String invoice_number = "";
        if (res.moveToFirst()) {
            invoice_number = res.getString(res.getColumnIndex(INVOICE_NO));
        }
        res.close();
        db.close();
        return invoice_number;
    }

    //fetch invoice number of any invoice if there is any without customer_id
    public String commonInvoiceNumber() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + INVOICE_NO + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=''";
        Cursor res = db.rawQuery(query, null);

        String invoice_number = "";
        if (res.moveToFirst()) {
            invoice_number = res.getString(res.getColumnIndex(INVOICE_NO));
        }
        res.close();
        db.close();
        return invoice_number;
    }

    /*get cusotmer route management id*/
     /*if customer invoice exists*/
    public String customerRouteManagementId(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + ROUTE_MANAGEMENT_ID + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        String route_management_id = "";
        if (res.moveToFirst()) {
            route_management_id = res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID));
        }
        res.close();
        db.close();
        return route_management_id;
    }

    //fetch route management idany invoice if there is any without customer_id
    public String commonRouteManagementId() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + ROUTE_MANAGEMENT_ID + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=''";
        Cursor res = db.rawQuery(query, null);

        String route_management_id = "";
        if (res.moveToFirst()) {
            route_management_id = res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID));
        }
        res.close();
        db.close();
        return route_management_id;
    }

    // route cashier code
    public String getRouteCashierCode() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + CASHIER_CODE + " from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        String cashier_code = "";
        if (res.moveToFirst()) {
            cashier_code = res.getString(res.getColumnIndex(CASHIER_CODE));
        }
        res.close();
        db.close();
        return cashier_code;
    }
}