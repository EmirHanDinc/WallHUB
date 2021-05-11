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
import com.dnc.wallhub.Models.Recent;
import com.dnc.wallhub.R;

import java.util.List;

public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.RecentViewHolder> {
    private Context mCtx;
    private List<Recent> recentList;

    public RecentAdapter(Context mCtx, List<Recent> recentList) {
        this.mCtx = mCtx;
        this.recentList = recentList;
    }

    @NonNull
    @Override
    public RecentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_wallpaper, parent, false);
        return new RecentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentViewHolder holder, int position) {
        Recent r = recentList.get(position);
        Glide.with(mCtx)
                .load(r.url)
                .into(holder.rvWallpaperImage);
    }

    @Override
    public int getItemCount() {
        return recentList.size();
    }

    class RecentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView rvWallpaperImage;
       public RecentViewHolder(View itemView){
           super(itemView);

           rvWallpaperImage = itemView.findViewById(R.id.rvWallpaperImage);
           rvWallpaperImage.setOnClickListener(this);
       }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition();
            Recent r = recentList.get(p);

            Intent intent = new Intent(mCtx, WallpaperActivity.class);
            intent.putExtra("wallpaperURL",r.url);
            intent.putExtra("id",r.id);

            mCtx.startActivity(intent);
        }
    }
}

