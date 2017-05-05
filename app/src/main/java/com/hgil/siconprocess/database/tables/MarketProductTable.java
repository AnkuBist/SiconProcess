package com.hgil.siconprocess.database.tables;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.database.dbModels.MarketProductModel;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 09-02-2017.
 */

public class MarketProductTable extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "market_product_db";
    private static final String TABLE_NAME = "market_product_table";

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

    public MarketProductTable(Context context) {
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
    public boolean insertMarketProducts(List<MarketProductModel> arrList, String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        // first erase the recent invoice belong to the same user
        eraseMarketOrders(db, customer_id);

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
            for (MarketProductModel marketProductModel : arrList) {
                ih.prepareForInsert();
                if (marketProductModel.getQuantity() > 0) {
                    ih.bind(customerIdColumn, marketProductModel.getCustomerId());
                    ih.bind(customerNameColumn, marketProductModel.getCustomerName());
                    ih.bind(itemIdColumn, marketProductModel.getItemId());
                    ih.bind(itemNameColumn, marketProductModel.getItemName());
                    ih.bind(itemQtyColumn, marketProductModel.getQuantity());
                    ih.bind(imeiNoColumn, marketProductModel.getImei_no());
                    ih.bind(latLngColumn, marketProductModel.getLat_lng());
                    ih.bind(curtimeColumn, Utility.timeStamp());
                    ih.bind(loginIdColumn, marketProductModel.getLogin_id());
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
    private void eraseMarketOrders(SQLiteDatabase db, String customer_id) {
        db.delete(TABLE_NAME, CUSTOMER_ID + " = ?", new String[]{customer_id});
    }

    public MarketProductModel getCustMarketItem(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=? and " + ITEM_ID + "=?", new String[]{customer_id, item_id});

        MarketProductModel marketProductModel = new MarketProductModel();
        if (res.moveToFirst()) {
            marketProductModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
            marketProductModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
            marketProductModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
            marketProductModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
            marketProductModel.setQuantity(res.getInt(res.getColumnIndex(ITEM_QTY)));
            marketProductModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
            marketProductModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
            marketProductModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
            marketProductModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
            marketProductModel.setOrderDate(res.getString(res.getColumnIndex(DATE)));
        }
        res.close();
        db.close();
        return marketProductModel;
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
    public ArrayList<MarketProductModel> getCustMarketProducts(String customer_id) {
        ArrayList<MarketProductModel> array_list = new ArrayList<MarketProductModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=?", new String[]{customer_id});
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                MarketProductModel marketProductModel = new MarketProductModel();
                marketProductModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                marketProductModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                marketProductModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                marketProductModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
                marketProductModel.setQuantity(res.getInt(res.getColumnIndex(ITEM_QTY)));
                marketProductModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                marketProductModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                marketProductModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                marketProductModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                marketProductModel.setOrderDate(res.getString(res.getColumnIndex(DATE)));
                array_list.add(marketProductModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // get route orders
    public ArrayList<MarketProductModel> getRouteOrder() {
        ArrayList<MarketProductModel> array_list = new ArrayList<MarketProductModel>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                MarketProductModel marketProductModel = new MarketProductModel();
                marketProductModel.setCustomerId(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                marketProductModel.setCustomerName(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                marketProductModel.setItemId(res.getString(res.getColumnIndex(ITEM_ID)));
                marketProductModel.setItemName(res.getString(res.getColumnIndex(ITEM_NAME)));
                marketProductModel.setQuantity(res.getInt(res.getColumnIndex(ITEM_QTY)));
                marketProductModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                marketProductModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                marketProductModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                marketProductModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                marketProductModel.setOrderDate(res.getString(res.getColumnIndex(DATE)));
                array_list.add(marketProductModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // cancel customer prepared invoice
    public void cancelInvoice(String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }
}