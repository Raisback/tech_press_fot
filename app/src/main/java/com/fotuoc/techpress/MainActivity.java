package com.fotuoc.techpress;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LinearLayout newsContainerLayout;

    // Dummy data list
    private List<NewsItem> allNewsItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsContainerLayout = findViewById(R.id.news_container_layout);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Initialize dummy data
        initializeDummyNewsData();

        // Set up Bottom Navigation Listener
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Load all news initially when the app starts
        loadNews(""); // Empty string means no specific filter initially
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_all) {
            loadNews(""); // Show all news
            return true;
        } else if (id == R.id.nav_sports) {
            loadNews("Sports");
            return true;
        } else if (id == R.id.nav_academic) {
            loadNews("Academic");
            return true;
        } else if (id == R.id.nav_faculty_events) {
            loadNews("Faculty Events");
            return true;
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(intent);
        }
        return false;
    }




}