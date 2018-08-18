package com.washmak.cingrous.washmak.firebasemessage;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

@SuppressLint("Registered")
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

        private static final String Tag = "FIREBASE MESSAGE";

        @Override
        public void onTokenRefresh() {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d(Tag, refreshedToken);
            sendRegistrationToServer(refreshedToken);
        }

        private void sendRegistrationToServer(String refreshedToken) {
            DatabaseReference myTokenRef = FirebaseDatabase.getInstance().getReference()
                    .child("MesaageToken");
            myTokenRef.setValue(refreshedToken);
        }

}
