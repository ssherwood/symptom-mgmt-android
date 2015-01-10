package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Medicine;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.MedicineResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Patient;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.util.TypedStringHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class PatientMedicineActivity extends ListActivity {

    Patient mCurrentPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medicine);

        mCurrentPatient = (Patient) getIntent().getParcelableExtra("patient");

        setListAdapter(new PatientMedicineAdapter(this, new ArrayList<Medicine>(), new ArrayList<Medicine>()));
        

        RestAPI.getSymptomMgmtAPI().getPatientMedicines(mCurrentPatient.getId(), new Callback<SDRFindAllResponse<MedicineResponse>>() {
            @Override
            public void success(SDRFindAllResponse<MedicineResponse> medicineResponseSDRFindAllResponse, Response response) {
                if (medicineResponseSDRFindAllResponse.getEmbedded() != null) {
                    ((PatientMedicineAdapter)getListAdapter()).setSelectedMedicines(medicineResponseSDRFindAllResponse.getEmbedded().getMedicine());
                    ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientMedicineActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });

        RestAPI.getSymptomMgmtAPI().getAllMedicines(new Callback<SDRFindAllResponse<MedicineResponse>>() {
            @Override
            public void success(SDRFindAllResponse<MedicineResponse> alertResponseSDRFindAllResponse, Response response) {

                ((ArrayAdapter) getListAdapter()).clear();

                if (alertResponseSDRFindAllResponse.getEmbedded() != null) {
                    //getListView().removeFooterView(footerView);
                    ((ArrayAdapter) getListAdapter()).addAll(alertResponseSDRFindAllResponse.getEmbedded().getMedicine());
                } else {
                    //getListView().addFooterView(footerView);
                }

                ((ArrayAdapter) getListAdapter()).notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientMedicineActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // when an item of the list is clicked
    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);

        Medicine selectedItem = (Medicine) getListView().getItemAtPosition(position);
        CheckBox medicineCheckBox = (CheckBox) view.findViewById(R.id.medicineCheckBox);

        if (medicineCheckBox.isChecked()) {
            ((PatientMedicineAdapter) getListAdapter()).getSelectedMedicines().remove(selectedItem);
        }
        else {
            ((PatientMedicineAdapter) getListAdapter()).getSelectedMedicines().add(selectedItem);
        }

        medicineCheckBox.toggle();

        saveSelectedItems();
    }

    private void saveSelectedItems() {
        List<String> medRefs = new ArrayList<>();

        for (int i = 0; i < getListAdapter().getCount(); i++) {
            View view = getListAdapter().getView(i, null, null);
            CheckBox medicineCheckBox = (CheckBox) view.findViewById(R.id.medicineCheckBox);

            if (medicineCheckBox.isChecked()) {
                Medicine m = (Medicine)getListAdapter().getItem(i);
                medRefs.add(m.getLinks().get("self").getHref());
            }
        }

        RestAPI.getSymptomMgmtAPI().associateMedicineToPatient(mCurrentPatient.getId(), TypedStringHelper.build(medRefs), new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                Toast.makeText(PatientMedicineActivity.this, "SUCCESS", Toast.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(PatientMedicineActivity.this, "Error: " + error.getResponse().getReason(), Toast.LENGTH_LONG).show();
            }
        });
    }


    public static class PatientMedicineAdapter extends ArrayAdapter<Medicine> {

        List<Medicine> selectedMedicine;

        public void setSelectedMedicines(List<Medicine> medicine) {
            this.selectedMedicine = medicine;
        }

        public List<Medicine> getSelectedMedicines() {
            return this.selectedMedicine;
        }

        private static class ViewHolder {
            CheckBox medicine;
            TextView medicineDescription;
        }

        public PatientMedicineAdapter(Context context, ArrayList<Medicine> allMedicine, ArrayList<Medicine> selectedMedicine) {
            super(context, R.layout.item_patient_medicine, allMedicine);
            this.selectedMedicine = selectedMedicine;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if (convertView == null) {
                viewHolder = new ViewHolder();

                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.item_patient_medicine, parent, false);
                viewHolder.medicine = (CheckBox) convertView.findViewById(R.id.medicineCheckBox);
                viewHolder.medicineDescription = (TextView) convertView.findViewById(R.id.medicineDescriptionView);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Medicine medicine = getItem(position);
            viewHolder.medicine.setText(medicine.getGenericName());
            viewHolder.medicine.setChecked(selectedMedicine.contains(medicine));
            viewHolder.medicineDescription.setText(medicine.getBrandName());

            return convertView;
        }
    }
}
