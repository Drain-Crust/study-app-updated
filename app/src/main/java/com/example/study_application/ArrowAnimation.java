package com.example.study_application;

import android.view.View;

public class ArrowAnimation {
    public static void toggleArrow(View view, boolean isExpanded) {
        // checks if the the boolean isExpanded is true or false
        if (isExpanded) {
            view.animate().setDuration(400).rotation(90);
        } else {
            view.animate().setDuration(400).rotation(0);
        }
    }
}
