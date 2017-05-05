package com.hgil.siconprocess.database.tables;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.database.dbModels.NextDayOrderModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 09-02-2017.
 */

public class NextDayOrderTable extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "next_day_order_db";
    private static final String TABLE_NAME = "next_day_order_table";

    private static final String CUSTOMER_ID = "Customer_id";
    private static final String CUSTOMER_NAME = "Customer_name";
    private static final String ITEM_ID = "Item_id";
    private static final String ITEM_NAME = "Item_name";
    private static final String ITEM_QTY = "order_quantity";
    private static final String DATE = "date";
    private static final String IMEI_NO = "imei_no";
    private static final String LAT_LNG = "lat_lng";
    private static final String CURTIME = "cur_time";
    private static final String LOGIN_ID = "login_id";

    private Context mContext;

    public NextDayOrderTable(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + CUSTOMER_ID + " TEXT NOT NULL, "
                + CUSTOMER_NAME + " TEXT NOT NULL, " + ITEM_ID + " TEXT NOT NULL, " + ITEM_NAME + " TEXT NOT NULL, "
                + ITEM_QTY + " INTEGER NOT NULL, "
                + IMEI_NO + " TEXT NULL, " + LAT_LNG + " TEXT NULL, " + CURTIME + " TEXT NULL, " + LOGIN_ID + " TEXT NULL, "
                + DATE + " TEXT NULL)");
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
    public boolean insertNextOrder(List<NextDayOrderModel> arrList, String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // first erase the recent invoice belong to the same user
        eraseUserOrder(db, customer_id);

        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

        // Get the numeric indexes for each of the columns that we're updating
        final int customerIdColumn = ih.getColumnIndex(CUSTOMER_ID);
        final int customerNameColumn = ih.getColumnIndex(CUSTOMER_NAME);
        final int itemIdColumn = ih.getColumnIndex(ITEM_ID);
        final int itemNameColumn = ih.getColumnIndex(ITEM_NAME);
        final int itemQtyColumn = ih.getColumnIndex(ITEM_QTY);
        final int imeiNoColumn = ih.getColumnIndex(IMEI_NO);
        final int latLngColumn = ih.getColumnIndex(LAT_LNG);
        final int curtimeColumn = ih.getColumnIndex(CURTIME);
        final int loginIdColumn = ih.getColumnIndex(LOGIN_ID);
        final int dateColumn = ih.getColumnIndex(DATE);

        try {
            db.beginTransaction();
            for (NextDayOrderModel nextDayOrderModel : arrList) {
                ih.prepareForInsert();
                if (nextDayOrderModel.getQuantity() > 0) {
                    ih.bind(customerIdColumn, nextDayOrderModel.getCustomerId());
                    ih.bind(customerNameColumn, nextDayOrderModel.getCustomerName());
                    ih.bind(itemIdColumn, nextDayOrderModel.getItemId());
                    ih.bind(itemNameColumn, nextDayOrderModel.getItemName());
                    ih.bind(itemQtyColumn, nextDayOrderModel.getQuantity());
                    ih.bind(imeiNoColumn, nextDayOrderModel.getImei_no());
                    ih.bind(latLngColumn, nextDayOrderModel.getLat_lng());
                    ih.bind(curtimeColumn, Utility.timeStamp());
                    ih.bind(loginIdColumn, nextDayOrderModel.getLogin_id());
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
    private void eraseUserOrder(SQLiteDatabase db, String customer_id) {
        db.delete(TABLE_NAME, CUSTOMER_ID + " = ?", new String[]{customer_id});
    }

    public NextDayOrderModel getCustOrderItem(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=? and " + ITEM_ID + "=?", new String[]{customer_id, item_id});

        NextDayOrderModel nextDayOrderModel = new NextDayOrderModel();
        if (res.moveToFirst()) {
            nextDayOrderModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            nextDayOrderModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            nextDayOrderModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            nextDayOrderModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
            nextDayOrderModel.setQuantity(res.getInt(res.getColumnIndex(ITEM_QTY)));
            nextDayOrderModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
            nextDayOrderModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
            nextDayOrderModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
            nextDayOrderModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
            nextDayOrderModel.setOrderDate(res.getString(res.getColumnIndex(DATE)));
        }
        res.close();
        db.close();
        return nextDayOrderModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, DATE + "<?", new String[]{Utility.getCurDate()});
        db.close();
        return numRows;
    }

 /*   public Integer deleteUserRoleMapById(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, USER_ROLE_ID + "= ? ", new String[]{Integer.toString(id)});
    }*/

    //get customer orders
    public ArrayList<NextDayOrderModel> getCustomerOrder(String customer_id) {
        ArrayList<NextDayOrderModel> array_list = new ArrayList<NextDayOrderModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                NextDayOrderModel nextDayOrderModel = new NextDayOrderModel();
                nextDayOrderModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                nextDayOrderModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                nextDayOrderModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                nextDayOrderModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
                nextDayOrderModel.setQuantity(res.getInt(res.getColumnIndex(ITEM_QTY)));
                nextDayOrderModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                nextDayOrderModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                nextDayOrderModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                nextDayOrderModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                nextDayOrderModel.setOrderDate(res.getString(res.getColumnIndex(DATE)));
                array_list.add(nextDayOrderModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // get route orders
    public ArrayList<NextDayOrderModel> getRouteOrder() {
        ArrayList<NextDayOrderModel> array_list = new ArrayList<NextDayOrderModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                NextDayOrderModel nextDayOrderModel = new NextDayOrderModel();
                nextDayOrderModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                nextDayOrderModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                nextDayOrderModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                nextDayOrderModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
                nextDayOrderModel.setQuantity(res.getInt(res.getColumnIndex(ITEM_QTY)));
                nextDayOrderModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                nextDayOrderModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                nextDayOrderModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                nextDayOrderModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                nextDayOrderModel.setOrderDate(res.getString(res.getColumnIndex(DATE)));
                array_list.add(nextDayOrderModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // cancel customer prepared invoice
    /*public void cancelInvoice(String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }*/

    /*// get item demand for customer
    public int getDemandQuantity(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + DEMAND_TARGET_QUANTITY + " from " + TABLE_NAME + " where " + ITEM_ID + "=? AND " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id, customer_id});

        int demandQty = 0;
        if (res.moveToFirst()) {
            demandQty = res.getInt(res.getColumnIndex(DEMAND_TARGET_QUANTITY));
        }
        res.close();
        db.close();
        return demandQty;
    }

    // get van sold item count
    public int getItemOrderQty(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select distinct " + ITEM_ID + ", sum(" + DEMAND_TARGET_QUANTITY + ") as loading_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=? group by " + ITEM_ID;
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
        String query = "select sum(" + DEMAND_TARGET_QUANTITY + "*" + TOTAL_AMOUNT + ") as total " +
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

    // this function is to display the invoice is there is not invoice exists for the customer.
    // this will calculate the available items in stock and display to the user.
    public ArrayList<InvoiceModel> getCustomerInvoiceOff(String customer_id) {
        ArrayList<InvoiceModel> array_list = new ArrayList<InvoiceModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        *//*select sum(InvQty_ps) as loading_qty, Item_id
from V_SD_DepotInvoice_Master where Route_managemnet_Date='2017-01-30' and Route_id='R/mom/0041' group by Item_id*//*

        Cursor res = db.rawQuery("SELECT sum(" + INVQTY_PS + ") as loading_qty, " + ITEM_ID + " FROM " + TABLE_NAME + " GROUP BY " + ITEM_ID, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                int stock = res.getInt(res.getColumnIndex("loading_qty"));
                if (stock > 0) {
                    InvoiceModel invoiceModel = getDepotInvoiceByItemId(customer_id, item_id);
                    if (invoiceModel != null) {
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


    public InvoiceModel getDepotInvoiceByItemId(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "='" + item_id + "'", null);

        InvoiceModel invoiceModel = new InvoiceModel();
        if (res.moveToFirst()) {
            invoiceModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            invoiceModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));

        }
        res.close();
        db.close();
        return invoiceModel;
    }

    // get customer invoice total
    public double custInvoiceTotalAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + ORDER_AMOUNT + ") as total " +
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
*/
}