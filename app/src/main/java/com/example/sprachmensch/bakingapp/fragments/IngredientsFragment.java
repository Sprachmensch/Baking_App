package com.example.sprachmensch.bakingapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sprachmensch.bakingapp.R;
import com.example.sprachmensch.bakingapp.adapter.IngredientsAdapter;
import com.example.sprachmensch.bakingapp.adapter.JSONSingleton;
import com.example.sprachmensch.bakingapp.data.Ingredient;
import com.example.sprachmensch.bakingapp.data.Recipe;

import java.util.List;


public class IngredientsFragment extends Fragment {

    RecyclerView recyclerViewIngredients;
    private JSONSingleton jsonSingleton;
    private List<Recipe> recipe;
    private int id;

    public IngredientsFragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        id=getArguments().getInt("id");

        View rootView = inflater.inflate(R.layout.frag_ingredient_detail, container, false);
        recyclerViewIngredients = rootView.findViewById(R.id.recyclerViewIngredients);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerViewIngredients.setLayoutManager(linearLayoutManager);

        getJSONData();

        return rootView;
    }

    private void getJSONData() {
        if (jsonSingleton.getInstance().isLoaded()) {
            recipe = jsonSingleton.getInstance().getRecipe();

            List<Ingredient> ingredients = recipe.get(id).getIngredients();

            IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(getContext(), ingredients);
            recyclerViewIngredients.setAdapter(ingredientsAdapter);

        } else {
            Log.d("JSON", "jsonSingleton not loaded!");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
    }


    public static IngredientsFragment newInstance(int id) {
        Log.d("IngredientsFragment", "newInstance!");
        IngredientsFragment fragment = new IngredientsFragment();

        Bundle arguments = new Bundle();
        arguments.putInt("id",id);
        fragment.setArguments(arguments);

        return fragment;
    }
}

