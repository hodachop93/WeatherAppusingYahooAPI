package com.example.hop.weatherapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


public class WeatherActivity extends ActionBarActivity {
    TextView txt;
    public static String PACKAGE_NAME;
    WeatherFragment wf;
    ForecastFragment ff;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        txt = (TextView) findViewById(R.id.textViewTest);
        PACKAGE_NAME = getPackageName();
        wf = new WeatherFragment();
        ff = new ForecastFragment();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, wf).commit();
        //new SendDataThread1().execute();
    }

    class SendDataThread1 extends AsyncTask<Void, String, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[4095];
            try {
                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("10.0.3.2");
                String sentence = "";
                sendData = sentence.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData,
                        sentence.length(), IPAddress, 8876);
                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData,
                        receiveData.length);
                clientSocket.receive(receivePacket);
                String receivedSentence = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                /*int i = receivePacket.getLength();
                receivedSentence = receivedSentence.substring(0, i);*/
                publishProgress(receivedSentence);

            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            txt.setText(values[0]);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent("android.intent.action.ABOUT");
                startActivity(intent);
                break;
            case R.id.change_city:
                showInputDialog();
                break;
            case R.id.forecast:
                showforecast();
                break;
        }
        return true;
    }

    private void showforecast() {

        getSupportFragmentManager().beginTransaction().replace(R.id.container, ff).commit();

    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city) {
        WeatherFragment wf = (WeatherFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container);
        wf.changeCity(city);

    }


}
