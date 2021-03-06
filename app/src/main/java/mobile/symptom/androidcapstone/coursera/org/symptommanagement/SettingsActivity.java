package mobile.symptom.androidcapstone.coursera.org.symptommanagement;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.util.Log;

import mobile.symptom.androidcapstone.coursera.org.symptommanagement.util.TimePreference;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setupSimplePreferencesScreen();
    }

    /**
     * Shows the simplified settings UI if the device configuration if the
     * device configuration dictates that a simplified, single-pane UI should be
     * shown.
     */
    private void setupSimplePreferencesScreen() {
        // Add 'general' preferences.
        addPreferencesFromResource(R.xml.pref_general);

        // Add 'notifications' preferences, and a corresponding header.
        PreferenceCategory fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_notifications);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_notification);

        // Add 'data and sync' preferences, and a corresponding header.
        fakeHeader = new PreferenceCategory(this);
        fakeHeader.setTitle(R.string.pref_header_data_sync);
        getPreferenceScreen().addPreference(fakeHeader);
        addPreferencesFromResource(R.xml.pref_data_sync);

        // Bind the summaries of EditText/List/Dialog/Ringtone preferences to
        // their values. When their values change, their summaries are updated
        // to reflect the new value, per the Android Design guidelines.
        bindPreferenceSummaryToValue(findPreference("example_text"));
        //bindPreferenceSummaryToValue(findPreference("pref_doctor_recommendations"));
        bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));

        TimePreference tp1 = (TimePreference) findPreference("sm-patient-checkin-1");
        tp1.setTimeInMillis(SymptomManagement.getSharedPreferences().getLong("sm-patient-checkin-1", 0L));
        tp1.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        TimePreference tp2 = (TimePreference) findPreference("sm-patient-checkin-2");
        tp2.setTimeInMillis(SymptomManagement.getSharedPreferences().getLong("sm-patient-checkin-2", 0L));
        tp2.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        TimePreference tp3 = (TimePreference) findPreference("sm-patient-checkin-3");
        tp3.setTimeInMillis(SymptomManagement.getSharedPreferences().getLong("sm-patient-checkin-3", 0L));
        tp3.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        TimePreference tp4 = (TimePreference) findPreference("sm-patient-checkin-4");
        tp4.setTimeInMillis(SymptomManagement.getSharedPreferences().getLong("sm-patient-checkin-4", 0L));
        tp4.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        bindPreferenceSummaryToValue(findPreference("sync_frequency"));
    }

    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            }
            else if (preference instanceof RingtonePreference) {
                // For ringtone preferences, look up the correct display value
                // using RingtoneManager.
                if (TextUtils.isEmpty(stringValue)) {
                    // Empty values correspond to 'silent' (no ringtone).
                    preference.setSummary(R.string.pref_ringtone_silent);

                } else {
                    Ringtone ringtone = RingtoneManager.getRingtone(
                            preference.getContext(), Uri.parse(stringValue));

                    if (ringtone == null) {
                        // Clear the summary if there was a lookup error.
                        preference.setSummary(null);
                    } else {
                        // Set the summary to reflect the new ringtone display
                        // name.
                        String name = ringtone.getTitle(preference.getContext());
                        preference.setSummary(name);
                    }
                }            }
            else if (preference instanceof TimePreference) {
                TimePreference tp = (TimePreference) preference;
                Log.d(SymptomManagement.LOG_ID, "Changing " + tp.getKey() + " to " + tp.getTimeInMillis());
                Log.d(SymptomManagement.LOG_ID, "Value is " + value);
                SymptomManagement.resetReminderAt(tp.getKey(), tp.getTimeInMillis());
            }
            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
            }

            return true;
        }
    };

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }
}