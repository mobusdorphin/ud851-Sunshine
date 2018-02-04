/*
 * Copyright (C) 2016 The Android Open Source Project
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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.android.sunshine.data.SunshinePreferences;

public class MainActivity extends AppCompatActivity {

    // TODO (1) Create a field to store the weather display TextView
    TextView weatherWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        // TODO (2) Use findViewById to get a reference to the weather display TextView
        weatherWindow = (TextView) findViewById(R.id.tv_weather_data);

        // TODO (3) Create an array of Strings that contain fake weather data
        String[] weatherData = {"Forecast for Cleveland, Ohio:",
                                "Cloudy and miserable, as usual",
                                "70% chance of rain, 0% chance of sports team victories",
                                "Expect intermittent patches of fog obscuring view during commute",
                                "Early reports of loud noises originally assumed to be thunderous rainstorms have been revealed to be just Mike Trivassano's normal blathering on the radio and that residents do not need to be more alarmed than usual"};

        // TODO (4) Append each String from the fake weather data array to the TextView
        for (String weatherDatum : weatherData) {
            weatherWindow.append(weatherDatum + "\n\n\n");
        }
    }
}