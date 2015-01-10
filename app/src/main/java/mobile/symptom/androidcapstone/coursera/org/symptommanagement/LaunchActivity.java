package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.util.Pools;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.SynchronousQueue;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.oauth.OAuth2Response;

/**
 */
public class LaunchActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            SymptomManagement.resetAuth();
            finish();
        }
        else {
            if (SymptomManagement.isIsAuthenticated()) {
                // todo should we refresh oauth every time?
                // if yes, we should do it here before launching the activity
                // maybe we should check the oauth timeout?  maybe we need to track the last time
                // we refreshed...
                launchUserActivity();
            } else {
                startActivityForResult(new Intent(LaunchActivity.this, LoginActivity.class), LoginActivity.LOGIN_RESULT);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case (LoginActivity.LOGIN_RESULT) : {
                if (resultCode == Activity.RESULT_OK) {
                    OAuth2Response authResponse = data.getParcelableExtra("oauth");
                    Long entityId = data.getLongExtra("entityId", 0L);
                    String entityRole =  data.getStringExtra("role");

                    Log.d(SymptomManagement.LOG_ID, "entityId = " + entityId + " & entityRole = " + entityRole);
                    SymptomManagement.handleAuth(authResponse, entityId, entityRole);
                }
                else if (resultCode == RESULT_CANCELED) {
                    SymptomManagement.resetAuth();
                    finish();
                }
                break;
            }
        }

        launchUserActivity();
    }

    private void launchUserActivity() {

        switch (SymptomManagement.getCurrentRole()) {
            case "PATIENT":
                SymptomManagement.cancelDoctorAlerts();
                SymptomManagement.initalizePatientReminders();
                startActivity(new Intent(LaunchActivity.this, PatientHomeActivity.class));
                finish();
                break;
            case "DOCTOR":
                // todo SymptomManagement.cancelPatientReminders();
                SymptomManagement.initializeDoctorAlerts();
                startActivity(new Intent(LaunchActivity.this, ListPatientsActivity.class));
                break;
            default:
                Toast.makeText(LaunchActivity.this, "Invalid Role", Toast.LENGTH_LONG).show();
        }

        finish();
    }


}