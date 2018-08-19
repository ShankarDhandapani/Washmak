package com.washmak.cingrous.washmak.fragementclasses;

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
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.washmak.cingrous.washmak.R;
import com.washmak.cingrous.washmak.WorkerdashbordActvity;
import com.washmak.cingrous.washmak.modelclasses.AddWorkerModel;

import java.util.ArrayList;
import java.util.Objects;

public class WorkerDetailsFragement extends Fragment {
    private FirebaseFirestore myDBRef = FirebaseFirestore.getInstance();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View mainview = inflater.inflate(R.layout.fragement_worker_details, container, false);

        final ArrayList<String> name = new ArrayList<>();
        final ListView listView = mainview.findViewById(R.id.user_details_list_view);


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
                            listView.setAdapter(adapter);

                        } else {
                            Log.d("Error", "Error getting documents: ", task.getException());
                        }
                    }
                });
        return mainview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Objects.requireNonNull(getActivity()).setTitle(R.string.worker_details);
    }
}
