package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

import java.util.Collections;
import java.util.List;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Medicine;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.MedicineResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.MedicineTaken;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckin;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.util.RetrofitResponseDecorator;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 */
public class CheckinMedicineActivity extends FragmentActivity {

    TextView mQuestionView;
    Button mNoButton;
    Button mYesButton;
    String mMedicineTakenRef;
    int mMedicineIndex;
    int mNextMedicineIndex = -1;
    boolean mLastMed = false;
    PatientCheckin mPatientCheckin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_medicine);

        mQuestionView = (TextView) findViewById(R.id.medicineQuestionView);
        mNoButton = (Button) findViewById(R.id.noButton);
        mYesButton = (Button) findViewById(R.id.yesButton);

        mPatientCheckin = getIntent().getParcelableExtra("patientCheckin");

        RestAPI.getSymptomMgmtAPI().getPatientMedicines(SymptomManagement.getCurrentEntityId(), new Callback<SDRFindAllResponse<MedicineResponse>>() {
            @Override
            public void success(SDRFindAllResponse<MedicineResponse> medicineResponseSDRFindAllResponse, Response response) {
                if (medicineResponseSDRFindAllResponse == null || medicineResponseSDRFindAllResponse.getEmbedded() == null) {
                    // skip these questions
                    Intent eatingIntent = new Intent(getApplicationContext(), CheckinEatingActivity.class);
                    eatingIntent.putExtra(CheckinPainActivity.EXTRA_PATIENT_CHECKIN, mPatientCheckin);
                    startActivity(eatingIntent);
                    finish();
                }
                else {
                    List<Medicine> medicineList = medicineResponseSDRFindAllResponse.getEmbedded().getMedicine();

                    Collections.sort(medicineList);

                    mMedicineIndex = getIntent().getIntExtra("medicineId", 0); // -1 will indicate we're at the end

                    if (mMedicineIndex >= medicineList.size()) {
                        // skip these questions
                        Intent eatingIntent = new Intent(getApplicationContext(), CheckinEatingActivity.class);
                        eatingIntent.putExtra(CheckinPainActivity.EXTRA_PATIENT_CHECKIN, mPatientCheckin);
                        startActivity(eatingIntent);
                        finish();
                    }
                    else {
                        Medicine m = medicineList.get(mMedicineIndex);
                        mQuestionView.setText("Question:\n\nDid you take your " + m.getGenericName() + "?");
                        mMedicineTakenRef = m.getLinks().get("self").getHref();

                        mNextMedicineIndex = mMedicineIndex + 1;

                        if (mNextMedicineIndex == medicineList.size()) {
                            mLastMed = true;
                        }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(CheckinMedicineActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });

        mYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(CheckinMedicineActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("When Taken?");

                Button okButton = (Button) dialog.findViewById(R.id.dateOkButton);
                final DatePicker dp = (DatePicker) dialog.findViewById(R.id.datePicker1);
                final TimePicker tp = (TimePicker) dialog.findViewById(R.id.timePicker1);

                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocalDateTime ldt = new LocalDateTime(dp.getYear(), dp.getMonth(), dp.getDayOfMonth(), tp.getCurrentHour(), tp.getCurrentMinute());

                        MedicineTaken medicineTaken = new MedicineTaken();
                        medicineTaken.setMedicine(mMedicineTakenRef);
                        medicineTaken.setTakenAt(ldt);

                        saveMedicineTaken(medicineTaken);

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        mNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MedicineTaken medicineTaken = new MedicineTaken();
                medicineTaken.setMedicine(mMedicineTakenRef);
                medicineTaken.setTakenAt(null);
                saveMedicineTaken(medicineTaken);
            }
        });
    }

    private void saveMedicineTaken(MedicineTaken medicineTaken) {
        RestAPI.getSymptomMgmtAPI().doMedicineTaken(medicineTaken, new Callback<Response>() {
            @Override
            public void success(Response reply, Response response) {

                RetrofitResponseDecorator dResponse = new RetrofitResponseDecorator(response);
                String locRef = dResponse.getHeader("Location");
                Log.d(SymptomManagement.LOG_ID, "Added at " + locRef);
                mPatientCheckin.addMedicineTakenRef(locRef);

                if (!mLastMed) {
                    Intent medIntent = new Intent(getApplicationContext(), CheckinMedicineActivity.class);
                    medIntent.putExtra(CheckinPainActivity.EXTRA_PATIENT_CHECKIN, mPatientCheckin);
                    medIntent.putExtra("medicineId", mNextMedicineIndex);
                    startActivity(medIntent);
                    finish();
                }
                else {
                    Intent eatingIntent = new Intent(getApplicationContext(), CheckinEatingActivity.class);
                    eatingIntent.putExtra(CheckinPainActivity.EXTRA_PATIENT_CHECKIN, mPatientCheckin);
                    startActivity(eatingIntent);
                    finish();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(CheckinMedicineActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });
    }
}