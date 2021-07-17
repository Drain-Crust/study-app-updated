package com.example.study_application;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class TaskScreen extends AppCompatActivity {

    // variables used to link to xml objects
    private Button startTimerButton, stopTimerButton;
    private ProgressBar timerBar;
    private TextView timeBarText;
    private TextView breakTimerTextView;
    private CountDownTimer countDownTimer;
    private CountDownTimer countBreakTimer;

    // used to check if the timers are running
    private Boolean breakTimerRunning = false;
    private Boolean countdownTimeRunning = false;
    private int completeIncomplete = 0;

    //variables used to save to file
    private String taskNames;
    private String taskCompletions;
    private String taskTimes;
    private String taskPosition;

    // variables used for the timers
    private long TimeLeft;
    private int originalTimeValue;
    private String actualNumber;
    private long BreakTimeLeft;

    // possible next screens
    Intent HomeScreen;
    ReadAndWrite readAndWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_screen);

        // linking ReadAndWrite to allow for the usage of its methods
        readAndWrite = new ReadAndWrite(TaskScreen.this);

        // links the variables to xml objects
        timerBar = findViewById(R.id.timerBar);
        startTimerButton = findViewById(R.id.StartTimer);
        timeBarText = findViewById(R.id.timeBarText);
        stopTimerButton = findViewById(R.id.StopTimer);
        breakTimerTextView = findViewById(R.id.BreakTimerTextView);

        HomeScreen = new Intent(this, HomeScreen.class);

        Intent lastPageInformation = getIntent();
        actualNumber = lastPageInformation.getStringExtra(ContentPopupScreen.EXTRA_STRING_ID);

        fileDataInformation();

        int time = Integer.parseInt(taskTimes);
        // time multiplied by 1000 as without it you cant get the specific minutes
        TimeLeft = time * 1000;
        originalTimeValue = time * 1000;

        updateCountDownText(timeBarText, TimeLeft);

        //starts timer if pressed and makes it disappear
        startTimerButton.setOnClickListener(v -> {
            countdownTimeRunning = true;
            startTimerButton.setVisibility(View.INVISIBLE);
            stopTimerButton.setVisibility(View.VISIBLE);
            if (TimeLeft > 1500000) {
                breakTimerRunning = true;
                startBreakTimer();
            }
            startTimer();
        });

        //appears after startTimerButton has been pressed
        stopTimerButton.setOnClickListener(v -> stopTimer());
    }

    private void startTimer() {
        //creates new count down timer
        countDownTimer = new CountDownTimer(TimeLeft, 500) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                TimeLeft = leftTimeInMilliseconds;
                updateCountDownText(timeBarText, TimeLeft);
                System.out.println(leftTimeInMilliseconds + "  " + originalTimeValue);

                timerBar.setProgress((int) (leftTimeInMilliseconds * 100 / originalTimeValue), true);
            }

            @Override
            public void onFinish() {
                if (timeBarText.getText().equals("00:00")) {
                    fileDataInformation();
                    completeIncomplete = 1;
                    //starts next screen
                    onBackPressed();
                }
            }
        }.start();
    }

    // updates text for the timer
    private void updateCountDownText(TextView timeText, long timeLeft) {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timeText.setText(timeLeftFormatted);
    }

    //stops the timers and saves the current time.
    private void stopTimer() {
        startTimerButton.setVisibility(View.VISIBLE);
        stopTimerButton.setVisibility(View.INVISIBLE);
        fileDataInformation();

        if (breakTimerRunning) {
            breakTimerRunning = false;
            countBreakTimer.cancel();
        }

        if (countdownTimeRunning) {
            countdownTimeRunning = false;
            countDownTimer.cancel();
            String textNameDataOld = taskPosition + " " + taskNames + " " + taskCompletions + " " + taskTimes;
            String textNameDataNew = taskPosition + " " + taskNames + " " + "Uncompleted" + " " + (TimeLeft / 1000);
            readAndWrite.replaceLines(textNameDataOld, textNameDataNew, "TaskNames.txt");
        }
    }

    // gets file data information and links the file data to variables to be used inside of the class
    @SuppressLint("SetTextI18n")
    private void fileDataInformation() {
        String[][] textNameData = readAndWrite.readTaskNameData("TaskNames.txt", true);

        for (int i = 1; i < textNameData.length; i++) {
            if (textNameData[i][0].equals(actualNumber)) {
                taskTimes = textNameData[i][3];
                taskNames = textNameData[i][1];
                taskCompletions = textNameData[i][2];
                taskPosition = textNameData[i][0];
            }
        }

        if (taskCompletions.equals("Uncompleted")) {
            startTimerButton.setText("Resume");
        }
    }

    public void onBackPressed() {
        startTimerButton.setVisibility(View.VISIBLE);
        stopTimerButton.setVisibility(View.GONE);
        // to determine if the user has completed the task or has left it uncompleted
        if (completeIncomplete == 0) {
            stopTimer();
        } else {
            saveCompleted();
        }
        super.onBackPressed();
    }

    private void saveCompleted() {
        //saves the new task data to the file
        String textNameDataOld = taskPosition + " " + taskNames + " " + taskCompletions + " " + taskTimes;
        String textNameDataNew = taskPosition + " " + taskNames + " " + "Completed" + " " + "0";
        readAndWrite.replaceLines(textNameDataOld, textNameDataNew, "TaskNames.txt");
    }

    private void startBreakTimer() {
        // as we are using milliseconds i have to increase it by multiplying it by 1000 the 1500 originally is the 25 minute mark.
        BreakTimeLeft = 1500000;
        //creates new count down timer
        countBreakTimer = new CountDownTimer(BreakTimeLeft, 500) {

            @Override
            public void onTick(long leftTimeInMilliseconds) {
                BreakTimeLeft = leftTimeInMilliseconds;
                updateCountDownText(breakTimerTextView, BreakTimeLeft);
            }

            @Override
            public void onFinish() {
                Intent BreakTimerScreen = new Intent(TaskScreen.this, BreakTimerScreen.class);

                stopTimer();
                //starts next screen
                startActivity(BreakTimerScreen);
            }
        }.start();
    }
}