package mobile.symptom.androidcapstone.coursera.org.symptommanagement.util;

import java.util.List;

import retrofit.mime.TypedString;

public class TypedStringHelper {

	public static <T> TypedString build(List<T> aList) {
		StringBuilder sb = new StringBuilder();
		
		for (T item : aList) {
			sb.append(item).append('\n');
		}
		
		return new TypedString(sb.toString());
	}
	
	public static TypedString build(String... items) {
		StringBuilder sb = new StringBuilder();
		
		for (String item : items) {
			sb.append(item).append('\n');
		}

		return new TypedString(sb.substring(0, sb.length() - 1));
	}
}
