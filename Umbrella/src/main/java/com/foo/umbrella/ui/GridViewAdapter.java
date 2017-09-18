package com.foo.umbrella.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foo.umbrella.R;
import com.foo.umbrella.data.AppData;
import com.foo.umbrella.data.model.HourlyForecastDataForDay;
import com.foo.umbrella.utility.UmbrellaConstants;
import com.foo.umbrella.utility.Utility;

import java.util.List;

import static android.R.id.list;
import static android.graphics.PorterDuff.Mode.SRC_ATOP;

/**
 * Created by gollaba on 9/17/17.
 */

public class GridViewAdapter extends BaseAdapter {

    private final Context context;
    private final List<HourlyForecastDataForDay.TempByHour> list;
    private int selectedMetrics = 0;

    public GridViewAdapter(List<HourlyForecastDataForDay.TempByHour> list, Context context) {
            this.list = list;
            this.context = context;
            this.selectedMetrics = AppData.instance(context).getMetricsSetting();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GridViewHolder holder;
        HourlyForecastDataForDay.TempByHour data = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.weather_grid_item, parent, false);
            holder = new GridViewHolder(convertView);
            convertView.setTag(holder);
        }
        else {
            holder = (GridViewHolder) convertView.getTag();
        }
        populateView(holder, data);
        return convertView;
    }

    private void populateView(GridViewHolder holder, HourlyForecastDataForDay.TempByHour data) {
            holder.hourText.setVisibility(View.VISIBLE);
            holder.hourText.setText(data.getHour());
            holder.tempText.setVisibility(View.VISIBLE);
            String temp = data.getTemperatureCentrigrade();;
             if(selectedMetrics == UmbrellaConstants.METRICS_FARENHEIT) {
                temp = data.getTemperatureFarenheit();
             }
            holder.tempText.setText(Utility.formTemperatureDisplayString(temp));
            holder.tempImage.setVisibility(View.VISIBLE);
            int id = context.getResources().getIdentifier("weather_"+data.getCondition(), "drawable", context.getPackageName());
            if(id > 0)
                holder.tempImage.setImageResource(id);

            holder.tempImage.clearColorFilter();
            holder.tempText.setTextColor(context.getResources().getColor(R.color.text_color_primary));
            holder.hourText.setTextColor(context.getResources().getColor(R.color.text_color_primary));
            if(data.isMax() && !data.isMin()) {
                holder.tempImage.setColorFilter(context.getResources().getColor(R.color.weather_warm), SRC_ATOP);
                //holder.tempImage.setColorFilter(context.getResources().getColor(R.color.weather_warm), android.graphics.PorterDuff.Mode.MULTIPLY);
                holder.tempText.setTextColor(context.getResources().getColor(R.color.weather_warm));
                holder.hourText.setTextColor(context.getResources().getColor(R.color.weather_warm));
            }
            if(data.isMin() && !data.isMax()) {
                holder.tempImage.setColorFilter(context.getResources().getColor(R.color.weather_cool), SRC_ATOP);
                holder.tempText.setTextColor(context.getResources().getColor(R.color.weather_cool));
                holder.hourText.setTextColor(context.getResources().getColor(R.color.weather_cool));
            }
        }


    public class GridViewHolder extends RecyclerView.ViewHolder {

        public TextView hourText;
        public ImageView tempImage;
        public TextView tempText;

        public GridViewHolder(View itemView) {
            super(itemView);
            hourText = (TextView) itemView.findViewById(R.id.hour);
            tempText = (TextView) itemView.findViewById(R.id.temp);
            tempImage = (ImageView) itemView.findViewById(R.id.weatherImage);
        }

    }
}
