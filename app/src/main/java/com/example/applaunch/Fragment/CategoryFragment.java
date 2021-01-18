package com.example.applaunch.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.applaunch.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryFragment extends Fragment {

    NavController navController;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        navController = Navigation.findNavController(view);
    }

    @OnClick(R.id.treesConstraint)
    public void treesClick() {
        CategoryFragmentDirections.ActionCategoryFragmentToImageFragment action =
                CategoryFragmentDirections.actionCategoryFragmentToImageFragment();
        action.setCategory("trees");
        navController.navigate(action);
    }

    @OnClick(R.id.animalConstraint)
    public void animalClick() {
        CategoryFragmentDirections.ActionCategoryFragmentToImageFragment action =
                CategoryFragmentDirections.actionCategoryFragmentToImageFragment();
        action.setCategory("Animal");
        navController.navigate(action);
    }

    @OnClick(R.id.flowerConstraint)
    public void flowerClick() {
        CategoryFragmentDirections.ActionCategoryFragmentToImageFragment action =
                CategoryFragmentDirections.actionCategoryFragmentToImageFragment();
        action.setCategory("flower");
        navController.navigate(action);
    }

    @OnClick(R.id.natureConstraint)
    public void natureClick() {
        CategoryFragmentDirections.ActionCategoryFragmentToImageFragment action =
                CategoryFragmentDirections.actionCategoryFragmentToImageFragment();
        action.setCategory("nature");
        navController.navigate(action);
    }

    @OnClick(R.id.otherConstraint)
    public void otherClick() {
        CategoryFragmentDirections.ActionCategoryFragmentToImageFragment action =
                CategoryFragmentDirections.actionCategoryFragmentToImageFragment();
        action.setCategory("other");
        navController.navigate(action);
    }

    @OnClick(R.id.favouriteConstraint)
    public void favClick() {
        navController.navigate(R.id.action_categoryFragment_to_favouriteFragment);
    }
}