package com.example.hop.weatherapp;


import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import jsonmodel.Channel;
import jsonmodel.Forecast;
import jsonmodel.Item;
import jsonmodel.YahooWeatherData;


/**
 * Created by Hop on 11/04/2015.
 */
public class WeatherFragment extends Fragment {
    TextView txtCityCountry;
    TextView txtLastUpdated;
    ImageView iconWeather;
    TextView txtTemperature;
    TextView txtDescription;
    TextView txtSunrise, txtSunset;
    TextView txtWind, txtHumidity, txtPressure;
    String jsonString = null;
    YahooWeatherData yhdata = null;
    String city;
    ProgressDialog progressDialog;

    public WeatherFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        txtCityCountry = (TextView) rootView.findViewById(R.id.txtCityCountry);
        txtLastUpdated = (TextView) rootView.findViewById(R.id.txtLastUpdated);
        txtTemperature = (TextView) rootView.findViewById(R.id.txtTemperature);
        txtDescription = (TextView) rootView.findViewById(R.id.txtDescription);
        txtSunrise = (TextView) rootView.findViewById(R.id.txtSunrise);
        txtSunset = (TextView) rootView.findViewById(R.id.txtSunset);
        txtWind = (TextView) rootView.findViewById(R.id.txtWind);
        txtHumidity = (TextView) rootView.findViewById(R.id.txtHumidity);
        txtPressure = (TextView) rootView.findViewById(R.id.txtPressure);
        iconWeather = (ImageView) rootView.findViewById(R.id.iconWeather);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Đang tải thông tin ...");
        progressDialog.setMessage("Vui lòng chờ ...");
        progressDialog.show();
        updateWeather(new CityPreference(getActivity()).getCity());

    }

    public void updateWeather(final String city) {
        new SendDataThread().execute(city);
    }

    public void changeCity(String city) {
        updateWeather(city);
        this.city = city;
    }

    class SendDataThread extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... params) {
            Boolean result;
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[4095];
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("10.0.3.2");
                String sentence = "CityName-" + params[0];
                sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sentence.length(), IPAddress, 8876);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String receivedSentence = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                jsonString = receivedSentence;
                yhdata = new Gson().fromJson(jsonString, YahooWeatherData.class);
                List<Forecast> forecastArrayList = yhdata.getQuery().getResults().getChannel().getItem().getForecast();
                if (yhdata != null) {
                    ForecastPreference.setForecasts(forecastArrayList);
                }
                publishProgress();
                result = true;
            } catch (SocketException e) {
                e.printStackTrace();
                result = false;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                result = false;
            } catch (IOException e) {
                e.printStackTrace();
                result = false;
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                result = false;
            } catch (Exception e) {
                e.printStackTrace();
                result = false;
            }
            return result;


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //Get data
            Channel channel = yhdata.getQuery().getResults().getChannel();
            Item item = channel.getItem();
            List<Forecast> f = item.getForecast();
            String city = channel.getLocation().getCity();
            String country = channel.getLocation().getCountry();
            String lastBuildDate = channel.getLastBuildDate().substring(0, 11);
            int resourceID = getResources().getIdentifier("@drawable/icon_" + channel.getItem().getCondition().getCode(),
                    null, WeatherActivity.PACKAGE_NAME);
            Drawable weatherIcon = getResources().getDrawable(resourceID);
            int temperature = Integer.parseInt(channel.getItem().getCondition().getTemp());
            String description = channel.getItem().getCondition().getText();
            String sunrise = channel.getAstronomy().getSunrise();
            String sunset = channel.getAstronomy().getSunset();
            String wind = channel.getWind().getSpeed();
            String humidity = channel.getAtmosphere().getHumidity();
            String pressure = channel.getAtmosphere().getPressure();

            //Update  Interface
            txtCityCountry.setText(String.format("%s, %s", city, country));
            txtLastUpdated.setText("Last updated: " + lastBuildDate);
            iconWeather.setImageDrawable(weatherIcon);
            char tmp = 0x00B0;
            txtTemperature.setText((((temperature - 32) * 5) / 9) + " " + (char) 0x00B0 + "C");
            txtDescription.setText(description);
            txtSunrise.setText(sunrise);
            txtSunset.setText(sunset);
            txtWind.setText(wind + " mph");
            txtHumidity.setText(humidity + " %");
            txtPressure.setText(pressure + " in");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (aBoolean) {
                CityPreference cityPref = new CityPreference(getActivity());
                cityPref.setCity(city);
            } else {
                Toast.makeText(getActivity(), "Sorry, no weather data found", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }
    }


}
