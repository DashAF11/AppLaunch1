package com.example.applaunch.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.applaunch.Dao.ImageDao;
import com.example.applaunch.Entity.ImageEntity;
import com.example.applaunch.Retrofit.APIClient;
import com.example.applaunch.Retrofit.ApiInterface;
import com.example.applaunch.Retrofit.pojo.CategoryDetails;
import com.example.applaunch.Retrofit.pojo.CategoryInfo;
import com.example.applaunch.RoomDB.RoomDB;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

public class DetailsViewModel extends AndroidViewModel {

    ApiInterface apiInterface;
    List<CategoryInfo> categoryInfos = new ArrayList<>();
    String url;
    private RoomDB roomDB;
    ImageDao imageDao;

    public DetailsViewModel(@NonNull Application application) {
        super(application);
        apiInterface = APIClient.getClient().create(ApiInterface.class);
        roomDB = RoomDB.getRoomDB(application);
        imageDao = roomDB.imageDao();
    }

    public List<CategoryInfo> categoryInfoList() {
        return categoryInfos;
    }

    public void getCategoryDetails() {
        Single.fromCallable(() -> {

            Response<CategoryDetails> categoryDetailsResponse = apiInterface.getCategory().execute();
            Timber.d("category_Response :\n %s", categoryDetailsResponse.body().getHits().toString());

            List<CategoryInfo> categoryInfoList = categoryDetailsResponse.body().getHits();
            int count = 0;

            for (int i = 0; i < categoryInfoList.size(); i++) {
                if (categoryInfoList.get(i).getTags().contains("trees")) {
                    count++;
                }
            }
            Timber.d("Count : %d", count);

            Timber.d("category_data : %s \n", categoryInfoList.size());
            //Timber.d("category_data \n%d", movies_data.size());
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
                        categoryInfos.addAll(categoryInfoList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public String getSingleImageURL() {
        Completable.fromAction(() -> {

            Response<CategoryDetails> categoryDetailsResponse = apiInterface.getCategory().execute();
            Timber.d("category_Response :\n %s", categoryDetailsResponse.body().getHits().toString());

            List<CategoryInfo> categoryInfoList = categoryDetailsResponse.body().getHits();

            Timber.d("category_data : %s \n", categoryInfoList.size());
            url = categoryInfoList.get(0).getLargeImageURL();
            Timber.d("category_url \n%s", url);

        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
        return url;
    }

    public void insertImage(ImageEntity imageEntity) {

        Completable.fromAction(() -> {

            imageDao.insert(imageEntity);

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("Inserted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });

    }

    public LiveData<List<ImageEntity>> getImageEntity() {
        return imageDao.getImageLiveData();
    }
    public LiveData<List<ImageEntity>> getFavImageLiveData() {
        return imageDao.getFavImageLiveData("true");
    }


    public void updateFavImage(long imgId, String fav) {

        Completable.fromAction(() -> {

            imageDao.updateFavImage(imgId, fav);

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Timber.d("updateFavouriteCategory_onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }
}