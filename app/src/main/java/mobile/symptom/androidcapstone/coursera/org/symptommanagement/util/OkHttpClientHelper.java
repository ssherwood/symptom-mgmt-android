package mobile.symptom.androidcapstone.coursera.org.symptommanagement.util;

import android.content.Context;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import com.squareup.okhttp.OkHttpClient;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.R;

public class OkHttpClientHelper {

	private static final char[] KEYSTORE_PASSWORD = "changeit".toCharArray();

	public static OkHttpClient getCustomSslOkHttpClient(Context context) {
        SSLContext sslContext = null;

        try {
            KeyStore keyStore = readKeyStoreAsResource(context);

            sslContext = SSLContext.getInstance("SSL");

            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEYSTORE_PASSWORD);

            sslContext.init(keyManagerFactory.getKeyManagers(),
                    trustManagerFactory.getTrustManagers(), new SecureRandom());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

		OkHttpClient client = new OkHttpClient();
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
		client.setSslSocketFactory(sslContext.getSocketFactory());

		return client;
	}

	private static KeyStore readKeyStoreAsResource(Context context) throws Exception {
		KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
		InputStream inputStream = context.getResources().openRawResource(R.raw.mytruststore);

        try {
			keystore.load(inputStream, KEYSTORE_PASSWORD);
		} finally {
			if (inputStream != null) {
                inputStream.close();
			}
		}

		return keystore;
	}
}
