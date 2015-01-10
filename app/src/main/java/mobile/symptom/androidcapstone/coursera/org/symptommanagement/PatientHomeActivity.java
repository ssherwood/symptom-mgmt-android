package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Patient;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckin;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckinResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PatientHomeActivity extends Activity {

    private TextView mPatientGreeting;
    private TextView mPatientSummary;
    private Button mPerformCheckinButton;
    private Button mReviewVideoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        mPatientGreeting = (TextView) findViewById(R.id.greetingPatientView);
        mPatientGreeting.setText("Loading...");

        mPatientSummary = (TextView) findViewById(R.id.patientSummaryView);
        mPatientSummary.setText("");

        mPerformCheckinButton = (Button) findViewById(R.id.doCheckinButton);

        mPerformCheckinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkinIntent = new Intent(PatientHomeActivity.this, CheckinPainActivity.class);
                startActivity(checkinIntent);
            }
        });


        mReviewVideoButton = (Button) findViewById(R.id.doVideoButton);

        mReviewVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(getApplicationContext(), ViewVideoActivity.class);
                startActivity(videoIntent);
            }
        });

        RestAPI.getSymptomMgmtAPI().getPatient(SymptomManagement.getCurrentEntityId(), new Callback<Patient>() {
            @Override
            public void success(Patient patient, Response response) {
                mPatientGreeting.setText("Hello, " + patient.getFirstName() + " " + patient.getLastName());
//                SymptomManagement.setPatient(patient);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientHomeActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });


        RestAPI.getSymptomMgmtAPI().getPatientCheckins(SymptomManagement.getCurrentEntityId(), new Callback<SDRFindAllResponse<PatientCheckinResponse>>() {
            @Override
            public void success(SDRFindAllResponse<PatientCheckinResponse> patientCheckinResponseSDRFindAllResponse, Response response) {
                if (patientCheckinResponseSDRFindAllResponse == null || patientCheckinResponseSDRFindAllResponse.getEmbedded() == null) {
                    mPatientSummary.setText("You have no check-ins yet!");
                }
                else {
                    List<PatientCheckin> patientCheckinList = patientCheckinResponseSDRFindAllResponse.getEmbedded().getPatientCheckins();
                    PatientCheckin patientCheckin = patientCheckinList.get(patientCheckinList.size() - 1);

                    mPatientSummary.setText(patientCheckin.toString());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientHomeActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        }
        else if (id == R.id.action_logout) {

            Log.d(SymptomManagement.LOG_ID, "Logging out...");
            SymptomManagement.resetAuth();
            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
