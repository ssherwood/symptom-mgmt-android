package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckin;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.util.RetrofitResponseDecorator;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.util.TypedStringHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class CheckinEatingActivity extends Activity {


    Button mNo;
    Button mSomeEat;
    Button mCantEat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_eating);

        mNo = (Button) findViewById(R.id.noButton);
        mSomeEat = (Button) findViewById(R.id.someEatButton);
        mCantEat = (Button) findViewById(R.id.cantEatButton);

        final PatientCheckin patientCheckin = getIntent().getParcelableExtra("patientCheckin");

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";

                switch (v.getId()) {
                    case R.id.noButton:
                        result = PatientCheckin.EATING_STATUS_YES;
                        break;
                    case R.id.someEatButton:
                        result = PatientCheckin.EATING_STATUS_SOME;
                        break;
                    case R.id.cantEatButton:
                        result = PatientCheckin.EATING_STATUS_NO;
                        break;
                }

                patientCheckin.setEatingStatus(result);

                RestAPI.getSymptomMgmtAPI().doCheckin(patientCheckin, new Callback<Response>() {
                    @Override
                    public void success(Response result, Response response) {

                        RetrofitResponseDecorator dResponse = new RetrofitResponseDecorator(response);
                        String locRef = dResponse.getHeader("Location");

                        Toast.makeText(CheckinEatingActivity.this, "SUCCESS " + locRef, Toast.LENGTH_LONG).show();

                        for (String medicineTakenRef : patientCheckin.getMedicineTakenRefs()) {
                            final Long aRefId = Long.valueOf(medicineTakenRef.substring(medicineTakenRef.lastIndexOf("/") + 1));

                            RestAPI.getSymptomMgmtAPI().associateMedicineTakenToCheckin(aRefId, TypedStringHelper.build(locRef), new Callback<Response>() {
                                @Override
                                public void success(Response response, Response response2) {
                                    Log.d(SymptomManagement.LOG_ID, "Added ref for " + aRefId);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Toast.makeText(CheckinEatingActivity.this, "Error " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                        Intent patientHome = new Intent(CheckinEatingActivity.this, PatientHomeActivity.class);
                        startActivity(patientHome);
                        finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(CheckinEatingActivity.this, "Error: " + error.getResponse(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        mNo.setOnClickListener(onClickListener);
        mSomeEat.setOnClickListener(onClickListener);
        mCantEat.setOnClickListener(onClickListener);
    }
}
