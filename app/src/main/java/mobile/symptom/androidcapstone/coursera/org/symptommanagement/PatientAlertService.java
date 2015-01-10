package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Alert;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.AlertDetail;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.AlertDetailResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.AlertResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 */
public class PatientAlertService extends IntentService {

    public PatientAlertService() {
        super("PatientAlertService");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        Log.i("PatientAlertService", "Service is running");

        long doctorId = intent.getLongExtra("doctorId", -1L);

        if (doctorId > -1L) {
            RestAPI.getSymptomMgmtAPI().getUnacknowledgedAlertsForADoctor(doctorId, new Callback<SDRFindAllResponse<AlertDetailResponse>>() {
                @Override
                public void success(SDRFindAllResponse<AlertDetailResponse> alertResponseSDRFindAllResponse, Response response) {

                    if (alertResponseSDRFindAllResponse != null && alertResponseSDRFindAllResponse.getEmbedded() != null) {
                        for (AlertDetail alert : alertResponseSDRFindAllResponse.getEmbedded().getPatientAlerts()) {
                            Log.d(SymptomManagement.LOG_ID, "Alert found for " + alert.getPatient().getFirstName() + " " + alert.getPatient().getLastName());

                            Intent alertIntent = new Intent(getApplicationContext(), PatientSummaryActivity.class);
                            alertIntent.putExtra("patient", alert.getPatient());

                            Notification aNotification = new Notification.Builder(getApplicationContext())
                                    .setSmallIcon(R.drawable.ic_launcher)
                                    .setWhen(System.currentTimeMillis())
                                    .setContentTitle("Symptom Management")
                                    .setContentText("A patient had an alert!")
                                    .setAutoCancel(true)
                                    .setVibrate(new long[] { 500, 500, 500})
                                    .setContentIntent(PendingIntent.getActivity(getApplicationContext(), 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                                    .build();

                            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.notify(alert.getPatient().getId().intValue(), aNotification);

                        }
                    }
                    else {
                        Log.d(SymptomManagement.LOG_ID, "No alerts found");
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(SymptomManagement.LOG_ID, "Error in service " + error.getResponse().getReason());
                }
            });
        }
    }
}
