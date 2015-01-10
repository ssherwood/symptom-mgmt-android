package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckin;

/**
 *
 */
public class CheckinPainActivity extends Activity {

    public static final String EXTRA_PATIENT_CHECKIN = "patientCheckin";

    Button mWellControlledButton;
    Button mModerateButton;
    Button mSevereButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_pain);

        mWellControlledButton = (Button) findViewById(R.id.wellControlledButton);
        mModerateButton = (Button) findViewById(R.id.moderateButton);
        mSevereButton = (Button) findViewById(R.id.severeButton);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

                switch (v.getId()) {
                    case R.id.wellControlledButton:
                        result = PatientCheckin.PAIN_STATUS_CONTROLLED;
                        break;
                    case R.id.moderateButton:
                        result = PatientCheckin.PAIN_STATUS_MODERATE;
                        break;
                    case R.id.severeButton:
                        result = PatientCheckin.PAIN_STATUS_SEVERE;
                        break;
                }

                PatientCheckin patientCheckin = new PatientCheckin();
                // todo there should be a cleaner way to do this
                patientCheckin.setPatient(RestAPI.REST_URL + "/patient/" + SymptomManagement.getCurrentEntityId());
                patientCheckin.setPainStatus(result);

                Intent intent = new Intent(CheckinPainActivity.this, CheckinMedicineActivity.class);
                intent.putExtra(EXTRA_PATIENT_CHECKIN, patientCheckin);
                startActivity(intent);
                finish();
            }
        };

        mWellControlledButton.setOnClickListener(onClickListener);
        mModerateButton.setOnClickListener(onClickListener);
        mSevereButton.setOnClickListener(onClickListener);
    }
}
