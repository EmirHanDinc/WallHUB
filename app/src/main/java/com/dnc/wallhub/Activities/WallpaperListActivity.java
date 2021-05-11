package com.dnc.wallhub.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dnc.wallhub.Adapters.WallpapersAdapter;
import com.dnc.wallhub.Models.Wallpaper;
import com.dnc.wallhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class WallpaperListActivity extends AppCompatActivity {
    List<Wallpaper> wallpaperList;
    List<Wallpaper> favList;
    RecyclerView recyclerView;
    WallpapersAdapter adapter;
    String id,url;

    DatabaseReference dbWallpapers, dbFavs;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper_list);
        if (Build.VERSION.SDK_INT >= 16) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(android.R.color.black));
        }
        Intent intent = getIntent();
        final String category = intent.getStringExtra("category");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(category);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_ios_24));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        favList = new ArrayList<>();
        wallpaperList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        int spacingInPixels = 30;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true, 0));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        adapter = new WallpapersAdapter(this, wallpaperList);
        recyclerView.setAdapter(adapter);

        dbWallpapers = FirebaseDatabase.getInstance().getReference("images")
                .child(category);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            dbFavs = FirebaseDatabase.getInstance().getReference("users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child("favourites")
                    .child(category);
            fetchFavWallpapers(category);
        } else {
            fetchWallpapers(category);
        }
    }

    private void fetchFavWallpapers(final String category) {
        dbFavs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);

                        Wallpaper w = new Wallpaper(id, title, desc, url, category);
                        favList.add(w);
                    }
                }
                fetchWallpapers(category);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fetchWallpapers(final String category) {
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        url = wallpaperSnapshot.child("url").getValue(String.class);

                        Wallpaper w = new Wallpaper(id, title, desc, url, category);
                        wallpaperList.add(w);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}