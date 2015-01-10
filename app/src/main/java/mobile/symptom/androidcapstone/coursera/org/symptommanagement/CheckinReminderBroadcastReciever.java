package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class CheckinReminderBroadcastReciever extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

    public CheckinReminderBroadcastReciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(SymptomManagement.LOG_ID, "Check-in reminder Broadcast received from " + intent.getIntExtra("alarmId", 0));

        Notification aNotification = new Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("Symptom Management")
                .setContentText("It's time to check-in")
                .setAutoCancel(true)
                .setVibrate(new long[] { 500, 500, 500})
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, CheckinPainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, aNotification);
    }
}
