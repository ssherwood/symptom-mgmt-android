package mobile.symptom.androidcapstone.coursera.org.symptommanagement.model;

import com.google.gson.annotations.SerializedName;

public class SDRFindAllResponse<T> {

	@SerializedName("_embedded")
	private T embedded;

	private Page page;

	//
	
	public T getEmbedded() {
		return embedded;
	}

	public void setEmbedded(T embedded) {
		this.embedded = embedded;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	
	/**
	 * 
	 */
	public class Page {
		private Integer size;
		private Integer totalElements;
		private Integer totalPages;
		private Integer number;

		public Integer getSize() {
			return size;
		}

		public void setSize(Integer size) {
			this.size = size;
		}

		public Integer getTotalElements() {
			return totalElements;
		}

		public void setTotalElements(Integer totalElements) {
			this.totalElements = totalElements;
		}

		public Integer getTotalPages() {
			return totalPages;
		}

		public void setTotalPages(Integer totalPages) {
			this.totalPages = totalPages;
		}

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}
	}
}