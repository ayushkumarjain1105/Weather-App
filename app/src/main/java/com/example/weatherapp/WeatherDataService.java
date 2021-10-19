package com.example.weatherapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    private static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    private static final String QUERY_FOR_WEATHER_BY_ID  = "https://www.metaweather.com/api/location/";
    Context context;
    String cityId;
    public WeatherDataService(Context context) {
        this.context = context;
    }
    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String cityId);
    }



    public void getCityID(String cityName, VolleyResponseListener volleyResponseListener){
        String url = QUERY_FOR_CITY_ID  +cityName;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                 cityId ="";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityId = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //this worked. but it did't return the id number to MainActivity
                //Toast.makeText(context, "City ID = " + cityId, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(context, "Something Wrong...", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something Wrong...");
            }
        });

//
        MySingleton.getInstance(context).addToRequestQueue(request);
        //return cityId;
        // return a NULL. PROBLEM
    }
    public interface ForeCastByIDResponse {
        void onError(String message);

        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityForecastByID(String cityID, ForeCastByIDResponse foreCastByIDResponse){
        List<WeatherReportModel> weatherReportModels = new ArrayList<>();
        String url =QUERY_FOR_WEATHER_BY_ID +cityID;
        //get the json object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");
                    // get the first item in the array


                    for(int i=0;i<consolidated_weather_list.length();i++){
                        WeatherReportModel One_day_weather = new WeatherReportModel();

                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                        One_day_weather.setId(first_day_from_api.getInt("id"));
                        One_day_weather.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        One_day_weather.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        One_day_weather.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        One_day_weather.setCreated(first_day_from_api.getString("created"));
                        One_day_weather.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        One_day_weather.setMin_temp(first_day_from_api.getLong("min_temp"));
                        One_day_weather.setMax_temp(first_day_from_api.getLong("max_temp"));
                        One_day_weather.setThe_temp(first_day_from_api.getLong("the_temp"));
                        One_day_weather.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        One_day_weather.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        One_day_weather.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        One_day_weather.setHumidity(first_day_from_api.getInt("humidity"));
                        One_day_weather.setVisibility(first_day_from_api.getLong("visibility"));
                        One_day_weather.setPredictability(first_day_from_api.getInt("predictability"));
                        weatherReportModels.add(One_day_weather);
                    }




                    foreCastByIDResponse.onResponse(weatherReportModels);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
                //get the property clled "consolodated_weather"

                //get each item in the array and assign it to a new WeatherReportModal object.

        MySingleton.getInstance(context).addToRequestQueue(request);
    }

    public interface GetCityForecastByNameCallback{
        void onError(String message);
        void onResponse(List<WeatherReportModel> weatherReportModels);
    }
    public void getCityForecastByName(String cityName, GetCityForecastByNameCallback getCityForecastByNameCallback){
        //fetch the city id given the city name
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityId) {
                getCityForecastByID(cityId, new ForeCastByIDResponse() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportModels) {
                        // we have the weather report.
                        getCityForecastByNameCallback.onResponse(weatherReportModels);
                    }
                });
            }
        });
        //featch the city forecast given the city id.
    }
}
