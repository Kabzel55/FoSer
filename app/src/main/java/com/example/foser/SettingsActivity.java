package com.example.foser;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            final SharedPreferences preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(getActivity());
            final SwitchPreference switchPreference1 = (SwitchPreference) findPreference("2s");
            final SwitchPreference switchPreference2 = (SwitchPreference) findPreference("5s");
            final SwitchPreference switchPreference3 = (SwitchPreference) findPreference("10s");

            if(preferences.getBoolean("2s", true)){
                switchPreference2.setChecked(false);
                switchPreference3.setChecked(false);

                switchPreference1.setEnabled(false);

            }
            else if(preferences.getBoolean("5s", true)){
                switchPreference1.setChecked(false);
                switchPreference3.setChecked(false);

                switchPreference2.setEnabled(false);
            }
            else if (preferences.getBoolean("10s", true)){
                switchPreference2.setChecked(false);
                switchPreference1.setChecked(false);

                switchPreference3.setEnabled(false);
            }

            assert switchPreference1 != null;
            switchPreference1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean turned = (Boolean) newValue;
                    if (turned) {
                        switchPreference2.setChecked(false);
                        switchPreference3.setChecked(false);
                        switchPreference1.setEnabled(false);

                        switchPreference2.setEnabled(true);
                        switchPreference3.setEnabled(true);

                    }
                    preferences.edit().putBoolean("2s", turned).apply();
                    return true;
                }
            });

            assert switchPreference2 != null;
            switchPreference2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean turned = (Boolean) newValue;
                    if (turned) {
                        switchPreference1.setChecked(false);
                        switchPreference3.setChecked(false);
                        switchPreference2.setEnabled(false);

                        switchPreference1.setEnabled(true);
                        switchPreference3.setEnabled(true);
                    }
                    preferences.edit().putBoolean("5s", turned).apply();
                    return true;
                }
            });

            assert switchPreference3 != null;
            switchPreference3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    boolean turned = (Boolean) newValue;
                    if (turned) {
                        switchPreference2.setChecked(false);
                        switchPreference1.setChecked(false);
                        switchPreference3.setEnabled(false);

                        switchPreference1.setEnabled(true);
                        switchPreference2.setEnabled(true);
                    }
                    preferences.edit().putBoolean("10s", turned).apply();
                    return true;
                }
            });
        }
    }
}