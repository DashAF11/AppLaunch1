package com.example.applaunch.Retrofit;

import com.example.applaunch.Retrofit.pojo.CategoryDetails;

import retrofit2.Call;
import retrofit2.http.GET;

import static com.example.applaunch.Retrofit.Constant_API.CASE_CATEGORY_DETAILS;

public interface ApiInterface {

    @GET(CASE_CATEGORY_DETAILS)
    Call<CategoryDetails> getCategory();

}
