/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {
//      DONE (21) Implement LoaderManager.LoaderCallbacks<Cursor>

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

//  DONE (18) Create a String array containing the names of the desired data columns from our ContentProvider
    public static final String[] MAIN_FORECAST_PROJECTION = {
        WeatherContract.WeatherEntry.COLUMN_DATE,
        WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
        WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
        WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
        WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
        WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
        WeatherContract.WeatherEntry.COLUMN_PRESSURE
    };
//  DONE (19) Create constant int values representing each column name's position above
    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_CONDITION_ID = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 2;
    public static final int INDEX_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_HUMIDITY = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_PRESSURE = 6;
//  DONE (20) Create a constant int to identify our loader used in DetailActivity
    public static final int LOADER_ID = 72;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

//  DONE (15) Declare a private Uri field called mUri
    private Uri mUri;

//  DONE (10) Remove the mWeatherDisplay TextView declaration

//  DONE (11) Declare TextViews for the date, description, high, low, humidity, wind, and pressure
    private TextView mDateTextView;
    private TextView mDescTextView;
    private TextView mHighTextView;
    private TextView mLowTextView;
    private TextView mHumidTextView;
    private TextView mWindTextView;
    private TextView mPressureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//      DONE (12) Remove mWeatherDisplay TextView
//      DONE (13) Find each of the TextViews by ID
        mDateTextView = (TextView) findViewById(R.id.tv_date_detail);
        mDescTextView = (TextView) findViewById(R.id.tv_desc_detail);
        mHighTextView = (TextView) findViewById(R.id.tv_high_detail);
        mLowTextView = (TextView) findViewById(R.id.tv_low_detail);
        mHumidTextView = (TextView) findViewById(R.id.tv_humid_detail);
        mWindTextView = (TextView) findViewById(R.id.tv_wind_detail);
        mPressureTextView = (TextView) findViewById(R.id.tv_pressure_detail);

//      DONE (14) Remove the code that checks for extra text
        Intent intentThatStartedThisActivity = getIntent();
//      DONE (16) Use getData to get a reference to the URI passed with this Activity's Intent
        mUri = intentThatStartedThisActivity.getData();
//      DONE (17) Throw a NullPointerException if that URI is null
        if (mUri == null) {
            throw new NullPointerException("DetailActivity did not receieve URI!");
        }
//      DONE (35) Initialize the loader for DetailActivity
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     *
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

//  DONE (22) Override onCreateLoader

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String date = mUri.getPathSegments().get(1);
        switch (id) {
            case LOADER_ID:
                return new CursorLoader(this,
                        mUri,
                        MAIN_FORECAST_PROJECTION,
                        WeatherContract.WeatherEntry.COLUMN_DATE + "=?",
                        new String[]{date},
                        null);
            default:
                throw new UnsupportedOperationException("Unknown Loader!");
        }
    }

//          DONE (23) If the loader requested is our detail loader, return the appropriate


//  DONE (24) Override onLoadFinished

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getColumnCount() == 0) return;
        data.moveToFirst();

        mDateTextView.setText(SunshineDateUtils.getFriendlyDateString(
                this, data.getLong(INDEX_WEATHER_DATE), true)
        );
        mDescTextView.setText(SunshineWeatherUtils.getStringForWeatherCondition(this,
                data.getInt(INDEX_WEATHER_CONDITION_ID)));
        mHighTextView.setText(data.getString(INDEX_WEATHER_MAX_TEMP));
        mLowTextView.setText(data.getString(INDEX_WEATHER_MIN_TEMP));
        mHumidTextView.setText(data.getString(INDEX_WEATHER_HUMIDITY));
        mWindTextView.setText(data.getString(INDEX_WEATHER_WIND_SPEED));
        mPressureTextView.setText(data.getString(INDEX_WEATHER_PRESSURE));

        mForecastSummary = "Forecast for " + mDateTextView.getText() + ": " + mDescTextView.getText() +
                " with a high of " + mHighTextView.getText() +
                " and the low for today is " + mLowTextView.getText();
    }

//      DONE (25) Check before doing anything that the Cursor has valid data
//      DONE (26) Display a readable data string
//      DONE (27) Display the weather description (using SunshineWeatherUtils)
//      DONE (28) Display the high temperature
//      DONE (29) Display the low temperature
//      DONE (30) Display the humidity
//      DONE (31) Display the wind speed and direction
//      DONE (32) Display the pressure
//      DONE (33) Store a forecast summary in mForecastSummary


//  DONE (34) Override onLoaderReset, but don't do anything in it yet


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}