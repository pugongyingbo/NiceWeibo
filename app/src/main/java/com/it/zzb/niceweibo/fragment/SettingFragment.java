package com.it.zzb.niceweibo.fragment;


import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.util.PrefUtils;


public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);



        final CheckBoxPreference checkboxPref = (CheckBoxPreference) getPreferenceManager()
                .findPreference(getString(R.string.save_net_mode));

        checkboxPref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            /**
             * @param preference The changed Preference.
             * @param newValue   The new value of the Preference.
             * @return True to update the state of the Preference with the new value.
             */
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = Boolean.valueOf(newValue.toString());
                PrefUtils.setSaveNetMode(checked);
                return true;

            }
        });
    }
}