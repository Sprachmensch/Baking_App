package com.example.sprachmensch.bakingapp.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sprachmensch.bakingapp.R;
import com.example.sprachmensch.bakingapp.activity.RecipeStepActivity;
import com.example.sprachmensch.bakingapp.adapter.JSONSingleton;
import com.example.sprachmensch.bakingapp.data.Recipe;
import com.example.sprachmensch.bakingapp.data.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailFragment extends Fragment {

    @BindView(R.id.video_view)
    PlayerView playerView;

    @BindView(R.id.shortDescTv)
    TextView shortDescTv;

    @BindView(R.id.descTv)
    TextView descTv;

    @BindView(R.id.previousStepBtn)
    ImageView previousStepBtn;

    @BindView(R.id.nextStepBtn)
    ImageView nextStepBtn;

    private ExoPlayer player;
    private int position;
    private int recipeNumber;
    private List<Recipe> recipe;
    private long playCurrenttime = 0;
    private boolean playRunning;
    private List<Step> steps;
    private String videoURL;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_ahhh_detail, container, false);
        ButterKnife.bind(this, rootView);

        Log.d("onSaveInstanceState", "\nonCreateView \nplayCurrenttime: " + playCurrenttime);
        if (savedInstanceState != null) {
            playCurrenttime = savedInstanceState.getLong("time");
            playRunning = savedInstanceState.getBoolean("play");
            Log.d("onSaveInstanceState", "\nrestored playCurrenttime: " + playCurrenttime);
            Log.d("onSaveInstanceState", "restored running: " + playRunning);
        }

        position = getArguments().getInt("position");
        recipeNumber = getArguments().getInt("recipeNumber");

        shortDescTv.setVisibility(View.GONE);
        previousStepBtn.setVisibility(View.INVISIBLE);
        nextStepBtn.setVisibility(View.INVISIBLE);

        initPlayer();
        GetStepNumber(rootView, recipeNumber, position);

        return rootView;
    }

    private void initPlayer() {
        Log.d("initPlayer", "...");
        if (player == null) {
            Log.d("onSaveInstanceState", "player==null");
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());

            playerView.setPlayer(player);
            playerView.setUseController(true);

        } else {
            Log.d("onSaveInstanceState", "player= not null");
        }

        if (videoURL != null)
            playVideo();
        Log.d("onSaveInstanceState", "videoURL: " + videoURL);
    }

    private void playVideo() {
        Log.d("onSaveInstanceState", "\nplayVideo \nplayCurrenttime: " + playCurrenttime);
        Uri uri = Uri.parse(videoURL);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource);

        player.seekTo(playCurrenttime);
        player.setPlayWhenReady(playRunning);
    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    private void GetStepNumber(View rootView, final int recipeNumber, final int stepNumber) {
        Log.d("jsonSingleton", "getInstance().isLoaded(): " + JSONSingleton.getInstance().isLoaded());

        if (JSONSingleton.getInstance().isLoaded()) {
            recipe = JSONSingleton.getInstance().getRecipe();
            steps = recipe.get(recipeNumber).getSteps();
            videoURL = steps.get(stepNumber).getVideoURL();

            if (videoURL.equals(""))
                playerView.setVisibility(View.GONE);
            else
                playVideo();

            shortDescTv.setText(steps.get(stepNumber).getShortDescription());
            descTv.setText(steps.get(stepNumber).getDescription());

            if (rootView.findViewById(R.id.tabletLayout) != null) {
                Log.d("onSaveInstanceState", "TABLET LAYOUT ");

            } else {
                Log.d("onSaveInstanceState", "SMARTPHONE LAYOUT ");
                handlePrevNextButtons();
            }

            Log.d("BAKING", "getName... " + recipe.get(recipeNumber).getName());
            Log.d("BAKING", "getDescription... " + steps.get(stepNumber).getDescription());
            Log.d("BAKING", "getVideoURL... " + steps.get(stepNumber).getVideoURL());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23 && player == null)
            initPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 && player == null))
            initPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23)
            releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23)
            releasePlayer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void releasePlayer() {
        if (player != null) {
            savePlayerState();
            player.stop();
            player.release();
            player = null;
        }
    }

    public static DetailFragment newInstance(int recipeNumber, int position) {
        DetailFragment fragment = new DetailFragment();

        Bundle arguments = new Bundle();
        arguments.putInt("recipeNumber", recipeNumber);
        arguments.putInt("position", position);

        fragment.setArguments(arguments);
        return fragment;
    }

    private void handlePrevNextButtons() {
        if (position > 0) {
            previousStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RecipeStepActivity.class);
                    intent.putExtra("step", String.valueOf(position - 1));
                    intent.putExtra("recipe", String.valueOf(recipeNumber));
                    getContext().startActivity(intent);
                }
            });
            previousStepBtn.setVisibility(View.VISIBLE);
        }

        if (position < steps.size() - 1) {
            nextStepBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RecipeStepActivity.class);
                    intent.putExtra("step", String.valueOf(position + 1));
                    intent.putExtra("recipe", String.valueOf(recipeNumber));
                    getContext().startActivity(intent);
                }
            });
            nextStepBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savePlayerState();
        outState.putLong("time", playCurrenttime);
        outState.putBoolean("play", playRunning);
        Log.d("onSaveInstanceState()", "\nonSaveInstanceState\ntime: " + playCurrenttime);
        Log.d("onSaveInstanceState()", "running: " + playRunning);

    }

    private void savePlayerState() {
        if (player != null) {
            playCurrenttime = player.getCurrentPosition();
            playRunning = player.getPlayWhenReady();
        }
        Log.d("savePlayerState()", "\nonSaveInstanceState\ntime: " + playCurrenttime);
        Log.d("savePlayerState()", "running: " + playRunning);
    }

}

