package com.apper.sarwar.fnr;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class UserStatus extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private static int count = 0;


    public UserStatus() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                // handleNow();
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {

            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());

            //Here notification is recieved from server
            try {
                sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    private void sendNotification(String title, String messageBody) {

        try {
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);  //you can use your launcher Activity insted of SplashActivity, But if the Activity you used here is not launcher Activty than its not work when App is in background.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//Add Any key-value to pass extras to intent
            intent.putExtra("pushnotification", "yes");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);//For Android Version Orio and greater than orio.

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_LOW;
                NotificationChannel mChannel = new NotificationChannel("Sesame", "Sesame", importance);
                mChannel.setDescription(messageBody);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                mNotifyManager.createNotificationChannel(mChannel);
            }//For Android Version lower than oreo.

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "Seasame");
            mBuilder.setContentTitle(title)
                    .setContentText(messageBody)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(Color.parseColor("#FFD600"))
                    .setContentIntent(pendingIntent)
                    .setChannelId("Sesame")
                    .setPriority(NotificationCompat.PRIORITY_LOW);

            mNotifyManager.notify(count, mBuilder.build());
            count++;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
