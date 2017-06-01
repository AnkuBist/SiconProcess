package com.hgil.siconprocess.database.tables;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hgil.siconprocess.adapter.invoiceRejection.CRejectionModel;
import com.hgil.siconprocess.adapter.invoiceRejection.FreshRejectionModel;
import com.hgil.siconprocess.adapter.invoiceRejection.MarketRejectionModel;
import com.hgil.siconprocess.database.masterTables.CustomerItemPriceTable;
import com.hgil.siconprocess.database.masterTables.DepotInvoiceView;
import com.hgil.siconprocess.retrofit.loginResponse.dbModels.CustomerItemPriceModel;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.RejectionDetailModel;
import com.hgil.siconprocess.syncPOJO.invoiceSyncModel.SyncInvoiceDetailModel;
import com.hgil.siconprocess.utils.Constant;
import com.hgil.siconprocess.utils.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohan.giri on 04-02-2017.
 */

public class CustomerRejectionTable extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "rej_db";
    private static final String TABLE_NAME = "rej_table";

    private static final String ROUTE_MANAGEMENT_ID = "route_management_id";
    private static final String INVOICE_NO = "invoice_no";
    private static final String BILL_NO = "bill_no";
    private static final String CASHIER_CODE = "cashier_code";
    private static final String ITEM_ID = "item_id";
    private static final String ITEM_NAME = "item_name";
    private static final String CUSTOMER_ID = "customer_id";
    private static final String CUSTOMER_NAME = "customer_name";
    private static final String VAN_STOCK = "van_stock";
    private static final String REJ_QTY = "rej_qty";
    private static final String PRICE = "price";

    private static final String FRESH_M_SHAPED = "mShaped";
    private static final String FRESH_TORN_POLLY = "tornPolly";
    private static final String FRESH_FUNGUS = "fungus";
    private static final String FRESH_WET_BREAD = "wetBread";
    private static final String FRESH_OTHERS = "others";

    private static final String MARKET_DAMAGED = "damaged";
    private static final String MARKET_EXPIRED = "expired";
    private static final String MARKET_RAT_EATEN = "ratEaten";

    private static final String GRAND_TOTAL = "grand_total";

    private static final String IMEI_NO = "imei_no";
    private static final String LAT_LNG = "lat_lng";
    private static final String CURTIME = "cur_time";
    private static final String LOGIN_ID = "login_id";
    private static final String DATE = "date";

    private static final String INV_STATUS = "inv_status";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" + ROUTE_MANAGEMENT_ID + " TEXT NULL, "
            + INVOICE_NO + " TEXT NULL, " + BILL_NO + " TEXT NULL, "
            + CASHIER_CODE + " TEXT NULL, " + ITEM_ID + " TEXT NOT NULL, " + ITEM_NAME + " TEXT NOT NULL, "
            + CUSTOMER_ID + " TEXT NOT NULL, " + CUSTOMER_NAME + " TEXT NOT NULL, " + VAN_STOCK + " INTEGER NULL, " //+ VAN_QTY + " INTEGER NOT NULL, "
            + REJ_QTY + " INTEGER NULL, " + PRICE + " REAL NULL, " + FRESH_M_SHAPED + " INTEGER NULL, "
            + FRESH_TORN_POLLY + " INTEGER NULL, " + FRESH_FUNGUS + " INTEGER NULL, "
            + FRESH_WET_BREAD + " INTEGER NULL, " + FRESH_OTHERS + " INTEGER NULL, " + MARKET_DAMAGED + " INTEGER NULL, "
            + MARKET_EXPIRED + " INTEGER NULL, " + MARKET_RAT_EATEN + " INTEGER NULL, " + GRAND_TOTAL + " REAL NULL, "
            + IMEI_NO + " TEXT NULL, " + LAT_LNG + " TEXT NULL, " + CURTIME + " TEXT NULL, " + LOGIN_ID + " TEXT NULL, "
            + DATE + " TEXT NULL, " + INV_STATUS + " TEXT NULL)";

    private Context mContext;

    public CustomerRejectionTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void eraseTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }

    /*update customer invoice status*/
    public void updateCustInvRejStatus(String customer_id, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INV_STATUS, status);
        db.update(TABLE_NAME, contentValues, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    // insert multiple rejections to table for selected user at once
    public void insertCustRejections(List<CRejectionModel> arrList, String customer_id) {
        // first erase data belong to the same user
        eraseCustRejections(customer_id);

        SQLiteDatabase db = this.getWritableDatabase();

        DatabaseUtils.InsertHelper ih = new DatabaseUtils.InsertHelper(db, TABLE_NAME);

        // Get the numeric indexes for each of the columns that we're updating
        final int routeManagementIdColumn = ih.getColumnIndex(ROUTE_MANAGEMENT_ID);
        final int billNoColumn = ih.getColumnIndex(BILL_NO);
        final int invoiceNumberColumn = ih.getColumnIndex(INVOICE_NO);
        final int cashierCodeColumn = ih.getColumnIndex(CASHIER_CODE);
        final int itemIdColumn = ih.getColumnIndex(ITEM_ID);
        final int itemNameColumn = ih.getColumnIndex(ITEM_NAME);
        final int customerIdColumn = ih.getColumnIndex(CUSTOMER_ID);
        final int customerNameColumn = ih.getColumnIndex(CUSTOMER_NAME);
        final int vanStockColumn = ih.getColumnIndex(VAN_STOCK);
        final int rejQtyColumn = ih.getColumnIndex(REJ_QTY);
        final int priceColumn = ih.getColumnIndex(PRICE);
        final int freshMShapedColumn = ih.getColumnIndex(FRESH_M_SHAPED);
        final int freshTornPollyColumn = ih.getColumnIndex(FRESH_TORN_POLLY);
        final int freshFungusColumn = ih.getColumnIndex(FRESH_FUNGUS);
        final int freshWetBreadColumn = ih.getColumnIndex(FRESH_WET_BREAD);
        final int freshOthersColumn = ih.getColumnIndex(FRESH_OTHERS);
        final int marketDamagedColumn = ih.getColumnIndex(MARKET_DAMAGED);
        final int marketExpiredColumn = ih.getColumnIndex(MARKET_EXPIRED);
        final int marketRatEatenColumn = ih.getColumnIndex(MARKET_RAT_EATEN);
        final int grandTotalColumn = ih.getColumnIndex(GRAND_TOTAL);
        final int imeiNoColumn = ih.getColumnIndex(IMEI_NO);
        final int latLngColumn = ih.getColumnIndex(LAT_LNG);
        final int curtimeColumn = ih.getColumnIndex(CURTIME);
        final int loginIdColumn = ih.getColumnIndex(LOGIN_ID);
        final int dateColumn = ih.getColumnIndex(DATE);

        try {
            db.beginTransaction();
            for (CRejectionModel rejectionModel : arrList) {
                ih.prepareForInsert();

                ih.bind(routeManagementIdColumn, rejectionModel.getRoute_management_id());
                ih.bind(billNoColumn, rejectionModel.getBill_no());
                ih.bind(invoiceNumberColumn, rejectionModel.getInvoice_no());
                ih.bind(cashierCodeColumn, rejectionModel.getCashier_code());
                ih.bind(itemIdColumn, rejectionModel.getItem_id());
                ih.bind(itemNameColumn, rejectionModel.getItem_name());
                ih.bind(customerIdColumn, rejectionModel.getCustomer_id());
                ih.bind(customerNameColumn, rejectionModel.getCustomer_name());
                ih.bind(vanStockColumn, rejectionModel.getVan_stock());
                ih.bind(rejQtyColumn, rejectionModel.getRej_qty());
                ih.bind(priceColumn, rejectionModel.getPrice());

                FreshRejectionModel freshRejection = rejectionModel.getFreshRejection();
                MarketRejectionModel marketRejection = rejectionModel.getMarketRejection();

                int fresh_total_rej = 0, market_total_rej = 0;
                if (freshRejection != null) {
                    ih.bind(freshMShapedColumn, freshRejection.getmShaped());
                    ih.bind(freshTornPollyColumn, freshRejection.getTornPolly());
                    ih.bind(freshFungusColumn, freshRejection.getFungus());
                    ih.bind(freshWetBreadColumn, freshRejection.getWetBread());
                    ih.bind(freshOthersColumn, freshRejection.getOthers());
                    fresh_total_rej = freshRejection.getTotal();
                }

                if (marketRejection != null) {
                    ih.bind(marketDamagedColumn, marketRejection.getDamaged());
                    ih.bind(marketExpiredColumn, marketRejection.getExpired());
                    ih.bind(marketRatEatenColumn, marketRejection.getRatEaten());
                    market_total_rej = marketRejection.getTotal();
                }

                double grand_total = (market_total_rej) * rejectionModel.getPrice();

                ih.bind(grandTotalColumn, grand_total);
                ih.bind(imeiNoColumn, rejectionModel.getImei_no());
                ih.bind(latLngColumn, rejectionModel.getLat_lng());
                ih.bind(curtimeColumn, Utility.timeStamp());
                ih.bind(loginIdColumn, rejectionModel.getLogin_id());
                ih.bind(dateColumn, Utility.getCurDate());
                if (market_total_rej > 0 || fresh_total_rej > 0)
                    ih.execute();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, DATE + "<?", new String[]{Utility.getCurDate()});
        db.close();
        return numRows;
    }

    // erase customer rejections
    public void eraseCustRejections(String customer_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, CUSTOMER_ID + "=?", new String[]{customer_id});
        db.close();
    }

    // get user all rejections by date
    // till now user has not restriction to make rejections.
    public ArrayList<CRejectionModel> getCustomerRejections(String customer_id) {
        ArrayList<CRejectionModel> array_list = new ArrayList<CRejectionModel>();

        DepotInvoiceView depotInvoiceView = new DepotInvoiceView(mContext);
        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(mContext);
        InvoiceOutTable invoiceOutTable = new InvoiceOutTable(mContext);
        CustomerRejectionTable rejectionTable = new CustomerRejectionTable(mContext);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + CUSTOMER_ID + "='" + customer_id + "'", null);
        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                CRejectionModel rejectionModel = new CRejectionModel();
                String item_id = res.getString(res.getColumnIndex(ITEM_ID));

                rejectionModel.setRoute_management_id(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
                rejectionModel.setBill_no(res.getString(res.getColumnIndex(BILL_NO)));
                rejectionModel.setInvoice_no(res.getString(res.getColumnIndex(INVOICE_NO)));
                rejectionModel.setCashier_code(res.getString(res.getColumnIndex(CASHIER_CODE)));
                rejectionModel.setItem_id(item_id);
                rejectionModel.setItem_name(res.getString(res.getColumnIndex(ITEM_NAME)));
                rejectionModel.setCustomer_id(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                rejectionModel.setCustomer_name(res.getString(res.getColumnIndex(CUSTOMER_NAME)));
                rejectionModel.setVan_stock(res.getInt(res.getColumnIndex(VAN_STOCK)));

                int stock_left = depotInvoiceView.itemVanStockLoadingCount(item_id)
                        - invoiceOutTable.getItemOrderQty(item_id)
                        - itemPriceTable.itemTotalSampleCount(item_id)
                        - rejectionTable.freshRejOtherThenCust(customer_id, item_id);

                rejectionModel.setStock_left(stock_left);
                rejectionModel.setRej_qty(res.getInt(res.getColumnIndex(REJ_QTY)));
                rejectionModel.setPrice(res.getDouble(res.getColumnIndex(PRICE)));

                FreshRejectionModel freshRejection = new FreshRejectionModel();
                MarketRejectionModel marketRejection = new MarketRejectionModel();

                freshRejection.setmShaped(res.getInt(res.getColumnIndex(FRESH_M_SHAPED)));
                freshRejection.setTornPolly(res.getInt(res.getColumnIndex(FRESH_TORN_POLLY)));
                freshRejection.setFungus(res.getInt(res.getColumnIndex(FRESH_FUNGUS)));
                freshRejection.setWetBread(res.getInt(res.getColumnIndex(FRESH_WET_BREAD)));
                freshRejection.setOthers(res.getInt(res.getColumnIndex(FRESH_OTHERS)));
                freshRejection.setTotal(freshRejection.getmShaped() + freshRejection.getTornPolly() + freshRejection.getFungus() + freshRejection.getWetBread() + freshRejection.getOthers());

                marketRejection.setDamaged(res.getInt(res.getColumnIndex(MARKET_DAMAGED)));
                marketRejection.setExpired(res.getInt(res.getColumnIndex(MARKET_EXPIRED)));
                marketRejection.setRatEaten(res.getInt(res.getColumnIndex(MARKET_RAT_EATEN)));
                marketRejection.setTotal(marketRejection.getDamaged() + marketRejection.getExpired() + marketRejection.getRatEaten());

                rejectionModel.setFreshRejection(freshRejection);
                rejectionModel.setMarketRejection(marketRejection);

                rejectionModel.setTotal(res.getDouble(res.getColumnIndex(GRAND_TOTAL)));

                rejectionModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                rejectionModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                rejectionModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));
                array_list.add(rejectionModel);
                res.moveToNext();
            }
        }
        res.close();
        db.close();
        return array_list;
    }

    // check if the product is in customer rejection list exists or not
    public int custRejExists(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME, CUSTOMER_ID + "=? AND " + ITEM_ID + "=?", new String[]{customer_id, item_id});
        db.close();
        return numRows;
    }

    // get grand total for customer rejections
    public double custRejTotalAmount(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + GRAND_TOTAL + ") as total " +
                "from " + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        double amount = 0;
        if (res.moveToFirst()) {
            amount = res.getDouble(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return amount;
    }

    // get total market rejctions for customer
    public int custTotalMRej(String customer_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + MARKET_DAMAGED + "+" + MARKET_EXPIRED + "+" + MARKET_RAT_EATEN + ") as total from "
                + TABLE_NAME + " where " + CUSTOMER_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id});

        int m_Rej = 0;
        if (res.moveToFirst()) {
            m_Rej = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return m_Rej;
    }

    // get van sold item count other than same customer
    public int freshRejOtherThenCust(String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + FRESH_M_SHAPED + "+" + FRESH_TORN_POLLY + "+"
                + FRESH_FUNGUS + "+" + FRESH_WET_BREAD + "+" + FRESH_OTHERS + ") as total from "
                + TABLE_NAME + " where " + CUSTOMER_ID + "<>? and " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{customer_id, item_id});

        int freshRej = 0;
        if (res.moveToFirst()) {
            freshRej = res.getInt(res.getColumnIndex("total"));
        }
        res.close();
        db.close();
        return freshRej;
    }


    // get product total market rejection
    public int productMarketRejection(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + MARKET_DAMAGED + "+" + MARKET_EXPIRED + "+" + MARKET_RAT_EATEN + ") as total_rej " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int rej_total = 0;
        if (res.moveToFirst()) {
            rej_total = res.getInt(res.getColumnIndex("total_rej"));
        }
        res.close();
        db.close();
        return rej_total;
    }

    // get product total fresh rejection
    public int productFreshRejection(String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + FRESH_M_SHAPED + "+" + FRESH_TORN_POLLY + "+"
                + FRESH_FUNGUS + "+" + FRESH_WET_BREAD + "+" + FRESH_OTHERS + ") as total_rej " +
                "from " + TABLE_NAME + " where " + ITEM_ID + "=?";
        Cursor res = db.rawQuery(query, new String[]{item_id});

        int rej_total = 0;
        if (res.moveToFirst()) {
            rej_total = res.getInt(res.getColumnIndex("total_rej"));
        }
        res.close();
        db.close();
        return rej_total;
    }

    // total of rejections
    // get total market rejection
    public int routeMarketRejection() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + MARKET_DAMAGED + "+" + MARKET_EXPIRED + "+" + MARKET_RAT_EATEN + ") as total_rej " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int rej_total = 0;
        if (res.moveToFirst()) {
            rej_total = res.getInt(res.getColumnIndex("total_rej"));
        }
        res.close();
        db.close();
        return rej_total;
    }

    // get total fresh rejection
    public int routeFreshRejection() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(" + FRESH_M_SHAPED + "+" + FRESH_TORN_POLLY + "+"
                + FRESH_FUNGUS + "+" + FRESH_WET_BREAD + "+" + FRESH_OTHERS + ") as total_rej " +
                "from " + TABLE_NAME;
        Cursor res = db.rawQuery(query, null);

        int rej_total = 0;
        if (res.moveToFirst()) {
            rej_total = res.getInt(res.getColumnIndex("total_rej"));
        }
        res.close();
        db.close();
        return rej_total;
    }

    // get product rejections over route
    public ArrayList<SyncInvoiceDetailModel> syncCompletedRejection(String route_id) {
        CustomerItemPriceTable itemPriceTable = new CustomerItemPriceTable(mContext);
        DepotInvoiceView depotInvoiceTable = new DepotInvoiceView(mContext);

        ArrayList<SyncInvoiceDetailModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + INV_STATUS + "=?",
                new String[]{Constant.STATUS_COMPLETE});

        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                SyncInvoiceDetailModel rejectionModel = new SyncInvoiceDetailModel();
                rejectionModel.setRoute_management_id(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
                rejectionModel.setBill_no(res.getString(res.getColumnIndex(BILL_NO)));
                rejectionModel.setInvoice_no(res.getString(res.getColumnIndex(INVOICE_NO)));
                rejectionModel.setInvoice_date(res.getString(res.getColumnIndex(DATE)));
                rejectionModel.setRoute_id(route_id);
                rejectionModel.setCustomer_id(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                rejectionModel.setItem_id(res.getString(res.getColumnIndex(ITEM_ID)));
                rejectionModel.setCashier_code(res.getString(res.getColumnIndex(CASHIER_CODE)));

                rejectionModel.setImei_no(res.getString(res.getColumnIndex(IMEI_NO)));
                rejectionModel.setLat_lng(res.getString(res.getColumnIndex(LAT_LNG)));
                rejectionModel.setTime_stamp(res.getString(res.getColumnIndex(CURTIME)));
                rejectionModel.setLogin_id(res.getString(res.getColumnIndex(LOGIN_ID)));

                //get van item total count
                int item_total_count = depotInvoiceTable.itemVanStockLoadingCount(rejectionModel.getItem_id());
                rejectionModel.setLoading_count(item_total_count);

                /*item price details*/
                // get item_price, disc type and discount for the customer on route
                CustomerItemPriceModel itemPriceModel = itemPriceTable.getItemPriceDiscById(rejectionModel.getCustomer_id(), rejectionModel.getItem_id());
                rejectionModel.setItem_price(itemPriceModel.getItemPrice());
                rejectionModel.setDisc_price(itemPriceModel.getDiscountPrice());
                rejectionModel.setDisc_percentage(itemPriceModel.getDiscountPercentage());
                rejectionModel.setDisc_type(itemPriceModel.getDiscountType());
                rejectionModel.setDiscounted_price(itemPriceModel.getDiscountedPrice());

                int m_shaped = (res.getInt(res.getColumnIndex(FRESH_M_SHAPED)));
                int torn_polly = (res.getInt(res.getColumnIndex(FRESH_TORN_POLLY)));
                int fungus = (res.getInt(res.getColumnIndex(FRESH_FUNGUS)));
                int wet_bread = (res.getInt(res.getColumnIndex(FRESH_WET_BREAD)));
                int others = (res.getInt(res.getColumnIndex(FRESH_OTHERS)));
                int total_fresh = m_shaped + torn_polly + fungus + wet_bread + others;

                int damaged = (res.getInt(res.getColumnIndex(MARKET_DAMAGED)));
                int expired = (res.getInt(res.getColumnIndex(MARKET_EXPIRED)));
                int rat_eaten = (res.getInt(res.getColumnIndex(MARKET_RAT_EATEN)));
                int total_market = damaged + expired + rat_eaten;

                rejectionModel.setFresh_rej(total_fresh);
                rejectionModel.setMarket_rej(total_market);
                arrayList.add(rejectionModel);
                res.moveToNext();
            }
        }

        res.close();
        db.close();
        return arrayList;
    }

    /*sync rejection details*/
    public ArrayList<RejectionDetailModel> syncCompletedRejectionDetails(String route_id) {
        ArrayList<RejectionDetailModel> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + INV_STATUS + "=?",
                new String[]{Constant.STATUS_COMPLETE});

        if (res.moveToFirst()) {
            while (res.isAfterLast() == false) {
                RejectionDetailModel rejectionDetails = new RejectionDetailModel();
                rejectionDetails.setRouteManagementId(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
                rejectionDetails.setBill_no(res.getString(res.getColumnIndex(BILL_NO)));
                rejectionDetails.setInvoice_no(res.getString(res.getColumnIndex(INVOICE_NO)));
                rejectionDetails.setInvoice_date(res.getString(res.getColumnIndex(DATE)));
                rejectionDetails.setRoute_id(route_id);
                rejectionDetails.setCustomer_id(res.getString(res.getColumnIndex(CUSTOMER_ID)));
                rejectionDetails.setItem_id(res.getString(res.getColumnIndex(ITEM_ID)));

                rejectionDetails.setFresh_m_shaped(res.getInt(res.getColumnIndex(FRESH_M_SHAPED)));
                rejectionDetails.setFresh_torn_polly(res.getInt(res.getColumnIndex(FRESH_TORN_POLLY)));
                rejectionDetails.setFresh_fungus(res.getInt(res.getColumnIndex(FRESH_FUNGUS)));
                rejectionDetails.setFresh_wet_bread(res.getInt(res.getColumnIndex(FRESH_WET_BREAD)));
                rejectionDetails.setFresh_others(res.getInt(res.getColumnIndex(FRESH_OTHERS)));

                rejectionDetails.setMarket_damaged(res.getInt(res.getColumnIndex(MARKET_DAMAGED)));
                rejectionDetails.setMarket_expired(res.getInt(res.getColumnIndex(MARKET_EXPIRED)));
                rejectionDetails.setMarket_rat_eaten(res.getInt(res.getColumnIndex(MARKET_RAT_EATEN)));

                arrayList.add(rejectionDetails);
                res.moveToNext();
            }
        }

        res.close();
        db.close();
        return arrayList;
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


    // single customer rejection details
    // get product rejections over route
    public SyncInvoiceDetailModel syncCompletedRejection(String route_id, String customer_id, String item_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where " + INV_STATUS + "=? and " + CUSTOMER_ID + "=? and " + ITEM_ID + "=?",
                new String[]{Constant.STATUS_COMPLETE, customer_id, item_id});
        SyncInvoiceDetailModel rejectionModel = new SyncInvoiceDetailModel();
        if (res.moveToFirst()) {
            rejectionModel.setRoute_management_id(res.getString(res.getColumnIndex(ROUTE_MANAGEMENT_ID)));
            rejectionModel.setBill_no(res.getString(res.getColumnIndex(BILL_NO)));
            rejectionModel.setInvoice_no(res.getString(res.getColumnIndex(INVOICE_NO)));
            rejectionModel.setInvoice_date(res.getString(res.getColumnIndex(DATE)));
            rejectionModel.setRoute_id(route_id);
            rejectionModel.setCashier_code(res.getString(res.getColumnIndex(CASHIER_CODE)));

            int m_shaped = (res.getInt(res.getColumnIndex(FRESH_M_SHAPED)));
            int torn_polly = (res.getInt(res.getColumnIndex(FRESH_TORN_POLLY)));
            int fungus = (res.getInt(res.getColumnIndex(FRESH_FUNGUS)));
            int wet_bread = (res.getInt(res.getColumnIndex(FRESH_WET_BREAD)));
            int others = (res.getInt(res.getColumnIndex(FRESH_OTHERS)));
            int total_fresh = m_shaped + torn_polly + fungus + wet_bread + others;

            int damaged = (res.getInt(res.getColumnIndex(MARKET_DAMAGED)));
            int expired = (res.getInt(res.getColumnIndex(MARKET_EXPIRED)));
            int rat_eaten = (res.getInt(res.getColumnIndex(MARKET_RAT_EATEN)));
            int total_market = damaged + expired + rat_eaten;

            rejectionModel.setFresh_rej(total_fresh);
            rejectionModel.setMarket_rej(total_market);
        }

        res.close();
        db.close();
        return rejectionModel;
    }

}

