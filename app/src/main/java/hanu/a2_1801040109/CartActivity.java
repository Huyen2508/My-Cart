package hanu.a2_1801040109;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import hanu.a2_1801040109.adapters.CartAdapter;
import hanu.a2_1801040109.db.ProductManager;
import hanu.a2_1801040109.models.Product;

public class CartActivity extends AppCompatActivity {
    ProductManager productManager;
    List<Product> cartProducts;
    CartAdapter cartAdapter;
    RecyclerView rvCartProducts;
    public TextView totalValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        productManager = ProductManager.getInstance(this);

        // get data
        productManager = ProductManager.getInstance(this);
        cartProducts = productManager.getAll();

        // adapter
        cartAdapter = new CartAdapter(cartProducts, this);

        // recycle view
        rvCartProducts = findViewById(R.id.rvCartProducts);
        rvCartProducts.setAdapter(cartAdapter);
        rvCartProducts.setLayoutManager(new LinearLayoutManager(this));

        totalValue = findViewById(R.id.totalValue);
        totalValue.setText("đ" + productManager.getTotalPrice());
    }
}