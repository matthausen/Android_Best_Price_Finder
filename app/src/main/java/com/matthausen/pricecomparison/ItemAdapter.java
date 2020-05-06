package com.matthausen.pricecomparison;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private ArrayList<Product> mDataset;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mtextView1;
        public TextView mtextView2;
        public ConstraintLayout parentLayout;

        public ItemViewHolder(View v) {
            super(v);
            parentLayout = v.findViewById(R.id.parent_layout);
            mImageView = v.findViewById(R.id.item_image);
            mtextView1 = v.findViewById(R.id.item_title);
            mtextView2 = v.findViewById(R.id.item_subtitle);
        }
    }

    public ItemAdapter(Context context, ArrayList<Product> productList) {
        mContext = context;
        mDataset = productList;
    }

    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view, parent, false);

        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final Product currentProduct = mDataset.get(position);

        new DownloadImageTask(holder.mImageView).execute(currentProduct.getImage());
        holder.mtextView1.setText(currentProduct.getTitle());
        holder.mtextView2.setText(currentProduct.getCurrency() + " " + currentProduct.getPrice());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: open new activity ");

                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                intent.putExtra("imageUrl",  currentProduct.getImage());
                intent.putExtra("title", currentProduct.getTitle());
                intent.putExtra("currency", currentProduct.getCurrency());
                intent.putExtra("price", currentProduct.getPrice());
                intent.putExtra("shipping", currentProduct.getShipping());
                intent.putExtra("condition", currentProduct.getCondition());
                intent.putExtra("originalUrl", currentProduct.getViewItemURL());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
