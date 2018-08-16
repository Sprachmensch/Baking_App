package com.example.sprachmensch.bakingapp.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.example.sprachmensch.bakingapp.fragments.DetailFragment;
import com.example.sprachmensch.bakingapp.R;

import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity {
    /*
      ############# NOTES #############

      jsonschema2pojo.org was used to create the POJO's
      codelabs.developers.google.com/codelabs/exoplayer-intro was used a reference

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_step_new);

        hideSystemUI();
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            int recipeNumber = Integer.parseInt(bundle.getString("recipe"));
            int stepNumber = Integer.parseInt(bundle.getString("step"));

            if (savedInstanceState == null) {
                DetailFragment detailFragment = DetailFragment.newInstance(recipeNumber, stepNumber);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(R.id.fragment_container, detailFragment);
                ft.commit();
            }
        }
    }

    private void hideSystemUI() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("KILL the ACTIONBAR", "shoo shoo go away...");
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
        }
    }
}
