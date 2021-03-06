package com.dnc.wallhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dnc.wallhub.Activities.WallpaperListActivity;
import com.dnc.wallhub.Models.Category;
import com.dnc.wallhub.R;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {


    private Context mCtx;
    private List<Category> categoryList;

    public CategoriesAdapter(Context mCtx, List<Category> categoryList) {
        this.mCtx = mCtx;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_categories, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category c = categoryList.get(position);
        holder.textView.setText(c.name);
        Glide.with(mCtx)
                .load(c.thumb)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textView;
        ImageView imageView;

        public CategoryViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.rvCategoriesTitle);
            imageView = itemView.findViewById(R.id.rvCategoriesImage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition();
            Category c = categoryList.get(p);

            Intent intent = new Intent(mCtx, WallpaperListActivity.class);
            intent.putExtra("category",c.name);

            mCtx.startActivity(intent);
        }
    }
}
