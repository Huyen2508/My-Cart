package hanu.a2_1801040109.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040109.models.Product;

public class ProductCursorWrapper extends CursorWrapper {

    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Product getProduct(){
        long id = getLong(getColumnIndex(DbSchema.CartTable.Cols.P_ID));
        String thumbnail = getString(getColumnIndex(DbSchema.CartTable.Cols.P_THUMBNAIL));
        String name = getString(getColumnIndex(DbSchema.CartTable.Cols.P_NAME));
        double unitPrice = getDouble(getColumnIndex(DbSchema.CartTable.Cols.P_UNIT_PRICE));
        int quantity = getInt(getColumnIndex(DbSchema.CartTable.Cols.P_QUANTITY));

        Product product = new Product(id, thumbnail, name, unitPrice,quantity);
        return product;
    }

    public Product getProductByID(){
        Product product = null;

        moveToFirst();
        if(!isAfterLast()){
            product = getProduct();
        }
        return product;
    }

    public Product getProductByName(){
        Product product = null;

        moveToFirst();
        if (!isAfterLast()){
            product = getProduct();
        }
        return product;
    }

    public List<Product> getProducts(){
        List<Product> products = new ArrayList<>();

        moveToFirst();
        while (!isAfterLast()){
            Product product = getProduct();
            products.add(product);

            moveToNext();
        }
        return products;
    }
}
