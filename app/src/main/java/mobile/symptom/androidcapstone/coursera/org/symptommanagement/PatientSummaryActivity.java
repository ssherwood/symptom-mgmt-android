package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.LocalDateTime;

import java.util.ArrayList;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Alert;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.AlertResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Patient;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PatientSummaryActivity extends Activity {

    TextView mPatientName;
    TextView mPatientMedicalId;
    TextView mDateOfBirth;

    ListView mPatientAlertsView;
    PatientAlertsAdapter mPatientAlertsAdapter;

    Patient mCurrentPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_summary);

        mCurrentPatient = (Patient) getIntent().getParcelableExtra("patient");

        mPatientName = (TextView) findViewById(R.id.patientNameLabel);
        mPatientName.setText(mCurrentPatient.getFirstName() + " " + mCurrentPatient.getLastName());

        mPatientMedicalId = (TextView) findViewById(R.id.patientMedicalIdView);
        mPatientMedicalId.setText(mCurrentPatient.getMedicalId());

        mDateOfBirth = (TextView) findViewById(R.id.dateOfBirthView);
        mDateOfBirth.setText(mCurrentPatient.getDateOfBirth().toString("MM/dd/yyyy"));

        mPatientAlertsView = (ListView) findViewById(R.id.patientAlertsListView);
        //mPatientAlertsView.setEmptyView(findViewById(R.id.empty));

        TextView headerView = new TextView(getApplicationContext());
        headerView.setText("Patient Alerts");

        final TextView footerView = new TextView(getApplicationContext());
        footerView.setText("No Alerts!");

        mPatientAlertsView.addHeaderView(headerView);

        ArrayList<Alert> arrayOfAlerts = new ArrayList<>();
        mPatientAlertsAdapter = new PatientAlertsAdapter(this, arrayOfAlerts);
        mPatientAlertsView.setAdapter(mPatientAlertsAdapter);


        RestAPI.getSymptomMgmtAPI().getPatientAlerts(mCurrentPatient.getId(), new Callback<SDRFindAllResponse<AlertResponse>>() {
            @Override
            public void success(SDRFindAllResponse<AlertResponse> alertResponseSDRFindAllResponse, Response response) {

                mPatientAlertsAdapter.clear();

                if (alertResponseSDRFindAllResponse.getEmbedded() != null) {
                    mPatientAlertsView.removeFooterView(footerView);
                    mPatientAlertsAdapter.addAll(alertResponseSDRFindAllResponse.getEmbedded().getPatientAlerts());
                } else {
                    mPatientAlertsView.addFooterView(footerView);
                }

                mPatientAlertsAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientSummaryActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });


        mPatientAlertsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(SymptomManagement.LOG_ID, "Alert clicked!");

                Alert alert = (Alert)mPatientAlertsView.getItemAtPosition(position);

                Alert patch = new Alert();
                patch.setAcknowledgedAt(LocalDateTime.now());

                RestAPI.getSymptomMgmtAPI().acknowledgeAlert(alert.getId(), patch, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        Toast.makeText(PatientSummaryActivity.this, "Acknowledged", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(PatientSummaryActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_patient_summary, menu);
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
            return true;
        }
        else if (id == R.id.action_patient_checkins) {
            Intent intent = new Intent(getApplicationContext(), PatientCheckinActivity.class);
            intent.putExtra("patient", mCurrentPatient);
            startActivity(intent);
        }
        else if (id == R.id.action_patient_medicine) {
            Intent intent = new Intent(getApplicationContext(), PatientMedicineActivity.class);
            intent.putExtra("patient", mCurrentPatient);
            startActivity(intent);
        }
        else if (id == R.id.action_logout) {
            Log.d(SymptomManagement.LOG_ID, "Logging out...");

            Intent intent = new Intent(getApplicationContext(), LaunchActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }



    public static class PatientAlertsAdapter extends ArrayAdapter<Alert> {

        private static class ViewHolder {
            TextView alertedAt;
            TextView alertDescription;
        }

        public PatientAlertsAdapter(Context context, ArrayList<Alert> patients) {
            super(context, R.layout.item_alerts, patients);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_alerts, parent, false);
                viewHolder.alertedAt = (TextView) convertView.findViewById(R.id.alertTimeTextView);
                viewHolder.alertDescription = (TextView) convertView.findViewById(R.id.alertDescriptionTextView);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Alert patientAlert = getItem(position);
            viewHolder.alertedAt.setText(patientAlert.getAlertedAt().toString());
            viewHolder.alertDescription.setText(patientAlert.getAlertType());

            return convertView;
        }
    }
}
