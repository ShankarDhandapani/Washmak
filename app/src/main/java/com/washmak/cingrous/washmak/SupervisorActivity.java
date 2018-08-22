package com.washmak.cingrous.washmak;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.washmak.cingrous.washmak.fragementclasses.AddWorkerFragement;
import com.washmak.cingrous.washmak.fragementclasses.AssignJobFragement;
import com.washmak.cingrous.washmak.fragementclasses.SensorDetailsFragement;
import com.washmak.cingrous.washmak.fragementclasses.UserDeatilsFragement;
import com.washmak.cingrous.washmak.fragementclasses.WelcomeFragement;
import com.washmak.cingrous.washmak.fragementclasses.WorkerDetailsFragement;

public class SupervisorActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout_supervisor);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        @SuppressLint("CommitTransaction") FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.layout_frame_from_content_supervisor, new WelcomeFragement());
        ft.commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout_supervisor);
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

    private void NavigateOnClick(int id) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id){
            case R.id.nav_user_details_from_supervisor_drawer:
                ft.replace(R.id.layout_frame_from_content_supervisor, new UserDeatilsFragement());
                break;
            case R.id.nav_add_worker_from_supervisor_drawer:
                ft.replace(R.id.layout_frame_from_content_supervisor, new AddWorkerFragement());
                break;
            case R.id.nav_work_details_from_supervisor_drawer:
                ft.replace(R.id.layout_frame_from_content_supervisor, new WorkerDetailsFragement());
                break;
            case R.id.nav_sensor_details_from_supervisor_drawer:
                ft.replace(R.id.layout_frame_from_content_supervisor, new SensorDetailsFragement());
                break;
            case R.id.nav_assign_job_from_supervisor_drawer:
                ft.replace(R.id.layout_frame_from_content_supervisor, new AssignJobFragement());
                break;
            case R.id.nav_logout_from_supervisor_drawer:
                logout();
                break;
        }
        ft.commit();
        DrawerLayout drawer = findViewById(R.id.drawer_layout_supervisor);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void logout() {
        new AlertDialog.Builder(SupervisorActivity.this)
                .setTitle(R.string.logout)
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(SupervisorActivity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null)
                .show();
    }
}
