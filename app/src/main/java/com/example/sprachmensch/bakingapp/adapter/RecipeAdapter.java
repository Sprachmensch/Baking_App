package com.example.sprachmensch.bakingapp.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sprachmensch.bakingapp.data.Ingredient;
import com.example.sprachmensch.bakingapp.R;
import com.example.sprachmensch.bakingapp.data.Recipe;
import com.example.sprachmensch.bakingapp.activity.RecipeSelectActivity;
import com.example.sprachmensch.bakingapp.widget.RecipeWidget;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder> {
    private final Context context;
    private final List<Recipe> recipes;
    private List<Ingredient> ingredients;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item,
                parent, false);

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecipeViewHolder holder, final int position) {
        holder.recipe_tv.setText(recipes.get(position).getName());
        holder.servings_tv.setText(" x " + String.valueOf(recipes.get(position).getServings()));

        holder.addWidget_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, RecipeWidget.class));
                Intent updateIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);

                ingredients = recipes.get(position).getIngredients();

                String widgetQuantity = createWidgetQuantity();
                String widgetRecipe = createWidgetRecipe();

                updateIntent.putExtra("quantity", widgetQuantity);
                updateIntent.putExtra("recipe", widgetRecipe);
                updateIntent.putExtra("recipeName", recipes.get(position).getName() + " Recipe");
                updateIntent.setComponent(new ComponentName(context, RecipeWidget.class));
                context.sendBroadcast(updateIntent);
                Toast.makeText(context, "Added Ingredients for " + recipes.get(position).getName() + " to Widget", Toast.LENGTH_SHORT).show();
                Log.d("onClick", "intent:recipe" + widgetRecipe);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RecipeSelectActivity.class);
                intent.putExtra("recipe", String.valueOf(position));
                context.startActivity(intent);
            }
        });
    }

    private String createWidgetRecipe() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Ingredient ingredient : ingredients) {
            stringBuilder.append(ingredient.getIngredient());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    private String createWidgetQuantity() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Ingredient ingredient : ingredients) {
            stringBuilder.append(String.valueOf(ingredient.getQuantity()));
            stringBuilder.append(" ");
            stringBuilder.append(ingredient.getMeasure());
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }
}

class RecipeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.descTv)
    TextView recipe_tv;

    @BindView(R.id.servings_tv)
    TextView servings_tv;

    @BindView(R.id.addWidget_tv)
    TextView addWidget_tv;

    @BindView(R.id.cardView)
    CardView cardView;

    public RecipeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
