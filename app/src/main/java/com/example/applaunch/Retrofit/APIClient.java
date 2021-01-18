package com.example.applaunch.Retrofit;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.applaunch.Retrofit.Constant_API.BASE_URL;

public class APIClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
       /* Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/

        if (retrofit == null) {
            Retrofit.Builder builder = new Retrofit.Builder();
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            /*if (BuildConfig.DEBUG) {
                builder.baseUrl(TRIAL_URL).client(client);
            } else {*/
            builder.baseUrl(BASE_URL).client(client);
            //}

            builder.addConverterFactory(GsonConverterFactory.create())//gson
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            ;
            retrofit = builder.build();
        }
        return retrofit;
    }
}
