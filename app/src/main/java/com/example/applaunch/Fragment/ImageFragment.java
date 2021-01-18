package com.example.applaunch.Fragment;

import android.app.AlertDialog;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.applaunch.Adapter.ImageAdapter;
import com.example.applaunch.Entity.ImageEntity;
import com.example.applaunch.R;
import com.example.applaunch.Retrofit.APIClient;
import com.example.applaunch.Retrofit.ApiInterface;
import com.example.applaunch.Retrofit.pojo.CategoryDetails;
import com.example.applaunch.Retrofit.pojo.CategoryInfo;
import com.example.applaunch.ViewModel.DetailsViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

import static android.content.Context.MODE_PRIVATE;

public class ImageFragment extends Fragment implements ImageAdapter.CategoryClickListener {

    @BindView(R.id.category_TextView)
    TextView category_TextView;

    @BindView(R.id.mProgressDialog)
    ProgressBar mProgressDialog;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    DetailsViewModel mViewModel;
    ImageAdapter imageAdapter;
    String category;
    ApiInterface apiInterface;
    RequestOptions requestOptions;

    private AsyncTask mMyTask;


    public static ImageFragment newInstance() {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);
        apiInterface = APIClient.getClient().create(ApiInterface.class);

        requestOptions = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background);

        if (getArguments() != null) {
            ImageFragmentArgs args = ImageFragmentArgs.fromBundle(getArguments());
            // Timber.d(" TaskFragmentArgs ==> catName : %s, CatId : %d", args.getCatName(), args.getCatId());
            category = args.getCategory();
            category_TextView.setText(category);
        }

        getCategoryData();
        setRecyclerView();
    }

    public void getCategoryData() {
//        mViewModel.getCategoryDetails(category);
//        imageAdapter.setImageList(mViewModel.categoryInfoList());

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
                        //  Timber.d("categoryOnSuccess : %s", categoryInfoList.toString());
                        ImageEntity imageEntity = new ImageEntity();

                        for (int i = 0; i < categoryInfoList.size(); i++) {
                            imageEntity.setImage_id(categoryInfoList.get(i).getId());
                            imageEntity.setImage_url(categoryInfoList.get(i).getLargeImageURL());
                            imageEntity.setImage_fav("false");
                            imageEntity.setImage_like(String.valueOf(categoryInfoList.get(i).getLikes()));
                            mViewModel.insertImage(imageEntity);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e);
                    }
                });
    }

    public void setRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);//new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imageAdapter = new ImageAdapter(requireActivity(), this);
        recyclerView.setAdapter(imageAdapter);

        setAdapterList();
    }

    public void setAdapterList() {
        mViewModel.getImageEntity().observe(getViewLifecycleOwner(), imageEntities -> {
            Timber.d("Image_Entities : %s", imageEntities.toString());
            imageAdapter.setImageList(imageEntities);
        });

    }

    @Override
    public void singleClickListener(ImageEntity imageEntity) {
        Timber.d("URL : %s", imageEntity.getImage_url());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        // Get the layout inflater
        LayoutInflater inflater = (requireActivity()).getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.popup_image_info_layout, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        TextView imageID_TextView = (TextView) dialogView.findViewById(R.id.imageId_TextView);
        TextView totalLikes_TextView = (TextView) dialogView.findViewById(R.id.totalLikes_TextView);

        ImageView download_ImageView = (ImageView) dialogView.findViewById(R.id.download_ImageView);
        ImageView favourite_ImageView = (ImageView) dialogView.findViewById(R.id.favourite_ImageView);
        ImageView close_ImageView = (ImageView) dialogView.findViewById(R.id.close_ImageView);
        ImageView image_ImageView = (ImageView) dialogView.findViewById(R.id.image_ImageView);

        imageID_TextView.setText("" + imageEntity.getImage_id());
        totalLikes_TextView.setText("  " + imageEntity.getImage_like());


        Glide.with(requireActivity())
                .load(imageEntity.getImage_url())
                .apply(requestOptions)
                .into(image_ImageView);

        builder.create();
        final AlertDialog ad = builder.show();
        // builder.show();
        close_ImageView.setOnClickListener(v12 -> ad.dismiss());

        download_ImageView.setOnClickListener(view -> {
            mMyTask = new DownloadTask()
                    .execute(stringToURL(
                            imageEntity.getImage_url()
                    ));

            Toast.makeText(requireActivity(), "Downloading Image", Toast.LENGTH_SHORT).show();
        });

        if (imageEntity.getImage_fav().equals("true")) {
            favourite_ImageView.setBackgroundResource(R.drawable.heart_fill_icon);
        } else {
            favourite_ImageView.setBackgroundResource(R.drawable.heart_blank_icon);
        }

        favourite_ImageView.setOnClickListener(view -> {
            Timber.d("Fav_Clicked");
            mViewModel.updateFavImage(imageEntity.getImage_id(), "true");
            ad.dismiss();
        });

    }

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {
        // Before the tasks execution
        protected void onPreExecute() {
            // Display the progress dialog on async task start
            mProgressDialog.setVisibility(View.VISIBLE);
        }

        // Do the task in background/non UI thread
        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;

            try {
                // Initialize a new http url connection
                connection = (HttpURLConnection) url.openConnection();

                // Connect the http url connection
                connection.connect();

                // Get the input stream from http url connection
                InputStream inputStream = connection.getInputStream();

                /*
                    BufferedInputStream
                        A BufferedInputStream adds functionality to another input stream-namely,
                        the ability to buffer the input and to support the mark and reset methods.
                */
                /*
                    BufferedInputStream(InputStream in)
                        Creates a BufferedInputStream and saves its argument,
                        the input stream in, for later use.
                */
                // Initialize a new BufferedInputStream from InputStream
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                /*
                    decodeStream
                        Bitmap decodeStream (InputStream is)
                            Decode an input stream into a bitmap. If the input stream is null, or
                            cannot be used to decode a bitmap, the function returns null. The stream's
                            position will be where ever it was after the encoded data was read.

                        Parameters
                            is InputStream : The input stream that holds the raw data
                                              to be decoded into a bitmap.
                        Returns
                            Bitmap : The decoded bitmap, or null if the image data could not be decoded.
                */
                // Convert BufferedInputStream to Bitmap object
                Bitmap bmp = BitmapFactory.decodeStream(bufferedInputStream);

                // Return the downloaded bitmap
                return bmp;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                // Disconnect the http url connection
                connection.disconnect();
            }
            return null;
        }

        // When all async task done
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            mProgressDialog.setVisibility(View.GONE);

            if (result != null) {
                // Display the downloaded image into ImageView
                //  mImageView.setImageBitmap(result);

                // Save bitmap to internal storage
                Uri imageInternalUri = saveImageToInternalStorage(result);
                // Set the ImageView image from internal storage
                //  mImageViewInternal.setImageURI(imageInternalUri);
            } else {
                // Notify user that an error occurred while downloading image
                // Snackbar.make(mCLayout,"Error",Snackbar.LENGTH_LONG).show();
            }
        }
    }

    // Custom method to convert string to url
    protected URL stringToURL(String urlString) {
        try {
            URL url = new URL(urlString);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Custom method to save a bitmap into internal storage
    protected Uri saveImageToInternalStorage(Bitmap bitmap) {
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(requireActivity());

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images", MODE_PRIVATE);

        // Create a file to save the image
        file = new File(file, System.currentTimeMillis() + ".jpg");

        try {
            // Initialize a new OutputStream
            OutputStream stream = null;

            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            // Flushes the stream
            stream.flush();

            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        Uri savedImageURI = Uri.parse(file.getAbsolutePath());

        // Return the saved image Uri
        return savedImageURI;
    }

}