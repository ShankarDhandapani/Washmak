package com.washmak.cingrous.washmak;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.style.TtsSpan;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

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
                                            updateUI("manager");
                                        } else if (data.get("type").equals("Worker")) {
                                            updateUI("worker");
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
            if (i.equals("manager")) {
                startActivity(new Intent(SplashActivity.this, ManagerActivity.class));
                //Toast.makeText(SplashActivity.this, "Manager Login", Toast.LENGTH_LONG).show();
            }
            if (i.equals("worker")) {
                startActivity(new Intent(SplashActivity.this, WorkerdashbordActvity.class));
                //Toast.makeText(SplashActivity.this, "Worker Login", Toast.LENGTH_LONG).show();
            }
        } else {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        }
        finish();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("NEW TOKEN", refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        }
        assert telephonyManager != null;
        @SuppressLint({"MissingPermission", "HardwareIds"}) String deviceId = telephonyManager.getSubscriberId();
        DatabaseReference myTokenRef = FirebaseDatabase.getInstance().getReference()
                .child("MesaageToken");
        myTokenRef.child(deviceId).child("tokenId").setValue(refreshedToken);
    }*/
}
