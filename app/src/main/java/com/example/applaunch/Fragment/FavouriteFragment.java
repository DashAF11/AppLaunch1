package com.example.applaunch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applaunch.Adapter.FavouriteAdapter;
import com.example.applaunch.Adapter.ImageAdapter;
import com.example.applaunch.R;
import com.example.applaunch.ViewModel.DetailsViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class FavouriteFragment extends Fragment {

    @BindView(R.id.favRecyclerView)
    RecyclerView favRecyclerView;

    DetailsViewModel mViewModel;
    FavouriteAdapter favouriteAdapter;

    public static FavouriteFragment newInstance() {
        FavouriteFragment fragment = new FavouriteFragment();
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
        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        setRecyclerView();
        setAdapterList();
    }

    public void setRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireActivity(), 2);
        favRecyclerView.setLayoutManager(gridLayoutManager);//new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false)
        favRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteAdapter = new FavouriteAdapter(requireActivity());
        favRecyclerView.setAdapter(favouriteAdapter);
    }

    private void setAdapterList() {
        mViewModel.getFavImageLiveData().observe(getViewLifecycleOwner(), imageEntities -> {
            Timber.d("Image_Entities : %s", imageEntities.toString());
            favouriteAdapter.setFavImageList(imageEntities);
        });
    }
}