package com.example.sprachmensch.bakingapp.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.sprachmensch.bakingapp.R;
import com.example.sprachmensch.bakingapp.adapter.JSONSingleton;
import com.example.sprachmensch.bakingapp.adapter.RecipeAdapter;
import com.example.sprachmensch.bakingapp.data.Recipe;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private RecipeAdapter recipeAdapter;
    private JSONSingleton jsonSingleton;
    private boolean tabletLayout;
    private String noConnection = "NO INTERNET - Please check your Internet connection!";
    private String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
    private ArrayList<Recipe> recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Log.d("CHECK LAYOUT", "?");
        if (findViewById(R.id.tabletLayout) != null) {
            Log.d("TABLET LAYOUT", ".");

            tabletLayout = true;

            GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            Log.d("SMARTPHONE", ".");
            LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this,
                    LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
        }

        if (savedInstanceState != null) {
            Log.d("savedInstanceState", "restored the recipe list");
            recipe = savedInstanceState.getParcelableArrayList("recipe");
            useRecipe();
        } else {
            Log.d("savedInstanceState", "created a new recipe list");
            makeJSONRequest();
        }
    }

    private void makeJSONRequest() {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        try {
                        Gson gson = new GsonBuilder().create();

                        Type category = new TypeToken<List<Recipe>>() {
                        }.getType();

                        recipe = gson.fromJson(response.toString(), category);

                        useRecipe();

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("BAKING", "VolleyError: " + error.toString());

                        Snackbar snackbar;
                        if (findViewById(R.id.tabletLayout) != null) {
                            snackbar = Snackbar.make(findViewById(R.id.tabletLayout), noConnection, Snackbar.LENGTH_INDEFINITE);
                        } else {
                            snackbar = Snackbar.make(findViewById(R.id.layout), noConnection, Snackbar.LENGTH_INDEFINITE);
                        }
                        snackbar.setAction("Try Again", new SnackbarListener());
                        snackbar.show();

                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void useRecipe() {
        jsonSingleton = new JSONSingleton(recipe);

        recipeAdapter = new RecipeAdapter(MainActivity.this, recipe);
        recyclerView.setAdapter(recipeAdapter);
    }

    public class SnackbarListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            makeJSONRequest();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("recipe", recipe);
    }
}
