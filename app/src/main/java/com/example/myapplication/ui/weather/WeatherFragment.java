package com.example.myapplication.ui.weather;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherFragment extends Fragment {

    private WeatherViewModel mViewModel;
    public static WeatherFragment newInstance() {
        return new WeatherFragment();
    }

    TextView tv_weather_condition,tv_weather_description,tv_temp,tv_feels_like,tv_humidity,tv_max,tv_min,tv_wind_speed;
    ImageView weather_img,temperature_image;
    AutoCompleteTextView autoCompleteTextView;
    Button go;
    String current_weather_url = "https://api.openweathermap.org/data/2.5/weather";
    String forecast_weather_url = "https://api.openweathermap.org/data/2.5/forecast";
    String app_id = "64558cdf1afc2d3a085c537c449b1770";
    String current_url = "";
    String forecast_url = "";
    RecyclerView recyclerView;
    List<WeatherDataModel> forecastList = new ArrayList<>();
    ForecastAdapter adapter;
    ProgressBar progressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        autoCompleteTextView = view.findViewById(R.id.searchView);
        go = view.findViewById(R.id.btn_weather_go);
        tv_weather_condition = view.findViewById(R.id.tv_weather_condition);
        weather_img = view.findViewById(R.id.weather_condition_image);
        tv_weather_description = view.findViewById(R.id.tv_weather_description);
        tv_temp = view.findViewById(R.id.tv_temp);
        tv_feels_like = view.findViewById(R.id.tv_feels_like);
        tv_humidity = view.findViewById(R.id.tv_humidity);
        tv_max = view.findViewById(R.id.tv_max_temp);
        tv_min = view.findViewById(R.id.tv_min_temp);
        tv_wind_speed = view.findViewById(R.id.tv_wind_speed);
        temperature_image = view.findViewById(R.id.temperature_img);
        recyclerView = view.findViewById(R.id.forecast_recycler_view);
        progressBar = view.findViewById(R.id.progressBar_weather);
        progressBar.setVisibility(View.INVISIBLE);

        //ArrayList<String> cities = new ArrayList<>();
        String[] cities_list = getResources().getStringArray(R.array.cities);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,cities_list);
        autoCompleteTextView.setAdapter(arrayAdapter);


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_city = autoCompleteTextView.getText().toString().trim();

                if(s_city.isEmpty())
                {
                    autoCompleteTextView.requestFocus();
                    autoCompleteTextView.setError("Enter a valid city");
                    //Toast.makeText(getContext(), "Enter a valid city", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                current_url = current_weather_url + "?q=" + s_city + "&APPID=" + app_id;
                forecast_url = forecast_weather_url + "?q=" + s_city + "&APPID=" + app_id;

                StringRequest stringRequest = new StringRequest(Request.Method.POST, current_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.d("WEATHER",response);
                        //Toast.makeText(getContext(), "Weather report for "+s_city+" fetched successfully", Toast.LENGTH_SHORT).show();
                        //String data = "";

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("weather");
                            JSONObject jsonWeather = jsonArray.getJSONObject(0);
                            String weather_condition = jsonWeather.getString("main");
                            String weather_description = jsonWeather.getString("description");

                            JSONObject jsonWeatherDetails = jsonObject.getJSONObject("main");
                            double temp = jsonWeatherDetails.getDouble("temp")-273.15;
                            double feels_like = jsonWeatherDetails.getDouble("feels_like")-273.15;
                            double humidity = jsonWeatherDetails.getDouble("humidity");
                            double max_temp = jsonWeatherDetails.getDouble("temp_max")-273.15;
                            double min_tmp = jsonWeatherDetails.getDouble("temp_min")-273.15;

                            JSONObject jsonWindDetails = jsonObject.getJSONObject("wind");
                            double wind_speed = jsonWindDetails.getDouble("speed");

                            //Toast.makeText(getContext(), weather_description, Toast.LENGTH_SHORT).show();

                            setDrawables(temp,weather_description);

//                            data = "Current weather of "+s_city+" is "+weather_condition+"\nTemp : "+String.valueOf(temp)+"\nFeels like : "+String.valueOf(feels_like);
//                            tv_data.setText(data);
                            tv_weather_condition.setText(weather_condition);
                            tv_weather_description.setText(weather_description);
                            tv_temp.setText(Math.round(temp)+"°C");
                            tv_feels_like.setText("Feels like : "+Math.round(feels_like)+"°C");
                            tv_max.setText("Max : "+Math.round(max_temp)+"°C");
                            tv_min.setText("Min : "+Math.round(min_tmp)+"°C");
                            tv_humidity.setText("Humidity : "+humidity+"%");
                            tv_wind_speed.setText(wind_speed+"m/s");

                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                        //Log.d("ERROR",error.getMessage());
                    }
                });

                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, forecast_url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        forecastList.clear();

                        for(int i=5 ; i>=0 ; i--)
                        {
                            try {

                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("list");
                                JSONObject json_day0 = jsonArray.getJSONObject(i);
                                JSONObject json_weather_main = json_day0.getJSONObject("main");

                                JSONArray json_weather_array = json_day0.getJSONArray("weather");
                                JSONObject json_weather0 = json_weather_array.optJSONObject(0);

                                String time_0 = json_day0.getString("dt_txt");
                                String separated[] = time_0.split(" ");
                                String date[] = separated[0].split("-");
                                String hrs[] = separated[1].split(":");
                                String hr = date[2]+"/"+date[1]+" | "+hrs[0]+":"+hrs[1]+"hrs";
                                double temp_0 = json_weather_main.getDouble("temp_min")-273.15;
                                double feels_0 = json_weather_main.getDouble("temp_max")-273.15;
                                String desc_0 = json_weather0.getString("description");
                                String condition_0 = json_weather0.getString("main");

                                forecastList.add(new WeatherDataModel(
                                        condition_0+"",
                                        desc_0+"",
                                        "Max : "+Math.round(feels_0)+"°C",
                                        "Min : "+Math.round(temp_0)+"°C",
                                        hr+""
                                ));


                            }

                            catch (JSONException e)
                            {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        adapter = new ForecastAdapter(getContext(),forecastList);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,true));
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        progressBar.setVisibility(View.INVISIBLE);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest);

                RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                requestQueue.add(stringRequest2);

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WeatherViewModel.class);
        // TODO: Use the ViewModel
    }

    public void setDrawables(double temp,String weather_description)
    {
        if(temp<=25)
        {
            temperature_image.setImageResource(R.drawable.cold);
        }
        else
        {
            temperature_image.setImageResource(R.drawable.hot);
        }

        if(weather_description.equals("clear sky"))
        {
            weather_img.setImageResource(R.drawable.clear_sky);
        }
        if(weather_description.equals("few clouds"))
        {
            weather_img.setImageResource(R.drawable.few_clouds);
        }
        if(weather_description.equals("scattered clouds"))
        {
            weather_img.setImageResource(R.drawable.scattered_clouds);
        }
        if(weather_description.equals("broken clouds"))
        {
            weather_img.setImageResource(R.drawable.broken_clouds);
        }
        if(weather_description.equals("shower rain"))
        {
            weather_img.setImageResource(R.drawable.shower_rain);
        }
        if(weather_description.equals("rain")||weather_description.equals("light rain")||weather_description.equals("moderate rain"))
        {
            weather_img.setImageResource(R.drawable.rain);
        }
        if(weather_description.equals("thunderstorm"))
        {
            weather_img.setImageResource(R.drawable.thunderstorm);
        }
        if(weather_description.equals("snow"))
        {
            weather_img.setImageResource(R.drawable.snow);
        }
        if(weather_description.equals("mist"))
        {
            weather_img.setImageResource(R.drawable.mist);
        }
    }

}