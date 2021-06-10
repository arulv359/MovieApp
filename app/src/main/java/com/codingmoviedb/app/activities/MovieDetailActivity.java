package com.codingmoviedb.app.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingmoviedb.app.R;
import com.codingmoviedb.app.utils.Constants;
import com.codingmoviedb.app.network.ServiceGenerator;
import com.codingmoviedb.app.model.MovieDetail;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;

public class MovieDetailActivity extends AppCompatActivity {

    private Context context;
    private String movieId;
    private ImageView imageView;
    private TextView tvTitle;
    private TextView tvRating;
    private TextView tvOverview;
    private TextView tvBudget;
    private TextView tvRevenue;
    private TextView tvRunTime;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);

        movieId = getIntent().getStringExtra("MOVIE_ID");
        context = this;
        imageView = findViewById(R.id.imageViewHeader);
        tvTitle = findViewById(R.id.tvTitle);
        tvRating = findViewById(R.id.tvRating);
        tvOverview = findViewById(R.id.tvOverview);
        progressBar = findViewById(R.id.progressBar);
        tvBudget = findViewById(R.id.budget);
        tvRevenue = findViewById(R.id.revenue);
        tvRunTime = findViewById(R.id.runtime);

        loadMoreDataFromApi(movieId);
    }

    // Network request
    public void loadMoreDataFromApi(final String id) {


        showProgressBar(true);

        Call<MovieDetail> movieResultCallback = ServiceGenerator.getMovieApi().getMovieDetail(id, Constants.API_KEY);

        // asynchronous call
        movieResultCallback.enqueue(new Callback<MovieDetail>() {

            @Override
            public void onResponse(Call<MovieDetail> call, retrofit2.Response<MovieDetail> response) {
                showProgressBar(false);
                MovieDetail movieDetail = response.body();

                if (movieDetail != null) {

                    RequestOptions requestOptions = new RequestOptions().centerCrop().error(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
                    Glide.with(context)
                            .setDefaultRequestOptions(requestOptions)
                            .load(Constants.IMAGE_BASE_PATH_W300 + movieDetail.getPoster_path()).into(imageView);
                    tvTitle.setText(movieDetail.getOriginal_title());
                    tvRating.setText(String.valueOf(movieDetail.getVote_average()));
                    tvOverview.setText(movieDetail.getOverview());
                    tvBudget.setText(getString(R.string.budget_label) + getFormattedCurrency(movieDetail.getBudget()));
                    tvRevenue.setText(getString(R.string.revenue_label) + getFormattedCurrency(movieDetail.getRevenue()));
                    tvRunTime.setText(getString(R.string.total_runtime_label) + getFormattedTime(movieDetail.getRuntime()));
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {

            }
        });

    }


    private String getFormattedTime(int minutes) {
        int hours = minutes / 60;
        int min = minutes % 60;
        return String.valueOf(hours) + ":" + String.valueOf(min) + " hr";
    }

    private String getFormattedCurrency(Long number) {
        DecimalFormat formatter = new DecimalFormat("$#,###,###");
        return formatter.format(number);
    }

    private void showProgressBar(boolean value) {
        if (value)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
