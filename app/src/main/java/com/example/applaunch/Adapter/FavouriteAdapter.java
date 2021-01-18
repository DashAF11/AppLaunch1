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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {

    Context context;
    List<ImageEntity> imageEntityList = new ArrayList<>();
    RequestOptions requestOptions;

    public FavouriteAdapter(Context context) {
        this.context = context;
    }

    public void setFavImageList(List<ImageEntity> imageList) {
        this.imageEntityList = imageList;
        Timber.d("imageEntityList_Size : %s", imageList.size());
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.fav_recycler_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageEntity imageEntity = imageEntityList.get(position);

        holder.fav_imageId_TextView.setText("" + imageEntity.getImage_id());
        holder.fav_totalLikes_TextView.setText("" + imageEntity.getImage_like());

        Glide.with(context)
                .load(imageEntity.getImage_url())
                .apply(requestOptions)
                .into(holder.fav_ImageView);

        if (imageEntity.getImage_fav().equals("true")){
            holder.fav_icon_ImageView.setBackgroundResource(R.drawable.heart_fill_icon);
        }else {
            holder.fav_icon_ImageView.setBackgroundResource(R.drawable.heart_blank_icon);
        }

    }

    @Override
    public int getItemCount() {
        return imageEntityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.fav_ImageView)
        ImageView fav_ImageView;
        @BindView(R.id.fav_icon_ImageView)
        ImageView fav_icon_ImageView;

        @BindView(R.id.fav_imageId_TextView)
        TextView fav_imageId_TextView;
        @BindView(R.id.fav_totalLikes_TextView)
        TextView fav_totalLikes_TextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            requestOptions = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background);
        }
    }
}
