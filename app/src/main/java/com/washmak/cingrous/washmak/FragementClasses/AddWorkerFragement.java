package com.washmak.cingrous.washmak.FragementClasses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.washmak.cingrous.washmak.R;
import com.washmak.cingrous.washmak.modelclasses.AddWorkerModel;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class AddWorkerFragement extends Fragment {


    final Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    TextView from_time, to_time;
    private ImageButton imgview;
    private static Bitmap bitmap;
    public static final int REQUEST_IMAGE = 100;
    FirebaseAuth myAuthRef;
    TextInputLayout con_pass_field;
    TextInputEditText worker_name, worker_address, worker_phone_number, worker_email, worker_pass, worker_con_pass, worker_type;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myAuthRef = FirebaseAuth.getInstance();
        final View view = inflater.inflate(R.layout.fragement_add_worker, container, false);



        imgview = view.findViewById(R.id.worker_photo_at_add_worker_tab_in_manager_login);
        Button create_account_from_manager = view.findViewById(R.id.create_account_button_at_add_worker_tab_in_manager_login);

        worker_name = view.findViewById(R.id.worker_name_from_add_worker);
        worker_address = view.findViewById(R.id.worker_address_from_add_worker);
        worker_phone_number = view.findViewById(R.id.worker_phone_number_from_add_worker);
        worker_email = view.findViewById(R.id.worker_email_from_add_worker);
        worker_pass = view.findViewById(R.id.worker_password_from_add_worker);
        worker_con_pass = view.findViewById(R.id.worker_con_password_from_add_worker);
        from_time = view.findViewById(R.id.time_from_from_add_details);
        to_time = view.findViewById(R.id.time_to_from_add_details);
        con_pass_field = view.findViewById(R.id.til_password_confirm);
        RadioGroup gender = view.findViewById(R.id.gender_at_add_student);
        int selectedId = gender.getCheckedRadioButtonId();
        final RadioButton radioSexButton = view.findViewById(selectedId);


        create_account_from_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wr_name = worker_name.getText().toString().trim();
                String wr_address = worker_address.getText().toString().trim();
                String wr_email = worker_email.getText().toString().trim();
                String wr_phono = worker_phone_number.getText().toString().trim();
                String wr_pass = worker_pass.getText().toString().trim();
                String wr_con_pass = worker_con_pass.getText().toString().trim();
                String wr_from_time = from_time.getText().toString().trim();
                String wr_to_time = to_time.getText().toString().trim();
                String wr_type = radioSexButton.getText().toString().trim();


                // All the DB link Function auth, Db, storage...
                pushData(wr_name, wr_address, wr_phono, wr_email, wr_pass, wr_con_pass, wr_from_time, wr_to_time, wr_type);

            }
        });


        view.findViewById(R.id.set_time_from_from_add_details).setOnClickListener(new View.OnClickListener() {
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

        view.findViewById(R.id.set_time_to_from_add_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        updateTime(hourOfDay, minute, 0);
                    }
                }, hr, min, false);
                timePickerDialog.show();
            }
        });

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });


        return view;


    }
    private void pushData(final String wr_name,
                          final String wr_address,
                          final String wr_phono,
                          final String wr_email,
                          final String wr_password,
                          final String wr_conpass,
                          final String wr_from_time,
                          final String wr_to_time,
                          final String wr_type) {

        showProgression(getContext()).show();
        final FirebaseFirestore myDBRef = FirebaseFirestore.getInstance();
        final FirebaseStorage storage = FirebaseStorage.getInstance();


        if (wr_password.equals(wr_conpass)){

            myAuthRef.createUserWithEmailAndPassword(wr_email, wr_conpass)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    FirebaseUser user = myAuthRef.getCurrentUser();
                    if (user!=null){
                        final String newuserid = user.getUid().trim();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        assert bitmap != null;
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] bytes = baos.toByteArray();

                        StorageReference myStorageRef = storage.getReference().child("Employee/"+wr_name+wr_phono+".jpg");
                        myStorageRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                AddWorkerModel newWorker = new AddWorkerModel(
                                        wr_name, wr_address,
                                        wr_phono, wr_email,
                                        wr_from_time, wr_to_time, wr_type
                                );
                                myDBRef.collection("Employee")
                                        .document(newuserid)
                                        .set(newWorker)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                showAlertDialog(wr_name+" Added Successfully", getContext());
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showAlertDialog("Database Error !", getContext());
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showAlertDialog("Storage Error !", getContext());
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showAlertDialog("Can't Create Employee", getContext());
                }
            });
        }else {
            con_pass_field.setError("Enter Same Password!!");
            showAlertDialog("Password Worrng", getContext());
        }

        showProgression(getContext()).dismiss();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE && resultCode == RESULT_OK){
            if (data!= null && data.getExtras() != null){
                bitmap = (Bitmap) data.getExtras().get("data");
                imgview.setImageBitmap(bitmap);
            }
        }
    }
    private void openCamera(){
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_IMAGE);
        }else {
            Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (picIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null) {
                startActivityForResult(picIntent, REQUEST_IMAGE);
            }
        }
    }
    private void updateTime(int hours, int mins, int i) {
        String timeSet = "";
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
        String minutes = "";
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
    public ProgressDialog showProgression(Context context ){
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.setTitle("Loading...");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDoalog;
    }
    public void showAlertDialog(String message, Context context){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton("Try Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                worker_name.setText(""); worker_address.setText("");
                worker_email.setText(""); worker_pass.setText("");
                worker_con_pass.setText(""); worker_phone_number.setText("");
                from_time.setText(R.string.time_pattern);
                to_time.setText(R.string.time_pattern);
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.add_worker);
    }

}
