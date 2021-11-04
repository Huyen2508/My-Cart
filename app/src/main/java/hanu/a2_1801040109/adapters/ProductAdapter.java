package hanu.a2_1801040109.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import hanu.a2_1801040109.R;
import hanu.a2_1801040109.db.ProductManager;
import hanu.a2_1801040109.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> implements Filterable {
    List<Product> products;
    List<Product> searchProducts;
    ProductManager productManager;

    public ProductAdapter(List<Product> products) {
        this.products = products;
        this.searchProducts = products;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Activity to display
        Context context = parent.getContext();

        //xml to java object
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_product, parent, false);

        return new ProductAdapter.ProductHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
        Product product = searchProducts.get(position);
        // bind data with view template
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return searchProducts.size();
    }

    public class ProductHolder extends RecyclerView.ViewHolder {
        private TextView productName, productPrice;
        private ImageButton btnAddToCart;
        private ImageView productThumbnail;
        private Context context;

        public ProductHolder(@NonNull View itemView, Context context) {
            super(itemView);

            productName = itemView.findViewById(R.id.tvName);
            productPrice = itemView.findViewById(R.id.tvUnitPrice);
            productThumbnail = itemView.findViewById(R.id.ivThumbnail);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            this.context = context;
        }

        public void bind(Product product) {
            productName.setText(product.getName());
            productPrice.setText("đ" + product.getUnitPrice());

            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productManager = ProductManager.getInstance(context);
                    boolean isAdded = false;
                    boolean isUpdated = false;
                    Product productDb = productManager.selectProductById(product.getId());
                    if(productDb == null){
                        product.increaseQty();
                        isAdded = productManager.addProduct(product);
                    }else{
                        productDb.increaseQty();
                        isUpdated = productManager.updateProduct(productDb);
                    }

                    if(isAdded || isUpdated){
                        Toast.makeText(context, "Add product successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(context, "Add product failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            ThumbnailLoader task = new ThumbnailLoader();
            task.execute(product.getThumbnail());
        }

        public class ThumbnailLoader extends AsyncTask<String, Void, Bitmap> {
            URL image_url;
            HttpURLConnection urlConnection;

            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    image_url = new URL(strings[0]);
                    urlConnection = (HttpURLConnection) image_url.openConnection();
                    urlConnection.connect();

                    InputStream is = urlConnection.getInputStream();
                    return BitmapFactory.decodeStream(is);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                productThumbnail.setImageBitmap(bitmap);
            }
        }
    }

    //Search
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String key = charSequence.toString();
                if (key.isEmpty()) {
                    searchProducts = products;
                } else {
                    List<Product> searched = new ArrayList<>();
                    for (Product pr : products) {
                        if (pr.getName().toLowerCase().contains(key.toLowerCase())) {
                            searched.add(pr);
                        }
                    }
                    searchProducts = searched;
                }
                FilterResults results = new FilterResults();
                results.values = searchProducts;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                searchProducts = (List<Product>) results.values;

                notifyDataSetChanged();
            }
        };
    }
}

