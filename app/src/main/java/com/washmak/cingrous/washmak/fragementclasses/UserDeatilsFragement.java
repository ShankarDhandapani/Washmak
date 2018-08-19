package com.washmak.cingrous.washmak.fragementclasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import com.washmak.cingrous.washmak.R;
import com.washmak.cingrous.washmak.modelclasses.AddWorkerModel;

import java.util.Objects;

public class UserDeatilsFragement extends Fragment {

    FirebaseAuth myAuthRef;
    FirebaseFirestore myDBRef;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragement_user_details, container, false);

        myAuthRef = FirebaseAuth.getInstance();
        myDBRef = FirebaseFirestore.getInstance();
        String currentuserid = myAuthRef.getUid();
        progressDialog = showProgression(getContext());

        assert currentuserid != null;
        myDBRef.collection("Employee").document(currentuserid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    progressDialog.show();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        progressDialog.dismiss();
                        AddWorkerModel currentWorker = new AddWorkerModel(Objects.requireNonNull(document.getData()));
                        view_profile(currentWorker, rootview);
                    }
                }
            }
        });



        return rootview;

    }

    private void view_profile(AddWorkerModel currentWorker, View rootview) {
        ImageView imageView = rootview.findViewById(R.id.user_profile_picture_from_user_frage);
        TextView name = rootview.findViewById(R.id.name_of_the_current_user);
        TextView email = rootview.findViewById(R.id.email_id_of_the_current_user);
        TextView address = rootview.findViewById(R.id.address_of_the_current_user);
        TextView phonenumber = rootview.findViewById(R.id.phone_number_of_the_current_user);
        TextView type = rootview.findViewById(R.id.worker_type_of_the_current_user);
        TextView from_time = rootview.findViewById(R.id.from_time_of_the_current_user);
        TextView to_time = rootview.findViewById(R.id.to_time_of_the_current_user);



        Picasso.with(getContext())
                .load(currentWorker.getImage())
                .into(imageView);

        name.setText(currentWorker.getName());
        email.setText(currentWorker.getEmail());
        address.setText(currentWorker.getAddress());
        phonenumber.setText(currentWorker.getPhonenumber());
        type.setText(currentWorker.getType());
        from_time.setText(currentWorker.getFrom_time());
        to_time.setText(currentWorker.getTo_time());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.user_details);
    }

    public ProgressDialog showProgression(Context context ){
        ProgressDialog progressDoalog = new ProgressDialog(context);
        progressDoalog.setMessage("Please wait...");
        progressDoalog.setTitle("Fetching Profile..");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        return progressDoalog;
    }
}
