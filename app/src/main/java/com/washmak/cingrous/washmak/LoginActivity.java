package com.washmak.cingrous.washmak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Map;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText email_from_login, password_from_login;
    private FirebaseFirestore myDBRef = FirebaseFirestore.getInstance();
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = showProgression(LoginActivity.this);
        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();



        email_from_login = findViewById(R.id.et_email_from_login);
        password_from_login = findViewById(R.id.et_pass_from_login);

        findViewById(R.id.btn_login_from_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                String email = email_from_login.getText().toString().trim();
                String pass = password_from_login.getText().toString().trim();

                if (!email.equals("") && !pass.equals("")) {
                    mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                final FirebaseUser user = mAuth.getCurrentUser();
                                if (user != null){
                                    myDBRef.collection("Employee").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()){
                                                final DocumentSnapshot document = task.getResult();
                                                myDBRef.collection("Employee")
                                                        .document(user.getUid())
                                                        .update("messageId", refreshedToken)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (document.exists()) {
                                                                    Map<String, Object> data = document.getData();
                                                                    assert data != null;
                                                                    if(data.get("type").equals("Manager")) {
                                                                        updateUI("Manager");
                                                                    } else if(data.get("type").equals("Worker")){
                                                                        updateUI("Worker");
                                                                    } else if (data.get("type").equals("Supervisor")){
                                                                        updateUI("Supervisor");
                                                                    }
                                                                }
                                                            }
                                                        });

                                                }
                                            }
                                    });

                                }
                            } else {
                                progressDialog.dismiss();
                                showAlertDialog("Authentication Failed!", "Try Again", LoginActivity.this);
                            }
                        }
                    });
                }else {
                    progressDialog.dismiss();
                    showAlertDialog("Email and Password can't be empty", "Try Again", LoginActivity.this);
                }
            }
        });
    }

    private void updateUI(String i) {
        if (i != null) {
            if (i.equals("Manager")) {
                startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
            }
            if (i.equals("Worker")) {
                startActivity(new Intent(LoginActivity.this, WorkerdashbordActvity.class));
            }
            if (i.equals("Supervisor")){
                startActivity(new Intent(LoginActivity.this, SupervisorActivity.class));
            }
        }
        finish();
    }
}
