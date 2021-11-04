package hanu.a2_1801040109;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import hanu.a2_1801040109.adapters.ProductAdapter;
import hanu.a2_1801040109.models.Product;

public class MainActivity extends AppCompatActivity {
    private EditText searchBox;
    private RecyclerView rvProducts;
    private ImageView btnCart;
    private List<Product> products;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchBox = findViewById(R.id.searchBox);
        rvProducts = findViewById(R.id.rvProducts);
        products = new ArrayList<>();
        btnCart = findViewById(R.id.btnCart);

        String url = "https://mpr-cart-api.herokuapp.com/products";
        ProductLoader taskProduct = new ProductLoader();
        taskProduct.execute(url);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence key, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence key, int start, int before, int count) {
                productAdapter.getFilter().filter(key);
            }

            @Override
            public void afterTextChanged(Editable keyW) {
            }
        });
    }

    public class ProductLoader extends AsyncTask<String, Void, String> {
        URL url;
        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream is = urlConnection.getInputStream();

                Scanner sc = new Scanner(is);
                StringBuilder result = new StringBuilder();
                String line;
                while(sc.hasNextLine()) {
                    line = sc.nextLine();
                    result.append(line);
                }
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(result == null){
                return;
            }

            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i ++){
                    JSONObject root = jsonArray.getJSONObject(i);

                    long productId = root.getLong("id");
                    String productThumbnail = root.getString("thumbnail");
                    String productName = root.getString("name");
                    double productPrice = root.getDouble("unitPrice");
                    Product product = new Product(productId, productThumbnail, productName, productPrice);

                    products.add(product);
                }
            } catch (JSONException e){
                e.printStackTrace();
            }

            rvProducts.setLayoutManager( new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false));
            productAdapter = new ProductAdapter(products);
            rvProducts.setAdapter(productAdapter);
        }
    }
}