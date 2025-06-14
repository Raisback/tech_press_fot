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

    private void initializeDummyNewsData() {
        allNewsItems = new ArrayList<>();

        // Dummy data for Sports (4 items)
        allNewsItems.add(new NewsItem(
                "UoC Honours Outstanding Sports Achievements",
                "University of Colombo celebrated the remarkable achievements of its undergraduate sportsmen and sportswomen at the Colours Awarding Ceremony held on April 4th, 2023",
                "2025-06-05", "Sports", "https://cmb.ac.lk/wp-content/uploads/colours-awarding-ceremony-2023-30.jpg"));
        allNewsItems.add(new NewsItem(
                "Certificate Course on System Dynamics for Sustainable Technological Resilience  ",
                "Discover the secrets of innovation, master resilient bio-industry strategies, and focus on making impactful decisions to shape a sustainable future.",
                "2025-05-05", "Academic", "https://tech.cmb.ac.lk/wp-content/uploads/2024/05/SDTSRBI_31_05_2024.png"));
        allNewsItems.add(new NewsItem(
                "Annual Inter-Faculty Sports Meet 2025",
                "Get ready for action! The Annual Inter-Faculty Sports Meet 2025 is now underway, bringing together athletes from every faculty",
                "2025-05-28", "Sports", "https://amaledu.lk/wp-content/uploads/2023/10/DSC_9062.jpg-scaled.jpg"));
        allNewsItems.add(new NewsItem(
                "Digital Detox Drive Launched at the University of Colombo\n",
                "he University of Colombo launched the “Digital Detox Drive,” an e-waste collection campaign aimed at promoting environmental responsibility and proper electronic waste disposal.",
                "2025-06-20", "Faculty Events", "https://cmb.ac.lk/wp-content/uploads/digital-detox-drive-launched-03.jpg"));
        allNewsItems.add(new NewsItem(
                "New Fitness Center Opens for Students",
                "A state-of-the-art fitness center, equipped with modern machinery and professional trainers, is now open to all registered students above Canteen",
                "2025-05-10", "Sports", "https://mannycantor.org/wp-content/uploads/2022/10/PhotobyBridgetBadore-9835-1536x1024.jpg"));

        // Dummy data for Academic (4 items)
        allNewsItems.add(new NewsItem(
                "Certificate Course in Geographic Information System ",
                "This Course Will Be conducted integrating both hands on and mnds on experience with mix approach of both lectures and laboratory exercises",
                "2025-06-15", "Academic", "https://arts.cmb.ac.lk/wp-content/uploads/2025/05/57-GIS.jpg"));

        allNewsItems.add(new NewsItem(
                "Registrations are open for the certificate courses",
                "Registrations are open for you to enroll in the certificate courses conducted by the Department of Instrumentation and Automation Technology, University of Colombo.",
                "2025-04-18", "Academic", "https://tech.cmb.ac.lk/wp-content/uploads/2023/03/Flyer-IAT-1448x2048.jpg"));

        // Dummy data for Faculty Events (4 items)

        allNewsItems.add(new NewsItem(
                "Beach Cleanup and Basic Water Safety Project",
                "members of the Environmental Technology Society, Faculty of Technology, University of Colombo united for a crucial cause at Panadura Beach. Recognizing the dire state of Sri Lanka’s polluted beaches, driven by their commitment to environmental protection, embarked on a mission to safeguard the coastline.",
                "2025-05-12", "Faculty Events", "https://tech.cmb.ac.lk/wp-content/uploads/2023/12/Beach-Cleanup3.jpg"));
        allNewsItems.add(new NewsItem(
                "Educational Visit to 'Diyasaru Uyana’, Battaramulla",
                "Students of the Department of Environmental Technology visited the ‘Diyasaru Uyana’ which is a well-functioning urban wetland park under the Sri Lanka Land Development Cooperation, Battaramulla.",
                "2025-03-15", "Faculty Events", "https://tech.cmb.ac.lk/wp-content/uploads/2019/12/IMG_20191101_100300.jpg"));
    }



    private void loadNews(String newsTypeFilter) {
        newsContainerLayout.removeAllViews(); // Clear previous news items

        if (allNewsItems != null) {
            // Log the filter being applied for debugging
            Log.d(TAG, "Loading news with filter: " + (newsTypeFilter.isEmpty() ? "All" : newsTypeFilter));

            for (NewsItem newsItem : allNewsItems) {
                // Apply filter: If filter is empty, show all; otherwise, match by type
                if (newsTypeFilter.isEmpty() || newsItem.getType().equalsIgnoreCase(newsTypeFilter)) {
                    addNewsCard(newsItem);
                }
            }
            if (newsContainerLayout.getChildCount() == 0 && !newsTypeFilter.isEmpty()) {
                Toast.makeText(this, "No news found for '" + newsTypeFilter + "'.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Error: Dummy data not initialized.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "allNewsItems list is null. Dummy data setup might be incorrect.");
        }
    }


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
        }

        // Add the populated card to the container layout
        newsContainerLayout.addView(newsCardView);
    }




}