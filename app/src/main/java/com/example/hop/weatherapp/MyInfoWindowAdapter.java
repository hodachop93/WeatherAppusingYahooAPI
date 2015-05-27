package com.example.hop.weatherapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import modelopenweather.OpenWeatherJSon;

/**
 * Created by Hop on 24/05/2015.
 */
public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private Activity context;
    Marker maker = null;
    OpenWeatherJSon openWeatherJSon = null;
    Bitmap myBitmap = null;
    NumberFormat format = new DecimalFormat("#0.0");
    double latitude;
    double longitude;

    @Override
    public View getInfoWindow(Marker marker) {

        return null;
    }

    public MyInfoWindowAdapter(Activity context, Marker maker, OpenWeatherJSon openWeatherJSon, Bitmap myBitmap,
                               double latitude, double longitude) {
        this.context = context;
        this.maker = maker;
        this.openWeatherJSon = openWeatherJSon;
        this.myBitmap = myBitmap;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View v = LayoutInflater.from(context).inflate(R.layout.activity_current_weather_location, null);

        TextView txtDiaChi = (TextView) v.findViewById(R.id.txtDiaChi);
        TextView txtNhietDoHienTai = (TextView) v.findViewById(R.id.txtNhietDoHienTai);
        TextView txtNhietDoNhoNhat = (TextView) v.findViewById(R.id.txtNhietDoNhoNhat);
        TextView txtNhietDoLonNhat = (TextView) v.findViewById(R.id.txtNhietDoLonNhat);
        TextView txtTocDoGio = (TextView) v.findViewById(R.id.txtTocDoGio);
        TextView txtApSuat = (TextView) v.findViewById(R.id.txtApSuat);
        TextView txtDoAm = (TextView) v.findViewById(R.id.txtDoAm);
        TextView txtMatTroiMoc = (TextView) v.findViewById(R.id.txtMatTroiMoc);
        TextView txtMatTroiLang = (TextView) v.findViewById(R.id.txtMatTroiLang);
        ImageView anhBauTroi = (ImageView) v.findViewById(R.id.anhBauTroi);

        //Get du lieu
        String cur_temp = format.format(openWeatherJSon.getMain().getTemp() - 273.15) + (char) 0x00B0 + "C";
        String min_temp = format.format(openWeatherJSon.getMain().getTemp_min() - 273.15) + (char) 0x00B0 + "C";
        String max_temp = format.format(openWeatherJSon.getMain().getTemp_max() - 273.15) + (char) 0x00B0 + "C";
        String wind = openWeatherJSon.getWind().getSpeed() + " m/s";
        String pressure = openWeatherJSon.getMain().getPressure() + " hpa";
        String humidity = openWeatherJSon.getMain().getHumidity() + " %";
        Date timeSunrise = new Date(openWeatherJSon.getSys().getSunrise() * 1000);
        String sunrise = timeSunrise.getHours() + ":" + timeSunrise.getMinutes() + " AM";
        Date timeSunSet = new Date(openWeatherJSon.getSys().getSunset() * 1000);
        String sunset = timeSunSet.getHours() + ":" + timeSunSet.getMinutes() + " PM";

        //Cap nhat du lieu
        txtNhietDoHienTai.setText(cur_temp);
        txtNhietDoLonNhat.setText(max_temp);
        txtNhietDoNhoNhat.setText(min_temp);
        txtTocDoGio.setText(wind);
        txtApSuat.setText(pressure);
        txtDoAm.setText(humidity);
        txtMatTroiMoc.setText(sunrise);
        txtMatTroiLang.setText(sunset);

        //Lay dia chi tren Google Map
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this.context, Locale.getDefault());
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            Address address=null;
            if(addresses.size()>0)
                address=addresses.get(0);
            if(address!=null)
            {
                txtDiaChi.setText(address.getAddressLine(0));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        v.setBackgroundColor (Color.WHITE);
        return v;
    }
}
