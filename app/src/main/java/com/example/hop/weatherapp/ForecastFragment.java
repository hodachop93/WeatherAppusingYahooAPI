package com.example.hop.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jsonmodel.Forecast;

public class ForecastFragment extends Fragment {
    TextView txtForecast0_date, txtForecast0_temperature, txtForecast0_text;
    ImageView imgViewForcast0;

    TextView txtForecast1_date, txtForecast1_temperature, txtForecast1_text;
    ImageView imgViewForcast1;

    TextView txtForecast2_date, txtForecast2_temperature, txtForecast2_text;
    ImageView imgViewForcast2;

    TextView txtForecast3_date, txtForecast3_temperature, txtForecast3_text;
    ImageView imgViewForcast3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast, container, false);
        txtForecast0_date = (TextView) rootView.findViewById(R.id.forecast0_date);
        txtForecast0_temperature = (TextView) rootView.findViewById(R.id.forecast0_temperature);
        txtForecast0_text = (TextView) rootView.findViewById(R.id.forecast0_text);
        imgViewForcast0 = (ImageView) rootView.findViewById(R.id.forecast0_icon);

        txtForecast1_date = (TextView) rootView.findViewById(R.id.forecast1_date);
        txtForecast1_temperature = (TextView) rootView.findViewById(R.id.forecast1_temperature);
        txtForecast1_text = (TextView) rootView.findViewById(R.id.forecast1_text);
        imgViewForcast1 = (ImageView) rootView.findViewById(R.id.forecast1_icon);

        txtForecast2_date = (TextView) rootView.findViewById(R.id.forecast2_date);
        txtForecast2_temperature = (TextView) rootView.findViewById(R.id.forecast2_temperature);
        txtForecast2_text = (TextView) rootView.findViewById(R.id.forecast2_text);
        imgViewForcast2 = (ImageView) rootView.findViewById(R.id.forecast2_icon);

        txtForecast3_date = (TextView) rootView.findViewById(R.id.forecast3_date);
        txtForecast3_temperature = (TextView) rootView.findViewById(R.id.forecast3_temperature);
        txtForecast3_text = (TextView) rootView.findViewById(R.id.forecast3_text);
        imgViewForcast3 = (ImageView) rootView.findViewById(R.id.forecast3_icon);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        List<Forecast> forecasts = ForecastPreference.getForecasts();
        if (forecasts != null) {
            new UpdateForecast(forecasts).execute();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private int ConvertFtoC(int temperature) {
        return (((temperature - 32) * 5) / 9);
    }

    class UpdateForecast extends AsyncTask<Void, Void, Void> {
        List<Forecast> forecasts;
        public UpdateForecast(List<Forecast> _forecasts) {
            this.forecasts = _forecasts;
        }
        @Override
        protected Void doInBackground(Void... params) {
            publishProgress();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            String date, temperature, description;
            int resourceID, high, low;
            Forecast tempData;

            //Get du lieu
            tempData = forecasts.get(0);
            date = tempData.getDate().substring(0, 7);
            resourceID = getResources().getIdentifier("@drawable/icon_" + tempData.getCode(), null, WeatherActivity.PACKAGE_NAME);
            high = ConvertFtoC(Integer.parseInt(tempData.getHigh()));
            low = ConvertFtoC(Integer.parseInt(tempData.getLow()));
            temperature = "" + low +"/" + high + " " + (char) 0x00B0 + "C";
            description = tempData.getText();
            //Cap nhat du lieu
            txtForecast0_date.setText(date);
            imgViewForcast0.setImageDrawable(getResources().getDrawable(resourceID));
            txtForecast0_temperature.setText(temperature);
            txtForecast0_text.setText(description);

            //Get du lieu
            tempData = forecasts.get(1);
            date = tempData.getDate().substring(0, 7);
            resourceID = getResources().getIdentifier("@drawable/icon_" + tempData.getCode(), null, WeatherActivity.PACKAGE_NAME);
            high = ConvertFtoC(Integer.parseInt(tempData.getHigh()));
            low = ConvertFtoC(Integer.parseInt(tempData.getLow()));
            temperature = "" + low +"/" + high + " " + (char) 0x00B0 + "C";
            description = tempData.getText();
            //Cap nhat du lieu
            txtForecast1_date.setText(date);
            imgViewForcast1.setImageDrawable(getResources().getDrawable(resourceID));
            txtForecast1_temperature.setText(temperature);
            txtForecast1_text.setText(description);

            //Get du lieu
            tempData = forecasts.get(2);
            date = tempData.getDate().substring(0, 7);
            resourceID = getResources().getIdentifier("@drawable/icon_" + tempData.getCode(), null, WeatherActivity.PACKAGE_NAME);
            high = ConvertFtoC(Integer.parseInt(tempData.getHigh()));
            low = ConvertFtoC(Integer.parseInt(tempData.getLow()));
            temperature = "" + low +"/" + high + " " + (char) 0x00B0 + "C";
            description = tempData.getText();
            //Cap nhat du lieu
            txtForecast2_date.setText(date);
            imgViewForcast2.setImageDrawable(getResources().getDrawable(resourceID));
            txtForecast2_temperature.setText(temperature);
            txtForecast2_text.setText(description);

            //Get du lieu
            tempData = forecasts.get(0);
            date = tempData.getDate().substring(0, 7);
            resourceID = getResources().getIdentifier("@drawable/icon_" + tempData.getCode(), null, WeatherActivity.PACKAGE_NAME);
            high = ConvertFtoC(Integer.parseInt(tempData.getHigh()));
            low = ConvertFtoC(Integer.parseInt(tempData.getLow()));
            temperature = "" + low +"/" + high + " " + (char) 0x00B0 + "C";
            description = tempData.getText();
            //Cap nhat du lieu
            txtForecast3_date.setText(date);
            imgViewForcast3.setImageDrawable(getResources().getDrawable(resourceID));
            txtForecast3_temperature.setText(temperature);
            txtForecast3_text.setText(description);

        }
    }
}