package com.washmak.cingrous.washmak;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.washmak.cingrous.washmak.modelclasses.FloorTokenModel;
import com.washmak.cingrous.washmak.modelclasses.SensorDetailsModel;

import java.util.Objects;


public class WorkerdashbordActvity extends BaseActivity {

    private FirebaseAuth myAuthRef;
    private FloorTokenModel model;
    private AlertDialog dialog;



    public void setModel(FloorTokenModel model) {
        this.model = model;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerdashbord_actvity);
        myAuthRef = FirebaseAuth.getInstance();
        RecyclerView recyclerView = findViewById(R.id.recyclerview_from_workeract);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference myDBRef = FirebaseDatabase.getInstance().getReference().child("FloorsTokens");
        final FirebaseFirestore mySensorDbRef = FirebaseFirestore.getInstance();

        FirebaseRecyclerAdapter<FloorTokenModel, FloorNotifyView> firebaseAdapter = new FirebaseRecyclerAdapter<FloorTokenModel, FloorNotifyView>(
                FloorTokenModel.class,
                R.layout.list_view_items,
                FloorNotifyView.class,
                myDBRef
        ) {

            @Override
            protected void populateViewHolder(final FloorNotifyView viewHolder, final FloorTokenModel model, final int position) {
                viewHolder.setSensor_id(model.getSensor_id());
                viewHolder.setBoolean_token(model.getBoolean_token());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_details_wash, null);
                        setModel(model);
                        final TextView main_head = customView.findViewById(R.id.notificatin_main_head);
                        final TextView main_content = customView.findViewById(R.id.notificatin_main_content);
                        mySensorDbRef.collection("Sensor").document(model.getSensor_id()).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()){
                                                SensorDetailsModel sensorDetails = new SensorDetailsModel(Objects.requireNonNull(document.getData()));
                                                main_head.setText("Floor Number: " + sensorDetails.getFloor_no() + "\n");
                                                main_content.setText("There is the Issue in " + sensorDetails.getLocation() + "restroom. \n" +
                                                "At " + "Floor Number: " + sensorDetails.getFloor_no() + "\n" + " Block Number: " + sensorDetails.getBlock_no() );
                                            }else {
                                                main_head.setText(model.getSensor_id());
                                            }
                                        }
                                    }
                                });
                        AlertDialog.Builder alert = new AlertDialog.Builder(WorkerdashbordActvity.this);
                        alert.setView(customView);
                        alert.setCancelable(true);
                        dialog = alert.create();
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
        };

        recyclerView.setAdapter(firebaseAdapter);



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
                if ((model.getSensor_id()).equals(result.getContents())){
                    showAlertDialog("Ok Done you job is done", WorkerdashbordActvity.this);

                    // Toast.makeText(WorkerdashbordActvity.this, "Done", Toast.LENGTH_LONG).show();
                }else {
                    showAlertDialog("You are on different floor!", WorkerdashbordActvity.this);
                }
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
                        myAuthRef.signOut();
                        startActivity(new Intent(WorkerdashbordActvity.this, LoginActivity.class));
                        finish();
                    }
                }).setNegativeButton("No", null)
                .show();
    }

    public static class FloorNotifyView extends RecyclerView.ViewHolder {
        TextView tv_sensor_id;
        TextView tv_boolean_token;
        public FloorNotifyView(View itemView) {
            super(itemView);
            tv_sensor_id = itemView.findViewById(R.id.floor_name_from_list_view_items);
            tv_boolean_token = itemView.findViewById(R.id.floor_time_from_list_view_items);
        }

        public void setSensor_id(String sensor_id) {
            tv_sensor_id.setText(sensor_id);
        }

        public void setBoolean_token(String boolean_token) {
            tv_boolean_token.setText(boolean_token);
        }
    }
}
