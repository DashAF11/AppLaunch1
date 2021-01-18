package com.example.applaunch.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.applaunch.Entity.ImageEntity;
import com.example.applaunch.R;
import com.example.applaunch.Retrofit.pojo.CategoryInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    Context context;
    List<ImageEntity> imageEntityList = new ArrayList<>();
    RequestOptions requestOptions;
    CategoryClickListener categoryClickListener;

    public ImageAdapter(Context context, CategoryClickListener categoryClickListener) {
        this.context = context;
        this.categoryClickListener = categoryClickListener;
    }

    public void setImageList(List<ImageEntity> imageList) {
        this.imageEntityList = imageList;
        Timber.d("imageEntityList_Size : %s", imageList.size());
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.image_recycler_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageEntity imageEntity = imageEntityList.get(position);

        holder.imageId_TextView.setText("" + imageEntity.getImage_id());
        holder.image_url_TextView.setText(imageEntity.getImage_url());

        String url = imageEntity.getImage_url();
        Timber.d("CategoryImage %s : ", url);
        Glide.with(context)
                .load(url)
                .apply(requestOptions)
                .into(holder.image_ImageView);

        holder.image_ImageView.setOnClickListener(view -> {
            Timber.d("Clicked");
            categoryClickListener.singleClickListener(imageEntity);
        });

    }

    @Override
    public int getItemCount() {
        return imageEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.imageId_TextView)
        TextView imageId_TextView;
        @BindView(R.id.image_url_TextView)
        TextView image_url_TextView;

        @BindView(R.id.image_ImageView)
        ImageView image_ImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background);
        }
    }

    public interface CategoryClickListener {
        public void singleClickListener(ImageEntity imageEntity);
    }
}
