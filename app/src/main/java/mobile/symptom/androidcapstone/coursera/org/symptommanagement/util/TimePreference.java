package mobile.symptom.androidcapstone.coursera.org.symptommanagement.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.DialogPreference;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.R;

/**
 */
public class TimePreference extends DialogPreference {
    private Calendar calendar;
    private TimePicker picker = null;

    public TimePreference(Context ctxt) {
        this(ctxt, null);
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        this(ctxt, attrs, 0);
    }

    public TimePreference(Context ctxt, AttributeSet attrs, int defStyle) {
        super(ctxt, attrs, defStyle);

        setPositiveButtonText(R.string.set);
        setNegativeButtonText(R.string.cancel);
        calendar = new GregorianCalendar();
    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);
        picker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        picker.setCurrentMinute(calendar.get(Calendar.MINUTE));
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            calendar.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            calendar.set(Calendar.MINUTE, picker.getCurrentMinute());

            setSummary(getSummary());
            if (callChangeListener(calendar.getTimeInMillis())) {
                persistLong(calendar.getTimeInMillis());
                notifyChanged();
            }
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

        Log.d("DEB", "restoreValue=" + restoreValue);
        Log.d("DEB", "defaultValue=" + defaultValue);

        if (restoreValue) {
            if (defaultValue == null) {
                calendar.setTimeInMillis(getPersistedLong(System.currentTimeMillis()));
            } else {
                calendar.setTimeInMillis(Long.parseLong(getPersistedString((String) defaultValue)));
            }
        } else {
            if (defaultValue == null) {
                calendar.setTimeInMillis(System.currentTimeMillis());
            } else {
                calendar.setTimeInMillis(Long.parseLong((String) defaultValue));
            }
        }
        setSummary(getSummary());
    }

    @Override
    public CharSequence getSummary() {
        if (calendar == null) {
            return null;
        }
        return DateFormat.getTimeFormat(getContext()).format(new Date(calendar.getTimeInMillis()));
    }

    public void setTimeInMillis(long timeInMillis) {
        calendar.setTimeInMillis(timeInMillis);
        setSummary(getSummary());
    }

    public long getTimeInMillis() {
        return calendar.getTimeInMillis();
    }
}
