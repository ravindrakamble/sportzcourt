package com.sportzcourt.booking.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportzcourt.booking.R;
import com.sportzcourt.booking.model.data.Category;

import java.util.List;

/**
 * Created by Narasimha.HS on 1/12/2016.
 * <p/>
 * Adapter for categories
 */
public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {
    private List<Category> categoriesList;

    public CategoriesAdapter(List<Category> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.list_categories_card, parent, false);

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categoriesList.get(position);
        holder.text.setText(category.getCategoryName());
        holder.bg.setBackgroundResource(category.getCategoryDrawable());
    }

    @Override
    public int getItemCount() {
        return categoriesList == null ? 0 : categoriesList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        protected RelativeLayout bg;
        protected TextView text;

        public CategoryViewHolder(View v) {
            super(v);

            bg = (RelativeLayout) v.findViewById(R.id.card_bg);
            text = (TextView) v.findViewById(R.id.card_text);
        }
    }
}
