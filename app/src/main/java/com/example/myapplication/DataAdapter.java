package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

        private Context mContext;
        private List<Data> weatherDataList = new ArrayList<>();


        public DataAdapter(Context mContext) {
            this.mContext = mContext;
//            options = new RequestOptions().centerCrop().override(100, 150).placeholder(R.drawable.drawable_loading_shape).error(R.drawable.drawable_loading_shape);
        }

        public void setMdata(List<Data> mdata) {
            this.weatherDataList = mdata;
            Log.i("TAG", "onResponse: ADapter 1" + weatherDataList.size());
            Log.i("TAG", "onResponse: ADapter 2" + mdata.size());
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            final LayoutInflater inflater = LayoutInflater.from(mContext);
            view = inflater.inflate(R.layout.view_schedule_item, parent, false);
            final MyViewHolder viewHolder = new MyViewHolder(view);

            return viewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String description = weatherDataList.get(position).getDescription();
            String startDate = weatherDataList.get(position).getStartTime();
            String endDate = weatherDataList.get(position).getEndTime();


            holder.date_txv.setText(startDate+" - "+endDate);

            holder.desc_txv.setText(description);
//


        }

        @Override
        public int getItemCount() {
            return weatherDataList.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            TextView desc_txv;
            TextView date_txv;



            public MyViewHolder(@NonNull View itemView) {

                super(itemView);

                desc_txv = (TextView)itemView.findViewById(R.id.description_txv);
                date_txv = (TextView)itemView.findViewById(R.id.date_txv);


            }
        }
    }


