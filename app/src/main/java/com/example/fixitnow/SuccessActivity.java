package com.example.fixitnow;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SuccessActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        TextView tvId = findViewById(R.id.tvBookingId);
        Button btnHome = findViewById(R.id.btnHome);

        int bookingId = getIntent().getIntExtra("booking_id", 0);
        String service = getIntent().getStringExtra("service");

        tvId.setText("Booking ID: FIX-" + bookingId + "\nService: " + service);

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
    }
}