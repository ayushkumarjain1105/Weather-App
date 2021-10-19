package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button btn_cityID , btn_getWeatherById, btn_getWeatherByName;
    EditText editdataInput;
    ListView lv_weaterReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_cityID= findViewById(R.id.btn_getCityId);
        btn_getWeatherById =findViewById(R.id.btn_getWeatherByCityId);
        btn_getWeatherByName =findViewById(R.id.btn_getWeatherBYName);

        editdataInput =findViewById(R.id.editdataInput);
        lv_weaterReport =findViewById(R.id.lv_wetherReports);
        final WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
        //Click Listeners for each button.
        btn_cityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // this didn't return anything.
                 weatherDataService.getCityID(editdataInput.getText().toString(), new WeatherDataService.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Something Wrong...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityId) {
                        Toast.makeText(MainActivity.this, "Returned an Id of " + cityId, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });
        btn_getWeatherById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(MainActivity.this, "You Clicked Me 1", Toast.LENGTH_SHORT).show();
                weatherDataService.getCityForecastByID(editdataInput.getText().toString(), new WeatherDataService.ForeCastByIDResponse() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "something wrong...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        // put the entire list into listview control
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModel);
                        lv_weaterReport.setAdapter(arrayAdapter);
                    }
                });
            }
        });
        btn_getWeatherByName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherDataService.getCityForecastByName(editdataInput.getText().toString(), new WeatherDataService.GetCityForecastByNameCallback() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "something wrong...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModel) {
                        // put the entire list into listview control
                        ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, weatherReportModel);
                        lv_weaterReport.setAdapter(arrayAdapter);
                    }
                });
            }
        });
    }
}