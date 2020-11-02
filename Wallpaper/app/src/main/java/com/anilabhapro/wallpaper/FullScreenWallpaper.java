package com.anilabhapro.wallpaper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import javax.sql.DataSource;


public class FullScreenWallpaper extends AppCompatActivity {
    String originalUrl = "";
    PhotoView photoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_wallpaper);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = getIntent();
        originalUrl = intent.getStringExtra("originalUrl");
        photoView = findViewById(R.id.photoView);

        Glide.with(this).load(originalUrl).centerCrop()
                .placeholder(R.drawable.progress_animation).into(photoView);


        getSupportActionBar().setTitle("Set Wallpaper");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void SetWallpaperEvent(View view) {

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper Successfully Set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void DownloadWallpaperEvent(View view) {

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(originalUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        downloadManager.enqueue(request);
        Toast.makeText(this, "Downloading Start", Toast.LENGTH_SHORT).show();
    }
}