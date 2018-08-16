package com.example.sprachmensch.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sprachmensch.bakingapp.data.Ingredient;
import com.example.sprachmensch.bakingapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsViewHolder> {
    private final Context context;
    private final List<Ingredient> ingredient;

    public IngredientsAdapter(Context context, List<Ingredient> ingredient) {
        this.context = context;
        this.ingredient = ingredient;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item,
                parent, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IngredientsViewHolder holder, final int position) {
        String removeNull = String.valueOf(ingredient.get(position).getQuantity());
        if (removeNull.contains(".0"))
            removeNull = removeNull.substring(0, removeNull.length() - 2);
        holder.quantity_tv.setText(removeNull);
        holder.measure_tv.setText(ingredient.get(position).getMeasure());
        holder.ingredient_tv.setText(ingredient.get(position).getIngredient());
    }


    @Override
    public int getItemCount() {
        return ingredient.size();
    }
}

class IngredientsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.ingredient_tv)
    TextView ingredient_tv;

    @BindView(R.id.descTv)
    TextView quantity_tv;

    @BindView(R.id.measure_tv)
    TextView measure_tv;

    public IngredientsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
