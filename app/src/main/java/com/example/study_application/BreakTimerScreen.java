package com.example.study_application;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class BreakTimerScreen extends AppCompatActivity {

    private long breakTimeLeft;
    private CountDownTimer countBreakTimer;
    private TextView progressBreakBar;
    private Button stopButton, startButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_timer_screen);

        // linking buttons to the xml buttons
        stopButton = findViewById(R.id.stopButton);
        startButton = findViewById(R.id.startButton);
        cancelButton = findViewById(R.id.cancelButton);
        progressBreakBar = findViewById(R.id.progressBreakBar);

        // as we are using milliseconds i have to increase it by multiplying it by 1000 the 300 originally is the 5 minute mark.
        breakTimeLeft = 300000;

        //showing the amount of time on screen before starting
        updateCountDownText();

        //limiting the area of the screen to layout inside xml and the transparent area brings the user back.
        getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        stopButton.setVisibility(View.INVISIBLE);

        //checks if the user has pressed the start button
        startButton.setOnClickListener(v -> {
            startButton.setVisibility(View.GONE);
            startBreakTimer();
        });

        //checks if the user has pressed the cancel button which then send the user back to the task screen
        cancelButton.setOnClickListener(v -> finish());
    }

    //the timer used to start the 5 minute timer
    private void startBreakTimer() {
        //creates new count down timer
        countBreakTimer = new CountDownTimer(300000, 500) {

            //is used to check every tick
            @Override
            public void onTick(long leftTimeInMilliseconds) {
                breakTimeLeft = leftTimeInMilliseconds;
                //used to update the text showing the timer.
                updateCountDownText();
            }

            //starts this method once the timer hits 00:00
            @Override
            public void onFinish() {
                if (breakTimeLeft < 600) {
                    countBreakTimer.cancel();
                    progressBreakBar.setText(R.string.breakEnd);
                    cancelButton.setVisibility(View.INVISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                    //sends the user back to the task screen
                    stopButton.setOnClickListener(v -> finish());
                }
            }
        }.start();
    }

    //updates the text timer.
    private void updateCountDownText() {
        int minutes = (int) (breakTimeLeft / 1000) / 60;
        int seconds = (int) (breakTimeLeft / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        progressBreakBar.setText(timeLeftFormatted);
    }
}