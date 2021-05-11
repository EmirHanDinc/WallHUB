package com.dnc.wallhub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dnc.wallhub.Activities.WallpaperActivity;
import com.dnc.wallhub.Activities.WallpaperListActivity;
import com.dnc.wallhub.Models.Category;
import com.dnc.wallhub.Models.Wallpaper;
import com.dnc.wallhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.WallpaperViewHolder>{
    private Context mCtx;
    private List<Wallpaper> wallpaperList;


    public WallpapersAdapter(Context mCtx, List<Wallpaper> wallpaperList) {
        this.mCtx = mCtx;
        this.wallpaperList = wallpaperList;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpaper, parent, false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holder, int position) {
        Wallpaper w = wallpaperList.get(position);
        Glide.with(mCtx)
                .load(w.url)
                .into(holder.rvWallpaperImage);
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    class WallpaperViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView rvWallpaperImage;
        public WallpaperViewHolder(View itemView) {
            super(itemView);
            rvWallpaperImage = itemView.findViewById(R.id.rvWallpaperImage);
            rvWallpaperImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition();
            Wallpaper w = wallpaperList.get(p);

            Intent intent = new Intent(mCtx, WallpaperActivity.class);
            intent.putExtra("wallpaperURL",w.url);
            intent.putExtra("id",w.id);

            mCtx.startActivity(intent);
        }

    }




}
