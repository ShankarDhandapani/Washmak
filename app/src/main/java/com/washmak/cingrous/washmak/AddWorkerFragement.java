package com.washmak.cingrous.washmak;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.washmak.cingrous.washmak.modelclasses.AddWorkerModel;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class AddWorkerFragement extends Fragment {
    final Calendar c = Calendar.getInstance();
    int hr = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);
    TextView from_time, to_time;
    private static Bitmap bitmap;
    private ImageButton imgview;
    public static final int REQUEST_IMAGE = 100;
    private FirebaseAuth myAuthRef;
    private FirebaseStorage myStorage;
    private DatabaseReference myDBRef;

    public AddWorkerFragement() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragement_add_worker, container, false);
        from_time = view.findViewById(R.id.time_from_from_add_details);
        to_time = view.findViewById(R.id.time_to_from_add_details);
        myAuthRef = FirebaseAuth.getInstance();
       // myDBRef = FirebaseDatabase.getInstance().getReference();

        imgview = view.findViewById(R.id.worker_photo_at_add_worker_tab_in_manager_login);
        Button create_account_from_manager = view.findViewById(R.id.create_account_button_at_add_worker_tab_in_manager_login);

        final TextInputEditText worker_name = view.findViewById(R.id.worker_name_from_add_worker);
        final TextInputEditText worker_address = view.findViewById(R.id.worker_address_from_add_worker);
        final TextInputEditText worker_phone_number = view.findViewById(R.id.worker_phone_number_from_add_worker);
        final TextInputEditText worker_email = view.findViewById(R.id.worker_email_from_add_worker);
        final TextInputEditText worker_pass = view.findViewById(R.id.worker_password_from_add_worker);
        final TextInputEditText worker_con_pass = view.findViewById(R.id.worker_con_password_from_add_worker);



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

        create_account_from_manager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wr_name, wr_address, wr_phono, wr_email, wr_password, wr_conpass;
                wr_name = worker_name.getText().toString().trim();
                wr_address = worker_address.getText().toString().trim();
                wr_phono = worker_phone_number.getText().toString().trim();
                wr_email = worker_email.getText().toString().trim();
                wr_password = worker_pass.getText().toString().trim();
                wr_conpass = worker_con_pass.getText().toString().trim();
                pushData(wr_name, wr_address, wr_phono, wr_email, wr_password, wr_conpass);
            }
        });

        return view;


    }

    private void openCamera(){
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(Objects.requireNonNull(getActivity()).getPackageManager()) != null){
            startActivityForResult(picIntent, REQUEST_IMAGE);
        }
    }

    private void pushData(String wr_name, String wr_address, String wr_phono, String wr_email, String wr_password, String wr_conpass) {


        /*ByteArrayOutputStream baos = new ByteArrayOutputStream();
        assert bitmap != null;
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        StorageReference mountainImagesRef = myStorage.getReference().child("images/worker"+wr_name+wr_phono+".jpg");
        mountainImagesRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                Toast.makeText(getContext(), "Uploaded the Image", Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Can't Upload the Image", Toast.LENGTH_LONG).show();
            }
        });*/


        myAuthRef.createUserWithEmailAndPassword(wr_email, wr_password).addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user = myAuthRef.getCurrentUser();
                    //Toast.makeText(getContext(), "Worker Added", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getContext(), "Something Wrong!!...", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*AddWorkerModel newWorker = new AddWorkerModel(wr_name, wr_address, wr_phono, wr_email);

        myDBRef.child("Worker").setValue(newWorker).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(getContext(), "DataAdded", Toast.LENGTH_LONG).show();
            }
        });
*/

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


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.add_worker);
    }

}
