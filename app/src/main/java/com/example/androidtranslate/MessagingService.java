package com.example.androidtranslate;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import static java.lang.Math.round;
import static java.sql.Types.TIMESTAMP;

public class MessagingService extends FirebaseMessagingService {
    private static final String TAG = "";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            try {
                JSONObject data = new JSONObject(remoteMessage.getData());
                String jsonMessage = data.getString("extra_information");
                Log.d(TAG, "ON MESSAGE RECEIVED" + jsonMessage);
            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getNotification().getClickAction();

            //long time = new Date().getTime();
            //long second = round(time/60000);

            Log.d(TAG, "Message Notification Title: " + title);
            Log.d(TAG, "Message Notification Body: " + message);
            Log.d(TAG, "Message Notification click: " + click_action);

            sendNotification(title, message, click_action);
        }
    }

    private void sendNotification(String title, String messageBody, String click_action) {

        Intent intent;
        if (click_action.equals("NOTIFICATION")) {
            intent = new Intent(this, Notification.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        } else if (click_action.equals("MAINACTIVITY")) {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
        intent.putExtra("title", title);
        intent.putExtra("message", messageBody);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);


        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.iconlauncher)
                        .setContentTitle(title)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

}


}