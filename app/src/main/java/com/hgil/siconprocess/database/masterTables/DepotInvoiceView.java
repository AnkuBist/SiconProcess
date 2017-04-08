package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.invoice.InvoiceModel;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.InvoiceDetailModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 24-01-2017.
 */

public class DepotInvoiceView extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Sicon_depot_invoice";
    private static final String TABLE_NAME = "V_SD_DepotInvoice_Master";

    private static final String MKEY = "Mkey";
    private static final String RKEY = "RKey";
    private static final String ROUTE_MANAGEMENT_DATE = "Route_management_Date";
    private static final String INVOICE_NO = "Invoice_No";
    private static final String INVOICE_DATE = "Invoice_Date";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String ROUTE_ID = "Route_Id";
    private static final String VEHICLE_NO = "Vehicle_No";
    private static final String DRIVER_CODE = "Driver_Code";
    private static final String CASHIER_CODE = "Cashier_Code";
    private static final String SUPERVISOR_PAYCODE = "Supervisor_Paycode";
    private static final String ITEM_ID = "Item_id";
    private static final String CRATE_ID = "Crate_id";
    private static final String INVQTY_CR = "InvQty_Cr";
    private static final String INVQTY_PS = "InvQty_ps";
    private static final String GROUP_ID = "Group_id";
    private static final String GROUP_PRICE_DATE = "Group_Price_Date";
    private static final String ITEM_RATE = "Item_Rate";
    private static final String ITEM_DISCOUNT = "Item_Discount";
    private static final String ITEM_CST = "Item_CST";
    private static final String ITEM_VAT = "Item_VAT";
    private static final String ITEM_SURCHARGE = "Item_Surcharge";
    private static final String DISCOUNT_AMOUNT = "Discount_Amount";
    private static final String CST_AMOUNT = "CST_Amount";
    private static final String VAT_AMOUNT = "VAT_Amount";
    private static final String SURCHARGE_AMOUNT = "Surcharge_Amount";
    private static final String TOTAL_AMOUNT = "Total_Amount";

    private Context mContext;

    public DepotInvoiceView(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + MKEY + " TEXT NULL, "
                + RKEY + " TEXT NULL, " + ROUTE_MANAGEMENT_DATE + " TEXT NULL, " + INVOICE_NO + " TEXT NULL, "
                + INVOICE_DATE + " TEXT NULL, " + CUSTOMER_ID + " TEXT NULL, " + ROUTE_ID + " TEXT NULL, "
                + VEHICLE_NO + " TEXT NULL, " + DRIVER_CODE + " TEXT NULL, "
                + CASHIER_CODE + " TEXT NULL, " + SUPERVISOR_PAYCODE + " TEXT NULL, " + ITEM_ID + " TEXT NULL, "
                + CRATE_ID + " TEXT NULL, " + INVQTY_CR + " REAL NULL, " + INVQTY_PS + " REAL NULL, "
                + GROUP_ID + " TEXT NULL, " + GROUP_PRICE_DATE + " TEXT NULL, " + ITEM_RATE + " REAL NULL, "
                + ITEM_DISCOUNT + " REAL NULL, " + ITEM_CST + " REAL NULL, " + ITEM_VAT + " REAL NULL, "
                + ITEM_SURCHARGE + " REAL NULL, " + DISCOUNT_AMOUNT + " REAL NULL, " + CST_AMOUNT + " REAL NULL, "
                + VAT_AMOUNT + " REAL NULL, " + SURCHARGE_AMOUNT + " REAL NULL, " + TOTAL_AMOUNT + " REAL NULL)");
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

    //insert single
    public boolean insertDepotInvoice(InvoiceDetailModel invoiceDetailModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MKEY, invoiceDetailModel.getMkey());
        contentValues.put(RKEY, invoiceDetailModel.getRKey());
        contentValues.put(ROUTE_MANAGEMENT_DATE, invoiceDetailModel.getRouteManagemnetDate());
        contentValues.put(INVOICE_NO, invoiceDetailModel.getInvoiceNo());
        contentValues.put(INVOICE_DATE, invoiceDetailModel.getInvoiceDate());
        contentValues.put(CUSTOMER_ID, invoiceDetailModel.getCustomerId());
        contentValues.put(ROUTE_ID, invoiceDetailModel.getRouteId());
        contentValues.put(VEHICLE_NO, invoiceDetailModel.getVehicleNo());
        //contentValues.put(DRIVER_CODE, invoiceDetailModel.getDriverCode());
        contentValues.put(CASHIER_CODE, invoiceDetailModel.getCashierCode());
        contentValues.put(SUPERVISOR_PAYCODE, invoiceDetailModel.getSupervisorPaycode());
        contentValues.put(ITEM_ID, invoiceDetailModel.getItemId());
        contentValues.put(CRATE_ID, invoiceDetailModel.getCrateId());
        contentValues.put(INVQTY_CR, invoiceDetailModel.getInvQtyCr());
        contentValues.put(INVQTY_PS, invoiceDetailModel.getInvQtyPs());
        contentValues.put(GROUP_ID, invoiceDetailModel.getGroupId());
        contentValues.put(GROUP_PRICE_DATE, invoiceDetailModel.getGroupPriceDate());
        contentValues.put(ITEM_RATE, invoiceDetailModel.getItemRate());
        contentValues.put(ITEM_DISCOUNT, invoiceDetailModel.getItemDiscount());
        contentValues.put(ITEM_CST, invoiceDetailModel.getItemCST());
        contentValues.put(ITEM_VAT, invoiceDetailModel.getItemVAT());
        contentValues.put(ITEM_SURCHARGE, invoiceDetailModel.getItemSurcharge());
        contentValues.put(DISCOUNT_AMOUNT, invoiceDetailModel.getDiscountAmount());
        contentValues.put(CST_AMOUNT, invoiceDetailModel.getCSTAmount());
        contentValues.put(VAT_AMOUNT, invoiceDetailModel.getVATAmount());
        contentValues.put(SURCHARGE_AMOUNT, invoiceDetailModel.getSurchargeAmount());
        contentValues.put(TOTAL_AMOUNT, invoiceDetailModel.getTotalAmount());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    // insert multiple
    public boolean insertDepotInvoice(List<InvoiceDetailModel> arrList) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < arrList.size(); i++) {
            InvoiceDetailModel invoiceDetailModel = arrList.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MKEY, invoiceDetailModel.getMkey());
            contentValues.put(RKEY, invoiceDetailModel.getRKey());
            contentValues.put(ROUTE_MANAGEMENT_DATE, invoiceDetailModel.getRouteManagemnetDate());
            contentValues.put(INVOICE_NO, invoiceDetailModel.getInvoiceNo());
            contentValues.put(INVOICE_DATE, invoiceDetailModel.getInvoiceDate());
            contentValues.put(CUSTOMER_ID, invoiceDetailModel.getCustomerId());
            contentValues.put(ROUTE_ID, invoiceDetailModel.getRouteId());
            contentValues.put(VEHICLE_NO, invoiceDetailModel.getVehicleNo());
            //contentValues.put(DRIVER_CODE, invoiceDetailModel.getDriverCode());
            contentValues.put(CASHIER_CODE, invoiceDetailModel.getCashierCode());
            contentValues.put(SUPERVISOR_PAYCODE, invoiceDetailModel.getSupervisorPaycode());
            contentValues.put(ITEM_ID, invoiceDetailModel.getItemId());
            contentValues.put(CRATE_ID, invoiceDetailModel.getCrateId());
            contentValues.put(INVQTY_CR, invoiceDetailModel.getInvQtyCr());
            contentValues.put(INVQTY_PS, invoiceDetailModel.getInvQtyPs());
            contentValues.put(GROUP_ID, invoiceDetailModel.getGroupId());
            contentValues.put(GROUP_PRICE_DATE, invoiceDetailModel.getGroupPriceDate());
            contentValues.put(ITEM_RATE, invoiceDetailModel.getItemRate());
            contentValues.put(ITEM_DISCOUNT, invoiceDetailModel.getItemDiscount());
            contentValues.put(ITEM_CST, invoiceDetailModel.getItemCST());
            contentValues.put(ITEM_VAT, invoiceDetailModel.getItemVAT());
            contentValues.put(ITEM_SURCHARGE, invoiceDetailModel.getItemSurcharge());
            contentValues.put(DISCOUNT_AMOUNT, invoiceDetailModel.getDiscountAmount());
            contentValues.put(CST_AMOUNT, invoiceDetailModel.getCSTAmount());
            contentValues.put(VAT_AMOUNT, invoiceDetailModel.getVATAmount());
            contentValues.put(SURCHARGE_AMOUNT, invoiceDetailModel.getSurchargeAmount());
            contentValues.put(TOTAL_AMOUNT, invoiceDetailModel.getTotalAmount());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public InvoiceDetailModel getDepotInvoiceByRoute(String route_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + INVOICE_DATE + "='" + route_id + "'", null);

        InvoiceDetailModel invoiceDetailModel = new InvoiceDetailModel();
        if (res.moveToFirst()) {
            invoiceDetailModel.setMkey(res.getString(res.getColumnIndex(MKEY)));
            invoiceDetailModel.setRKey(res.getString(res.getColumnIndex(RKEY)));
            invoiceDetailModel.setRouteManagemnetDate(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_DATE)));
            invoiceDetailModel.setInvoiceNo(res.getString(res.getColumnIndex(INVOICE_NO)));
            invoiceDetailModel.setInvoiceDate(res.getString(res.getColumnIndex(INVOICE_DATE)));
            invoiceDetailModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            invoiceDetailModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            invoiceDetailModel.setVehicleNo(res.getString(res.getColumnIndex(VEHICLE_NO)));
            //invoiceDetailModel.setDriverCode(res.getString(res.getColumnIndex(DRIVER_CODE)));
            invoiceDetailModel.setCashierCode(res.getString(res.getColumnIndex(CASHIER_CODE)));
            invoiceDetailModel.setSupervisorPaycode(res.getString(res.getColumnIndex(SUPERVISOR_PAYCODE)));
            invoiceDetailModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            invoiceDetailModel.setCrateId(res.getString(res.getColumnIndex(CRATE_ID)));
            invoiceDetailModel.setInvQtyCr(res.getFloat(res.getColumnIndex(INVQTY_CR)));
            invoiceDetailModel.setInvQtyPs(res.getFloat(res.getColumnIndex(INVQTY_PS)));
            invoiceDetailModel.setGroupId(res.getString(res.getColumnIndex(GROUP_ID)));
            invoiceDetailModel.setGroupPriceDate(res.getString(res.getColumnIndex(GROUP_PRICE_DATE)));
            invoiceDetailModel.setItemRate(res.getFloat(res.getColumnIndex(ITEM_RATE)));
            invoiceDetailModel.setItemDiscount(res.getFloat(res.getColumnIndex(ITEM_DISCOUNT)));
            invoiceDetailModel.setItemCST(res.getFloat(res.getColumnIndex(ITEM_CST)));
            invoiceDetailModel.setItemVAT(res.getFloat(res.getColumnIndex(ITEM_VAT)));
            invoiceDetailModel.setItemSurcharge(res.getFloat(res.getColumnIndex(ITEM_SURCHARGE)));
            invoiceDetailModel.setDiscountAmount(res.getFloat(res.getColumnIndex(DISCOUNT_AMOUNT)));
            invoiceDetailModel.setCSTAmount(res.getFloat(res.getColumnIndex(CST_AMOUNT)));
            invoiceDetailModel.setVATAmount(res.getFloat(res.getColumnIndex(VAT_AMOUNT)));
            invoiceDetailModel.setSurchargeAmount(res.getFloat(res.getColumnIndex(SURCHARGE_AMOUNT)));
            invoiceDetailModel.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));
        }
        res.close();
        db.close();
        return invoiceDetailModel;
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

    public ArrayList<InvoiceDetailModel> getAllCustomerInvoice(String customer_id) {
        ArrayList<InvoiceDetailModel> array_list = new ArrayList<InvoiceDetailModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "='" + customer_id + "'", null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                InvoiceDetailModel invoiceDetailModel = new InvoiceDetailModel();
                invoiceDetailModel.setMkey(res.getString(res.getColumnIndex(MKEY)));
                invoiceDetailModel.setRKey(res.getString(res.getColumnIndex(RKEY)));
                invoiceDetailModel.setRouteManagemnetDate(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_DATE)));
                invoiceDetailModel.setInvoiceNo(res.getString(res.getColumnIndex(INVOICE_NO)));
                invoiceDetailModel.setInvoiceDate(res.getString(res.getColumnIndex(INVOICE_DATE)));
                invoiceDetailModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                invoiceDetailModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                invoiceDetailModel.setVehicleNo(res.getString(res.getColumnIndex(VEHICLE_NO)));
                //invoiceDetailModel.setDriverCode(res.getString(res.getColumnIndex(DRIVER_CODE)));
                invoiceDetailModel.setCashierCode(res.getString(res.getColumnIndex(CASHIER_CODE)));
                invoiceDetailModel.setSupervisorPaycode(res.getString(res.getColumnIndex(SUPERVISOR_PAYCODE)));
                invoiceDetailModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                invoiceDetailModel.setCrateId(res.getString(res.getColumnIndex(CRATE_ID)));
                invoiceDetailModel.setInvQtyCr(res.getFloat(res.getColumnIndex(INVQTY_CR)));
                invoiceDetailModel.setInvQtyPs(res.getFloat(res.getColumnIndex(INVQTY_PS)));
                invoiceDetailModel.setGroupId(res.getString(res.getColumnIndex(GROUP_ID)));
                invoiceDetailModel.setGroupPriceDate(res.getString(res.getColumnIndex(GROUP_PRICE_DATE)));
                invoiceDetailModel.setItemRate(res.getFloat(res.getColumnIndex(ITEM_RATE)));
                invoiceDetailModel.setItemDiscount(res.getFloat(res.getColumnIndex(ITEM_DISCOUNT)));
                invoiceDetailModel.setItemCST(res.getFloat(res.getColumnIndex(ITEM_CST)));
                invoiceDetailModel.setItemVAT(res.getFloat(res.getColumnIndex(ITEM_VAT)));
                invoiceDetailModel.setItemSurcharge(res.getFloat(res.getColumnIndex(ITEM_SURCHARGE)));
                invoiceDetailModel.setDiscountAmount(res.getFloat(res.getColumnIndex(DISCOUNT_AMOUNT)));
                invoiceDetailModel.setCSTAmount(res.getFloat(res.getColumnIndex(CST_AMOUNT)));
                invoiceDetailModel.setVATAmount(res.getFloat(res.getColumnIndex(VAT_AMOUNT)));
                invoiceDetailModel.setSurchargeAmount(res.getFloat(res.getColumnIndex(SURCHARGE_AMOUNT)));
                invoiceDetailModel.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));

                if (new ProductView(mContext).checkProduct(invoiceDetailModel.getItemId()))
                    array_list.add(invoiceDetailModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }


    /*required details by the application side in single pojo*/
    public ArrayList<InvoiceModel> getCustomerInvoice(String customer_id) {
        ArrayList<InvoiceModel> array_list = new ArrayList<InvoiceModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        ProductView dbItemDetails = new ProductView(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        DemandTargetTable dbDemandTarget = new DemandTargetTable(mContext);
        FixedSampleTable dbSample = new FixedSampleTable(mContext);
        CustomerItemPriceTable dbPriceTable = new CustomerItemPriceTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "='" + customer_id + "'", null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                InvoiceModel invoiceModel = new InvoiceModel();
                invoiceModel.setMkey(res.getString(res.getColumnIndex(MKEY)));
                invoiceModel.setRKey(res.getString(res.getColumnIndex(RKEY)));
                invoiceModel.setRouteManagemnetDate(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_DATE)));
                invoiceModel.setInvoiceNo(res.getString(res.getColumnIndex(INVOICE_NO)));
                invoiceModel.setInvoiceDate(res.getString(res.getColumnIndex(INVOICE_DATE)));
                invoiceModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                invoiceModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
                invoiceModel.setVehicleNo(res.getString(res.getColumnIndex(VEHICLE_NO)));
                //invoiceModel.setDriverCode(res.getString(res.getColumnIndex(DRIVER_CODE)));
                invoiceModel.setCashierCode(res.getString(res.getColumnIndex(CASHIER_CODE)));
                invoiceModel.setSupervisorPaycode(res.getString(res.getColumnIndex(SUPERVISOR_PAYCODE)));
                invoiceModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                invoiceModel.setCrateId(res.getString(res.getColumnIndex(CRATE_ID)));
                invoiceModel.setInvQtyCr(res.getFloat(res.getColumnIndex(INVQTY_CR)));
                invoiceModel.setInvQtyPs(res.getFloat(res.getColumnIndex(INVQTY_PS)));
                invoiceModel.setGroupId(res.getString(res.getColumnIndex(GROUP_ID)));
                invoiceModel.setGroupPriceDate(res.getString(res.getColumnIndex(GROUP_PRICE_DATE)));
                // reset item price/rate with actual price
                //invoiceModel.setItemRate(res.getFloat(res.getColumnIndex(ITEM_RATE)));
                invoiceModel.setItemDiscount(res.getFloat(res.getColumnIndex(ITEM_DISCOUNT)));
                invoiceModel.setItemCST(res.getFloat(res.getColumnIndex(ITEM_CST)));
                invoiceModel.setItemVAT(res.getFloat(res.getColumnIndex(ITEM_VAT)));
                invoiceModel.setItemSurcharge(res.getFloat(res.getColumnIndex(ITEM_SURCHARGE)));
                invoiceModel.setDiscountAmount(res.getFloat(res.getColumnIndex(DISCOUNT_AMOUNT)));
                invoiceModel.setCSTAmount(res.getFloat(res.getColumnIndex(CST_AMOUNT)));
                invoiceModel.setVATAmount(res.getFloat(res.getColumnIndex(VAT_AMOUNT)));
                invoiceModel.setSurchargeAmount(res.getFloat(res.getColumnIndex(SURCHARGE_AMOUNT)));
                invoiceModel.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));

                String item_id = invoiceModel.getItemId();
                invoiceModel.setItemRate((float) (dbPriceTable.getItemPriceById(item_id, customer_id)));

                //---------------if invoice exists-------------------//

                float orderQty = 0;

                //first check whether invoice out table contains the user or not
                if (invoiceOutTable.checkUser(customer_id)) {
                    // get item demand if invoice made lastly
                    orderQty = invoiceOutTable.getDemandQuantity(customer_id, item_id);
                } else {
                    // get invoice item target
                    orderQty = dbDemandTarget.getDemandTargetByItem(item_id, customer_id).getTargetQty();
                }
                invoiceModel.setDemandTargetQty(orderQty);
                invoiceModel.setOrderAmount(Utility.roundTwoDecimals(orderQty * invoiceModel.getItemRate()));

                // get item sample
                invoiceModel.setFixedSample(dbSample.getFixedSampleItem(item_id, customer_id).getSQty());
                int total_sample_stock = dbSample.getSampleCount(item_id);

                // exactly have to calculate the actual orders placed for the same in out invoice
                int totalDemand = invoiceOutTable.getItemOrderQty(item_id);

                // subtract ordered quantity from stock and also the sample collected
                int stock = getLoadingCount(item_id) - totalDemand - total_sample_stock;

                invoiceModel.setStockAvail(stock);
                invoiceModel.setTempStock(stock);

                if (dbItemDetails.checkProduct(invoiceModel.getItemId()) && stock > 0) {
                    invoiceModel.setItemName(dbItemDetails.getProductById(item_id).getItemName());
                    array_list.add(invoiceModel);
                }
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // get van loading count
    public int getLoadingCount(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        /*String query = "select distinct " + ITEM_ID + ", " + ITEM_RATE + ", sum(" + INVQTY_PS + ") over (partition by " + ITEM_ID + ") as loading_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";*/
        String query = "select distinct " + ITEM_ID + ", sum(" + INVQTY_PS + ") as loading_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=? group by " + ITEM_ID;
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int total_item_count = 0;
        if (res.moveToFirst()) {
            total_item_count = res.getInt(res.getColumnIndex("loading_qty"));
        }
        res.close();
        db.close();
        return total_item_count;
    }

    // total van item loading count
    // get van loading count
    public int totalItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        /*String query = "select distinct " + ITEM_ID + ", " + ITEM_RATE + ", sum(" + INVQTY_PS + ") over (partition by " + ITEM_ID + ") as loading_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";*/
        String query = "select sum(" + INVQTY_PS + ") as loading_qty " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total_item_count = 0;
        if (res.moveToFirst()) {
            total_item_count = res.getInt(res.getColumnIndex("loading_qty"));
        }
        res.close();
        db.close();
        return total_item_count;
    }

    // this function is to display the invoice is there is not invoice exists for the customer.
    // this will calculate the available items in stock and display to the user.
    public ArrayList<InvoiceModel> getCustomerInvoiceOff(String customer_id) {
        ArrayList<InvoiceModel> array_list = new ArrayList<InvoiceModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        FixedSampleTable dbSample = new FixedSampleTable(mContext);

        /*select sum(InvQty_ps) as loading_qty, Item_id
from V_SD_DepotInvoice_Master where Route_management_Date='2017-01-30' and Route_id='R/mom/0041' group by Item_id*/

        Cursor res = db.rawQuery("SELECT sum(" + INVQTY_PS + ") as loading_qty, " + ITEM_ID + " FROM " + TABLE_NAME + " GROUP BY " + ITEM_ID, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                int stock = res.getInt(res.getColumnIndex("loading_qty"));
                if (stock > 0) {
                    InvoiceModel invoiceModel = getDepotInvoiceByItemId(customer_id, item_id);
                    if (invoiceModel != null) {
                        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
                        int totalDemand = invoiceOutTable.getItemOrderQty(item_id);

                        int total_sample_stock = dbSample.getSampleCount(item_id);

                        stock += (-total_sample_stock - totalDemand);

                        invoiceModel.setStockAvail(stock);
                        invoiceModel.setTempStock(stock);
                        invoiceModel.setCustomerId(customer_id);
                        array_list.add(invoiceModel);
                    }
                }
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    /*get invoice number for customer if exists*/
    //fetch invoice number of any invoice if there is any without customer_id
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


    public InvoiceModel getDepotInvoiceByItemId(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ProductView dbItemDetails = new ProductView(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        DemandTargetTable dbDemandTarget = new DemandTargetTable(mContext);
        FixedSampleTable dbSample = new FixedSampleTable(mContext);
        CustomerItemPriceTable dbPriceTable = new CustomerItemPriceTable(mContext);

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "='" + item_id + "'", null);

        InvoiceModel invoiceModel = new InvoiceModel();
        if (res.moveToFirst()) {
            invoiceModel.setMkey(res.getString(res.getColumnIndex(MKEY)));
            invoiceModel.setRKey(res.getString(res.getColumnIndex(RKEY)));
            invoiceModel.setRouteManagemnetDate(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_DATE)));
            //invoiceModel.setInvoiceNo(res.getString(res.getColumnIndex(INVOICE_NO)));

            // get invoice number if any with null customer_id attached to it
            invoiceModel.setInvoiceNo(commonInvoiceNumber());

            invoiceModel.setInvoiceDate(res.getString(res.getColumnIndex(INVOICE_DATE)));
            invoiceModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            invoiceModel.setRouteId(res.getString(res.getColumnIndex(ROUTE_ID)));
            invoiceModel.setVehicleNo(res.getString(res.getColumnIndex(VEHICLE_NO)));
            //invoiceModel.setDriverCode(res.getString(res.getColumnIndex(DRIVER_CODE)));
            invoiceModel.setCashierCode(res.getString(res.getColumnIndex(CASHIER_CODE)));
            invoiceModel.setSupervisorPaycode(res.getString(res.getColumnIndex(SUPERVISOR_PAYCODE)));
            invoiceModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            invoiceModel.setCrateId(res.getString(res.getColumnIndex(CRATE_ID)));
            invoiceModel.setInvQtyCr(res.getFloat(res.getColumnIndex(INVQTY_CR)));
            invoiceModel.setInvQtyPs(res.getFloat(res.getColumnIndex(INVQTY_PS)));
            invoiceModel.setGroupId(res.getString(res.getColumnIndex(GROUP_ID)));
            invoiceModel.setGroupPriceDate(res.getString(res.getColumnIndex(GROUP_PRICE_DATE)));
            // reset item price/rate for customer
            //invoiceModel.setItemRate(res.getFloat(res.getColumnIndex(ITEM_RATE)));
            invoiceModel.setItemDiscount(res.getFloat(res.getColumnIndex(ITEM_DISCOUNT)));
            invoiceModel.setItemCST(res.getFloat(res.getColumnIndex(ITEM_CST)));
            invoiceModel.setItemVAT(res.getFloat(res.getColumnIndex(ITEM_VAT)));
            invoiceModel.setItemSurcharge(res.getFloat(res.getColumnIndex(ITEM_SURCHARGE)));
            invoiceModel.setDiscountAmount(res.getFloat(res.getColumnIndex(DISCOUNT_AMOUNT)));
            invoiceModel.setCSTAmount(res.getFloat(res.getColumnIndex(CST_AMOUNT)));
            invoiceModel.setVATAmount(res.getFloat(res.getColumnIndex(VAT_AMOUNT)));
            invoiceModel.setSurchargeAmount(res.getFloat(res.getColumnIndex(SURCHARGE_AMOUNT)));
            invoiceModel.setTotalAmount(res.getFloat(res.getColumnIndex(TOTAL_AMOUNT)));

            //---------------if invoice exists-------------------//
            invoiceModel.setItemRate((float) (dbPriceTable.getItemPriceById(item_id, customer_id)));

            float orderQty = 0;

            //first check whether invoice out table contains the user or not
            if (invoiceOutTable.checkUser(customer_id)) {
                // get item demand if invoice made lastly
                orderQty = invoiceOutTable.getDemandQuantity(customer_id, item_id);
            } else {
                // get invoice item target
                orderQty = dbDemandTarget.getDemandTargetByItem(item_id, customer_id).getTargetQty();
            }
            invoiceModel.setDemandTargetQty(orderQty);
            invoiceModel.setOrderAmount(Utility.roundTwoDecimals(orderQty * invoiceModel.getItemRate()));

            // get item sample
            invoiceModel.setFixedSample(dbSample.getFixedSampleItem(item_id, customer_id).getSQty());

            if (dbItemDetails.checkProduct(invoiceModel.getItemId())) {
                invoiceModel.setItemName(dbItemDetails.getProductById(item_id).getItemName());
            } else {
                invoiceModel = null;
            }
        }
        res.close();
        db.close();
        return invoiceModel;
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