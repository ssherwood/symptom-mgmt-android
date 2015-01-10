package mobile.symptom.androidcapstone.coursera.org.symptommanagement.oauth;


import retrofit.RequestInterceptor;

import com.google.common.io.BaseEncoding;

public class OAuth2RequestInterceptor implements RequestInterceptor {

    private final String username;
    private final String password;
    private OAuth2Response oauth2Response;

    public OAuth2RequestInterceptor(final String username, final String password) {
		this.username = username;
		this.password = password;
		this.oauth2Response = null;
	}
    
    @Override
    public void intercept(RequestFacade requestFacade) {
        if (oauth2Response == null) {
            requestFacade.addHeader("Authorization", "Basic " + BaseEncoding.base64().encode((username + ":" + password).getBytes())); 
        }
        else {
        	requestFacade.addHeader("Authorization", "Bearer " + oauth2Response.getAccessToken());
        }
    }
    
    ///

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public OAuth2Response getOAuth2Response() {
		return oauth2Response;
	}

	public void setOAuth2Response(OAuth2Response oauth2Response) {
		this.oauth2Response = oauth2Response;
	}
}