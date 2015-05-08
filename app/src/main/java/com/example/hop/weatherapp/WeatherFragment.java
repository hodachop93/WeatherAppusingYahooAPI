package com.example.hop.weatherapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

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
    String jsonString;
    YahooWeatherData yhdata;
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
        updateWeather(new CityPreference(getActivity()).getCity());
    }

    public void updateWeather(final String city) {
        new SendDataThread().execute();
    }

    private void renderWeather() {
        String city = yhdata.getQuery().getResults().getChannel().getLocation().getCity();
        String country = yhdata.getQuery().getResults().getChannel().getLocation().getCountry();
        String lastBuildDate = yhdata.getQuery().getResults().getChannel().getLastBuildDate().substring(0, 11);
        try {
            txtCityCountry.setText(String.format("%s, %s", city, country));
            txtLastUpdated.setText("Last updated: " + lastBuildDate);
            
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }



    public void changeCity(String city) {
        updateWeather(city);
    }

    class SendDataThread extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[4095];
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("10.0.3.2");
                String ip = IPAddress.toString();
                String sentence = "Da Nang";
                sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sentence.length(), IPAddress, 8876);
                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String receivedSentence = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                jsonString = receivedSentence;
                convertJSONToJavaClass();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void convertJSONToJavaClass() {
        yhdata = new Gson().fromJson(jsonString, YahooWeatherData.class);
        renderWeather();
    }
}
