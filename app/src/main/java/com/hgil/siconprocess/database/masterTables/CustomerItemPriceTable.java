package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.routeTarget.RouteTargetModel;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerItemPriceModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 07-02-2017.
 */

public class CustomerItemPriceTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_NAME = "Sicon_item_price_db";
    private static final String TABLE_NAME = "customer_item_price_table";

    private static final String ITEM_ID = "Item_Id";
    private static final String CUSTOMER_ID = "Customer_id";
    private static final String ITEM_PRICE = "item_price";
    private static final String DISCOUNT_PRICE = "discount_price";
    private static final String DISCOUNT_PERCENTAGE = "discount_percentage";
    private static final String DISCOUNT_TYPE = "discountType";
    private static final String DISCOUNTED_PRICE = "discounted_prc";
    private static final String TARGET_QTY = "target_qty";
    private static final String SAMPLE_QTY = "sample_qty";

    private Context mContext;

    public CustomerItemPriceTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ITEM_ID + " TEXT NOT NULL, "
                + CUSTOMER_ID + " TEXT NOT NULL, " + ITEM_PRICE + " REAL NOT NULL, "
                + DISCOUNT_PRICE + " REAL NOT NULL, " + DISCOUNT_PERCENTAGE + " REAL NOT NULL, "
                + DISCOUNT_TYPE + " TEXT NOT NULL, " + DISCOUNTED_PRICE + " REAL NOT NULL, "
                + TARGET_QTY + " REAL NOT NULL, " + SAMPLE_QTY + " INTEGER NOT NULL)");
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
    public boolean insertCustomerItemPrice(CustomerItemPriceModel customerItemPriceModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ITEM_ID, customerItemPriceModel.getItemId());
        contentValues.put(CUSTOMER_ID, customerItemPriceModel.getCustomerId());
        contentValues.put(ITEM_PRICE, customerItemPriceModel.getItemPrice());
        contentValues.put(DISCOUNT_PRICE, customerItemPriceModel.getDiscountPrice());
        contentValues.put(DISCOUNT_PERCENTAGE, customerItemPriceModel.getDiscountPercentage());
        contentValues.put(DISCOUNT_TYPE, customerItemPriceModel.getDiscountType());
        contentValues.put(DISCOUNTED_PRICE, customerItemPriceModel.getDiscountedPrice());
        contentValues.put(TARGET_QTY, customerItemPriceModel.getTarget_qty());
        contentValues.put(SAMPLE_QTY, customerItemPriceModel.getSample_qty());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    // insert multiple items price
    public boolean insertCustomerItemPrice(List<CustomerItemPriceModel> arrCustomerItemPrice) {
        SQLiteDatabase db = this.getWritableDatabase();

        for (int i = 0; i < arrCustomerItemPrice.size(); i++) {
            CustomerItemPriceModel customerItemPriceModel = arrCustomerItemPrice.get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(ITEM_ID, customerItemPriceModel.getItemId());
            contentValues.put(CUSTOMER_ID, customerItemPriceModel.getCustomerId());
            contentValues.put(ITEM_PRICE, customerItemPriceModel.getItemPrice());
            contentValues.put(DISCOUNT_PRICE, customerItemPriceModel.getDiscountPrice());
            contentValues.put(DISCOUNT_PERCENTAGE, customerItemPriceModel.getDiscountPercentage());
            contentValues.put(DISCOUNT_TYPE, customerItemPriceModel.getDiscountType());
            contentValues.put(DISCOUNTED_PRICE, customerItemPriceModel.getDiscountedPrice());
            contentValues.put(TARGET_QTY, customerItemPriceModel.getTarget_qty());
            contentValues.put(SAMPLE_QTY, customerItemPriceModel.getSample_qty());
            db.insert(TABLE_NAME, null, contentValues);
        }
        db.close();
        return true;
    }

    public double getItemPriceById(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + DISCOUNTED_PRICE + " FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        double price = 0;
        if (res.moveToFirst()) {
            price = res.getDouble(res.getColumnIndex(DISCOUNTED_PRICE));
        }
        res.close();
        db.close();
        return price;
    }

    // get required details for the product for a customer on route
    public CustomerItemPriceModel getItemPriceDiscById(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "=? AND " + ITEM_ID + "=?", new String[]{customer_id, item_id});

        CustomerItemPriceModel itemPriceModel = new CustomerItemPriceModel();
        if (res.moveToFirst()) {
            itemPriceModel.setItemPrice(res.getDouble(res.getColumnIndex(ITEM_PRICE)));
            itemPriceModel.setDiscountPrice(res.getDouble(res.getColumnIndex(DISCOUNT_PRICE)));
            itemPriceModel.setDiscountPercentage(res.getDouble(res.getColumnIndex(DISCOUNT_PERCENTAGE)));
            itemPriceModel.setDiscountType(res.getString(res.getColumnIndex(DISCOUNT_TYPE)));
            itemPriceModel.setDiscountedPrice(res.getDouble(res.getColumnIndex(DISCOUNTED_PRICE)));
        }
        res.close();
        db.close();
        return itemPriceModel;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    /*sample related info*/
    public int getFixedSampleItem(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + SAMPLE_QTY + " FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        int sample_qty = 0;
        if (res.moveToFirst()) {
            sample_qty = (res.getInt(res.getColumnIndex(SAMPLE_QTY)));
        }
        res.close();
        db.close();
        return sample_qty;
    }

    // get sample count
    public int getSampleCount(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        /*String query = "select distinct " + ITEM_ID + ", " + ITEM_RATE + ", sum(" + INVQTY_PS + ") over (partition by " + ITEM_ID + ") as loading_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";*/
        String query = "select distinct " + ITEM_ID + ", sum(" + SAMPLE_QTY + ") as loading_sample_qty " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=? group by " + ITEM_ID;
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int total_item_count = 0;
        if (res.moveToFirst()) {
            total_item_count = res.getInt(res.getColumnIndex("loading_sample_qty"));
        }
        res.close();
        db.close();
        return total_item_count;
    }

    /*customer target qty info*/
    public float getDemandTargetByItem(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + TARGET_QTY + " FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        float target_Qty = 0;
        if (res.moveToFirst()) {
            target_Qty = (res.getFloat(res.getColumnIndex(TARGET_QTY)));
        }
        res.close();
        db.close();
        return target_Qty;
    }

    /*get total of target amount*/
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

}