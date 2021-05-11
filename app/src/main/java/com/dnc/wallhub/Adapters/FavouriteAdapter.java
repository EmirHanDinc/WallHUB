package com.dnc.wallhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dnc.wallhub.Activities.WallpaperActivity;
import com.dnc.wallhub.Models.Favourite;
import com.dnc.wallhub.R;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>{
    private Context mCtx;
    private List<Favourite> favouriteList;

    public FavouriteAdapter(Context mCtx, List<Favourite> favouriteList) {
        this.mCtx = mCtx;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public FavouriteAdapter.FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpaper, parent, false);
        return new FavouriteAdapter.FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Favourite f = favouriteList.get(position);
        Glide.with(mCtx)
                .load(f.url)
                .into(holder.rvWallpaperImage);
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView rvWallpaperImage;
        public FavouriteViewHolder(View itemView){
            super(itemView);

            rvWallpaperImage = itemView.findViewById(R.id.rvWallpaperImage);
            rvWallpaperImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition();
            Favourite f = favouriteList.get(p);

            Intent intent = new Intent(mCtx, WallpaperActivity.class);
            intent.putExtra("wallpaperURL",f.url);
            intent.putExtra("id",f.id);

            mCtx.startActivity(intent);
        }
    }
}
