package com.anilabhapro.wallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;
    String query = "Mobile hd wallpaper";

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems;
    // String url = "https://api.pexels.com/v1/curated/?page=" + pageNumber + "&per_page=80";
    String url = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        recyclerView = findViewById(R.id.recyclerView);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);

        recyclerView.setAdapter(wallpaperAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }


            }
        });


        fetchWallpaper();

    }

    public void fetchWallpaper() {
        ProgressDialog pro = new ProgressDialog(this);
        pro.setMessage("Loding....");
        pro.show();

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pro.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                int id = object.getInt("id");

                                JSONObject objectImages = object.getJSONObject("src");

                                String orignalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id, orignalUrl, mediumUrl);
                                wallpaperModelList.add(wallpaperModel);


                            }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;

                        } catch (JSONException e) {

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f91700001000001b5ee10d1e8994d0ab970ba1fc0fc0156");

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

//        if (id == R.id.nav_search) {
//
//            AlertDialog.Builder alert = new AlertDialog.Builder(this);
//            final EditText editText = new EditText(this);
//            editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//
//            alert.setMessage("Enter Category Example : Nature");
//            alert.setTitle("Search Wallpaper");
//
//            alert.setView(editText);
//
//
//            alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                    String query = editText.getText().toString();
//
//                    if (query.isEmpty() == false) {
//
//                        url = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + query;
//                        wallpaperModelList.clear();
//                        fetchWallpaper();
//                    } else {
//                        Toast.makeText(getApplicationContext(), "Please Give a Search input!", Toast.LENGTH_SHORT).show();
//
//                    }
//
//
//                }
//            });
//
//
//            alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//
//                }
//            });
//
//            alert.show();
//
//
//        }
        if (id == R.id.info) {
            startActivity(new Intent(MainActivity.this, Info.class));

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {

        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(MainActivity.this);

        builder.setMessage("Do you want to exit ?");


        builder.setCancelable(false);

        // Set the positive button with yes name
        // OnClickListener method is use of
        // DialogInterface interface.

        builder
                .setPositiveButton(
                        "Yes",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                finish();
                            }
                        });

        builder
                .setNegativeButton(
                        "No",
                        new DialogInterface
                                .OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {

                                // If user click no
                                // then dialog box is canceled.
                                dialog.cancel();
                            }
                        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
    }
}


