package com.example.study_application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MenuScreen extends AppCompatActivity {
    private final DrawerLayout DRAWER;
    private final Context CONTEXT;
    private final Activity ACTIVITY;

    // gets context and allows it to be used as a variable inside the method
    public MenuScreen(Context context) {
        this.CONTEXT = context;
        this.ACTIVITY = (Activity) CONTEXT;
        DRAWER = ACTIVITY.findViewById(R.id.navigation_layout);
    }

    public void toSetDrawer() {
        // the ACTIVITY is there as it we need to specify the location of where this drawer is going to be located
        Toolbar toolBar = ACTIVITY.findViewById(R.id.toolBar);
        NavigationView navigationView = ACTIVITY.findViewById(R.id.nav_view);
        // listens if any of the items inside the drawer are pressed and handles them in the method onNavigationItemSelected
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        //checks if the drawer button has been clicked
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((Activity) CONTEXT, DRAWER, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        DRAWER.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.bringToFront();
    }

    @SuppressLint("NonConstantResourceId")
    private boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        // context wrapper needed as it needs a starting point else it will be determined as null and give an error
        ContextWrapper contextWrapper = new ContextWrapper(CONTEXT);
        Intent destination;
        switch (item.getItemId()) {
            case R.id.nav_main_page:
                destination = new Intent(CONTEXT, HomeScreen.class);
                break;
            case R.id.nav_message:
                destination = new Intent(CONTEXT, TaskListScreen.class);
                break;
            case R.id.nav_logout:
                destination = new Intent(CONTEXT, MainActivity.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        // checks if the item selected is the screen they are currently on. if so it will close the drawer instead.
        if (!destination.getComponent().getClassName().equals(CONTEXT.getClass().getName())) {
            contextWrapper.startActivity(destination);
        }
        DRAWER.closeDrawer(GravityCompat.START);
        return true;
    }
}
