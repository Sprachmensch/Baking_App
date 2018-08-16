package com.example.sprachmensch.bakingapp.adapter;

import android.util.Log;

import com.example.sprachmensch.bakingapp.data.Recipe;

import java.util.List;

public class JSONSingleton {
    private static List<Recipe> recipe;
    private static JSONSingleton INSTANCE = null;
    private final boolean isLoaded;

    public JSONSingleton(List<Recipe> recipe) {
        this.recipe = recipe;
        isLoaded=true;
        Log.d("JSONSingleton", "is created!");
    }

    public static synchronized JSONSingleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new JSONSingleton(recipe);
        }
        return (INSTANCE);
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public List<Recipe> getRecipe() {
        Log.d("JSONSingleton", "getRecipe()");
        return recipe;
    }


}


