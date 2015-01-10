package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import com.squareup.okhttp.Call;

import java.lang.reflect.Type;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Alert;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.AlertDetailResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.AlertResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Doctor;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.DoctorResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Medicine;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.MedicineResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.MedicineTaken;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.Patient;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckin;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientCheckinResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.PatientResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.model.SDRFindAllResponse;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.oauth.OAuth2Response;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.PATCH;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedString;

/**
 */
public interface SymptomMgmtAPI {
    final String TOKEN_PATH = "/oauth/token";

    @FormUrlEncoded
    @POST(TOKEN_PATH)
    public OAuth2Response getAuth(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST(TOKEN_PATH)
    public void getAuthViaCallback(@Field("username") String username, @Field("password") String password, @Field("grant_type") String grantType, Callback<OAuth2Response> handler);


    @GET("/doctors")
    public SDRFindAllResponse<DoctorResponse> getDoctors();

    @GET("/doctors/{id}")
    public Doctor getDoctor(@Path("id") Long id);

    @POST("/doctors")
    public Response addDoctor(@Body Doctor doctor);

    @PATCH("/doctors/{id}")
    public Response patchDoctor(@Path("id") Long id, @Body Doctor doctor);

    @Headers({ "Content-type: text/uri-list"})
    @PUT("/doctors/{id}/patients")
    public Response associatePatientToDoctor(@Path("id") Long id, @Body TypedString hrefs);


    @GET("/patients")
    public SDRFindAllResponse<PatientResponse> getPatients();

    @GET("/patients")
    public void getPatients(Callback<SDRFindAllResponse<PatientResponse>> callback);

    @POST("/patients")
    public Response addPatient(@Body Patient patient);

    @GET("/patients/{id}")
    public void getPatient(@Path("id") Long id, Callback<Patient> patientCallback);

    @GET("/patients/search/findByDoctors_Id?sort=lastName,asc")
    public void getPatientsByDoctorId(@Query("doctorId") Long id, Callback<SDRFindAllResponse<PatientResponse>> patientResponseCallback);

    // https://localhost:8443/patients/search/findByNameForDoctor?doctorId=1&searchTerm=m
    @GET("/patients/search/findByNameForDoctor?sort=lastName,asc")
    public void getPatientsByDoctorIdThatMatchTerm(@Query("doctorId") Long id, @Query("searchTerm") String searchTerm, Callback<SDRFindAllResponse<PatientResponse>> patientResponseCallback);

    @GET("/patients/{id}/checkins")
    public void getPatientCheckins(@Path("id") Long id, Callback<SDRFindAllResponse<PatientCheckinResponse>> patientCheckinCallback);

    // https://localhost:8443/patientCheckins/search/findByPatient_Id?patientId=1&sort=checkinAt,desc
    @GET("/patientCheckins/search/findByPatient_Id?sort=checkinAt,desc")
    public void getCheckinsByPatient(@Query("patientId") Long patientId, Callback<SDRFindAllResponse<PatientCheckinResponse>> patientCheckinCallback);

    @GET("/patients/{id}/medicine")
    public void getPatientMedicines(@Path("id") Long patientId, Callback<SDRFindAllResponse<MedicineResponse>> callback);

    @PATCH("/patients/{id}")
    public Response patchPatient(@Path("id") Long id, @Body Patient patient);

    @Headers({ "Content-type: text/uri-list"})
    @PUT("/patients/{id}/medicine")
    public void associateMedicineToPatient(@Path("id") Long id, @Body TypedString hrefs, Callback<Response> response);

    @Headers({ "Content-type: text/uri-list"})
    @DELETE("/patients/{id}/medicine/{medicineId}")
    public Response disassociateMedicineToPatient(@Path("id") Long id, @Path("medicineId") Long medicineId);

    @GET("/patients/{id}/alerts")
    public void getPatientAlerts(@Path("id") Long patientId, Callback<SDRFindAllResponse<AlertResponse>> callback);

    @POST("/patientCheckins")
    public void doCheckin(@Body PatientCheckin patientCheckin, Callback<Response> responseCallback);

    @POST("/medicineTakens")
    public void doMedicineTaken(@Body MedicineTaken medicineTaken, Callback<Response> responseCallback);

    @Headers({ "Content-type: text/uri-list"})
    @PUT("/medicineTakens/{id}/patientCheckin")
    public void associateMedicineTakenToCheckin(@Path("id") Long medicineTakenId, @Body TypedString hrefs, Callback<Response> responseCallback);

    @GET("/medicine?sort=genericName")
    public void getAllMedicines(Callback<SDRFindAllResponse<MedicineResponse>> callback);


    //https://localhost:8443/patientAlerts/search/findByPatient_Doctors_IdAndAcknowledgedAtIsNull?doctorId=2
    @GET("/patientAlerts/search/findByPatient_Doctors_IdAndAcknowledgedAtIsNull?projection=alertDetail")
    public void getUnacknowledgedAlertsForADoctor(@Query("doctorId") Long doctorId, Callback<SDRFindAllResponse<AlertDetailResponse>> callback);

    @PATCH("/patientAlerts/{id}")
    public void acknowledgeAlert(@Path("id") Long alertId, @Body Alert alert, Callback<Response> responseCallback);
}
