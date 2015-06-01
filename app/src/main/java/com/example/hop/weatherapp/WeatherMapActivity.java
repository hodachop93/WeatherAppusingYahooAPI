package com.example.hop.weatherapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import modelopenweather.OpenWeatherJSon;

public class WeatherMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ProgressDialog progressDialog;
    private boolean infoWindowIsShow = false;
    private Marker lastMarker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_map);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đang tải map ...");
        progressDialog.setMessage("Vui lòng chờ ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        setUpMapIfNeeded();
        getDefaultLocation();
        addEventGetWeather();
    }

    private void addEventGetWeather() {
        if(mMap==null)return;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                moveAndShowWeather(latLng);
            }
        });
        mMap.setOnMarkerClickListener(new MarkerClickListener());
    }

    private void moveAndShowWeather(LatLng latLng) {
        if (latLng != null) {

            /*WeatherAsyncTask task=new WeatherAsyncTask(maker,mMap,WeatherMapsActivity.this,latLng.latitude,latLng.longitude);
            task.execute();*/
            int mZoom = 6;
            CameraPosition cameraPosition = new CameraPosition(latLng, mZoom, 0, 0);
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
            Marker marker = mMap.addMarker(markerOptions);
            SendLocationThread myThread = new SendLocationThread(this, latLng.latitude, latLng.longitude, marker);
            myThread.execute();
            if (lastMarker != null)
                lastMarker.remove();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
               mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                   @Override
                   public void onMapLoaded() {
                       progressDialog.dismiss();
                   }
               });
            }
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.setMyLocationEnabled(true);
        }
    }

    private void getDefaultLocation() {
        //Toa do thanh pho Da Nang
        double latitude = 16.07;
        double longtitude = 108.22;
        float mZoom = 6;
        float mtilt = 0;
        float mbearing = 00;

        //Khoi tao mot vi tri moi cho camera
        CameraPosition cam = new CameraPosition(new LatLng(latitude, longtitude), mZoom, mtilt, mbearing);
        //Update Map den vi tri ta vua moi khoi tao
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cam));
        //Them Marker  cho Map
        /*MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title("Đà Nẵng");
        markerOptions.position(new LatLng(latitude, longtitude));
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        Marker marker = mMap.addMarker(markerOptions);
        marker.showInfoWindow();
*/


    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    class SendLocationThread extends AsyncTask<Void, Void, Void> {
        ProgressDialog dialog;
        Activity activity;
        double latitude;
        double longtitude;
        OpenWeatherJSon openWeatherJSon = null;
        Bitmap bitmap;
        Marker marker;
        public SendLocationThread(Activity activity, double latitude, double longtitude, Marker marker) {
            this.activity = activity;
            this.latitude = latitude;
            this.longtitude = longtitude;
            this.marker = marker;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(activity);
            dialog.setTitle("Đang tải thông tin ...");
            dialog.setMessage("Vui lòng chờ ...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String jsonString;
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[4095];
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("10.0.3.2");
                String sentence = String.format("Location-%f-%f", latitude, longtitude);
                sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sentence.length(), IPAddress, 8876);
                clientSocket.send(sendPacket);
                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String receivedSentence = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength());
                jsonString = receivedSentence;
                openWeatherJSon = new Gson().fromJson(jsonString, OpenWeatherJSon.class);
                String idIcon = openWeatherJSon.getWeather().get(0).getIcon();
                //get icon
                String urlIcon = "http://openweathermap.org/img/w/"+idIcon+".png";
                URL url = new URL(urlIcon);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream is = httpURLConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                publishProgress();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (mMap != null) {
                mMap.setInfoWindowAdapter(new MyInfoWindowAdapter(activity, marker, openWeatherJSon, bitmap, latitude, longtitude));
                marker.showInfoWindow();
                this.dialog.dismiss();

                return;
            }
        }
    }

    private class MarkerClickListener implements GoogleMap.OnMarkerClickListener {
        @Override
        public boolean onMarkerClick(Marker marker) {

            if(lastMarker == null){
                marker.showInfoWindow();
                lastMarker = marker;
                infoWindowIsShow=true;
            }else
            if (marker.getId().equals(lastMarker.getId())) {
                if (infoWindowIsShow) {
                    marker.hideInfoWindow();
                    infoWindowIsShow = false;
                } else {
                    marker.showInfoWindow();
                    infoWindowIsShow = true;
                }
            }
            else{
                if (infoWindowIsShow) {
                    lastMarker.hideInfoWindow();
                    marker.showInfoWindow();
                    infoWindowIsShow = true;
                    lastMarker = marker;
                } else {
                    marker.showInfoWindow();
                    infoWindowIsShow = true;
                    lastMarker = marker;
                }
            }
            return true;
        }
    }
}
