package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.Api.Api;
import com.example.myapplication.Api.ApiCallUtils;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private DataAdapter scheduleAdapter = null;
    private RecyclerView recyclerView;
    List<Data> scheduleList = new ArrayList<>();

    private AppCompatButton meetingScheduleBtn;
    private ImageView backImg;
    private TextView backText;
    private ImageView nextImg;
    private TextView nextText;
    private TextView dateTxt;
    private Date currentDate;
    Calendar cal =Calendar.getInstance();


    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
    String currentFormatedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scheduleAdapter = new DataAdapter(getApplicationContext());
        recyclerView = findViewById(R.id.scheduleRV);
        backImg = findViewById(R.id.previous_button);
        backText = findViewById(R.id.txt_back);
        nextImg = findViewById(R.id.next_button);
        nextText = findViewById(R.id.txt_next);
        dateTxt = findViewById(R.id.txt_date_header);
        meetingScheduleBtn = findViewById(R.id.ms_button_schedule_meeting);

        backImg.setOnClickListener(this);
        backText.setOnClickListener(this);
        nextImg.setOnClickListener(this);
        nextText.setOnClickListener(this);
        meetingScheduleBtn.setOnClickListener(this);

        currentDate = Calendar.getInstance().getTime();
        currentFormatedDate = df.format(currentDate);
        dateTxt.setText(currentFormatedDate);
        callApi(currentFormatedDate);
    }



    public void callApi(String date){
        Api api = ApiCallUtils.createApiRequest();

        Call iCallback = api.getSchedule(date);


        iCallback.clone().enqueue(new Callback<List<Data>>() {
            @Override
            public void onResponse(Call<List<Data>> call, Response<List<Data>> response) {
//                Data data = response.body();
                scheduleList = response.body();
                addNotifyRecyclerView(scheduleList);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("TAG", "onFailure: " + t.getMessage());

            }
        });


    }
    private void addNotifyRecyclerView(List<Data> iDataList) {
        Log.i("TAG", "onResponse: ADapter" + iDataList.size());
        scheduleAdapter.setMdata(iDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(scheduleAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_back:
            case R.id.previous_button:
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);
                //Increment Date by 1
                calendar.add(Calendar.DATE,-1);
                currentDate = calendar.getTime();
                String finPrevDate = df.format(currentDate);
                dateTxt.setText(finPrevDate);
                callApi(finPrevDate);
                break;
            case R.id.txt_next:
            case R.id.next_button:
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(currentDate);
                //Increment Date by 1
                calendar2.add(Calendar.DATE,+1);
                currentDate = calendar2.getTime();
                String finNextDate = df.format(currentDate);
                dateTxt.setText(finNextDate);
                callApi(finNextDate);
                break;
            case R.id.ms_button_schedule_meeting:
                Intent intent = new Intent(MainActivity.this,ScheduleMeetingActivity.class);


                Bundle args = new Bundle();
                args.putSerializable("ScheduleList",(Serializable)scheduleList);
                args.putString("Date",df.format(currentDate));
                intent.putExtra("BUNDLE",args);

                startActivity(intent);
                break;

        }
    }
}