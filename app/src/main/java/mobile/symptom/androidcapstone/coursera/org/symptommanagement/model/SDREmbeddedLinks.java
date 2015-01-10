package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import java.util.Map;

import com.google.gson.annotations.SerializedName;

public class SDREmbeddedLinks {

	@SerializedName("_links")
	private Map<String, HRef> links;
	
	public Map<String, HRef> getLinks() {
		return links;
	}

	public void setLinks(Map<String, HRef> links) {
		this.links = links;
	}
	
	/**
	 * 
	 */
	public class HRef {
		private String href;

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}
	}
}
