package com.hellomicke89gmail.projektsmartlock;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by hello on 2016-05-03.
 */
public class GCMListener extends GcmListenerService {
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message=data.getString("message");
        sendNotification(message);


    }


    private void sendNotification(String message){
        Log.v("GCMListener",message);
        if(message.equals("unlock")||message.equals("Change to card id data on server")||message.equals("token")) {
            Intent intent = new Intent();
            intent.setAction(message);
            sendBroadcast(intent);
        }
        else {

            Intent intent = new Intent(this, ApprovedListView.class);
            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
            int requestCode = 0;
            PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.icon)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, builder.build());
            
        }

    }
}
