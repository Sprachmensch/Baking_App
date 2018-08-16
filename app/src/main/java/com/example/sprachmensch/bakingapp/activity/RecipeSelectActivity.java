package com.example.sprachmensch.bakingapp.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.sprachmensch.bakingapp.data.Ingredient;
import com.example.sprachmensch.bakingapp.fragments.IngredientsFragment;
import com.example.sprachmensch.bakingapp.R;
import com.example.sprachmensch.bakingapp.data.Recipe;
import com.example.sprachmensch.bakingapp.data.Step;
import com.example.sprachmensch.bakingapp.adapter.IngredientsAdapter;
import com.example.sprachmensch.bakingapp.adapter.JSONSingleton;
import com.example.sprachmensch.bakingapp.adapter.StepsAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeSelectActivity extends AppCompatActivity {

    @BindView(R.id.recyclerViewIngredients)
    RecyclerView recyclerViewIngredients;

    @BindView(R.id.recyclerViewSteps)
    RecyclerView recyclerViewSteps;

    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private List<Recipe> recipe;
    private boolean tabletLayout;
    private int id;
    private FragmentManager fragmentManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_select);
        ButterKnife.bind(this);

        recyclerViewIngredients.setNestedScrollingEnabled(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RecipeSelectActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewIngredients.setLayoutManager(linearLayoutManager);

        LinearLayoutManager linearLayoutManagerSteps = new LinearLayoutManager(RecipeSelectActivity.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewSteps.setLayoutManager(linearLayoutManagerSteps);

        Log.d("CHECK LAYOUT", "?");
        if (findViewById(R.id.tabletLayout) != null) {
            tabletLayout = true;
            recyclerViewIngredients.setVisibility(View.GONE);
            fragmentManager = getSupportFragmentManager();
            Log.d("TABLET LAYOUT", ".");
        }else{
            Log.d("SMARTPHONE LAYOUT", ".");
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d("BUNDLE", " " + bundle.getString("recipe"));
            id = Integer.parseInt(bundle.getString("recipe"));
            GetRecipeNumber(id);
        } else {
            Log.d("BUNDLE", " NOPE NO RECIPE :(");
        }
    }

    private void GetRecipeNumber(final int id) {

        if (JSONSingleton.getInstance().isLoaded()) {
            recipe = JSONSingleton.getInstance().getRecipe();

            List<Ingredient> ingredients = recipe.get(id).getIngredients();
            List<Step> steps = recipe.get(id).getSteps();

            ingredientsAdapter = new IngredientsAdapter(RecipeSelectActivity.this, ingredients);
            recyclerViewIngredients.setAdapter(ingredientsAdapter);

            stepsAdapter = new StepsAdapter(RecipeSelectActivity.this, steps, id, tabletLayout, fragmentManager);
            recyclerViewSteps.setAdapter(stepsAdapter);

            if (tabletLayout) {
                IngredientsFragment fragment = IngredientsFragment.newInstance(id);
                fragmentManager.beginTransaction()
                        .add(R.id.step_container, fragment)
                        .commit();
            }
        }
    }
}
