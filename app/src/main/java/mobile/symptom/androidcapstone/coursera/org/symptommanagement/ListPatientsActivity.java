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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Patient;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ListPatientsActivity extends Activity {

    private ListView mPatientListView;
    private PatientAdapter mPatientAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_patients);

        ArrayList<Patient> arrayOfUsers = new ArrayList<>();
        mPatientAdapter = new PatientAdapter(this, arrayOfUsers);

        mPatientListView = (ListView)findViewById(R.id.patientListView);
        mPatientListView.setAdapter(mPatientAdapter);

        RestAPI.getSymptomMgmtAPI().getPatientsByDoctorId(SymptomManagement.getCurrentEntityId(), new Callback<SDRFindAllResponse<PatientResponse>>() {
            @Override
            public void success(SDRFindAllResponse<PatientResponse> patientResponseSDRFindAllResponse, Response response) {
                mPatientAdapter.clear();

                if (patientResponseSDRFindAllResponse != null && patientResponseSDRFindAllResponse.getEmbedded() != null) {
                    mPatientAdapter.addAll(patientResponseSDRFindAllResponse.getEmbedded().getPatients());
                }

                mPatientAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(ListPatientsActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });

        mPatientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient patient = mPatientAdapter.getItem(position);
                Intent patientSummaryIntent = new Intent(ListPatientsActivity.this, PatientSummaryActivity.class);
                patientSummaryIntent.putExtra("patient", patient);
                startActivity(patientSummaryIntent);
            }
        });


        mSearchView = (SearchView) findViewById(R.id.searchView);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                RestAPI.getSymptomMgmtAPI().getPatientsByDoctorIdThatMatchTerm(SymptomManagement.getCurrentEntityId(), newText.toLowerCase(), new Callback<SDRFindAllResponse<PatientResponse>>() {
                    @Override
                    public void success(SDRFindAllResponse<PatientResponse> patientResponseSDRFindAllResponse, Response response) {

                        mPatientAdapter.clear();

                        if (patientResponseSDRFindAllResponse != null && patientResponseSDRFindAllResponse.getEmbedded() != null) {
                            mPatientAdapter.addAll(patientResponseSDRFindAllResponse.getEmbedded().getPatients());
                        }
                        else {
                            //mPatientListView.addFooterView();
                        }

                        mPatientAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(ListPatientsActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
                    }
                });

                return true;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_patients, menu);
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




    public static class PatientAdapter extends ArrayAdapter<Patient> {

        private static class ViewHolder {
            TextView name;
            TextView email;
        }

        public PatientAdapter(Context context, ArrayList<Patient> patients) {
            super(context, R.layout.item_patients, patients);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_patients, parent, false);
                viewHolder.name = (TextView) convertView.findViewById(R.id.nameTextView);
                viewHolder.email = (TextView) convertView.findViewById(R.id.emailTextView);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Patient patient = getItem(position);
            viewHolder.name.setText(patient.getFirstName() + " " + patient.getLastName());
            viewHolder.email.setText(patient.getEmailAddress());

            return convertView;
        }
    }

}
