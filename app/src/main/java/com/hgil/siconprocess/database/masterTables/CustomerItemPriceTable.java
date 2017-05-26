package com.hgil.siconprocess.database.masterTables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.routeTarget.RouteTargetModel;
import com.hgil.siconprocess.database.tables.CustomerRejectionTable;
import com.hgil.siconprocess.database.tables.InvoiceOutTable;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerItemPriceModel;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.ProductModel;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncInvoiceDetailModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mohan.giri on 07-02-2017.
 */

public class CustomerItemPriceTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

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

    //customer item discount price and sample count
    public CustomerItemPriceModel custItemPriceNSample(String item_id, String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT " + DISCOUNTED_PRICE + ", " + SAMPLE_QTY + " FROM " + TABLE_NAME +
                " WHERE " + ITEM_ID + "=? and " + CUSTOMER_ID + "=?", new String[]{item_id, customer_id});

        CustomerItemPriceModel priceModel = new CustomerItemPriceModel();
        if (res.moveToFirst()) {
            priceModel.setDiscountedPrice(res.getDouble(res.getColumnIndex(DISCOUNTED_PRICE)));
            priceModel.setSample_qty(res.getInt(res.getColumnIndex(SAMPLE_QTY)));
        }
        res.close();
        db.close();
        return priceModel;
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

    /*item price*/
    public double itemPrice(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT distinct " + ITEM_PRICE + " FROM " + TABLE_NAME + " WHERE " + ITEM_ID + "=?", new String[]{item_id});

        double price = 0;
        if (res.moveToFirst()) {
            price = res.getDouble(res.getColumnIndex(ITEM_PRICE));
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
            itemPriceModel.setSample_qty(res.getInt(res.getColumnIndex(SAMPLE_QTY)));
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
    public int itemTotalSampleCount(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + SAMPLE_QTY + ") as loading_sample_qty from " + TABLE_NAME + " where " + ITEM_ID + "=?";
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
                ProductModel productModel = productView.getProductById(item_id);
                routeTargetModel.setItem_name(productModel.getItemName());
                routeTargetModel.setItemSequence(productModel.getITEMSEQUENCE());
                routeTargetModel.setDemand(productModel.getDemandQty());

                // get the product invoice count from the local table
                routeTargetModel.setAchieved(invoiceOutTable.soldItemTargetCount(item_id));
                routeTargetModel.setVariance(routeTargetModel.getAchieved() - routeTargetModel.getTarget());

                if (routeTargetModel.getAchieved() > 0 || routeTargetModel.getTarget() > 0 || routeTargetModel.getDemand() > 0)
                    array_list.add(routeTargetModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();

        ArrayList<RouteTargetModel> sortedArrayList = new ArrayList<RouteTargetModel>(array_list);
        Collections.sort(sortedArrayList, new Comparator<RouteTargetModel>() {
            public int compare(RouteTargetModel p1, RouteTargetModel p2) {
                return Integer.valueOf(p1.getItemSequence()).compareTo(p2.getItemSequence());
            }
        });
        return sortedArrayList;
    }

    /*customer final invoice sale and rejection management*/
    /*get total of target amount*/
    public ArrayList<SyncInvoiceDetailModel> syncInvoiceSaleRej(String route_id) {
        ArrayList<SyncInvoiceDetailModel> arrSyncModel = new ArrayList<>();
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRejectionTable customerRejectionTable = new CustomerRejectionTable(mContext);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " order by " + CUSTOMER_ID, null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                SyncInvoiceDetailModel syncModel = new SyncInvoiceDetailModel();
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));
                String customer_id = res.getString(res.getColumnIndex(CUSTOMER_ID));
                double item_price = res.getDouble(res.getColumnIndex(ITEM_PRICE));
                double discount_price = (res.getDouble(res.getColumnIndex(DISCOUNT_PRICE)));
                double discount_percentage = (res.getDouble(res.getColumnIndex(DISCOUNT_PERCENTAGE)));
                String discount_type = (res.getString(res.getColumnIndex(DISCOUNT_TYPE)));
                double discounted_price = (res.getDouble(res.getColumnIndex(DISCOUNTED_PRICE)));
                int sample = res.getInt(res.getColumnIndex(SAMPLE_QTY));

                syncModel.setItem_price(item_price);
                syncModel.setDisc_price(discount_price);
                syncModel.setDisc_percentage(discount_percentage);
                syncModel.setDisc_type(discount_type);
                syncModel.setDiscounted_price(discounted_price);
                syncModel.setSample(sample);

                syncModel.setItem_id(item_id);
                syncModel.setCustomer_id(customer_id);

                //get customer item sale
                SyncInvoiceDetailModel custItemSale = invoiceOutTable.syncCompletedInvoiceItem(customer_id, item_id);
                int saleCount = custItemSale.getSale_count();
                if (saleCount > 0) {
                    syncModel.setRoute_management_id(custItemSale.getRoute_management_id());
                    syncModel.setBill_no(custItemSale.getBill_no());
                    syncModel.setInvoice_no(custItemSale.getInvoice_no());
                    syncModel.setInvoice_date(custItemSale.getInvoice_date());
                    syncModel.setRoute_id(custItemSale.getRoute_id());
                    syncModel.setCashier_code(custItemSale.getCashier_code());          // CASHIER_CODE

                    syncModel.setSale_count(saleCount);
                }

                //get customer rejection details
                SyncInvoiceDetailModel custItemRej = customerRejectionTable.syncCompletedRejection(route_id, customer_id, item_id);
                int fresh_rej = custItemRej.getFresh_rej();
                int market_rej = custItemRej.getMarket_rej();
                if (fresh_rej > 0 || market_rej > 0) {
                    syncModel.setRoute_management_id(custItemRej.getRoute_management_id());
                    syncModel.setBill_no(custItemRej.getBill_no());
                    syncModel.setInvoice_no(custItemRej.getInvoice_no());
                    syncModel.setInvoice_date(custItemRej.getInvoice_date());
                    syncModel.setRoute_id(custItemRej.getRoute_id());
                    syncModel.setCashier_code(custItemRej.getCashier_code());

                    syncModel.setFresh_rej(fresh_rej);
                    syncModel.setMarket_rej(market_rej);
                }

                // final details
                int actual_sale_count = saleCount - market_rej;
                syncModel.setActual_sale_count(actual_sale_count);

                //can be calculated here only
                syncModel.setTotal_sale_amount(actual_sale_count * discounted_price);
                syncModel.setTotal_disc_amount((item_price - discounted_price) * actual_sale_count);
                syncModel.setF_rej_amount(discounted_price * fresh_rej);
                syncModel.setM_rej_amount(discounted_price * market_rej);

                if (saleCount > 0 || fresh_rej > 0 || market_rej > 0)
                    arrSyncModel.add(syncModel);

                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return arrSyncModel;
    }
}