package com.dnc.wallhub.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dnc.wallhub.Activities.GridSpacingItemDecoration;
import com.dnc.wallhub.Adapters.CategoriesAdapter;
import com.dnc.wallhub.Adapters.RecentAdapter;
import com.dnc.wallhub.Adapters.WallpapersAdapter;
import com.dnc.wallhub.Models.Category;
import com.dnc.wallhub.Models.Recent;
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

public class RecentFragment extends Fragment {
    List<Recent> recentList;
    List<Recent> favList;
    RecyclerView recyclerView;
    RecentAdapter adapter;
    private View lottiebutton;

    DatabaseReference dbWallpapers;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lottiebutton = view.findViewById(R.id.creditcardscanner);


        favList = new ArrayList<>();
        recentList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        int spacingInPixels = 30;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true, 0));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter = new RecentAdapter(getContext(), recentList);
        recyclerView.setAdapter(adapter);

        fetchWallpapers();
    }

    private void fetchWallpapers() {
        dbWallpapers = FirebaseDatabase.getInstance().getReference("images").child("allImages");
        dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {

                        String id = wallpaperSnapshot.getKey();
                        String title = wallpaperSnapshot.child("title").getValue(String.class);
                        String desc = wallpaperSnapshot.child("desc").getValue(String.class);
                        String url = wallpaperSnapshot.child("url").getValue(String.class);

                        Recent w = new Recent(id, title, desc, url);
                        recentList.add(w);
                        lottiebutton.setVisibility(View.GONE);
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