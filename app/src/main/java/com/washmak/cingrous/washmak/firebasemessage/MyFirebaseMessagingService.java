package com.washmak.cingrous.washmak.firebasemessage;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.washmak.cingrous.washmak.R;

import java.util.Map;
import java.util.Objects;

@SuppressLint("Registered")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static int count = 0;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        sendNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),
                remoteMessage.getNotification().getBody(), remoteMessage.getData());
    }

    private void sendNotification(String title, String body, Map<String, String> data) {
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.color.colorAccentText))
                .setSmallIcon(R.color.colorAccentText)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(null);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(count, notificationBuilder.build());
        count++;
    }
}
