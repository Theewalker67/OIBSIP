package com.example.myapplication;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView timeView;
    private Button startButton, stopButton, holdButton;

    private long startTime = 0L;
    private Handler handler = new Handler();
    private boolean isRunning = false;
    private boolean isPaused = false;
    private long pausedTime = 0L;

    // Runnable to update the stopwatch
    Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - startTime;
            int seconds = (int) (elapsedTime / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            int milliseconds = (int) (elapsedTime % 1000);

            timeView.setText(String.format("%02d:%02d:%03d", minutes, seconds, milliseconds));
            handler.postDelayed(this, 50);  // Update every 50ms
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        timeView = findViewById(R.id.timer);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.reset_button);
        holdButton = findViewById(R.id.stop_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    if (isPaused) {
                        startTime = System.currentTimeMillis() - pausedTime;
                    } else {
                        startTime = System.currentTimeMillis();
                    }
                    handler.postDelayed(updateTimer, 0);
                    isRunning = true;
                    isPaused = false;
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacks(updateTimer);
                isRunning = false;
                pausedTime = 0;
                timeView.setText("00:00:000");
            }
        });

        holdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRunning) {
                    handler.removeCallbacks(updateTimer);
                    isPaused = true;
                    pausedTime = System.currentTimeMillis() - startTime;
                    isRunning = false;
                }
            }
        });
    }
}
