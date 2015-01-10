package mobile.symptom.androidcapstone.coursera.org.symptommanagement.oauth;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * 
 */
public class OAuth2Response implements Parcelable {
	
	@SerializedName("access_token")
	public String accessToken;
	
	@SerializedName("token_type")
	private String tokenType;
	
	@SerializedName("refresh_token")
	public String refreshToken;

	@SerializedName("expires_in")
	private Integer expiresIn;

	private String scope;

    @SerializedName("error")
    private String error;

    @SerializedName("error_description")
    private String errorDescription;

    ///

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getTokenType() {
		return tokenType;
	}

	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accessToken);
        dest.writeString(this.tokenType);
        dest.writeString(this.refreshToken);
        dest.writeValue(this.expiresIn);
        dest.writeString(this.scope);
        dest.writeString(this.error);
        dest.writeString(this.errorDescription);
    }

    private OAuth2Response(Parcel in) {
        this.accessToken = in.readString();
        this.tokenType = in.readString();
        this.refreshToken = in.readString();
        this.expiresIn = (Integer) in.readValue(Integer.class.getClassLoader());
        this.scope = in.readString();
        this.error = in.readString();
        this.errorDescription = in.readString();
    }

    public static Parcelable.Creator<OAuth2Response> CREATOR = new Parcelable.Creator<OAuth2Response>() {
        public OAuth2Response createFromParcel(Parcel source) {
            return new OAuth2Response(source);
        }

        public OAuth2Response[] newArray(int size) {
            return new OAuth2Response[size];
        }
    };
}
