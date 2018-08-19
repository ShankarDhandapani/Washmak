package com.washmak.cingrous.washmak.firebasemessage;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.washmak.cingrous.washmak.R;

import java.util.Map;
import java.util.Objects;

@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FIREBASE MESSAGE";
    private static int count = 0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String string = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Notification Message TOKEN: " + string);
        Log.d(TAG, "Notification Message TITLE: " + Objects.requireNonNull(remoteMessage.getNotification()).getTitle());
        Log.d(TAG, "Notification Message BODY: " + remoteMessage.getNotification().getBody());
        Log.d(TAG, "Notification Message DATA: " + remoteMessage.getData().toString());
//Calling method to generate notification
        sendNotification(remoteMessage.getNotification().getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData());
    }

    private void sendNotification(String title, String body, Map<String, String> data) {
        PendingIntent contentIntent = null;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(contentIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }
}
