package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.ListActivity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Patient;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckin;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckinResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PatientCheckinActivity extends ListActivity {

    Patient mCurrentPatient;
    static Drawable green_ball;
    static Drawable yellow_ball;
    static Drawable red_ball;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_checkin);

        green_ball = getResources().getDrawable(R.drawable.ball_green);
        yellow_ball = getResources().getDrawable(R.drawable.ball_yellow);
        red_ball = getResources().getDrawable(R.drawable.ball_red);

        mCurrentPatient = (Patient) getIntent().getParcelableExtra("patient");

        setListAdapter(new PatientCheckinAdapter(this, new ArrayList<PatientCheckin>()));

        RestAPI.getSymptomMgmtAPI().getCheckinsByPatient(mCurrentPatient.getId(), new Callback<SDRFindAllResponse<PatientCheckinResponse>>() {
            @Override
            public void success(SDRFindAllResponse<PatientCheckinResponse> patientCheckinResponseSDRFindAllResponse, Response response) {
                ((PatientCheckinAdapter) getListAdapter()).clear();

                if (patientCheckinResponseSDRFindAllResponse != null && patientCheckinResponseSDRFindAllResponse.getEmbedded() != null) {
                    ((PatientCheckinAdapter) getListAdapter()).addAll(patientCheckinResponseSDRFindAllResponse.getEmbedded().getPatientCheckins());
                }

                ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientCheckinActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
    }


    public static class PatientCheckinAdapter extends ArrayAdapter<PatientCheckin> {

        private static class ViewHolder {
            TextView checkinDescription;
            ImageView painImageView;
            ImageView eatingImageView;
        }

        public PatientCheckinAdapter(Context context, ArrayList<PatientCheckin> patientCheckin) {
            super(context, R.layout.item_patient_checkin, patientCheckin);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_patient_checkin, parent, false);
                viewHolder.checkinDescription = (TextView) convertView.findViewById(R.id.checkinDescriptionView);
                viewHolder.painImageView = (ImageView) convertView.findViewById(R.id.painImagView);
                viewHolder.eatingImageView = (ImageView) convertView.findViewById(R.id.eatingImageView);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            PatientCheckin checkin = getItem(position);

            StringBuilder fmtString = new StringBuilder();
            fmtString.append(checkin.getCheckinAt().toString("MM/dd hh:mm a") + "\n\n");
            fmtString.append("The patient reported '" + checkin.getPainReadableStatus() + "'" + " pain and was " + checkin.getEatingReadableStatus() + ".");

            viewHolder.checkinDescription.setText(fmtString.toString());

            if (PatientCheckin.PAIN_STATUS_CONTROLLED.equals(checkin.getPainStatus())) {
                viewHolder.painImageView.setImageDrawable(green_ball);
            }
            else if (PatientCheckin.PAIN_STATUS_MODERATE.equals(checkin.getPainStatus())) {
                viewHolder.painImageView.setImageDrawable(yellow_ball);
            }
            else {
                viewHolder.painImageView.setImageDrawable(red_ball);
            }

            if (PatientCheckin.EATING_STATUS_YES.equals(checkin.getEatingStatus())) {
                viewHolder.eatingImageView.setImageDrawable(green_ball);
            }
            else if (PatientCheckin.EATING_STATUS_SOME.equals(checkin.getEatingStatus())) {
                viewHolder.eatingImageView.setImageDrawable(yellow_ball);
            }
            else {
                viewHolder.eatingImageView.setImageDrawable(red_ball);
            }

            return convertView;
        }
    }
}
