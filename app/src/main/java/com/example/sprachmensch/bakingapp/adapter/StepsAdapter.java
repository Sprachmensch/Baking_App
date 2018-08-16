package com.example.sprachmensch.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sprachmensch.bakingapp.fragments.DetailFragment;
import com.example.sprachmensch.bakingapp.R;
import com.example.sprachmensch.bakingapp.activity.RecipeStepActivity;
import com.example.sprachmensch.bakingapp.data.Step;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsViewHolder> {
    private final Context context;
    private final List<Step> steps;
    private final int recipeNumber;
    private boolean isTablet;
    private FragmentManager fragmentManager;
    private int selectedPosition = -1;

    public StepsAdapter(Context context, List<Step> steps, int recipeNumber, boolean isTablet, FragmentManager fragmentManager) {
        this.context = context;
        this.steps = steps;
        this.recipeNumber = recipeNumber;
        this.isTablet = isTablet;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item,
                parent, false);
        return new StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StepsViewHolder holder, final int position) {
        holder.stepNr_tv.setText(steps.get(position).getId().toString());
        holder.shortDesc_tv.setText(steps.get(position).getShortDescription());

        Log.d("onBindVH", "pos: " + position);
        if (selectedPosition == position)
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorAccentSoft));
        else
            holder.cardView.setBackgroundColor(context.getResources().getColor(R.color.colorBackground));

        if (!steps.get(position).getThumbnailURL().equals("")) {
            holder.playBtn.setVisibility(View.VISIBLE);

            Log.d("onBindVH - Picasso", "ThumbnailURL: " + steps.get(position).getThumbnailURL());
            Picasso.get()
                    .load(steps.get(position).getThumbnailURL())
                    .into(holder.playBtn);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTablet) {
                    notifyItemChanged(selectedPosition);
                    selectedPosition = holder.getAdapterPosition();
                    notifyItemChanged(selectedPosition);

                    DetailFragment fragment = DetailFragment.newInstance(recipeNumber, position);
                    fragmentManager.beginTransaction()
                            .replace(R.id.step_container, fragment)
                            .commit();
                    Log.d("onBindViewHolder", "fragment added!");

                } else {

                    Intent intent = new Intent(context, RecipeStepActivity.class);
                    intent.putExtra("recipe", String.valueOf(recipeNumber));
                    intent.putExtra("step", String.valueOf(position));
                    context.startActivity(intent);
                    Log.d("onBindViewHolder", "NO fragment added!");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return steps.size();
    }

}

class StepsViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.stepNr_tv)
    TextView stepNr_tv;

    @BindView(R.id.shortDesc_tv)
    TextView shortDesc_tv;

    @BindView(R.id.playBtn)
    ImageView playBtn;

    @BindView(R.id.cardView)
    RelativeLayout cardView;

    public StepsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
