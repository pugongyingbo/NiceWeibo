package com.it.zzb.niceweibo.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import com.it.zzb.niceweibo.R;
import com.it.zzb.niceweibo.activity.AccountActivity;
import com.it.zzb.niceweibo.util.PrefUtils;


public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);


        //网络设置
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
        //账号管理
        findPreference("account").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {

            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent;
                intent = new Intent(getActivity(), AccountActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}