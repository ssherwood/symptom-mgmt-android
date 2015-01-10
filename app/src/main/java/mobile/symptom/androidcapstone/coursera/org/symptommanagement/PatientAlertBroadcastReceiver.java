package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class PatientAlertBroadcastReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 200010;
    public static final String ACTION = "mobile.symptom.androidcapstone.coursera.org.symptommanagement.alert";


    public PatientAlertBroadcastReceiver() {
    }

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(SymptomManagement.LOG_ID, "PatientAlertBroadcastReceiver received intent...");
        Log.d(SymptomManagement.LOG_ID, "Alert id = " + intent.getIntExtra("alarmId", 0) + " for doctorId = " + intent.getLongExtra("doctorId", -1L));

        Intent i = new Intent(context, PatientAlertService.class);
        i.putExtra("doctorId", intent.getLongExtra("doctorId", -1L));
        context.startService(i);
    }
}