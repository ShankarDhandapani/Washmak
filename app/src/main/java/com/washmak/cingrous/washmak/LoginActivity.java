package com.washmak.cingrous.washmak;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends BaseActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText email_from_login, password_from_login;
    private FirebaseFirestore myDBRef = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        email_from_login = findViewById(R.id.et_email_from_login);
        password_from_login = findViewById(R.id.et_pass_from_login);

        findViewById(R.id.btn_login_from_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showProgression(LoginActivity.this).show();

                String email = email_from_login.getText().toString().trim();
                String pass = password_from_login.getText().toString().trim();

                if (!email.equals("") || !pass.equals("")) {
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
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {
                                                    Map<String, Object> data = document.getData();
                                                    assert data != null;
                                                    if(data.get("type").equals("Manager")) {
                                                        updateUI(1);
                                                    } else if(data.get("type").equals("Worker")){
                                                        updateUI(0);
                                                    }
                                                }
                                            }
                                        }
                                    });

                                }
                            } else {
                                showProgression(LoginActivity.this).dismiss();
                                showAlertDialog("Authentication Failed!", LoginActivity.this);
                            }
                        }
                    });
                }else {
                    showProgression(LoginActivity.this).dismiss();
                    showAlertDialog("Email and Password can't be empty", LoginActivity.this);
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentuser = mAuth.getCurrentUser();
        if(currentuser != null)
            updateUI(1);
    }

    private void updateUI(int i) {
        if (i == 1){
            startActivity(new Intent(LoginActivity.this, ManagerActivity.class));
            Toast.makeText(LoginActivity.this, "Manager Login", Toast.LENGTH_LONG).show();
        }else {
            startActivity(new Intent(LoginActivity.this, WorkerdashbordActvity.class));
            Toast.makeText(LoginActivity.this, "Worker Login", Toast.LENGTH_LONG).show();
        }
        finish();
    }
}
