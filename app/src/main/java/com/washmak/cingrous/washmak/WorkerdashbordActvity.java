package com.washmak.cingrous.washmak;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.washmak.cingrous.washmak.modelclasses.HistoryModel;
import com.washmak.cingrous.washmak.modelclasses.SensorDetailsModel;
import com.washmak.cingrous.washmak.modelclasses.SupervisorTriggerModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;


public class WorkerdashbordActvity extends BaseActivity {

    private FirebaseAuth myAuthRef;
    private CollectionReference myHistoryRef;
    private SensorDetailsModel sensorDetails;
    private FloorNotifyView viewHolder;
    private String sendsor_id;

    public void setViewHolder(FloorNotifyView viewHolder) {
        this.viewHolder = viewHolder;
    }

    private AlertDialog dialog;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workerdashbord_actvity);
        myAuthRef = FirebaseAuth.getInstance();
        myHistoryRef = FirebaseFirestore.getInstance().collection("Work History");
        RecyclerView recyclerView = findViewById(R.id.recyclerview_from_workeract);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference myDBRef = FirebaseDatabase.getInstance().getReference().child("SupervisorTrigger");
        final FirebaseFirestore mySensorDbRef = FirebaseFirestore.getInstance();

        FirebaseRecyclerAdapter<SupervisorTriggerModel, FloorNotifyView> supervisorTriggerAdapter = new FirebaseRecyclerAdapter<SupervisorTriggerModel, FloorNotifyView>(
                SupervisorTriggerModel.class,
                R.layout.list_view_items,
                FloorNotifyView.class,
                myDBRef
        ) {
            @Override
            protected void populateViewHolder(final FloorNotifyView viewHolder, final SupervisorTriggerModel model, int position) {
                viewHolder.setTv_floor_location(model.getLocation());
                viewHolder.setTv_time_fixed(model.getTime_fixed());
                viewHolder.setTv_worker_name(model.getWorker_assign());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = getLayoutInflater();
                        @SuppressLint("InflateParams") View customView = inflater.inflate(R.layout.layout_details_wash, null);
                        setViewHolder(viewHolder);
                        final TextView main_head = customView.findViewById(R.id.notificatin_main_head);
                        final TextView main_content = customView.findViewById(R.id.notificatin_main_content);
                        mySensorDbRef.collection("Sensor")
                                .whereEqualTo("location", model.getLocation())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()){
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("WHERE DEMO", document.getId() + " => " + document.getData());
                                                sendsor_id = document.getId();
                                                sensorDetails = new SensorDetailsModel(document.getData());

                                                main_head.setText("Floor Number: " + sensorDetails.getFloor_no() + "\n");
                                                main_content.setText("There is the Issue in " + sensorDetails.getLocation() + " rest room. \n\n" +
                                                        "At " + "Floor Number: " + sensorDetails.getFloor_no() + "\n\n" + "Block Number: " + sensorDetails.getBlock_no() );
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

        recyclerView.setAdapter(supervisorTriggerAdapter);



    }


    @SuppressLint("ResourceAsColor")
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
                if ((sendsor_id).equals(result.getContents())){
                    viewHolder.list_card.setCardBackgroundColor(R.color.transparent_yellow);
                    @SuppressLint("SimpleDateFormat") String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
                    @SuppressLint("SimpleDateFormat") String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());

                    HistoryModel historyModel = new HistoryModel(
                            time+"",
                            date+"",
                            Objects.requireNonNull(myAuthRef.getCurrentUser()).getUid()+"",
                            myAuthRef.getCurrentUser().getEmail()+"",
                            sendsor_id+"",
                            sensorDetails.getFloor_no()+"",
                            sensorDetails.getBlock_no()+"",
                            sensorDetails.getLocation()+"",
                            "Job Done");
                    myHistoryRef.document(date + " " + time).set(historyModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            new AlertDialog.Builder(WorkerdashbordActvity.this)
                                    .setTitle("Your job is Done")
                                    .setMessage("Your credit is Saved")
                                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    });
                }else {
                    new AlertDialog.Builder(WorkerdashbordActvity.this)
                            .setTitle("Your job are on Wrong Floor")
                            .setMessage("Please check the description and block number")
                            .setNegativeButton("Sorry", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();
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
        new AlertDialog.Builder(WorkerdashbordActvity.this)
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
        TextView tv_floor_location;
        TextView tv_time_fixed;
        TextView tv_worker_name;
        CardView list_card;

        public FloorNotifyView(View itemView) {
            super(itemView);
            tv_floor_location = itemView.findViewById(R.id.floor_name_from_list_view_items);
            tv_time_fixed = itemView.findViewById(R.id.floor_time_from_list_view_items);
            tv_worker_name = itemView.findViewById(R.id.worker_name_from_list_view_items);
            list_card = itemView.findViewById(R.id.recyclerview_card_view);
        }

        public void setTv_floor_location(String tv_floor_location) {
            this.tv_floor_location.setText(tv_floor_location);
        }

        public void setTv_time_fixed(String tv_time_fixed) {
            this.tv_time_fixed.setText(tv_time_fixed);
        }

        public void setTv_worker_name(String tv_worker_name){
            this.tv_worker_name.setText(tv_worker_name);
        }
    }
}
