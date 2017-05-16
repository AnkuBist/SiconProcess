package com.hgil.siconprocess.database.tables;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.activity.fragments.invoiceSyncModel.SyncInvoiceDetailModel;
import com.hgil.siconprocess.adapter.invoice.InvoiceModel;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerItemPriceModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 02-02-2017.
 */

public class InvoiceOutTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "invoice_out_db";
    private static final String TABLE_NAME = "depot_invoice_out";

    private static final String INVOICE_NO = "Invoice_No";
    private static final String BILL_NO = "bill_no";
    private static final String INVOICE_DATE = "Invoice_Date";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String ROUTE_ID = "Route_Id";
    private static final String VEHICLE_NO = "Vehicle_No";
    private static final String ITEM_ID = "Item_id";
    private static final String CASHIER_CODE = "Cashier_code";
    private static final String CRATE_ID = "Crate_id";
    private static final String INVQTY_CR = "InvQty_Cr";
    private static final String INVQTY_PS = "InvQty_ps";
    private static final String ITEM_RATE = "Item_Rate";
    private static final String TOTAL_AMOUNT = "Total_Amount";

    // columns to be added for sync process only

    /*new added columns only merged to this invoice out table*/
    private static final String FIXED_SAMPLE = "fixedSample";
    // private static final String DEMAND_TARGET_QUANTITY = "demandTargetQty";
    private static final String ORDER_AMOUNT = "orderAmount";
    private static final String STOCK_AVAIL = "stockAvail";
    private static final String TEMP_STOCK = "tempStock";
    private static final String ITEM_NAME = "itemName";
/*    private static final String REJECTION_QTY = "rejectionQty";
    private static final String REJECTION_TOTAL_AMOUNT = "rejTotalAmount";*/

    private static final String IMEI_NO = "imei_no";
    private static final String LAT_LNG = "lat_lng";
    private static final String CURTIME = "cur_time";
    private static final String LOGIN_ID = "login_id";
    private static final String DATE = "date";

    private Context mContext;

    public InvoiceOutTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + INVOICE_NO + " TEXT NULL, "
                + BILL_NO + " TEXT NULL, "
                + INVOICE_DATE + " TEXT NULL, " + CUSTOMER_ID + " TEXT NULL, " + ROUTE_ID + " TEXT NULL, "
                + VEHICLE_NO + " TEXT NULL, " + ITEM_ID + " TEXT NULL, " + CASHIER_CODE + " TEXT NULL, "
                + CRATE_ID + " TEXT NULL, " + INVQTY_CR + " REAL NULL, " + INVQTY_PS + " REAL NULL, "
                + ITEM_RATE + " REAL NULL, " + TOTAL_AMOUNT + " REAL NULL, "
                + FIXED_SAMPLE + " INTEGER NULL, " //+ DEMAND_TARGET_QUANTITY + " REAL NULL, "
                + ORDER_AMOUNT + " REAL NULL, "
                + STOCK_AVAIL + " INTEGER NULL, " + TEMP_STOCK + " INTEGER NULL, " + ITEM_NAME + " TEXT NULL, "
                + IMEI_NO + " TEXT NULL, " + LAT_LNG + " TEXT NULL, " + CURTIME + " TEXT NULL, " + LOGIN_ID + " TEXT NULL, "
                + DATE + " TEXT NULL)");
               /* + REJECTION_QTY + " INTEGER NULL, " + REJECTION_TOTAL_AMOUNT + " REAL NULL)");*/
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
    public boolean insertInvoiceOut(List<InvoiceModel> arrList, String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // first erase the recent invoice belong to the same user
        eraseInvoiceUser(db, customer_id);

        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

        // Get the numeric indexes for each of the columns that we're updating
        final int billNoColumn = ih.getColumnIndex(BILL_NO);
        final int invoiceNumberColumn = ih.getColumnIndex(INVOICE_NO);
        final int invoiceDateColumn = ih.getColumnIndex(INVOICE_DATE);
        final int customerIdColumn = ih.getColumnIndex(CUSTOMER_ID);
        final int routeIdColumn = ih.getColumnIndex(ROUTE_ID);
        final int vehicleNoColumn = ih.getColumnIndex(VEHICLE_NO);
        final int itemIdColumn = ih.getColumnIndex(ITEM_ID);
        final int cashierCodeColumn = ih.getColumnIndex(CASHIER_CODE);
        final int crateIdColumn = ih.getColumnIndex(CRATE_ID);
        final int invQtyPsColumn = ih.getColumnIndex(INVQTY_PS);
        final int itemRateColumn = ih.getColumnIndex(ITEM_RATE);
        final int totalAmountColumn = ih.getColumnIndex(TOTAL_AMOUNT);
        final int fixedSampleColumn = ih.getColumnIndex(FIXED_SAMPLE);
        //final int demandTargetQuantityColumn = ih.getColumnIndex(DEMAND_TARGET_QUANTITY);
        final int orderAmountColumn = ih.getColumnIndex(ORDER_AMOUNT);
        final int stockAvailColumn = ih.getColumnIndex(STOCK_AVAIL);
        final int tempStockColumn = ih.getColumnIndex(TEMP_STOCK);
        final int itemNameColumn = ih.getColumnIndex(ITEM_NAME);
        final int imeiNoColumn = ih.getColumnIndex(IMEI_NO);
        final int latLngColumn = ih.getColumnIndex(LAT_LNG);
        final int curtimeColumn = ih.getColumnIndex(CURTIME);
        final int loginIdColumn = ih.getColumnIndex(LOGIN_ID);
        final int dateColumn = ih.getColumnIndex(DATE);

        try {
            db.beginTransaction();
            for (InvoiceModel invoiceModel : arrList) {
                if (invoiceModel.getOrderAmount() > 0 && invoiceModel.getInvQtyPs() > 0) {
                    ih.prepareForInsert();

                    ih.bind(billNoColumn, invoiceModel.getBill_no());
                    ih.bind(invoiceNumberColumn, invoiceModel.getInvoiceNo());
                    ih.bind(invoiceDateColumn, invoiceModel.getInvoiceDate());
                    ih.bind(customerIdColumn, invoiceModel.getCustomerId());
                    ih.bind(routeIdColumn, invoiceModel.getRouteId());
                    ih.bind(vehicleNoColumn, invoiceModel.getVehicleNo());
                    ih.bind(itemIdColumn, invoiceModel.getItemId());
                    ih.bind(cashierCodeColumn, invoiceModel.getCashierCode());
                    ih.bind(crateIdColumn, invoiceModel.getCrateId());
                    ih.bind(invQtyPsColumn, invoiceModel.getInvQtyPs());
                    ih.bind(itemRateColumn, invoiceModel.getItemRate());
                    ih.bind(totalAmountColumn, invoiceModel.getTotalAmount());
                    ih.bind(fixedSampleColumn, invoiceModel.getFixedSample());
                    //ih.bind(demandTargetQuantityColumn, invoiceModel.getDemandTargetQty());
                    ih.bind(orderAmountColumn, invoiceModel.getOrderAmount());
                    ih.bind(stockAvailColumn, invoiceModel.getStockAvail());
                    ih.bind(tempStockColumn, invoiceModel.getTempStock());
                    ih.bind(itemNameColumn, invoiceModel.getItemName());
                    ih.bind(imeiNoColumn, invoiceModel.getImei_no());
                    ih.bind(latLngColumn, invoiceModel.getLat_lng());
                    ih.bind(curtimeColumn, Utility.timeStamp());
                    ih.bind(loginIdColumn, invoiceModel.getLogin_id());
                    ih.bind(dateColumn, Utility.getCurDate());

                    ih.execute();
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
        return true;
    }

    /* erase customer recent invoice items*/
    private void eraseInvoiceUser(SQLiteDatabase db, String customer_id) {
        db.delete(TABLE_NAME, CUSTOMER_ID + " = ?", new String[]{customer_id});
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, DATE + "<?", new String[]{Utility.getCurDate()});
        db.close();
        return numRows;
    }

    // check for user exists in invoice or not
    public boolean checkUser(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + CUSTOMER_ID + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        boolean status = false;
        if (res != null) {
            if (res.moveToFirst()) {
                status = true;
            }
        }
        res.close();
        db.close();
        return status;
    }

    // get invoice item qty for customer
    public int customerInvOutItemQty(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + INVQTY_PS + " from " + TABLE_NAME + " where " + ITEM_ID + "=? AND " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id, customer_id});

        int invQty = 0;
        if (res.moveToFirst()) {
            invQty = res.getInt(res.getColumnIndex(INVQTY_PS));
        }
        res.close();
        db.close();
        return invQty;
    }

    // get van sold item count other than same customer
    public int totalItemOrderQtyOTSelf(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + INVQTY_PS + ") as loading_qty from " + TABLE_NAME + " where " + CUSTOMER_ID + "<>? and " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id, item_id});

        int demandQty = 0;
        if (res.moveToFirst()) {
            demandQty = res.getInt(res.getColumnIndex("loading_qty"));
        }
        res.close();
        db.close();
        return demandQty;
    }

    // get van sold item count
    public int getItemOrderQty(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + INVQTY_PS + ") as loading_qty from " + TABLE_NAME + " where " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int demandQty = 0;
        if (res.moveToFirst()) {
            demandQty = res.getInt(res.getColumnIndex("loading_qty"));
        }
        res.close();
        db.close();
        return demandQty;
    }

    //TODO
    // get customer grand total for invoice prepared.
    public double custOrderTotalAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + INVQTY_PS + "*" + TOTAL_AMOUNT + ") as total " +
                "from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        double amount = 0;
        if (res.moveToFirst()) {
            amount = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return amount;
    }

    // get customer invoice total.....customer gross sale
    public double custInvoiceTotalAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + ORDER_AMOUNT + ") as total " +
                "from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        double amount = 0;
        if (res.moveToFirst()) {
            amount = res.getDouble(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return Utility.roundTwoDecimals(amount);
    }

    /*generate array list to sync data*/
    public ArrayList<SyncInvoiceDetailModel> syncInvoice() {
        SQLiteDatabase db = this.getReadableDatabase();
        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(mContext);
        DepotInvoiceView depotInvoiceTable = new DepotInvoiceView(mContext);

        ArrayList<SyncInvoiceDetailModel> arrayList = new ArrayList<>();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                SyncInvoiceDetailModel syncModel = new SyncInvoiceDetailModel();
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));

                syncModel.setBill_no(res.getString(res.getColumnIndex(BILL_NO)));
                syncModel.setInvoice_no(res.getString(res.getColumnIndex(INVOICE_NO)));
                syncModel.setInvoice_date(res.getString(res.getColumnIndex(INVOICE_DATE)));
                syncModel.setCustomer_id(customer_id);
                syncModel.setRoute_id(res.getString(res.getColumnIndex(ROUTE_ID)));
                syncModel.setCashier_code(res.getString(res.getColumnIndex(CASHIER_CODE)));          // CASHIER_CODE
                syncModel.setItem_id(item_id);
                syncModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                syncModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                syncModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                syncModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));

                //get van item total count
                int item_total_count = depotInvoiceTable.itemVanStockLoadingCount(item_id);
                syncModel.setLoading_count(item_total_count);
                syncModel.setSale_count(res.getInt(res.getColumnIndex(INVQTY_PS)));

                // get item_price, disc type and discount for the customer on route
                CustomerItemPriceModel itemPriceModel = itemPriceTable.getItemPriceDiscById(customer_id, item_id);
                syncModel.setItem_price(itemPriceModel.getItemPrice());
                syncModel.setDisc_price(itemPriceModel.getDiscountPrice());
                syncModel.setDisc_percentage(itemPriceModel.getDiscountPercentage());
                syncModel.setDisc_type(itemPriceModel.getDiscountType());
                syncModel.setDiscounted_price(itemPriceModel.getDiscountedPrice());
                syncModel.setSample(res.getInt(res.getColumnIndex(FIXED_SAMPLE)));

                syncModel.setTotal_sale_amount(syncModel.getSale_count() * syncModel.getDiscounted_price());
                syncModel.setTotal_disc_amount(
                        (syncModel.getItem_price() - syncModel.getDiscounted_price()) *
                                syncModel.getSale_count());

                arrayList.add(syncModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return arrayList;
    }

    // totals items sold overall count
    public int soldItemCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + FIXED_SAMPLE + "+" + INVQTY_PS + ") as total " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int total = 0;
        if (res.moveToFirst()) {
            total = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return total;
    }

    /*sold items count for the respect product only*/
    public int soldItemTargetCount(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + FIXED_SAMPLE + "+" + INVQTY_PS + ") as total " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int total = 0;
        if (res.moveToFirst()) {
            total = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return total;
    }

    // cancel customer prepared invoice
    public void cancelInvoice(String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    /*get only the bill no of customer if any*/
    public String returnCustomerBillNo(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + BILL_NO + " from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";

        Cursor res = db.rawQuery(query, new String[]{customer_id});

        String bill_no = "";
        if (res.moveToFirst()) {
            bill_no = res.getString(res.getColumnIndex(BILL_NO));
        }
        res.close();
        db.close();
        return bill_no;
    }

    /*GET MAX BILL IF ANY*/
    public String returnMaxBillNo() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select max (" + BILL_NO + ") as expected_bill_no from " + TABLE_NAME;

        Cursor res = db.rawQuery(query, null);

        String bill_no = "";
        if (res.moveToFirst()) {
            bill_no = res.getString(res.getColumnIndex("expected_bill_no"));
        }
        res.close();
        db.close();
        return bill_no;
    }
}