package nl.mprog.project.bieraanbiedingnotificatie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 14-1-2016.
 *
 * This class creates and maintaines two databases.
 * One with all discount information and one with all supermarket information
 * The database is query'd with SQLite
 *
 * It holds simple functions to write lists of objects to the database and read lists of objects.
 * It also has functionality to delete all entry's
 */

public class DataBaseHandler extends SQLiteOpenHelper{

    // database version
    private static final int DATABASE_VERSION = 1;
    // database name
    private static final String DATABASE_NAME = "BeerDataBase";

    private static final String TABLE_DISCOUNTS = "Discounts";
    private static final String DISCOUNT_ID = "id";
    private static final String DISCOUNT_BRAND = "brand";
    private static final String DISCOUNT_BRAND_PRINT = "brandPrint";
    private static final String DISCOUNT_FORMAT = "format";
    private static final String DISCOUNT_PRICE = "price";
    private static final String DISCOUNT_LITER_PRICE = "pricePerLiter";
    private static final String DISCOUNT_SUPERMARKT = "supermarkt";
    private static final String DISOCUNT_PERIOD = "discountPeriod";
    private static final String DISCOUNT_TYPE = "type";
    private static final String DISCOUNT_NOTIFY_FLAG = "notificationFlag";

    private static final String[] DISCOUNTS_COLUMNS = {DISCOUNT_ID, DISCOUNT_BRAND,
            DISCOUNT_BRAND_PRINT, DISCOUNT_FORMAT, DISCOUNT_PRICE, DISCOUNT_LITER_PRICE,
            DISCOUNT_SUPERMARKT, DISOCUNT_PERIOD, DISCOUNT_TYPE, DISCOUNT_NOTIFY_FLAG };

    private static final String TABLE_SUPERMARKETS = "Supermarkets";
    private static final String SUPERMARKET_ID = "id";
    private static final String SUPERMARKET_CHAIN_NAME = "chainName";
    private static final String SUPERMARKET_NAME = "individualName";
    private static final String SUPERMARKET_ADRES = "adres";
    private static final String SUPERMARKET_LATITUDE = "latitude";
    private static final String SUPERMARKET_LONGITUDE = "longitude";
    private static final String SUPERMARKET_DISTANCE = "distance";

    private static final String[] SUPERMARKET_COLUMNS = {SUPERMARKET_ID, SUPERMARKET_CHAIN_NAME,
            SUPERMARKET_NAME, SUPERMARKET_ADRES, SUPERMARKET_LATITUDE, SUPERMARKET_LONGITUDE, SUPERMARKET_DISTANCE };

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create discounts table
        String Query = "CREATE TABLE " + TABLE_DISCOUNTS +  " ( " +
                DISCOUNT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DISCOUNT_BRAND + " TEXT, " +
                DISCOUNT_BRAND_PRINT + " TEXT, " +
                DISCOUNT_FORMAT + " TEXT, " +
                DISCOUNT_PRICE + " DOUBLE, " +
                DISCOUNT_LITER_PRICE + " DOUBLE, " +
                DISCOUNT_SUPERMARKT + " TEXT, " +
                DISOCUNT_PERIOD + " TEXT, " +
                DISCOUNT_TYPE + " TEXT, " +
                DISCOUNT_NOTIFY_FLAG + " INTEGER )";
        db.execSQL(Query);

        String secondQuery = "CREATE TABLE " + TABLE_SUPERMARKETS +  " ( " +
                SUPERMARKET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                SUPERMARKET_CHAIN_NAME + " TEXT, " +
                SUPERMARKET_NAME + " TEXT, " +
                SUPERMARKET_ADRES + " TEXT, " +
                SUPERMARKET_LATITUDE + " DOUBLE, " +
                SUPERMARKET_LONGITUDE + " DOUBLE, " +
                SUPERMARKET_DISTANCE + " DOUBLE )";
        db.execSQL(secondQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // drop table if already exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISCOUNTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SUPERMARKETS);
        this.onCreate(db);
    }

    // This function takes a list of superMarket objects and saves it in the database
    // The stored superMarkets are those that are close to the user
    public void storeSuperMarkets(List<SuperMarket> superMarkets){

        // Delete all previous entry's
        deleteAllSuperMarkets();

        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();

        for ( SuperMarket superMarket : superMarkets) {

            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(SUPERMARKET_CHAIN_NAME, superMarket.chainName);
            values.put(SUPERMARKET_NAME, superMarket.individualName);
            values.put(SUPERMARKET_ADRES, superMarket.adres);
            values.put(SUPERMARKET_LATITUDE, superMarket.latitude);
            values.put(SUPERMARKET_LONGITUDE, superMarket.longitude);
            values.put(SUPERMARKET_DISTANCE, superMarket.distance);

            // insert superMarket
            db.insert(TABLE_SUPERMARKETS, null, values);
        }
        // close database transaction
        db.close();
    }

    public List<SuperMarket> getNearbySuperMarkets() {
        List<SuperMarket> superMarkets = new ArrayList<>();

        // select  query
        String query = "SELECT * FROM " + TABLE_SUPERMARKETS;

        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        SuperMarket superMarket;
        if (cursor.moveToFirst()) {
            do {
                superMarket = new SuperMarket();
                superMarket.id = Integer.parseInt(cursor.getString(0));
                superMarket.chainName = cursor.getString(1);
                superMarket.individualName = cursor.getString(2);
                superMarket.adres = cursor.getString(3);
                superMarket.latitude = cursor.getDouble(4);
                superMarket.longitude = cursor.getDouble(5);
                superMarket.distance = cursor.getDouble(6);
                // Add object to the array
                superMarkets.add(superMarket);
            } while (cursor.moveToNext());
        }
        return superMarkets;
    }

    public void deleteAllSuperMarkets(){
        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "DELETE FROM " + TABLE_SUPERMARKETS;
        db.execSQL(Query);
    }

    // This function returns all discounts in the database in a list
    public void storeDiscounts(List<DiscountObject> discountArray) {

        // Delete all previous entry's
        deleteAllDiscounts();

        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();

        for ( DiscountObject discountObject : discountArray) {

            // make values to be inserted
            ContentValues values = new ContentValues();
            values.put(DISCOUNT_BRAND, discountObject.brand);
            values.put(DISCOUNT_BRAND_PRINT, discountObject.brandPrint);
            values.put(DISCOUNT_FORMAT, discountObject.format);
            values.put(DISCOUNT_PRICE, discountObject.price);
            values.put(DISCOUNT_LITER_PRICE, discountObject.pricePerLiter);
            values.put(DISCOUNT_SUPERMARKT, discountObject.superMarkt);
            values.put(DISOCUNT_PERIOD, discountObject.discountPeriod);
            values.put(DISCOUNT_TYPE, discountObject.type);
            values.put(DISCOUNT_NOTIFY_FLAG, discountObject.notificationFlag);

            // insert Discount
            db.insert(TABLE_DISCOUNTS, null, values);
        }

        // close database transaction
        db.close();
    }


    public List<DiscountObject> getAllDiscounts() {
        List<DiscountObject> discountsArray = new ArrayList<>();

        // select book query
        String query = "SELECT * FROM " + TABLE_DISCOUNTS;

        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        DiscountObject discountObject;
        if (cursor.moveToFirst()) {
            do {
                discountObject = new DiscountObject();
                discountObject.id = Integer.parseInt(cursor.getString(0));
                discountObject.brand = cursor.getString(1);
                discountObject.brandPrint = cursor.getString(2);
                discountObject.format = cursor.getString(3);
                discountObject.price = cursor.getDouble(4);
                discountObject.pricePerLiter = cursor.getDouble(5);
                discountObject.superMarkt = cursor.getString(6);
                discountObject.discountPeriod = cursor.getString(7);
                discountObject.type = cursor.getString(8);
                discountObject.notificationFlag = cursor.getInt(9);
                // Add object to the array
                discountsArray.add(discountObject);
            } while (cursor.moveToNext());
        }
        return discountsArray;
    }

    public void deleteAllDiscounts(){
        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "DELETE FROM " + TABLE_DISCOUNTS;
        db.execSQL(Query);
    }

    public List<DiscountObject> getNotifyFlaggedDiscounts(){
        // get reference of the database
        SQLiteDatabase db = this.getReadableDatabase();

        // get player query
        Cursor cursor = db.query(TABLE_DISCOUNTS, DISCOUNTS_COLUMNS,
                " " + DISCOUNT_NOTIFY_FLAG + " = ?", new String[]{String.valueOf(1)}, null, null, null, null);

        List<DiscountObject> notifyFLaggedArray = new ArrayList<>();

        DiscountObject discountObject;
        if (cursor.moveToFirst()) {
            do {
                discountObject = new DiscountObject();
                discountObject.id = Integer.parseInt(cursor.getString(0));
                discountObject.brand = cursor.getString(1);
                discountObject.brandPrint = cursor.getString(2);
                discountObject.format = cursor.getString(3);
                discountObject.price = cursor.getDouble(4);
                discountObject.pricePerLiter = cursor.getDouble(5);
                discountObject.superMarkt = cursor.getString(6);
                discountObject.discountPeriod = cursor.getString(7);
                discountObject.type = cursor.getString(8);
                discountObject.notificationFlag = cursor.getInt(9);
                // Add object to the array
                notifyFLaggedArray.add(discountObject);
            } while (cursor.moveToNext());
        }
        return notifyFLaggedArray;
    }

    // This function sets all notify flags to 0 when the user changes the notify settings
    public void resetNotifyFlags(){
        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "UPDATE " + TABLE_DISCOUNTS +
                " SET " + DISCOUNT_NOTIFY_FLAG +
                " = " + String.valueOf(0);
        db.execSQL(Query);
    }


    // This function sets the notify flag of all the discounts from one supermarket to 1.
    public void setFlagsBySuperMarket(String superMarket){
        // get reference of the database
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "UPDATE " + TABLE_DISCOUNTS +
                " SET " + DISCOUNT_NOTIFY_FLAG +
                " = " + String.valueOf(1) +
                " WHERE " + DISCOUNT_SUPERMARKT +
                " = " + superMarket;
        db.execSQL(Query);
    }


//    public DiscountObject readDiscount(int id) {
//        // get reference of the database
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // get discount query
//        Cursor cursor = db.query(TABLE_DISCOUNTS, // a. table
//                DISCOUNTS_COLUMNS, " id = ?", new String[]{String.valueOf(id)}, null, null, null, null);
//
//        // if results !=null, parse the first one
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        DiscountObject discountObject = new DiscountObject();
//        discountObject.id = Integer.parseInt(cursor.getString(0));
//        discountObject.brand = cursor.getString(1);
//        discountObject.brandPrint = cursor.getString(2);
//        discountObject.format = cursor.getString(3);
//        discountObject.price = cursor.getFloat(4);
//        discountObject.pricePerLiter = cursor.getFloat(5);
//        discountObject.superMarkt = cursor.getString(6);
//        discountObject.discountPeriod = cursor.getString(7);
//        discountObject.type = cursor.getString(8);
//        discountObject.notificationFlag = cursor.getInt(9);
//
//        return discountObject;
//    }

//    public Player readPlayerByName(String Name) {
//        // get reference of the database
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // get player query
//        Cursor cursor = db.query(TABLE_DISCOUNTS, // a. table
//                DISCOUNTS_COLUMNS, " " + PLAYER_NAME + " = ?", new String[]{Name}, null, null, null, null);
//
//        // if results !=null, parse the first one
//        if ( cursor.moveToFirst() ) {
//            // start activity a
//            Player player = new Player();
//            player.Id = Integer.parseInt(cursor.getString(0));
//            player.Name = cursor.getString(1);
//            player.Score = Integer.parseInt(cursor.getString(2));
//            return player;
//        } else {
//            return null;
//        }
//    }


//    public int updatePlayer(Player player) {
//
//        // get reference of the BookDB database
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // make values to be inserted
//        ContentValues values = new ContentValues();
//        values.put(PLAYER_NAME, player.Name); // get Name
//        values.put(PLAYER_SCORE, player.Score); // get Score
//
//        // update
//        int i = db.update(TABLE_DISCOUNTS, values, DISCOUNT_ID + " = ?", new String[] { String.valueOf(player.Id) });
//
//        db.close();
//        return i;
//    }

//    // Deleting single player
//    public void deletePlayer(Player player) {
//        // get reference of the database
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        // delete player
//        db.delete(TABLE_DISCOUNTS, DISCOUNT_ID + " = ?", new String[] { String.valueOf(player.Id)});
//        db.close();
//    }

}

