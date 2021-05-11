package com.dnc.wallhub.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dnc.wallhub.Activities.GridSpacingItemDecoration;
import com.dnc.wallhub.Adapters.FavouriteAdapter;
import com.dnc.wallhub.Adapters.RecentAdapter;
import com.dnc.wallhub.Models.Favourite;
import com.dnc.wallhub.Models.Recent;
import com.dnc.wallhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesFragment extends Fragment {
    List<Favourite> favouriteList;
    List<Favourite> favList;
    RecyclerView recyclerView;
    FavouriteAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private View lottiebutton;

    DatabaseReference dbWallpapers;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        favList = new ArrayList<>();
        lottiebutton = view.findViewById(R.id.creditcardscanner);
        swipeRefreshLayout = view.findViewById(R.id.swipe);
        favouriteList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        int spacingInPixels = 30;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, spacingInPixels, true, 0));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        adapter = new FavouriteAdapter(getContext(), favouriteList);
        recyclerView.setAdapter(adapter);

        fetchWallpapers();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new CountDownTimer(1000,1000){

                    @Override
                    public void onTick(long millisUntilFinished) {}

                    @Override
                    public void onFinish() {
                        fetchWallpapers();
                        lottiebutton.setVisibility(View.VISIBLE);
                    }
                }.start();
            }
        });
    }

    private void fetchWallpapers() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null){

        }else {
            dbWallpapers = FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("favourites");
            dbWallpapers.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        favouriteList.clear();
                        for (DataSnapshot wallpaperSnapshot : dataSnapshot.getChildren()) {
                            String id = wallpaperSnapshot.child("ID").getValue(String.class);
                            String url = wallpaperSnapshot.child("URL").getValue(String.class);

                            Favourite w = new Favourite(id,url);
                            favouriteList.add(w);
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

}