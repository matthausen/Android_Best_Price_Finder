package com.matthausen.pricecomparison;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private Context mContext;
    private ArrayList<Product>  mDataset;

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mtextView1;
        public TextView mtextView2;
        public RelativeLayout parentLayout;

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
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Product currentProduct = mDataset.get(position);

        // holder.mImageView.setText(mDataset[position]);
        holder.mtextView1.setText(currentProduct.getTitle());
        holder.mtextView2.setText(currentProduct.getPrice());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: open new activity ");

                Intent intent = new Intent(mContext, ProductDetailActivity.class);
                // intent.putExtra("image_url", mImages.get(position));
                // intent.putExtra("image_name", mImageNames.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
