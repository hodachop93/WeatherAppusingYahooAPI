package com.example.hop.weatherapp;

import java.util.List;

import jsonmodel.Forecast;

/**
 * Created by Hop on 11/05/2015.
 */
public class ForecastPreference {
    public static List<Forecast> forecasts;

    public static List<Forecast> getForecasts() {
        return forecasts;
    }

    public static void setForecasts(List<Forecast> forecasts) {
        ForecastPreference.forecasts = forecasts;
    }
}
