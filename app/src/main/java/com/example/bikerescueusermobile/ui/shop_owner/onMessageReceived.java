package com.example.bikerescueusermobile.ui.shop_owner;

import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.bikerescueusermobile.data.model.request.MessageRequestFB;
import com.example.bikerescueusermobile.data.model.request.RequestDTO;
import com.example.bikerescueusermobile.util.MyInstances;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

public class onMessageReceived extends FirebaseMessagingService {
    private static final String TAG = "onMessageReceived";

    public onMessageReceived() {
    }
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
//                handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
//            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle());

            if(remoteMessage.getNotification().getTitle().equals(MyInstances.TITTLE_SHOP)) {
                Intent shopIntent = new Intent("BikeRescueShop");
                shopIntent.putExtra("message", remoteMessage.getNotification().getBody());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(shopIntent);
            }

            if(remoteMessage.getNotification().getTitle().equals(MyInstances.TITTLE_BIKER)) {
                Intent bikerIntent = new Intent("BikeRescueBiker");
                bikerIntent.putExtra("message", remoteMessage.getNotification().getBody());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(bikerIntent);
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }
}
