package com.example.hop.weatherapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class WeatherActivity extends ActionBarActivity {
    TextView txt;
    public static String PACKAGE_NAME;
    WeatherFragment wf;
    ForecastFragment ff;
    private final String TAG = "myApp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        PACKAGE_NAME = getPackageName();
        wf = new WeatherFragment();
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, wf).commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
            case R.id.weatherMap:
                showWeatherMapActivity();
                break;
        }
        return true;
    }

    private void showWeatherMapActivity() {
        if (!isNetworkConnected()) {
               Toast.makeText(this, "Vui long kiem tra ket noi Internet", Toast.LENGTH_LONG).show();
        return;
    }

        Intent myintent = new Intent(this, WeatherMapActivity.class);
        startActivity(myintent);

    }
/*
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }*/

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    private void showforecast() {
        if (!isNetworkConnected()) {
            Toast.makeText(this, "Vui long kiem tra ket noi Internet", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(this, ForecastActivity.class);
        startActivity(intent);
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
