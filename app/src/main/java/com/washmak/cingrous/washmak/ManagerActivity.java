package com.washmak.cingrous.washmak;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.washmak.cingrous.washmak.fragementclasses.AddWorkerFragement;
import com.washmak.cingrous.washmak.fragementclasses.SensorDetailsFragement;
import com.washmak.cingrous.washmak.fragementclasses.UserDeatilsFragement;
import com.washmak.cingrous.washmak.fragementclasses.WorkerDetailsFragement;

import java.util.Objects;


public class ManagerActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
   /* WebView dashboard;*/

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth =  FirebaseAuth.getInstance();

        /*dashboard = findViewById(R.id.dashboard);
        dashboard.getSettings().setJavaScriptEnabled(true);
        dashboard.loadUrl("https://github.com/");*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView nameView = header.findViewById(R.id.nav_current_user);
        TextView emailView = header.findViewById(R.id.nav_current_user_email);

        nameView.setText(mAuth.getUid());
        emailView.setText(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        NavigateOnClick(id);

        return true;
    }

    private void NavigateOnClick(int id){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.nav_user_details_from_drawer:
                ft.replace(R.id.layout_frame_from_content_manager, new UserDeatilsFragement());
                break;
            case R.id.nav_add_worker_from_drawer:
                ft.replace(R.id.layout_frame_from_content_manager, new AddWorkerFragement());
                break;
            case R.id.nav_work_details_from_drawer:
                ft.replace(R.id.layout_frame_from_content_manager, new WorkerDetailsFragement());
                break;
            case R.id.nav_sensor_details_from_drawer:
                ft.replace(R.id.layout_frame_from_content_manager, new SensorDetailsFragement());
                break;
            case R.id.nav_logout_from_drawer:
                logout();
                break;
        }
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(ManagerActivity.this)
                .setTitle(R.string.logout)
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(ManagerActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null)
                .show();
    }

}
