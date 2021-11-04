package hanu.a2_1801040109.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1801040109.models.Product;

public class ProductManager {
    //singleton
    private static ProductManager instance;
    private DbHelper dbHelper;
    private SQLiteDatabase db;
    private static final String INSERT_STM = "INSERT INTO " +
            DbSchema.CartTable.NAME + "(id, thumbnail, name, unitPrice, quantity) VALUES (?, ?, ?, ?, ?)";

    public static ProductManager getInstance(Context context){
        if(instance == null){
            instance = new ProductManager(context);
        }
        return instance;
    }

    public ProductManager(Context context){
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public List<Product> getAll(){
        String sql = "SELECT * FROM " + DbSchema.CartTable.NAME;
        Cursor cursor = db.rawQuery(sql, null);
        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProducts();
    }

    public Product selectProductById(long productId){
        String sql = "SELECT * FROM " + DbSchema.CartTable.NAME + " WHERE " + DbSchema.CartTable.Cols.P_ID + " = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{productId + ""});

        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProductByID();
    }

    public Product selectProductByName(String name){
        String sql = "SELECT * FROM " + DbSchema.CartTable.NAME + " WHERE name = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{name});

        ProductCursorWrapper cursorWrapper = new ProductCursorWrapper(cursor);

        return cursorWrapper.getProductByName();
    }

    public double getTotalPrice(){
        String sql = "SELECT SUM ("+ DbSchema.CartTable.Cols.P_QUANTITY +" * "  + DbSchema.CartTable.Cols.P_UNIT_PRICE +  ") AS total FROM " + DbSchema.CartTable.NAME;
        Cursor cursor =db.rawQuery(sql, null);

        double total;
        cursor.moveToFirst();
        cursor.isAfterLast();

        total = cursor.getDouble(0);
        return total;
    }

    /**
     * @modifies product
     */
    public boolean addProduct(Product product){
        SQLiteStatement statement = db.compileStatement(INSERT_STM);
        statement.bindLong(1, product.getId());
        statement.bindString(2, product.getThumbnail());
        statement.bindString(3, product.getName());
        statement.bindDouble(4, product.getUnitPrice());
        statement.bindString(5, product.getQuantity() +"");

        long id = statement.executeInsert();
        return id > 0;

    }

    public boolean deleteProduct(long productId){
        int result = db.delete(DbSchema.CartTable.NAME,
                DbSchema.CartTable.Cols.P_ID + " = ?", new String[]{productId + ""} );

        return result > 0;
    }

    public boolean updateProduct(Product product){

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", product.getId());
        contentValues.put("thumbnail", product.getThumbnail());
        contentValues.put("name", product.getName());
        contentValues.put("unitPrice", product.getUnitPrice());
        contentValues.put("quantity", product.getQuantity());

        int result = db.update(DbSchema.CartTable.NAME, contentValues, DbSchema.CartTable.Cols.P_ID + " = ?", new String[]{product.getId() + ""});
        return result > 0;
    }
}

