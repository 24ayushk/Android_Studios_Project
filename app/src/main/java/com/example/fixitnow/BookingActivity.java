package com.example.fixitnow;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;

public class BookingActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private FullScreenVideoView videoBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DatabaseHelper(this);

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
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        EditText etName = findViewById(R.id.etName);
        EditText etPhone = findViewById(R.id.etPhone);
        Spinner spinnerService = findViewById(R.id.spinnerService);
        EditText etDate = findViewById(R.id.etDate);
        ImageButton btnDatePicker = findViewById(R.id.btnDatePicker);
        EditText etAddress = findViewById(R.id.etAddress);
        Button btnBook = findViewById(R.id.btnBook);

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "");
        String phone = prefs.getString("phone", "");

        etName.setText(name);
        etPhone.setText(phone);

        etName.setEnabled(false);
        etPhone.setEnabled(false);

        btnBook.setEnabled(true);

        DatePickerDialog.OnDateSetListener onDateSet = (view, year, month, day) -> {
            String date = String.format("%02d/%02d/%04d", day, month + 1, year);
            etDate.setText(date);
        };

        btnDatePicker.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            DatePickerDialog dpd = new DatePickerDialog(
                    this,
                    onDateSet,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            dpd.show();
        });

        etDate.setOnClickListener(v -> btnDatePicker.performClick());

        btnBook.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Booking");
            builder.setMessage("Book " + spinnerService.getSelectedItem() + " service?");
            builder.setPositiveButton("Yes", (dialog, which) -> {

                long id = dbHelper.insertBooking(
                        name,
                        phone,
                        spinnerService.getSelectedItem().toString(),
                        etDate.getText().toString(),
                        etAddress.getText().toString()
                );

                if (id == -1) {
                    Toast.makeText(this, "Booking failed", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Booking Confirmed!", Toast.LENGTH_SHORT).show();

                NotificationHelper.sendBookingNotification(this, (int) id);

                Intent intent = new Intent(this, SuccessActivity.class);
                intent.putExtra("booking_id", (int) id);
                intent.putExtra("service", spinnerService.getSelectedItem().toString());
                startActivity(intent);
                finish();
            });
            builder.setNegativeButton("No", null);
            builder.show();
        });
    }
}