package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import com.fatboyindustrial.gsonjodatime.Converters;
import com.google.gson.GsonBuilder;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.oauth.OAuth2RequestInterceptor;
import mobile.symptom.androidcapstone.coursera.org.symptommanagement.util.OkHttpClientHelper;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;

/**
 */
public class RestAPI {
    public static final String REST_URL = "https://10.0.2.2:8443";

    private static SymptomMgmtAPI symptomMgmtAPI;
    private static OAuth2RequestInterceptor oauth2Interceptor;

    static {
        oauth2Interceptor = new OAuth2RequestInterceptor("android-symptoms-management", "secret");

        // todo need a better way to get the context
        symptomMgmtAPI = new RestAdapter.Builder()
                .setClient(new OkClient(OkHttpClientHelper.getCustomSslOkHttpClient(SymptomManagement.getAppContext())))
                .setRequestInterceptor(oauth2Interceptor)
                .setConverter(new GsonConverter(Converters.registerAll(new GsonBuilder()).create()))
                .setEndpoint(REST_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL).build()
                .create(SymptomMgmtAPI.class);
    }

    public static SymptomMgmtAPI getSymptomMgmtAPI() {
        return symptomMgmtAPI;
    }

    public static OAuth2RequestInterceptor getOauth2Interceptor() {
        return oauth2Interceptor;
    }
}
