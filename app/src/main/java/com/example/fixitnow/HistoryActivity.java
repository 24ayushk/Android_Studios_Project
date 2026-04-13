package com.example.fixitnow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    private FullScreenVideoView videoBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        videoBackground = findViewById(R.id.videoBackground);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bg_video);
        videoBackground.setVideoURI(videoUri);
        videoBackground.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0f, 0f);
            videoBackground.start();
        });

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        ListView listView = findViewById(R.id.listHistory);

        if (listView == null) {
            Toast.makeText(this, "Layout error: listHistory not found", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(this);
        ArrayList<HashMap<String, String>> bookings = db.getAllBookings();
        ArrayList<String> displayList = new ArrayList<>();

        if (bookings.isEmpty()) {
            displayList.add("No bookings yet.");
        } else {
            for (HashMap<String, String> map : bookings) {
                displayList.add(
                        "#" + map.get("id") + "  |  " + map.get("service") + "\n" +
                                "Name: "   + map.get("name")   + "\n" +
                                "Date: "   + map.get("date")   + "\n" +
                                "Status: " + map.get("status")
                );
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayList
        );
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoBackground != null && !videoBackground.isPlaying()) {
            videoBackground.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoBackground != null && videoBackground.isPlaying()) {
            videoBackground.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoBackground != null) {
            videoBackground.stopPlayback();
        }
    }
}