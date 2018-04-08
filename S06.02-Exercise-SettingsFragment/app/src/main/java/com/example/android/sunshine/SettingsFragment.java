package com.example.android.sunshine;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsFragment extends PreferenceFragmentCompat
    implements SharedPreferences.OnSharedPreferenceChangeListener {
    // Do steps 5 - 11 within SettingsFragment
    // DONE (10) Implement OnSharedPreferenceChangeListener from SettingsFragment

    // DONE (8) Create a method called setPreferenceSummary that accepts a Preference and an
    // Object and sets the summary of the preference

    // DONE (5) Override onCreatePreferences and add the preference xml file using
    // addPreferencesFromResource

    // Do step 9 within onCreatePreference
    // DONE (9) Set the preference summary on each preference that isn't a CheckBoxPreference



    // DONE (11) Override onSharedPreferenceChanged to update non CheckBoxPreferences when
    // they are changed
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.prefs);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            Preference p = preferenceScreen.getPreference(i);
            if (!(p instanceof CheckBoxPreference)) {
                setPreferenceSummary(p, sharedPreferences.getString(p.getKey(), ""));
            }
        }
    }

    public void setPreferenceSummary(Preference preference, String s) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(s);
            if (index >= 0) {
                listPreference.setSummary(listPreference.getEntries()[index]);
            }
        } else if (preference instanceof EditTextPreference) {
            preference.setSummary(s);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference p = findPreference(key);
        if (p != null) {
            if (!(p instanceof CheckBoxPreference)) {
                setPreferenceSummary(p, sharedPreferences.getString(p.getKey(), ""));
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
}
