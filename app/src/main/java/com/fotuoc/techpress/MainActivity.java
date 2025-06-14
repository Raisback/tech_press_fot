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

// --- Firebase Firestore Imports ---
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private LinearLayout newsContainerLayout;
    private BottomNavigationView bottomNavigationView; // Made global for easier access

    // Firebase Firestore instance
    private FirebaseFirestore db;

    // List to hold fetched news items
    private List<NewsItem> allNewsItems;

    // --- Firestore Collection Name and Field Names ---
    // This is the name of your collection in Firestore where news data is stored.
    public static final String NEWS_COLLECTION_NAME = "news_items";

    // These are the field names within each document in the 'news_items' collection.
    // Ensure these match exactly with your Firestore document field keys.
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_BODY = "body";
    public static final String FIELD_DATE = "date";
    public static final String FIELD_TYPE = "type";
    public static final String FIELD_IMAGE_URL = "imageUrl";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsContainerLayout = findViewById(R.id.news_container_layout);
        bottomNavigationView = findViewById(R.id.bottom_navigation); // Initialize global variable

        // --- Initialize Firebase Firestore ---
        db = FirebaseFirestore.getInstance();
        allNewsItems = new ArrayList<>(); // Initialize the list

        // Set up Bottom Navigation Listener
        bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // --- Fetch news data from Firestore when the activity is created ---
        fetchNewsFromFirestore();
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        // Clear selection to prevent item from staying highlighted if navigation goes to another activity
        // or if you want to reset highlight for dynamic content.
        // bottomNavigationView.getMenu().setGroupCheckable(0, false, true);

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
            // We return false here because we are navigating to a new activity.
            // The item won't stay "selected" on the bottom nav.
            return false;
        }
        return false;
    }

    /**
     * Fetches news data from the Firestore collection specified by NEWS_COLLECTION_NAME.
     * Populates the allNewsItems list and then displays them.
     */
    private void fetchNewsFromFirestore() {
        Log.d(TAG, "Attempting to fetch news from Firestore collection: " + NEWS_COLLECTION_NAME);

        // Clear existing items to avoid duplicates on refresh or re-fetch
        allNewsItems.clear();

        db.collection(NEWS_COLLECTION_NAME)
                .get() // Get all documents from the collection
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "News data fetch successful.");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Extract data for each NewsItem
                                String title = document.getString(FIELD_TITLE);
                                String body = document.getString(FIELD_BODY);
                                String date = document.getString(FIELD_DATE);
                                String type = document.getString(FIELD_TYPE);
                                String imageUrl = document.getString(FIELD_IMAGE_URL);

                                // Create a NewsItem object and add it to the list
                                allNewsItems.add(new NewsItem(title, body, date, type, imageUrl));
                                Log.d(TAG, "Added news item: " + title);
                            }
                            // After fetching all data, load the news into the UI
                            loadNews(""); // Load all news by default initially
                        } else {
                            Log.w(TAG, "Error getting news documents: ", task.getException());
                            Toast.makeText(MainActivity.this, "Failed to load news: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * Loads news items into the UI, optionally filtering by newsTypeFilter.
     *
     * @param newsTypeFilter The type of news to display (e.g., "Sports", "Academic", "Faculty Events").
     * An empty string "" will display all news.
     */
    private void loadNews(String newsTypeFilter) {
        newsContainerLayout.removeAllViews(); // Clear previous news items

        if (allNewsItems != null && !allNewsItems.isEmpty()) {
            Log.d(TAG, "Displaying news with filter: " + (newsTypeFilter.isEmpty() ? "All" : newsTypeFilter));

            boolean itemsFound = false;
            for (NewsItem newsItem : allNewsItems) {
                // Apply filter: If filter is empty, show all; otherwise, match by type
                if (newsTypeFilter.isEmpty() || newsItem.getType().equalsIgnoreCase(newsTypeFilter)) {
                    addNewsCard(newsItem);
                    itemsFound = true;
                }
            }
            if (!itemsFound && !newsTypeFilter.isEmpty()) {
                Toast.makeText(this, "No news found for '" + newsTypeFilter + "'.", Toast.LENGTH_SHORT).show();
            }
        } else if (allNewsItems != null && allNewsItems.isEmpty()) {
            Toast.makeText(this, "No news items available. Please check Firestore.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "allNewsItems is empty.");
        } else {
            Toast.makeText(MainActivity.this, "Error: News data not initialized.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "allNewsItems list is null.");
        }
    }

    /**
     * Inflates and populates a single news card view, then adds it to the newsContainerLayout.
     *
     * @param newsItem The NewsItem object containing data for the card.
     */
    private void addNewsCard(NewsItem newsItem) {
        // Inflate the card layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View newsCardView = inflater.inflate(R.layout.item_news_card, newsContainerLayout, false);

        // Find views within the inflated card
        ImageView newsImage = newsCardView.findViewById(R.id.news_image);
        TextView newsTitle = newsCardView.findViewById(R.id.news_title);
        TextView newsDate = newsCardView.findViewById(R.id.news_date);
        TextView newsBody = newsCardView.findViewById(R.id.news_body);
        TextView newsType = newsCardView.findViewById(R.id.news_type);

        // Populate views with data
        newsTitle.setText(newsItem.getTitle());
        newsDate.setText(newsItem.getDate());
        newsBody.setText(newsItem.getBody());
        newsType.setText(newsItem.getType());

        // Load image using Glide
        if (newsItem.getImageUrl() != null && !newsItem.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(newsItem.getImageUrl())
                    .placeholder(R.color.design_default_color_secondary_variant) // Placeholder while loading
                    .error(R.drawable.ic_broken_image) // Error image if loading fails (create this drawable if you don't have it)
                    .into(newsImage);
        } else {
            // If no image URL, set a default background or hide the ImageView
            newsImage.setImageResource(R.drawable.ic_broken_image); // Or set a solid color background
            // You might want to hide the ImageView if there's no image: newsImage.setVisibility(View.GONE);
        }

        // Add the populated card to the container layout
        newsContainerLayout.addView(newsCardView);
    }
}