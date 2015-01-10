package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Calendar;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.oauth.OAuth2Response;

/**
 */
public class SymptomManagement extends Application {

    public static final String LOG_ID = "SymptomMgmt";

    public static final int ALARM_1 = 100010;
    public static final int ALARM_2 = 100020;
    public static final int ALARM_3 = 100030;
    public static final int ALARM_4 = 100040;

    private static Context context;
    private static Long ENTITY_ID;
    private static String ENTITY_ROLE;
    private static SharedPreferences mSharedPreferences;
    private static boolean isAuthenticated = false;

    public void onCreate(){
        super.onCreate();
        SymptomManagement.context = getApplicationContext();

        // todo find a more "secure" way to do this
        mSharedPreferences = getSharedPreferences("userauth", MODE_PRIVATE);
        ENTITY_ID = mSharedPreferences.getLong("entityId", 0L);
        ENTITY_ROLE = mSharedPreferences.getString("role", "USER");
        String oauth = mSharedPreferences.getString("oauth", null);

        if (oauth != null) {
            isAuthenticated = true;

            Log.d("SM", "Initializing Custom Request Interceptor...");
            Gson gson = new Gson();
            RestAPI.getOauth2Interceptor().setOAuth2Response(gson.fromJson(oauth, OAuth2Response.class));
        }
    }

    public static void handleAuth(OAuth2Response authResponse, Long entityId, String entityRole) {
        RestAPI.getOauth2Interceptor().setOAuth2Response(authResponse);

        Gson gson = new Gson();
        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putString("oauth", gson.toJson(authResponse));
        edit.putString("role", entityRole);
        edit.putLong("entityId", entityId);
        edit.commit();

        isAuthenticated = true;
        ENTITY_ID = mSharedPreferences.getLong("entityId", 0L);
        ENTITY_ROLE = mSharedPreferences.getString("role", "USER");
    }

    public static SharedPreferences getSharedPreferences() {
        return SymptomManagement.mSharedPreferences;
    }

    public static boolean isIsAuthenticated() {
        return SymptomManagement.isAuthenticated;
    }

    public static Context getAppContext() {
        return SymptomManagement.context;
    }

    public static String getCurrentRole() {
        return SymptomManagement.ENTITY_ROLE;
    }

    public static Long getCurrentEntityId() {
        return SymptomManagement.ENTITY_ID;
    }


    public static void resetAuth() {
        isAuthenticated = false;
        RestAPI.getOauth2Interceptor().setOAuth2Response(null);

        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.remove("oauth");
        edit.remove("role");
        edit.remove("entityId");
        edit.commit();
    }


    public static void initializeDoctorAlerts() {
        Log.d(SymptomManagement.LOG_ID, "Initializing doctor alerts...");

        Intent intent = new Intent(SymptomManagement.context, PatientAlertBroadcastReceiver.class);
        intent.putExtra("alarmId", PatientAlertBroadcastReceiver.REQUEST_CODE);
        intent.putExtra("doctorId", getCurrentEntityId());
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, PatientAlertBroadcastReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setup periodic alarm every 30 seconds
        long firstMillis = System.currentTimeMillis();
        int intervalMillis = 30 * 1000; // 30 seconds

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pIntent);
    }

    public static void cancelDoctorAlerts() {
        Log.d(SymptomManagement.LOG_ID, "Cancelling doctor alerts...");

        Intent intent = new Intent(SymptomManagement.context, PatientAlertBroadcastReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(context, PatientAlertBroadcastReceiver.REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    public static void initalizePatientReminders() {
        Log.d(SymptomManagement.LOG_ID, "Setting new alarms...");

        if (!mSharedPreferences.contains("sm-patient-checkin-1")) {
            Log.d(SymptomManagement.LOG_ID, "Setting alarms1...");
            long alarmTime = getAlarm("sm-patient-checkin-1").getTimeInMillis();
            resetReminderAt("sm-patient-checkin-1", alarmTime);
        }

        if (!mSharedPreferences.contains("sm-patient-checkin-2")) {
            Log.d(SymptomManagement.LOG_ID, "Setting alarms2...");
            long alarmTime = getAlarm("sm-patient-checkin-2").getTimeInMillis();
            resetReminderAt("sm-patient-checkin-2", alarmTime);
        }

        if (!mSharedPreferences.contains("sm-patient-checkin-3")) {
            Log.d(SymptomManagement.LOG_ID, "Setting alarms3...");
            long alarmTime = getAlarm("sm-patient-checkin-3").getTimeInMillis();
            resetReminderAt("sm-patient-checkin-3", alarmTime);
        }

        if (!mSharedPreferences.contains("sm-patient-checkin-4")) {
            Log.d(SymptomManagement.LOG_ID, "Setting alarms4...");
            long alarmTime = getAlarm("sm-patient-checkin-4").getTimeInMillis();
            resetReminderAt("sm-patient-checkin-4", alarmTime);
        }
    }

    public static Calendar getAlarm(String key) {
        Calendar alarm = Calendar.getInstance();

        if (mSharedPreferences.contains(key)) {
            alarm.setTimeInMillis(mSharedPreferences.getLong(key, 0L));
        }
        else {
            int keyId = Integer.valueOf("" + key.charAt(key.length() - 1));
            int timeOffset = 9 + ((keyId - 1)  * 3);

            alarm.setTimeInMillis(System.currentTimeMillis());
            alarm.set(Calendar.HOUR_OF_DAY, timeOffset);
            alarm.set(Calendar.MINUTE, 0);
            alarm.set(Calendar.SECOND, 0);
            alarm.set(Calendar.MILLISECOND, 0);

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putLong(key, alarm.getTimeInMillis());
            editor.commit();
        }

        return alarm;
    }

    public static void resetReminderAt(String key, long timeInMillis) {
        int alarmId = 0;

        if ("sm-patient-checkin-1".equals(key)) {
            alarmId = ALARM_1;
        }
        else if ("sm-patient-checkin-2".equals(key)) {
            alarmId = ALARM_2;
        }
        else if ("sm-patient-checkin-3".equals(key)) {
            alarmId = ALARM_3;
        }
        else {
            alarmId = ALARM_4;
        }

        Log.d(LOG_ID, "Setting repeating alarm " + alarmId + " at " + timeInMillis);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent reminderIntent = new Intent(context, CheckinReminderBroadcastReciever.class);
        reminderIntent.putExtra("alarmId", alarmId);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timeInMillis, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(context, alarmId, reminderIntent, PendingIntent.FLAG_UPDATE_CURRENT));

        SharedPreferences.Editor edit = mSharedPreferences.edit();
        edit.putLong(key, timeInMillis);
        edit.commit();
    }
}