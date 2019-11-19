package com.example.appinventiv.rcc_newproject_mvvm.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.appinventiv.rcc_newproject_mvvm.R;
import com.example.appinventiv.rcc_newproject_mvvm.constants.AppConstants;
import com.example.appinventiv.rcc_newproject_mvvm.data.preferences.PreferenceManager;
import com.example.appinventiv.rcc_newproject_mvvm.ui.home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    /**
     * This variable is used to have a unique id of the every notification
     */
    private int mUniqueId;
    /**
     * This variable is used to set the title of the notification
     */
    private String mTitle;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        PreferenceManager.getInstance(this).putString(AppConstants.PreferenceConstants.DEVICE_TOKEN, s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //handling notification
        sendNotification(remoteMessage.getData());
    }

    //method to get notificationManager
    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    //This method is only generating push notification
    private void sendNotification(Map<String, String> data) {
        mUniqueId = 0;
        try {
            //Generating unique id
            generateUniqueId();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.putExtra(AppConstants.NOTIFICATION_TYPE, data.get("type"));
            //Set title by getting from the push notification
            if (data.get("alert") != null)
                mTitle = data.get("alert");

            PendingIntent pendingIntent = PendingIntent.getActivity(this, mUniqueId, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = getNotificationManager();


            if (notificationManager != null) {
                String id = "channel" + mUniqueId;
                //Check for oreo (Making notification channel
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel mChannel = new NotificationChannel(id, mTitle, importance);
                    mChannel.setDescription(getString(R.string.app_name));
                    mChannel.setShowBadge(true);
                    mChannel.enableLights(true);
                    mChannel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

                    notificationManager.createNotificationChannel(mChannel);

                }

                //Set Notification for other devices
                NotificationCompat.Builder notificationBuilder =
                        new NotificationCompat.Builder(this, id)
                                .setSmallIcon(getNotificationIcon())
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                                .setContentText(mTitle)
                                .setContentTitle(getString(R.string.app_name))
                                .setAutoCancel(true).setChannelId(id)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(pendingIntent);

                // Set a message count to associate with this notification in the long-press menu.
                // Create a notification and set a number to associate with it.

                Notification notification = notificationBuilder.build();

                notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                notification.flags |=
                        Notification.FLAG_AUTO_CANCEL; //Do not clear  the notification
                notification.defaults |= Notification.DEFAULT_LIGHTS; // LED
                notification.defaults |= Notification.DEFAULT_VIBRATE;//Vibration

                notificationManager.notify(mUniqueId, notification);

                //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //notificationManager.notify(mUniqueId, notificationBuilder.build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * This method is used to generate unique id for the notifications
     */
    private void generateUniqueId() {
        mUniqueId = (int) System.currentTimeMillis();
    }

    /**
     * This method is used to set the notification icon on the push notifications for marshmallow
     * or oreo devices
     *
     * @return drawable on the basis of the type of device
     */
    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_launcher : R.mipmap.ic_launcher;
    }
}
