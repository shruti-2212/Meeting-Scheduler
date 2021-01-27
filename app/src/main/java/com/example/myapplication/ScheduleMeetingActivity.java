package com.example.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.gson.Gson;

import java.sql.Time;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ScheduleMeetingActivity extends AppCompatActivity implements View.OnClickListener {

    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());


    private EditText dateEdt;
    private EditText startTimeEdt;
    private EditText endTImeEdt;
    private EditText descriptionEdt;

    private ImageView mBackImage;
    private TextView mBackText;
    private AppCompatButton mSubmitBtn;
    private String currentDate;

    private String selectedDate;
    private String selectedStartTime;
    private String selectedEndTime;
    private String setDescription;
    Data mData;
    List<Data> scheduleList = new ArrayList<>();
    List<Date> startTimeList = new ArrayList<>();
    List<Date> endTimeList = new ArrayList<>();
    List<Slot> slotList = new ArrayList<>();
    public static SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm",Locale.ENGLISH);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeting_schedule_view);

        mData = new Data();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        scheduleList = (ArrayList<Data>) args.getSerializable("ScheduleList");

        for (Data data:scheduleList){
            Date start,end;
            try {
            start = timeFormatter.parse(data.getStartTime());
            end  = timeFormatter.parse(data.getEndTime());
//                startTimeList.add(start);
//                endTimeList.add(end);
                slotList.add(new Slot(start,end));
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        currentDate = args.getString("Date");


        dateEdt = findViewById(R.id.edit_txt_date);
        startTimeEdt = findViewById(R.id.edit_txt_startTime);
        endTImeEdt = findViewById(R.id.edit_txt_endTime);
        mBackImage = findViewById(R.id.back_Img);
        mBackText = findViewById(R.id.txt_back_meeting);
        descriptionEdt = findViewById(R.id.desc_edt);
        mSubmitBtn = findViewById(R.id.submit_meeting_button);

        dateEdt.setOnClickListener(this);
        startTimeEdt.setOnClickListener(this);
        endTImeEdt.setOnClickListener(this);
        mBackImage.setOnClickListener(this);
        mBackText.setOnClickListener(this);

        mSubmitBtn.setOnClickListener(this);

        dateEdt.setText(currentDate);
//        mSubmitBtn.setEnabled(false);
    }

    public void dateClick() {

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(selectedyear, selectedmonth, selectedday);
                Date date = newDate.getTime();
                String selectedDate = df.format(date);
                dateEdt.setText(selectedDate);
//                setHour(0);
                // TODO Auto-generated method stub
                /*      Your code   to get date and time    */
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis());
        mDatePicker.show();

    }

    public void timePicker(final EditText edt) {
        Calendar mcurrentDate = Calendar.getInstance();
        int mHour = mcurrentDate.get(Calendar.HOUR);
        int mMin = mcurrentDate.get(Calendar.MINUTE);
        TimePickerDialog nTimePickerDialog = new TimePickerDialog(ScheduleMeetingActivity.this, 2,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker pTimePicker, int hr, int min) {
                        edt.setText(MessageFormat.format("{0}:{1}", hr, min));
                    }
                },
                mHour, mMin, false);
        nTimePickerDialog.setTitle("Select Intervals");
        nTimePickerDialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_txt_date:
                dateClick();
                break;
            case R.id.edit_txt_startTime:
                timePicker(startTimeEdt);
                break;
            case R.id.edit_txt_endTime:
                timePicker(endTImeEdt);
                break;
            case R.id.back_Img:
            case R.id.txt_back_meeting:
                finish();
                break;
            case R.id.submit_meeting_button:
                onSubmitClicked();
                break;

        }
    }
    public void onSubmitClicked() {

        try {
            if (!TextUtils.isEmpty(selectedStartTime)) {
                startTimeList.remove(timeFormatter.parse(selectedStartTime));
            }
            if (!TextUtils.isEmpty(selectedEndTime)) {
                endTimeList.remove(timeFormatter.parse(selectedEndTime));
            }
        selectedDate = dateEdt.getText().toString();
        selectedStartTime = startTimeEdt.getText().toString();
        selectedEndTime = endTImeEdt.getText().toString();
        setDescription = descriptionEdt.getText().toString();
        Date startTime,endTime;

             startTime = timeFormatter.parse(selectedStartTime);
             endTime = timeFormatter.parse(selectedEndTime);
//             startTimeList.add(startTime);
//             endTimeList.add(endTime);

            Slot slotEntered = new Slot(startTime,endTime);


        //Check Slot

        Toast.makeText(this,correctSlot(slotList,slotEntered),Toast.LENGTH_LONG).show();
        }catch (Exception e){

        }
    }


//    public String correctSlot(List<Date> startTime, List<Date> endTime){
//
//        Boolean lock=false;
//        Boolean flag=false;
//
//        for(int i=0,j=0; i<startTime.size()||j<endTime.size(); ){
//
//            if(startTime.get(i).before(endTime.get(j))){
//                if(lock){flag=true;break;}
//                lock=true;
//                i++;
//            }
//
//            else{
//                if(!lock){flag=true;break;}
//                lock=false;
//                j++;
//            }
//        }
//
//        if(flag)
//            return ("Slot not Available");
//        else
//            return ("Slot Available");
//    }

    static String correctSlot(List<Slot> meetingSlots, Slot check){


        Boolean flag=false;

        for(int i=0; i<meetingSlots.size();i++){

            if((check.getStartTime().before(meetingSlots.get(i).getEndTime())&&check.getStartTime().after(meetingSlots.get(i).getEndTime()))
                    || (check.getEndTime().before(meetingSlots.get(i).getEndTime())&&check.getEndTime().after(meetingSlots.get(i).getStartTime()))){

                flag=true;
                break;
            }
        }

        if(flag)
            return ("Slot not Available");
        else
            return ("Slot Available");
    }
}
