package com.example.myapplication.ui.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    List<WeatherDataModel> forecastList;

    public ForecastAdapter(Context context, List<WeatherDataModel> forecastList)
    {
        this.forecastList = forecastList;
    }

    @NonNull
    @Override
    public ForecastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_weather_card,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapter.ViewHolder holder, int position) {

        holder.tv_max.setText(forecastList.get(position).getMax_temp());
        holder.tv_min.setText(forecastList.get(position).getMin_temp());
        holder.tv_desc.setText(forecastList.get(position).getDescription());
        holder.tv_time.setText(forecastList.get(position).getTime());
        
        String s_condition = holder.tv_desc.getText().toString();
        s_condition=s_condition.toLowerCase();

        if(s_condition.equals("clear sky"))
        {
              holder.weather_image.setImageResource(R.drawable.clear_sky);
        }
        if(s_condition.equals("few clouds"))
        {
              holder.weather_image.setImageResource(R.drawable.few_clouds);
        }
        if(s_condition.equals("scattered clouds"))
        {
              holder.weather_image.setImageResource(R.drawable.scattered_clouds);
        }
        if(s_condition.equals("broken clouds"))
        {
              holder.weather_image.setImageResource(R.drawable.broken_clouds);
        }
        if(s_condition.equals("shower rain"))
        {
              holder.weather_image.setImageResource(R.drawable.shower_rain);
        }
        if(s_condition.equals("light rain")||s_condition.equals("moderate rain"))
        {
              holder.weather_image.setImageResource(R.drawable.rain);
        }
        if(s_condition.equals("thunderstorm"))
        {
              holder.weather_image.setImageResource(R.drawable.thunderstorm);
        }
        if(s_condition.equals("snow"))
        {
              holder.weather_image.setImageResource(R.drawable.snow);
        }
        if(s_condition.equals("mist"))
        {
              holder.weather_image.setImageResource(R.drawable.mist);
        }
        

    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_max,tv_min,tv_desc,tv_time;
        ImageView weather_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_max = itemView.findViewById(R.id.custom_weather_card_max_temp);
            tv_min = itemView.findViewById(R.id.custom_weather_card_min_temp);
            tv_desc = itemView.findViewById(R.id.custom_weather_card_description);
            tv_time = itemView.findViewById(R.id.custom_weather_card_time);
            weather_image = itemView.findViewById(R.id.custom_weather_image);

        }
    }

    public void setDrawables(double temp,String s_condition)
    {
        
    }
}
