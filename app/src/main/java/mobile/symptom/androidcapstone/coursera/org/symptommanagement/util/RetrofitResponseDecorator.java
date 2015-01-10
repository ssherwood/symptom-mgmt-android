package mobile.symptom.androidcapstone.coursera.org.symptommanagement.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.mime.TypedInput;

public class RetrofitResponseDecorator {

	private Response response;
	private Map<String, String> headers = new HashMap<>();

	public RetrofitResponseDecorator(Response response) {
		this.response = response;
		
		// decorate poor header implementation with a map
		for (Header h : response.getHeaders()) {
			headers.put(h.getName(), h.getValue());
		}
	}

	/** Request URL. */
	public String getUrl() {
		return response.getUrl();
	}

	/** Status line code. */
	public int getStatus() {
		return response.getStatus();
	}

	/** Status line reason phrase. */
	public String getReason() {
		return response.getReason();
	}

	/** An unmodifiable collection of headers. */
	public List<Header> getHeaders() {
		return response.getHeaders();
	}

	/** Response body. May be {@code null}. */
	public TypedInput getBody() {
		return response.getBody();
	}
	
	/** Custom wrapper for header values */
	public String getHeader(String key) {
		return headers.get(key);
	}
}
