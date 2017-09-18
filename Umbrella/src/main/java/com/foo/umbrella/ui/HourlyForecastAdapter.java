package com.foo.umbrella.ui;

/**
 * Created by gollaba on 9/16/17.
 */

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.model.HourlyForecastDataForDay;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.PorterDuff.Mode.SRC_ATOP;


/**
 * Created by gollaba on 9/5/17.
 */
public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.MainViewHolder> {

    private List<HourlyForecastDataForDay> forecastList = null;
    private Context context;

    public HourlyForecastAdapter(List<HourlyForecastDataForDay> forecastList, Context context) {
        super();
        this.forecastList = forecastList;
        this.context = context;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_list_item, parent, false);
        MainViewHolder viewHolder = new MainViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        HourlyForecastDataForDay data = forecastList.get(position);
        populateData(data, holder);
    }

    @Override
    public int getItemCount() {
        return forecastList.size() ;
    }

    private void populateData(HourlyForecastDataForDay data, MainViewHolder holder) {
        if(data.isToday())
            holder.dayText.setText(context.getResources().getString(R.string.today));
        else if(data.isTomorrow())
            holder.dayText.setText(context.getResources().getString(R.string.tomorrow));
        else
            holder.dayText.setText(data.getWeekDay() + ", " + data.getMonth() + " " + data.getDayOfTheMonth());
        holder.gridView.setAdapter(new GridViewAdapter(data.getTempByHourList(), context));
    }

    class MainViewHolder extends RecyclerView.ViewHolder {
        public TextView dayText;
        public GridView gridView;


        public MainViewHolder(View itemView) {
            super(itemView);
            dayText= (TextView) itemView.findViewById(R.id.itemHeader);
            gridView = (GridView) itemView.findViewById(R.id.GridLayout1);
        }

    }
}
