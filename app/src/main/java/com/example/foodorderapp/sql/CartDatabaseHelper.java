package com.example.foodorderapp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.foodorderapp.model.Cart;
import com.example.foodorderapp.model.Food;
import com.example.foodorderapp.model.Restaurant;
import com.example.foodorderapp.model.UserAccount;

import java.util.ArrayList;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "FoodOrderCart.db";
    static final String DB_TABLE_ACCOUNT = "Account";
    static final String DB_TABLE_CART = "Cart";
    static final String DB_TABLE_RESTAURANT = "Restaurant";
    static final String DB_TABLE_FOOD = "Food";
    static final String DB_TABLE_FOOD_LIST = "FoodList";
    static final int DB_VERSION = 1;

    public CartDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableRestaurant = "CREATE TABLE Restaurant(" +
                "idRes TEXT NOT NULL PRIMARY KEY," +
                "resName TEXT," +
                "resProvideType TEXT," +
                "resImage TEXT," +
                "resAddress TEXT," +
                "resPhone TEXT," +
                "resEmail TEXT," +
                "resRate FLOAT)";
        String createTableFood = "CREATE TABLE Food(" +
                "idFood TEXT NOT NULL PRIMARY KEY," +
                "foodName TEXT," +
                "foodImage TEXT," +
                "foodPrice INTEGER," +
                "foodCount INTEGER," +
                "foodCategory TEXT," +
                "idRes TEXT NOT NULL," +
                "FOREIGN KEY (idRes) REFERENCES Restaurant(idRes))";
        String createTableCart = "CREATE TABLE Cart(" +
                "idCart TEXT NOT NULL PRIMARY KEY," +
                "idRes TEXT NOT NULL," +
                "amount INTEGER," +
                "cost INTEGER," +
                "FOREIGN KEY (idRes) REFERENCES Restaurant(idRes))";

//        String createFoodList = "CREATE TABLE FoodList(" +
//                "id TEXT NOT NULL PRIMARY KEY," +
//                "foodName TEXT," +
//                "foodImage TEXT," +
//                "foodPrice INTEGER," +
//                "foodCount INTEGER," +
//                "foodCategory TEXT)";
        String createAccount = "CREATE TABLE Account(" +
                "phone TEXT NOT NULL PRIMARY KEY," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL," +
                "avatar TEXT," +
                "address TEXT," +
                "money INTEGER)";
        db.execSQL(createTableRestaurant);
        db.execSQL(createTableFood);
        db.execSQL(createTableCart);
//        db.execSQL(createAccount);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion != oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_RESTAURANT);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_FOOD);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ACCOUNT);
            onCreate(db);
        }
    }

    public void insertAccount(String phone, String username, String password, String avatar, String address, long money) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", phone);
        values.put("username", username);
        values.put("password", password);
        values.put("avatar", avatar);
        values.put("address", address);
        values.put("money", money);
        sql.insert(DB_TABLE_ACCOUNT, null, values);
    }

    public void insertRestaurant(String id, String name, String provideType, String image, String address, String phone, String email, double rate) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idRes", id);
        values.put("resName", name);
        values.put("resProvideType", provideType);
        values.put("resImage", image);
        values.put("resAddress", address);
        values.put("resPhone", phone);
        values.put("resEmail", email);
        values.put("resRate", rate);
        sql.insert(DB_TABLE_RESTAURANT, null, values);
    }

    public void insertFood(String id, String name, String image, int count, long price, String category, String idRes) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idFood", id);
        values.put("foodName", name);
        values.put("foodImage", image);
        values.put("foodCount", count);
        values.put("foodPrice", price);
        values.put("foodCategory", category);
        values.put("idRes", idRes);
        sql.insert(DB_TABLE_FOOD, null, values);
    }

    public void insertCart(String idCart, String idRes, int amount, int cost) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idCart", idCart);
        values.put("idRes", idRes);
        values.put("amount", amount);
        values.put("cost", cost);
        sql.insert(DB_TABLE_CART, null, values);
    }

    public void insertFoodList(String id, String name, String image, int count, long price, String category) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id", id);
        values.put("foodName", name);
        values.put("foodImage", image);
        values.put("foodCount", count);
        values.put("foodPrice", price);
        values.put("foodCategory", category);
        sql.insert(DB_TABLE_FOOD_LIST, null, values);
    }

//    public void dropAllTable(){
//        SQLiteDatabase sql = getWritableDatabase();
//        sql.execSQL("DROP TABLE " + DB_TABLE_RESTAURANT);
//        sql.execSQL("DROP TABLE " + DB_TABLE_FOOD);
//        sql.execSQL("DROP TABLE " + DB_TABLE_CART);
//    }

    public void deleteAccount(UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "phone=?";
        sql.delete(DB_TABLE_ACCOUNT, whereClause, new String[]{userAccount.getPhone()});
    }

    public void deleteRestaurant(Restaurant restaurant) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idRes=?";
        sql.delete(DB_TABLE_RESTAURANT, whereClause, new String[]{restaurant.getId()});
    }

    public void deleteFood(Food food, Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idFood=?";
        sql.delete(DB_TABLE_FOOD, whereClause, new String[]{food.getId()});
//        String query = "UPDATE Cart SET amount = amount - Food.foodCount, cost = cost - (Food.foodPrice * Food.foodCount)" +
//                "WHERE CART.idRes = Food.idRes AND idFood=?";
//        Cursor cursor = sql.rawQuery(query,new String[]{food.getId()});
        String whereClause1 = "idCart=?";
        ContentValues values = new ContentValues();
        values.put("idCart", cart.getIdCart());
        values.put("idRes", cart.getIdRes());
        values.put("amount", cart.getAmount());
        values.put("cost", cart.getTotalPrice());
        sql.update(DB_TABLE_CART, values, whereClause1, new String[]{cart.getIdCart()});
    }

    public void deleteAllFood() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_FOOD);
    }

    public void deleteAllFoodList() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_FOOD_LIST);
    }

    public void deleteCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        String whereClause = "idCart=?";
        sql.delete(DB_TABLE_CART, whereClause, new String[]{cart.getIdCart()});
    }

    public void deleteAllCart() {
        SQLiteDatabase sql = getWritableDatabase();
        sql.execSQL("DELETE FROM " + DB_TABLE_CART);
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

    public void updateAccount(UserAccount userAccount) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("phone", userAccount.getPhone());
        values.put("username", userAccount.getUsername());
        values.put("password", userAccount.getPassword());
        values.put("avatar", userAccount.getAvatar());
        values.put("address", userAccount.getAddress());
        values.put("balance", userAccount.getMoney());
        String whereClause = "phone=?";
        sql.update(DB_TABLE_ACCOUNT, values, whereClause, new String[]{userAccount.getPhone()});
    }

//    public void updateFoodList(Food food){
//        SQLiteDatabase sql = getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put("id", food.getId());
//        values.put("foodName", food.getName());
//        values.put("foodImage", food.getImage());
//        values.put("foodCount", food.getCount());
//        values.put("foodPrice", food.getPrice());
//        values.put("foodCategory", food.getCategory());
//        String whereClause = "id=?";
//        sql.update(DB_TABLE_FOOD_LIST, values, whereClause, new String[]{food.getId()});
//    }

    public void updateFood(Food food) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idFood", food.getId());
        values.put("foodName", food.getName());
        values.put("foodImage", food.getImage());
        values.put("foodCount", food.getCount());
        values.put("foodPrice", food.getPrice());
        values.put("foodCategory", food.getCategory());
//        values.put("idRes", food.getIdRes());
        String whereClause = "idFood=?";
        sql.update(DB_TABLE_FOOD, values, whereClause, new String[]{food.getId()});
    }

    public void updateCart(Cart cart) {
        SQLiteDatabase sql = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idCart", cart.getIdCart());
        values.put("idRes", cart.getIdRes());
        values.put("amount", cart.getAmount());
        values.put("cost", cart.getTotalPrice());
        String whereClause = "idCart=? AND idRes=?";
        sql.update(DB_TABLE_CART, values, whereClause, new String[]{cart.getIdCart(), cart.getIdRes()});
    }

    public UserAccount getAccount() {
        UserAccount account = null;
        String phone, username, password, avatar, address;
        long money;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorAccount = sql.query(false, DB_TABLE_ACCOUNT, null, null, null, null, null, null, null);
        if (cursorAccount.moveToNext()) {
            phone = cursorAccount.getString(cursorAccount.getColumnIndex("phone"));
            username = cursorAccount.getString(cursorAccount.getColumnIndex("username"));
            password = cursorAccount.getString(cursorAccount.getColumnIndex("password"));
            avatar = cursorAccount.getString(cursorAccount.getColumnIndex("avatar"));
            address = cursorAccount.getString(cursorAccount.getColumnIndex("address"));
            money = cursorAccount.getLong(cursorAccount.getColumnIndex("money"));
            account = new UserAccount(phone, username, password, avatar, address, money);
        }

        return account;
    }

    public Restaurant getRestaurant() {
        Restaurant restaurant = null;
        String idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail;
        double resRate;
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorRestaurant = sql.query(false, DB_TABLE_RESTAURANT, null, null, null, null, null, null, null);
        if (cursorRestaurant.moveToNext()) {
            idRes = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("idRes"));
            resName = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("resName"));
            resProvideType = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("resProvideType"));
            resImage = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("resImage"));
            resAddress = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("resAddress"));
            resPhone = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("resPhone"));
            resEmail = cursorRestaurant.getString(cursorRestaurant.getColumnIndex("resEmail"));
            resRate = cursorRestaurant.getDouble(cursorRestaurant.getColumnIndex("resRate"));
            restaurant = new Restaurant(idRes, resName, resProvideType, resImage, resAddress, resPhone, resEmail, resRate);
        }
        cursorRestaurant.close();
        return restaurant;
    }

    public ArrayList<Food> getListFood() {
        Food food;
        String idFood, idRes, foodName, foodImage, foodCategory;
        int foodCount;
        long foodPrice;
        ArrayList<Food> foodList = new ArrayList<>();
        SQLiteDatabase sql = getReadableDatabase();
        Cursor cursorFood = sql.query(false, DB_TABLE_FOOD, null, null, null, null, null, null, null);
        while (cursorFood.moveToNext()) {
            idFood = cursorFood.getString(cursorFood.getColumnIndex("idFood"));
            foodName = cursorFood.getString(cursorFood.getColumnIndex("foodName"));
            foodImage = cursorFood.getString(cursorFood.getColumnIndex("foodImage"));
            foodCategory = cursorFood.getString(cursorFood.getColumnIndex("foodCategory"));
            foodCount = cursorFood.getInt(cursorFood.getColumnIndex("foodCount"));
            foodPrice = cursorFood.getLong(cursorFood.getColumnIndex("foodPrice"));
            idRes = cursorFood.getString(cursorFood.getColumnIndex("idRes"));
            food = new Food(idFood, idRes, foodName, foodImage, foodCategory, foodCount, foodPrice);
            foodList.add(food);

        }
        cursorFood.close();
        return foodList;
    }

    public Cart getCart() {
        Cart cart = null;
        Restaurant restaurant = getRestaurant();
        ArrayList<Food> foodList = getListFood();


        SQLiteDatabase sql = getReadableDatabase();
        String idCart, idRes;
        int amount;
        long cost;


        Cursor cursorCart = sql.query(false, DB_TABLE_CART, null, null, null, null, null, null, null);
        if (cursorCart.moveToNext()) {
            idCart = cursorCart.getString(cursorCart.getColumnIndex("idCart"));
            idRes = cursorCart.getString(cursorCart.getColumnIndex("idRes"));
            amount = cursorCart.getInt(cursorCart.getColumnIndex("amount"));
            cost = cursorCart.getLong(cursorCart.getColumnIndex("cost"));
            cart = new Cart(idCart, idRes, amount, cost);
        }

//        cart.setRestaurant(restaurant);
//        cart.getRestaurant().setFoodList(foodList);
        cursorCart.close();
        return cart;

    }


}
