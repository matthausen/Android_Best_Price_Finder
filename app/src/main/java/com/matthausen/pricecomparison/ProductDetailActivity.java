package com.matthausen.pricecomparison;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


public class ProductDetailActivity extends AppCompatActivity {
    private static final String TAG = "Product Details";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail_view);

        getDetails();

    }

    private void getDetails() {

        if(getIntent().hasExtra("imageUrl")){
            String imageUrl = getIntent().getStringExtra("imageUrl");
            ImageView image = findViewById(R.id.detail_image);
            Glide.with(this)
                    .asBitmap()
                    .load(imageUrl)
                    .into(image);
        }

        if(getIntent().hasExtra("title")) {
            String title = getIntent().getStringExtra("title");
            TextView detail_title = findViewById(R.id.detail_title);
            detail_title.setText(title);
        }
        if(getIntent().hasExtra("price") && getIntent().hasExtra("currency")) {
            String price = getIntent().getStringExtra("price");
            String currency = getIntent().getStringExtra("currency");
            TextView detail_price = findViewById(R.id.detail_price);
            detail_price.setText("Price: " + currency + " " + price);
        }
        if(getIntent().hasExtra("shipping") && getIntent().hasExtra("currency")) {
            String shipping = getIntent().getStringExtra("shipping");
            String currency = getIntent().getStringExtra("currency");
            TextView detail_shipping = findViewById(R.id.detail_shipping);
            detail_shipping.setText("Shipping: " + currency + " " + shipping);
        }
        if(getIntent().hasExtra("condition")) {
            String condition = getIntent().getStringExtra("condition");
            TextView detail_condition = findViewById(R.id.detail_condition);
            detail_condition.setText(condition);
        }
        if(getIntent().hasExtra("originalUrl")) {
            final String originalUrl = getIntent().getStringExtra("originalUrl");
            Button buyBtn = findViewById(R.id.buy);
            buyBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View arg0) {
                    Intent viewIntent =
                            new Intent("android.intent.action.VIEW",
                                    Uri.parse(originalUrl));
                    startActivity(viewIntent);
                }
            });

        }
    }
}


