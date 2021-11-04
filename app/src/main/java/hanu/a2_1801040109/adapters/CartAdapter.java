package hanu.a2_1801040109.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hanu.a2_1801040109.CartActivity;
import hanu.a2_1801040109.R;
import hanu.a2_1801040109.db.ProductManager;
import hanu.a2_1801040109.models.Product;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {
    List<Product> cartProducts;
    private CartActivity cartActivity;
    private Context context;
    ProductManager productManager;

    public CartAdapter(List<Product> cartProducts, CartActivity cartActivity){
        this.cartProducts = cartProducts;
        this.cartActivity = cartActivity;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Activity to display
        context = parent.getContext();

        //XML to java Object
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.item_product_in_cart, parent, false);

        return new CartHolder(itemView, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartHolder holder, int position) {
        Product product = this.cartProducts.get(position);

        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return this.cartProducts.size();
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        private ImageView cartPThumbnail;
        private TextView cartPName, cartPUnitPrice, cartPQuantity, cartPTotalPrice;
        private ImageButton btnIncrease, btnDecrease;
        private Context context;

        public CartHolder(@NonNull View itemView, Context context) {
            super(itemView);

            cartPThumbnail = itemView.findViewById(R.id.cartP_thumbnail);
            cartPName = itemView.findViewById(R.id.cartP_name);
            cartPUnitPrice = itemView.findViewById(R.id.cartP_unitPrice);
            cartPQuantity = itemView.findViewById(R.id.cartP_quantity);
            cartPTotalPrice = itemView.findViewById(R.id.cartP_totalPrice);

            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            this.context = context;
        }

        public void bind(Product product){
            cartPName.setText(product.getName());
            cartPQuantity.setText(product.getQuantity()+"");
            cartPUnitPrice.setText("đ" + product.getUnitPrice());
            cartPTotalPrice.setText("đ" + product.getPrice());

            btnIncrease.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productManager = ProductManager.getInstance(context);

                    boolean isUpdated;
                    product.increaseQty();
                    isUpdated = productManager.updateProduct(product);

                    if( isUpdated){
                        cartActivity.totalValue.setText("đ" + productManager.getTotalPrice());
                    }else{
                        Toast.makeText(context, "Add failed", Toast.LENGTH_SHORT).show();
                    }
                    notifyDataSetChanged();
                }
            });

            btnDecrease.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    productManager = ProductManager.getInstance(context);

                    if(product.getQuantity() ==  1){
                        new AlertDialog.Builder(context)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to remove this product from the cart?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        boolean isDeleted = productManager.deleteProduct(product.getId());
                                        if(isDeleted){
                                            cartProducts.remove(product);
                                            cartActivity.totalValue.setText("đ" + productManager.getTotalPrice());
                                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                                        }
                                        notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("no",new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }).show();

                    }else{
                        product.decreaseQty();
                        productManager.updateProduct(product);
                        cartActivity.totalValue.setText("đ" + productManager.getTotalPrice());
                        notifyDataSetChanged();
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

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                cartPThumbnail.setImageBitmap(bitmap);
            }
        }
    }
}