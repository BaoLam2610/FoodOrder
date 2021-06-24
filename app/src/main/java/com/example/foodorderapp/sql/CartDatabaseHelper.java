package com.example.foodorderapp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.DetailCart;
import com.example.foodorderapp.model.DetailFavorite;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;
import com.example.foodorderapp.model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "FoodOrderCart.db";
    static final String DB_TABLE_ACCOUNT = "Account";
    static final String DB_TABLE_CART = "Cart";
    static final String DB_TABLE_RESTAURANT = "Restaurant";
    static final String DB_TABLE_FOOD = "Food";
    static final String DB_TABLE_DETAIL_CART = "DetailCart";
    static final String DB_TABLE_VOUCHER = "Voucher";
    static final String DB_TABLE_DETAIL_FAVORITE = "DetailFavorite";
    static final int DB_VERSION = 33;//32
    Context context;

    public CartDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableAccount = "CREATE TABLE Account(" +
                "phone TEXT NOT NULL PRIMARY KEY," +
                "username TEXT," +
                "password TEXT," +
                "avatar BLOB," +
                "address TEXT," +
                "status INTEGER" +
                ")";

        String createTableRestaurant = "CREATE TABLE Restaurant(" +
                "idRes TEXT NOT NULL PRIMARY KEY," +
                "resName TEXT," +
                "resProvideType TEXT," +
                "resImage TEXT," +
                "resAddress TEXT," +
                "resPhone TEXT," +
                "resEmail TEXT," +
                "resRate FLOAT," +
                "resStatus INTEGER," +
                "resFavorite INTEGER" +
                ")";

        String createTableFood = "CREATE TABLE Food(" +
                "idFood TEXT NOT NULL PRIMARY KEY," +
                "foodName TEXT," +
                "foodImage TEXT," +
                "foodPrice INTEGER," +
                "foodCategory TEXT," +
                "idRes TEXT NOT NULL," +
                "FOREIGN KEY (idRes) REFERENCES Restaurant(idRes))";

        String createTableCart = "CREATE TABLE Cart(" +
                "idCart TEXT NOT NULL PRIMARY KEY," +
                "idRes TEXT NOT NULL," +
                "amount INTEGER," +
                "cost INTEGER," +
                "date TEXT," +
                "status INTEGER," +
                "note TEXT," +
                "idVoucher TEXT," +
                "phone TEXT," +
                "FOREIGN KEY (idRes) REFERENCES Restaurant(idRes)," +
                "FOREIGN KEY (idVoucher) REFERENCES Voucher(idVoucher)," +
                "FOREIGN KEY (phone) REFERENCES Account(phone)" +
                ")";
        String createTableDetailCart = "CREATE TABLE DetailCart(" +
                "idFood TEXT NOT NULL," +
                "idCart TEXT NOT NULL," +
                "count INTEGER," +
                "status INTEGER," +
                "PRIMARY KEY(idFood,idCart)," +
                "FOREIGN KEY (idFood) REFERENCES Food(idFood)," +
                "FOREIGN KEY (idCart) REFERENCES Cart(idCart)" +
                ")";

        String createTableVoucher = "CREATE TABLE Voucher(" +
                "idVoucher TEXT NOT NULL," +
                "name TEXT," +
                "discount INTEGER" +
                ")";

        String createTableDetailFavorite = "CREATE TABLE DetailFavorite(" +
                "phone TEXT," +
                "idRes TEXT," +
                "PRIMARY KEY(phone,idRes)," +
                "FOREIGN KEY (phone) REFERENCES Account(phone)," +
                "FOREIGN KEY (idRes) REFERENCES Restaurant(idRes)" +
                ")";

        db.execSQL(createTableVoucher);
        db.execSQL(createTableAccount);
        db.execSQL(createTableRestaurant);
        db.execSQL(createTableFood);
        db.execSQL(createTableCart);
        db.execSQL(createTableDetailCart);
        db.execSQL(createTableDetailFavorite);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_RESTAURANT);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_FOOD);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_DETAIL_CART);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_DETAIL_FAVORITE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_VOUCHER);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ACCOUNT);
            onCreate(db);
        }
    }

    public void insertAccount(UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.ACCOUNT_PHONE, userAccount.getPhone());
        values.put(SqlTextHelper.ACCOUNT_USERNAME, userAccount.getUsername());
        values.put(SqlTextHelper.ACCOUNT_PASSWORD, userAccount.getPassword());
        values.put(SqlTextHelper.ACCOUNT_AVATAR, userAccount.getAvatar());
        values.put(SqlTextHelper.ACCOUNT_ADDRESS, userAccount.getAddress());
        values.put(SqlTextHelper.ACCOUNT_STATUS, userAccount.getStatus());
        sql.insert(DB_TABLE_ACCOUNT, null, values);
    }


    public void insertRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.RESTAURANT_ID, restaurant.getId());
        values.put(SqlTextHelper.RESTAURANT_NAME, restaurant.getName());
        values.put(SqlTextHelper.RESTAURANT_PROVIDE, restaurant.getProvideType());
        values.put(SqlTextHelper.RESTAURANT_IMAGE, restaurant.getImage());
        values.put(SqlTextHelper.RESTAURANT_ADDRESS, restaurant.getAddress());
        values.put(SqlTextHelper.RESTAURANT_PHONE, restaurant.getPhone());
        values.put(SqlTextHelper.RESTAURANT_EMAIL, restaurant.getEmail());
        values.put(SqlTextHelper.RESTAURANT_RATE, restaurant.getRate());
        values.put(SqlTextHelper.RESTAURANT_STATUS, 0);
        values.put(SqlTextHelper.RESTAURANT_FAVORITE, 0);
        sql.insert(DB_TABLE_RESTAURANT, null, values);
    }


    public void insertFood(Food food) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.FOOD_ID, food.getId());
        values.put(SqlTextHelper.FOOD_NAME, food.getName());
        values.put(SqlTextHelper.FOOD_IMAGE, food.getImage());
//        values.put(SqlTextHelper.FOOD_COUNT, food.getCount());
        values.put(SqlTextHelper.FOOD_PRICE, food.getPrice());
        values.put(SqlTextHelper.FOOD_CATEGORY, food.getCategory());
        values.put(SqlTextHelper.RESTAURANT_ID, food.getRestaurant().getId());
//        values.put(SqlTextHelper.CART_ID, food.getCart());
        sql.insert(DB_TABLE_FOOD, null, values);
    }


    public void insertCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.CART_ID, cart.getIdCart());
        values.put(SqlTextHelper.RESTAURANT_ID, cart.getRestaurant().getId());
        values.put(SqlTextHelper.CART_TOTAL_AMOUNT, cart.getAmount());
        values.put(SqlTextHelper.CART_TOTAL_PRICE, cart.getTotalPrice());
        values.put(SqlTextHelper.CART_STATUS, cart.getStatus());
        values.put(SqlTextHelper.VOUCHER_ID, (String) null);
        sql.insert(DB_TABLE_CART, null, values);
    }

    public void insertDetailCart(DetailCart detailCart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.CART_ID, detailCart.getCart().getIdCart());
        values.put(SqlTextHelper.FOOD_ID, detailCart.getFood().getId());
        values.put(SqlTextHelper.DETAIL_CART_COUNT, detailCart.getCount());
        values.put(SqlTextHelper.DETAIL_CART_STATUS, detailCart.getStatus());
        sql.insert(DB_TABLE_DETAIL_CART, null, values);
    }

    public void insertVoucher(Voucher voucher) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.VOUCHER_ID, voucher.getId());
        values.put(SqlTextHelper.VOUCHER_NAME, voucher.getName());
        values.put(SqlTextHelper.VOUCHER_DISCOUNT, voucher.getDiscount());
        sql.insert(DB_TABLE_VOUCHER, null, values);
    }

    public void deleteAccount(UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "phone=?";
        sql.delete(DB_TABLE_ACCOUNT, whereClause, new String[]{userAccount.getPhone()});
    }


    public void deleteRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idRes=? and status=0";
        sql.delete(DB_TABLE_RESTAURANT, whereClause, new String[]{restaurant.getId()});
    }

    public void deleteFavoriteFood(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idFood NOT IN (SELECT idFood FROM DetailCart) AND idRes = ?";
        sql.delete(DB_TABLE_FOOD, whereClause, new String[]{restaurant.getId()});
    }

    public void deleteFood(Food food, Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idFood=?";
        sql.delete(DB_TABLE_FOOD, whereClause, new String[]{food.getId()});
//
        String whereClause1 = "idCart=?";
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.CART_ID, cart.getIdCart());
        values.put(SqlTextHelper.RESTAURANT_ID, cart.getRestaurant().getId());
        values.put(SqlTextHelper.CART_TOTAL_AMOUNT, cart.getAmount());
        values.put(SqlTextHelper.CART_TOTAL_PRICE, cart.getTotalPrice());
        sql.update(DB_TABLE_CART, values, whereClause1, new String[]{cart.getIdCart()});
    }

    public void deleteAllRestaurant() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_RESTAURANT + " WHERE resStatus=0" +
                " AND idRes not in (SELECT idRes FROM DetailFavorite)");
    }

    public void deleteFavorite(){
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_RESTAURANT + " WHERE resFavorite=0");
    }

    public void deleteAllFood() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_FOOD);
    }

    public void deleteAllDetailCart() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_DETAIL_CART + " WHERE status=0");
    }

    public void deleteDetailCart(DetailCart detailCart) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idFood=? and idCart=?";
        sql.delete(DB_TABLE_DETAIL_CART, whereClause, new String[]{detailCart.getFood().getId(), detailCart.getCart().getIdCart()});
    }

    public void deleteCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idCart=? and status=0";
        sql.delete(DB_TABLE_CART, whereClause, new String[]{cart.getIdCart()});
    }

    public void deleteAllCart() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_CART + " WHERE status=0");
    }

    public void deleteAllCartEmpty() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM Cart WHERE status=0");
    }

    public UserAccount checkStatusAccount() {
        UserAccount user = null;

        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Account WHERE status=1", null);
        if (cursor.moveToNext()) {
            String phone = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_PHONE));
            String username = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_USERNAME));
            String password = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_PASSWORD));
            byte[] avatar = cursor.getBlob(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_AVATAR));
            String address = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_ADDRESS));
            user = new UserAccount(phone, username, password, avatar, address, 1);
        }

        return user;
    }


    public boolean checkAccount(String phone, String password) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Account WHERE phone=? AND password=?", new String[]{phone, password});
        return cursor.moveToNext();
    }

    public void setStatusAccount(String phone) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.ACCOUNT_PHONE, phone);
        values.put(SqlTextHelper.ACCOUNT_STATUS, 1);
        String whereClause = "phone=?";
        sql.update(DB_TABLE_ACCOUNT, values, whereClause, new String[]{phone});
    }

    public boolean findAccount(String phone) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Account WHERE phone=?", new String[]{phone});
        return cursor.moveToNext();
    }

    public UserAccount getAccount(String phone) {
        UserAccount user = null;
        String username, password, address;
        byte[] avatar;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Account WHERE phone=?", new String[]{phone});
        if (cursor.moveToNext()) {
            username = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_USERNAME));
            password = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_PASSWORD));
            avatar = cursor.getBlob(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_AVATAR));
            address = cursor.getString(cursor.getColumnIndex(SqlTextHelper.ACCOUNT_ADDRESS));
            user = new UserAccount(phone, username, password, avatar, address, 0);
        }
        return user;
    }
    public boolean findDetailFavorite(DetailFavorite detailFavorite){
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM DetailFavorite WHERE idRes=? and phone=?",
                new String[]{detailFavorite.getRestaurant().getId(),detailFavorite.getUserAccount().getPhone()});
        return cursor.moveToNext();
    }

    public boolean findFavoriteRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Restaurant WHERE idRes=? and resFavorite=1", new String[]{restaurant.getId()});
        return cursor.moveToNext();
    }

    public boolean findRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Restaurant WHERE idRes=?", new String[]{restaurant.getId()});
        return cursor.moveToNext();
    }

    public boolean findFoodList(Food food) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM FoodList WHERE id=?", new String[]{food.getId()});
        return cursor.moveToNext();
    }

    public boolean findFood(Food food) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Food WHERE idFood=?", new String[]{food.getId()});
        return cursor.moveToNext();
    }

    public boolean findCart(Cart cart) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Cart WHERE idCart=?", new String[]{cart.getIdCart()});
        return cursor.moveToNext();
    }

    public void logoutAccount(UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.ACCOUNT_STATUS, 0);
        String whereClause = "phone=?";
        sql.update(DB_TABLE_ACCOUNT, values, whereClause, new String[]{userAccount.getPhone()});
    }

    public void updateAccount(UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.ACCOUNT_PHONE, userAccount.getPhone());
        values.put(SqlTextHelper.ACCOUNT_USERNAME, userAccount.getUsername());
        values.put(SqlTextHelper.ACCOUNT_AVATAR, userAccount.getAvatar());
        values.put(SqlTextHelper.ACCOUNT_ADDRESS, userAccount.getAddress());
//        values.put(SqlTextHelper.ACCOUNT_STATUS, userAccount.getStatus());
        String whereClause = "phone=?";
        sql.update(DB_TABLE_ACCOUNT, values, whereClause, new String[]{userAccount.getPhone()});
    }

    public void deleteDetailFavorite(DetailFavorite detailFavorite) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idRes=? and phone=?";
        sql.delete(DB_TABLE_DETAIL_FAVORITE, whereClause, new String[]{detailFavorite.getRestaurant().getId(),
                detailFavorite.getUserAccount().getPhone()});
    }

    public void deleteFavoriteRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.RESTAURANT_FAVORITE, 0);
        String whereClause = "idRes=?";
        sql.update(DB_TABLE_RESTAURANT, values, whereClause, new String[]{restaurant.getId()});
    }

    public void setDetailFavorite(DetailFavorite detailFavorite) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.RESTAURANT_ID, detailFavorite.getRestaurant().getId());
        values.put(SqlTextHelper.ACCOUNT_PHONE, detailFavorite.getUserAccount().getPhone());
        sql.insert(DB_TABLE_DETAIL_FAVORITE, null, values);
    }

    public void setFavoriteRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SqlTextHelper.RESTAURANT_STATUS, 1);
        values.put(SqlTextHelper.RESTAURANT_FAVORITE, 1);
        String whereClause = "idRes=?";

        sql.update(DB_TABLE_RESTAURANT, values, whereClause, new String[]{restaurant.getId()});

    }

    public void setStatusRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.RESTAURANT_STATUS, 1);
        String whereClause = "idRes=?";
        sql.update(DB_TABLE_RESTAURANT, values, whereClause, new String[]{restaurant.getId()});

    }

    public void updateFood(Food food) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.FOOD_ID, food.getId());
        values.put(SqlTextHelper.FOOD_NAME, food.getName());
        values.put(SqlTextHelper.FOOD_IMAGE, food.getImage());
        values.put(SqlTextHelper.FOOD_PRICE, food.getPrice());
        values.put(SqlTextHelper.FOOD_CATEGORY, food.getCategory());
//        values.put(SqlTextHelper.RESTAURANT_ID, food.getIdRes());
        String whereClause = "idFood=?";
        sql.update(DB_TABLE_FOOD, values, whereClause, new String[]{food.getId()});
    }

    public void setStatusDetailCart(DetailCart detailCart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.DETAIL_CART_STATUS, 1);
        String whereClause = "idCart=?";
        sql.update(DB_TABLE_DETAIL_CART, values, whereClause, new String[]{detailCart.getCart().getIdCart()});
    }

    public void updateDetailCart(DetailCart detailCart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.DETAIL_CART_COUNT, detailCart.getCount());
        String whereClause = "idFood=? and idCart=?";
        sql.update(DB_TABLE_DETAIL_CART, values, whereClause, new String[]{detailCart.getFood().getId(), detailCart.getCart().getIdCart()});
    }

    public void updateCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.CART_ID, cart.getIdCart());
        values.put(SqlTextHelper.RESTAURANT_ID, cart.getRestaurant().getId());
        values.put(SqlTextHelper.CART_TOTAL_AMOUNT, cart.getAmount());
        values.put(SqlTextHelper.CART_TOTAL_PRICE, cart.getTotalPrice());
        values.put(SqlTextHelper.CART_DATE, cart.getDate());
        values.put(SqlTextHelper.CART_NOTE, cart.getNote());
        Voucher voucher = cart.getVoucher();
        if (voucher != null)
            values.put(SqlTextHelper.VOUCHER_ID, cart.getVoucher().getId());
        else
            values.put(SqlTextHelper.VOUCHER_ID, (String) null);
        String whereClause = "idCart=?";
        sql.update(DB_TABLE_CART, values, whereClause, new String[]{cart.getIdCart()});
    }

    public void setDateCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.CART_DATE, cart.getDate());
        String whereClause = "idCart=? and idRes=?";
        sql.update(DB_TABLE_CART, values, whereClause, new String[]{cart.getIdCart(), cart.getRestaurant().getId()});
    }

    public void setStatusCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.CART_STATUS, 1);
        String whereClause = "idCart=? and idRes=?";
        sql.update(DB_TABLE_CART, values, whereClause, new String[]{cart.getIdCart(), cart.getRestaurant().getId()});
    }

    public void setAccountOrder(Cart cart, UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SqlTextHelper.ACCOUNT_PHONE, userAccount.getPhone());
        String whereClause = "idCart=?";
        sql.update(DB_TABLE_CART, values, whereClause, new String[]{cart.getIdCart()});
    }

    public Restaurant getRestaurant(Cart cart) {
        Restaurant restaurant = null;
        String idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail;
        double resRate;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorRestaurant = sql.rawQuery("SELECT * FROM Cart c INNER JOIN Restaurant r ON c.idRes = r.idRes" +
                " WHERE idCart=?", new String[]{cart.getIdCart()});
        if (cursorRestaurant.moveToNext()) {
            idRes = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            resName = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_NAME));
            resProvideType = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PROVIDE));
            resImage = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_IMAGE));
            resAddress = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ADDRESS));
            resPhone = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PHONE));
            resEmail = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_EMAIL));
            resRate = cursorRestaurant.getDouble(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_RATE));
            restaurant = new Restaurant(idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail, resRate);
//            String id, String name, String provideType, String image, String address, String phone, String email, double rate
        }
        cursorRestaurant.close();
        return restaurant;
    }


    public Restaurant getRestaurant() {
        Restaurant restaurant = null;
        String idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail;
        double resRate;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorRestaurant = sql.query(false, DB_TABLE_RESTAURANT, null, null, null, null, null, null, null);
        if (cursorRestaurant.moveToNext()) {
            idRes = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            resName = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_NAME));
            resProvideType = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PROVIDE));
            resImage = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_IMAGE));
            resAddress = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ADDRESS));
            resPhone = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PHONE));
            resEmail = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_EMAIL));
            resRate = cursorRestaurant.getDouble(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_RATE));
            restaurant = new Restaurant(idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail, resRate);
//            String id, String name, String provideType, String image, String address, String phone, String email, double rate
        }
        cursorRestaurant.close();
        return restaurant;
    }

    public Restaurant findRestaurant(String idRes) {
        Restaurant restaurant = null;
        String resName, resProvideType, resImage, resAddress, resPhone, resEmail;
        double resRate;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorRestaurant = sql.rawQuery("SELECT * FROM Restaurant WHERE idRes=?", new String[]{idRes});
        if (cursorRestaurant.moveToNext()) {
            resName = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_NAME));
            resProvideType = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PROVIDE));
            resImage = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_IMAGE));
            resAddress = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ADDRESS));
            resPhone = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PHONE));
            resEmail = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_EMAIL));
            resRate = cursorRestaurant.getDouble(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_RATE));
            restaurant = new Restaurant(idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail, resRate);
        }

        return restaurant;
    }

    public boolean findVoucher(Voucher voucher) {
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM Voucher WHERE idVoucher=?", new String[]{voucher.getId()});
        return cursor.moveToNext();
    }

    public Voucher getVoucher(Cart cart) {
        Voucher voucher = null;
        String id, name;

        int discount;

        SQLiteDatabase sql = getReadableDatabase();
//        if(cart.getVoucher() == null)
//            return null;
        Cursor cursor = sql.rawQuery("SELECT * FROM Voucher v INNER JOIN Cart c ON v.idVoucher = c.idVoucher " +
                "WHERE idCart=?", new String[]{cart.getIdCart()});
        if (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex(SqlTextHelper.VOUCHER_ID));
            name = cursor.getString(cursor.getColumnIndex(SqlTextHelper.VOUCHER_NAME));

            discount = cursor.getInt(cursor.getColumnIndex(SqlTextHelper.VOUCHER_DISCOUNT));
            voucher = new Voucher(id, name, discount);
        }
        return voucher;
    }

    public DetailCart findDetailCart(Food food, Cart cart) {
        DetailCart detailCart = null;
        int count;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursor = sql.rawQuery("SELECT * FROM DetailCart WHERE idFood=? and idCart=?", new String[]{food.getId(), cart.getIdCart()});
        if (cursor.moveToNext()) {
            count = cursor.getInt(cursor.getColumnIndex(SqlTextHelper.DETAIL_CART_COUNT));
            detailCart = new DetailCart(food, cart);
            detailCart.setCount(count);
        }
        return detailCart;
    }

    public Cart findCart(String idCart) {
        Cart cart = null;
        String idRes;
        int totalAmount;
        long totalPrice;
        int status;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorCart = sql.rawQuery("SELECT * FROM Cart WHERE idCart=?", new String[]{idCart});
        if (cursorCart.moveToNext()) {
            idRes = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            totalAmount = cursorCart.getInt(cursorCart.getColumnIndex(SqlTextHelper.CART_TOTAL_AMOUNT));
            totalPrice = cursorCart.getLong(cursorCart.getColumnIndex(SqlTextHelper.CART_TOTAL_PRICE));
            status = cursorCart.getInt(cursorCart.getColumnIndex(SqlTextHelper.CART_STATUS));
            Restaurant restaurant = findRestaurant(idRes);
            cart = new Cart(idCart, restaurant, totalAmount, totalPrice, status);
        }
        return cart;
    }


    public ArrayList<Food> getFood(Cart cart) {
        Food food;
        String idFood, idRes, foodName, foodImage, foodCategory;
        int foodCount;
        long foodPrice;
        ArrayList<Food> foodList = new ArrayList<>();
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorFood = sql.rawQuery("SELECT * FROM Food f INNER JOIN DetailCart d ON f.idFood = d.idFood " +
                "WHERE idCart=?", new String[]{cart.getIdCart()});
//        Cursor cursorFood = sql.query(false, DB_TABLE_FOOD, null, null, null, null, null, null, null);
        while (cursorFood.moveToNext()) {
            idFood = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_ID));
            foodName = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_NAME));
            foodImage = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_IMAGE));
            foodCategory = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_CATEGORY));
            foodPrice = cursorFood.getLong(cursorFood.getColumnIndex(SqlTextHelper.FOOD_PRICE));
            idRes = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.RESTAURANT_ID));

            Restaurant restaurant = findRestaurant(idRes);
//            Cart cart = findCart(idCart);
            food = new Food(idFood, foodName, foodImage, foodPrice, foodCategory, restaurant);
//            String id, String name, String image, long price, String category, int count, Restaurant restaurant
//            food.setCart(idCart);
            foodList.add(food);

        }
        cursorFood.close();
        return foodList;
    }

    public List<Food> getListFavoriteFood(Restaurant restaurant) {
        Food food;
        String idFood, idRes, foodName, foodImage, foodCategory;
        int foodCount;
        long foodPrice;
        ArrayList<Food> foodList = new ArrayList<>();
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorFood = sql.rawQuery("SELECT * FROM Food WHERE idRes=?", new String[]{restaurant.getId()});
        while (cursorFood.moveToNext()) {
            idFood = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_ID));
            foodName = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_NAME));
            foodImage = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_IMAGE));
            foodCategory = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_CATEGORY));
            foodPrice = cursorFood.getLong(cursorFood.getColumnIndex(SqlTextHelper.FOOD_PRICE));
//            Cart cart = findCart(idCart);
            food = new Food(idFood, foodName, foodImage, foodPrice, foodCategory, restaurant);
//            String id, String name, String image, long price, String category, int count, Restaurant restaurant
//            food.setCart(idCart);
            foodList.add(food);

        }
        cursorFood.close();
        return foodList;
    }

    public List<Restaurant> getListDetailFavorite(DetailFavorite detailFavorite) {
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant;
        String idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail;
        double resRate;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorRestaurant = sql.rawQuery("SELECT * FROM DetailFavorite d INNER JOIN Restaurant r ON d.idRes = r.idRes " +
                "INNER JOIN Account a ON d.phone = a.phone " +
                        "WHERE d.phone=?",
                new String[]{detailFavorite.getUserAccount().getPhone()});
        String s = detailFavorite.getUserAccount().getPhone();
        System.out.println(s);
        while (cursorRestaurant.moveToNext()) {
            idRes = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            resName = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_NAME));
            resProvideType = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PROVIDE));
            resImage = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_IMAGE));
            resAddress = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ADDRESS));
            resPhone = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PHONE));
            resEmail = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_EMAIL));
            resRate = cursorRestaurant.getDouble(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_RATE));
            restaurant = new Restaurant(idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail, resRate);
//            String id, String name, String provideType, String image, String address, String phone, String email, double rate
            restaurantList.add(restaurant);
        }
        cursorRestaurant.close();
        return restaurantList;
    }

    public List<Restaurant> getListFavoriteRestaurant() {
        List<Restaurant> restaurantList = new ArrayList<>();
        Restaurant restaurant;
        String idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail;
        double resRate;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorRestaurant = sql.rawQuery("SELECT * FROM Restaurant WHERE resFavorite=1", null);
        while (cursorRestaurant.moveToNext()) {
            idRes = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            resName = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_NAME));
            resProvideType = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PROVIDE));
            resImage = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_IMAGE));
            resAddress = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_ADDRESS));
            resPhone = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_PHONE));
            resEmail = cursorRestaurant.getString(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_EMAIL));
            resRate = cursorRestaurant.getDouble(cursorRestaurant.getColumnIndex(SqlTextHelper.RESTAURANT_RATE));
            restaurant = new Restaurant(idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail, resRate);
//            String id, String name, String provideType, String image, String address, String phone, String email, double rate
            restaurantList.add(restaurant);
        }
        cursorRestaurant.close();
        return restaurantList;
    }

    public List<Cart> getMyOrderList() {
        List<Cart> cartList = new ArrayList<>();
        Cart cart;
        String idCart, idRes;
        int amount;
        long cost;
        int status = 0;
        String date;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorCart = sql.rawQuery("SELECT * FROM Cart c INNER JOIN Account a ON c.phone = a.phone" +
                " WHERE c.status=1 AND a.status = 1", null);
        while (cursorCart.moveToNext()) {
            idCart = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.CART_ID));
            idRes = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            amount = cursorCart.getInt(cursorCart.getColumnIndex(SqlTextHelper.CART_TOTAL_AMOUNT));
            cost = cursorCart.getLong(cursorCart.getColumnIndex(SqlTextHelper.CART_TOTAL_PRICE));
            date = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.CART_DATE));
            status = cursorCart.getInt(cursorCart.getColumnIndex(SqlTextHelper.CART_STATUS));
            Restaurant restaurant = findRestaurant(idRes);

            cart = new Cart(idCart, restaurant, amount, cost, status);
            cart.setDate(date);

            List<Food> foodList = getMyOrderListFood(cart);
            cart.getRestaurant().setFoodList((ArrayList<Food>) foodList);

            cartList.add(cart);
        }
        return cartList;
    }

    public List<Food> getMyOrderListFood(Cart cart) {

        ArrayList<Food> foodList = new ArrayList<>();
        Food food;
        String idFood, foodName, foodImage, foodCategory;
        int foodCount;
        long foodPrice;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorFood = sql.rawQuery("SELECT * FROM Food f INNER JOIN DetailCart d ON f.idFood = d.idFood " +
                "WHERE idCart=?", new String[]{cart.getIdCart()});
        while (cursorFood.moveToNext()) {
            idFood = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_ID));
            foodName = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.FOOD_NAME));

            foodPrice = cursorFood.getLong(cursorFood.getColumnIndex(SqlTextHelper.FOOD_PRICE));
//            idRes = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
//            idCart = cursorFood.getString(cursorFood.getColumnIndex(SqlTextHelper.CART_ID));
//            Restaurant restaurant = findRestaurant(idRes);
//            Cart cart = findCart(idCart);
            food = new Food(idFood, foodName, null, foodPrice, null, null);
//            String id, String name, String image, long price, String category, int count, Restaurant restaurant

            foodList.add(food);

        }
        cursorFood.close();
        return foodList;
    }

    public Cart getCart() {
        Cart cart = null;


        SQLiteDatabase sql = getReadableDatabase();
        String idCart, idRes;
        int amount;
        long cost;
        int status;
        String date;

        Cursor cursorCart = sql.query(false, DB_TABLE_CART, null, null, null, null, null, null, null);
        if (cursorCart.moveToNext()) {
            idCart = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.CART_ID));
            idRes = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.RESTAURANT_ID));
            amount = cursorCart.getInt(cursorCart.getColumnIndex(SqlTextHelper.CART_TOTAL_AMOUNT));
            cost = cursorCart.getLong(cursorCart.getColumnIndex(SqlTextHelper.CART_TOTAL_PRICE));
            date = cursorCart.getString(cursorCart.getColumnIndex(SqlTextHelper.CART_DATE));
            status = cursorCart.getInt(cursorCart.getColumnIndex(SqlTextHelper.CART_STATUS));
            Restaurant restaurant = findRestaurant(idRes);
            cart = new Cart(idCart, restaurant, amount, cost, status);
            cart.setDate(date);
        }

//        cart.setRestaurant(restaurant);
//        cart.getRestaurant().setFoodList(foodList);
        cursorCart.close();
        return cart;

    }


}
