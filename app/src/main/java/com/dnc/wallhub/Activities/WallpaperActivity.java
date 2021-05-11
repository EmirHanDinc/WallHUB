package com.dnc.wallhub.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.dnc.wallhub.BuildConfig;
import com.dnc.wallhub.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.dnc.wallhub.R.drawable.ic_favorite_active;
import static com.dnc.wallhub.R.drawable.ic_favorite_default;

public class WallpaperActivity extends AppCompatActivity {
    ImageView imageView,share,download,setWallpaper;
    ImageView addFavourite;

    String imageFileName;
    DisplayMetrics displayMetrics;
    BitmapDrawable bitmapDrawable;
    Bitmap bitmap;
    WallpaperManager wallpaperManager;
    String wallpaperURL;
    String Id;
    int sayi = 1;
    ArrayList<String> favs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_wallpaper);

        checkFavourites();
        Intent intent = getIntent();
        Id = intent.getStringExtra("id");

        favs = new ArrayList<String>();
        share = findViewById(R.id.share);
        download = findViewById(R.id.download);
        setWallpaper = findViewById(R.id.wallpaper);
        imageView = findViewById(R.id.imageView);
        addFavourite = findViewById(R.id.addFavourite);

        wallpaperURL = intent.getStringExtra("wallpaperURL");

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareWallpaper();
            }
        });

        setWallpaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(WallpaperActivity.this, setWallpaper);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.lock:
                                setWallpaper("Lock");
                                break;
                            case R.id.anasayfa:
                                setWallpaper("Home");
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext())
                        .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                downloadFromImageView();
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                                Toast.makeText(getApplicationContext(), "This Process Need Permissions.", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                Toast.makeText(WallpaperActivity.this, permissionRequest.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }).check();
            }
        });

        addFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FirebaseAuth.getInstance().getCurrentUser() == null){
                    Toast.makeText(WallpaperActivity.this, "Plesae Login First", Toast.LENGTH_SHORT).show();
                    addFavourite.setImageResource(R.drawable.ic_favorite_default);
                    return;
                }

                DatabaseReference dbFavs = FirebaseDatabase.getInstance().getReference("users")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("favourites");

                if (sayi == 1){
                    HashMap<String, Object> fav = new HashMap<>();
                    fav.put("ID", Id);
                    fav.put("URL",wallpaperURL);
                    dbFavs.child(Id).setValue(fav);
                    sayi = 0;
                }
                else {
                    dbFavs.child(Id).removeValue();
                    addFavourite.setImageResource(ic_favorite_default);
                }
            }
        });



        Glide.with(this).load(wallpaperURL).into(imageView);
    }

    public void onClick(View view){
        onBackPressed();
    }

    private void setWallpaper(String type){
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        int[] size = getScreenSize();
        wallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
        bitmap = bitmapDrawable.getBitmap();
        bitmap = Bitmap.createScaledBitmap(bitmap,size[0],size[1],false);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                if (type.equals("Lock")){
                    wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
                    Toast.makeText(this, "Lock Screen Wallpaper Set", Toast.LENGTH_SHORT).show();
                }else {
                    wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_SYSTEM);
                    Toast.makeText(this, "Home Screen Wallpaper Set", Toast.LENGTH_SHORT).show();
                }
            }else
            {
                wallpaperManager.setBitmap(bitmap);
                Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
            }
            wallpaperManager.suggestDesiredDimensions(size[0],size[1]);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private int[] getScreenSize() {
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int[] size = new int[2];
        size[0] = displayMetrics.widthPixels;
        size[1] = displayMetrics.heightPixels;
        return size;
    }

    private void downloadFromImageView(){
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageFileName = "WallHUB"+ System.currentTimeMillis();


        FileOutputStream fileOutputStream=null;
        File file=getdisc();
        if (!file.exists() && !file.mkdirs())
        {
            Toast.makeText(getApplicationContext(),"Sorry Can Not Make Dir",Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="WallHUB-"+date+".jpeg";
        String file_name=file.getAbsolutePath()+"/"+name;
        File new_file=new File(file_name);
        try {
            ProgressBar progressBar = new ProgressBar(this);
            progressBar.setVisibility(View.VISIBLE);
            fileOutputStream =new FileOutputStream(new_file);
            Bitmap bitmap=viewToBitmap(imageView,imageView.getWidth(),imageView.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(getApplicationContext(),name + " Saved Succesfully", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch
        (FileNotFoundException e) {
            e.getLocalizedMessage();
        } catch (IOException e) {
            e.printStackTrace();
        } refreshGallery(file);

    }

    private void refreshGallery(File file)
    {
        Intent i=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(file));
        sendBroadcast(i);
    }

    private static Bitmap viewToBitmap(View view, int widh, int hight)
    {
        Bitmap bitmap=Bitmap.createBitmap(widh,hight, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private File getdisc(){
        File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"WallHUB");
    }
    private void shareWallpaper() {

        Glide.with(this)
                .asBitmap()
                .load(wallpaperURL)
                .into(new SimpleTarget<Bitmap>() {
                          @Override
                          public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                              Intent intent = new Intent(Intent.ACTION_SEND);
                              intent.setType("image/*");
                              intent.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource));
                              startActivity(Intent.createChooser(intent, "Wallpapers Hub"));
                          }
                      }
                );
    }

    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "wallpaper_hub_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void checkFavourites(){
        String query = null;
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            query = "users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/favourites";
        }else {
            query = "users";
        }

        DatabaseReference fav = FirebaseDatabase.getInstance().getReference(query);
        fav.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    String value = ds.child("ID").getValue(String.class);
                    favs.add(value);
                }
                if (favs.size() == 0){

                }else {
                    for(int i = 0; i < favs.size(); i++) {
                        if (Id.equals(favs.get(i))) {
                            addFavourite.setImageResource(ic_favorite_active);
                            sayi = 0;
                            favs.clear();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }
}