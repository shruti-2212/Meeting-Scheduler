package com.example.myapplication.Api;

import com.example.myapplication.Data;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
//    http://fathomless-shelf-5846.herokuapp.com/api/schedule?date="7/8/2015"
    String baseUrl = " http://fathomless-shelf-5846.herokuapp.com/";

    @GET("api/schedule")
    Call<List<Data>> getSchedule(@Query("date") String date);


}

