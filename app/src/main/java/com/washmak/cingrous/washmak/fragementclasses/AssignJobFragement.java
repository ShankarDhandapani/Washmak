package com.washmak.cingrous.washmak.fragementclasses;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.washmak.cingrous.washmak.R;
import com.washmak.cingrous.washmak.modelclasses.AddWorkerModel;
import com.washmak.cingrous.washmak.modelclasses.SensorDetailsModel;
import com.washmak.cingrous.washmak.modelclasses.SupervisorTriggerModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;

public class AssignJobFragement extends Fragment {
    final Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    private String location_selected;
    TextView from_time, to_time;


    private FirebaseFirestore myDBRef = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragement_assign_job, container, false);

        final DatabaseReference myTriggerDB = FirebaseDatabase.getInstance().getReference("SupervisorTrigger");
        final ArrayList<String> name = new ArrayList<>();
        final ArrayList<String> location = new ArrayList<>();
        final AutoCompleteTextView selectuser = rootview.findViewById(R.id.select_user_from_assign_job);
        final Spinner selectedjob = rootview.findViewById(R.id.select_job_from_assign_job);
        from_time = rootview.findViewById(R.id.time_from_from_assign_job);

        myDBRef.collection("Employee")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AddWorkerModel addWorkerModel = document.toObject(AddWorkerModel.class);
                                name.add(addWorkerModel.getName());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, name);
                            selectuser.setAdapter(adapter);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });



        myDBRef.collection("Sensor")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                SensorDetailsModel detailsModel = new SensorDetailsModel(document.getData());
                                location.add(detailsModel.getLocation());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, location);
                            adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                            selectedjob.setAdapter(adapter);
                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });


        selectedjob.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                location_selected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        rootview.findViewById(R.id.set_time_from_from_assign_job).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        updateTime(hourOfDay, minute, 1);
                    }
                }, hr, min, false);
                timePickerDialog.show();
            }
        });

        rootview.findViewById(R.id.assign_job_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hello", Toast.LENGTH_LONG).show();
                final String fix_time = from_time.getText().toString();
                final String worker_selected = selectuser.getText().toString();
                myDBRef.collection("Employee")
                        .whereEqualTo("name", worker_selected)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    for (QueryDocumentSnapshot document : task.getResult()){
                                        Map<String, Object> data = document.getData();
                                        SupervisorTriggerModel triggerModel = new SupervisorTriggerModel(
                                                1,
                                                location_selected+"",
                                                worker_selected+"",
                                                fix_time+"",
                                                data.get("messageId") +""

                                        );
                                        myTriggerDB.push().setValue(triggerModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                new AlertDialog.Builder(Objects.requireNonNull(getContext()))
                                                        .setTitle("Work Assigned")
                                                        .setMessage("Worker will get notified ")
                                                        .setPositiveButton("Ok", null)
                                                        .show();
                                            }
                                        });
                                    }
                                }
                            }
                        });

            }
        });

        return rootview;
    }

    private void updateTime(int hours, int mins, int i) {
        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);
        String aTime = String.valueOf(hours) + ':' + minutes + " " + timeSet;

        if(i == 1){
            from_time.setText(aTime);
        }else {
            to_time.setText(aTime);

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.assign_job);
    }
}
