package com.washmak.cingrous.washmak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SplashActivity extends BaseActivity {
    private FirebaseFirestore myDBRef = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();


        if (isOnline()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    FirebaseUser currentuser = mAuth.getCurrentUser();
                    if (currentuser != null) {
                        myDBRef.collection("Employee").document(currentuser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Map<String, Object> data = document.getData();
                                        assert data != null;
                                        if (data.get("type").equals("Manager")) {
                                            updateUI("Manager");
                                        } else if (data.get("type").equals("Worker")) {
                                            updateUI("Worker");
                                        }else if (data.get("type").equals("Supervisor")){
                                            updateUI("Supervisor");
                                        }
                                    }
                                }
                            }
                        });
                    } else {
                        updateUI(null);

                    }

                }
            }, 4000);
        } else {
            showAlertDialog("Please Connect to internet!", "Try Again", SplashActivity.this);
        }
    }

    private void updateUI(String i) {
        if (i != null) {
            if (i.equals("Manager")) {
                startActivity(new Intent(SplashActivity.this, ManagerActivity.class));
            }
            if (i.equals("Worker")) {
                startActivity(new Intent(SplashActivity.this, WorkerdashbordActvity.class));
            }
            if (i.equals("Supervisor")){
                startActivity(new Intent(SplashActivity.this, SupervisorActivity.class));
            }
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }
}
