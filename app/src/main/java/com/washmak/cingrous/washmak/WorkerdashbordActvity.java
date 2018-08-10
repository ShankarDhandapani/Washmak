package com.washmak.cingrous.washmak;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.washmak.cingrous.washmak.listViewAdapter.ListViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class WorkerdashbordActvity extends AppCompatActivity {

    PopupWindow popupWindow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerdashbord_actvity);
        mAuth = FirebaseAuth.getInstance();

        final ListView listview = findViewById(R.id.lv_from_workeract);
        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
                "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
                "Android", "iPhone", "WindowsMobile" };

        final ArrayList<String> list = new ArrayList<>(Arrays.asList(values));
        ListViewAdapter listViewAdapter = new ListViewAdapter(WorkerdashbordActvity.this, list);
        //final ArrayAdapter<String> adapter = new ArrayAdapter<>(WorkerdashbordActvity.this, R.layout.list_view_items, list);
        listview.setAdapter(listViewAdapter);


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                LayoutInflater inflater = getLayoutInflater();
                @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_details_wash, null);
                AlertDialog.Builder alert = new AlertDialog.Builder(WorkerdashbordActvity.this);
                alert.setView(customView);
                alert.setCancelable(true);
                AlertDialog dialog = alert.create();
                dialog.show();

                customView.findViewById(R.id.float_btn_qr_scan_from_details_wash).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new IntentIntegrator(WorkerdashbordActvity.this).setCaptureActivity(ScannerActivity.class).initiateScan();
                    }
                });


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //We will get scan results here
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        //check for null
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan Cancelled", Toast.LENGTH_LONG).show();
            } else {
                //show dialogue with result
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_in_workerdashboard_actvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_from_worker_dash:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        AlertDialog alertDialog = new AlertDialog.Builder(WorkerdashbordActvity.this)
                .setTitle(R.string.logout)
                .setMessage("Are you sure you want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        startActivity(new Intent(WorkerdashbordActvity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null)
                .show();
    }
}
