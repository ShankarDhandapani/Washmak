package com.washmak.cingrous.washmak.fragementclasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.washmak.cingrous.washmak.ManagerActivity;
import com.washmak.cingrous.washmak.R;
import com.washmak.cingrous.washmak.modelclasses.SensorDetailsModel;

import java.util.Objects;

public class SensorDetailsFragement extends Fragment {
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragement_sensor_details, container, false);

        progressDialog = showProgression(getContext());
        final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        final TextInputEditText tv_sensor_id = rootview.findViewById(R.id.sensor_id_from_sensor_details);
        final TextInputEditText tv_floor_no = rootview.findViewById(R.id.sensor_floor_no);
        final TextInputEditText tv_block_no = rootview.findViewById(R.id.sensor_block_no);
        final TextInputEditText tv_location = rootview.findViewById(R.id.sensor_loction);
        Button add_sensor = rootview.findViewById(R.id.add_sensor);

        add_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String sensor_id = tv_sensor_id.getText().toString().trim();
                String floor_no = tv_floor_no.getText().toString().trim();
                String block_no = tv_block_no.getText().toString().trim();
                String location = tv_location.getText().toString().trim();
                if (!sensor_id.equals("") && !floor_no.equals("") && !block_no.equals("") && !location.equals("")){

                    SensorDetailsModel model = new SensorDetailsModel(block_no, floor_no, location);
                    firestore.collection("Sensor").document(sensor_id).set(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                    .setTitle("Success")
                                    .setMessage(" Added Successfully")
                                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startActivity(new Intent(getContext(), ManagerActivity.class));
                                        }
                                    })
                                    .show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                    .setTitle("Error")
                                    .setMessage("Database Error!")
                                    .setNegativeButton("Try Again", null)
                                    .show();
                        }
                    });
                }
            }
        });



        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle("Sensor Details");

    }

    public ProgressDialog showProgression(Context context ){
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.setTitle("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDoalog;
    }
}
