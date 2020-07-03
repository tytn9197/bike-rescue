package com.example.bikerescueusermobile.ui.create_request;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class SendNotificationService extends FirebaseMessagingService {
    public SendNotificationService() {
    }

    private static final String TAG = "onMessageReceived";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
            } else {
                // Handle message within 10 seconds
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void sendMessageToActivity(String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("mess", msg);
//        Bundle b = new Bundle();
//        b.putParcelable("Location", l);
//        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(getBaseContext()).sendBroadcast(intent);
    }
    @Override
    public void onNewToken(String token) {
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
//        sendRegistrationToServer(token);
    }
}
