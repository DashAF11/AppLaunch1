package com.example.applaunch.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.applaunch.R;
import com.example.applaunch.Retrofit.APIClient;
import com.example.applaunch.Retrofit.ApiInterface;
import com.example.applaunch.Retrofit.pojo.CategoryDetails;
import com.example.applaunch.Retrofit.pojo.CategoryInfo;
import com.example.applaunch.ViewModel.DetailsViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.splashImage_ImageView)
    ImageView splashImage_ImageView;

    DetailsViewModel mViewModel;
    ApiInterface apiInterface;
    RequestOptions requestOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash_screen_layout);
        ButterKnife.bind(this);

        apiInterface = APIClient.getClient().create(ApiInterface.class);
        mViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.white)
                .error(R.color.white);

        setImage();
        SplashScreen.logolauncher lc = new SplashScreen.logolauncher();
        lc.start();
    }

    private class logolauncher extends Thread {
        public void run() {
            try {
                sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            SplashScreen.this.finish();
        }
    }

    public void setImage() {

        Single.fromCallable(() -> {
            Response<CategoryDetails> categoryDetailsResponse = apiInterface.getCategory().execute();
            // Timber.d("category_Response :\n %s", categoryDetailsResponse.body().getHits().toString());

            List<CategoryInfo> categoryInfoList = categoryDetailsResponse.body().getHits();

            Timber.d("category_data : %s \n", categoryInfoList.size());

            return categoryInfoList;
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<List<CategoryInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onSuccess(List<CategoryInfo> categoryInfoList) {
                        Timber.d("categoryOnSuccess : %s", categoryInfoList.toString());
                        Glide.with(getApplication())
                                .load(categoryInfoList.get(0).getLargeImageURL())
                                .apply(requestOptions)
                                .into(splashImage_ImageView);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}