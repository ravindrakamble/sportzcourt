package com.sportzcourt.booking.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.data.Item;

import java.util.List;

/**
 * Created by Narasimha.HS on 1/12/2016.
 * <p/>
 * Adapter for Items
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private List<Item> itemsList;

    public ItemAdapter(List<Item> categoriesList) {
        this.itemsList = categoriesList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_listings_card, parent, false);

        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = itemsList.get(position);
        holder.image.setImageResource(item.getItemDrawable());
        holder.text.setText(item.getName());
        holder.price.setText(item.getPrice());
    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        protected ImageView image;
        protected TextView text;
        protected TextView price;

        public ItemViewHolder(View v) {
            super(v);

            image = (ImageView) v.findViewById(R.id.item_image);
            text = (TextView) v.findViewById(R.id.item_name);
            price = (TextView) v.findViewById(R.id.item_price);
        }
    }
}
