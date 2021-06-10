package com.codingmoviedb.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingmoviedb.app.R;
import com.codingmoviedb.app.adapters.MovieAdapter;
import com.codingmoviedb.app.utils.Constants;
import com.codingmoviedb.app.network.ServiceGenerator;
import com.codingmoviedb.app.model.Movie;
import com.codingmoviedb.app.model.MovieList;
import com.codingmoviedb.app.utils.IListItemClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;


public class MainActivity extends AppCompatActivity implements IListItemClickListener {

    private Context context;
    private List<Movie> movies;
    private MovieAdapter movieAdapter;
    private RecyclerView recyclerView;
    private TextView txvEmptyList;
    private ProgressBar progressBar;
    private AppCompatButton btnLoadMore;
    private int mPage;
    private int mTotalPages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        mPage = 1;
        recyclerView = findViewById(R.id.recyclerView);
        txvEmptyList = findViewById(R.id.txvEmptyList);
        progressBar = findViewById(R.id.progressBar);
        btnLoadMore = findViewById(R.id.btn_load_more);
        movies = new ArrayList<>();
        movieAdapter = new MovieAdapter(context, this, movies);
        recyclerView.setAdapter(movieAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)  && mTotalPages >= mPage+1) {
                    btnLoadMore.setEnabled(true);
                    loadMoreDataFromApi(++mPage);
                }
                else if( mTotalPages < mPage+1)
                {
                    btnLoadMore.setVisibility(View.GONE);
                }
            }
        });

        btnLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mTotalPages >= mPage+1) {
                    loadMoreDataFromApi(++mPage);
                }
                else
                {
                    btnLoadMore.setVisibility(View.GONE);
                }
            }
        });

        this.loadMoreDataFromApi(mPage);
    }

    @Override
    public void onListItemClick(View view, int position) {

        Movie obj = movies.get(position);
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra("MOVIE_ID", obj.getId());
        startActivity(intent);
    }


    // Network request
    public void loadMoreDataFromApi(final int page) {

        //Show Progress Dialog
        if (page == 1)
            showProgressBar(true);

        Call<MovieList> movieResultCallback = ServiceGenerator.getMovieApi().getPopularMovies(Constants.API_KEY, page);

        // asynchronous call
        movieResultCallback.enqueue(new Callback<MovieList>() {

            @Override
            public void onResponse(Call<MovieList> call, retrofit2.Response<MovieList> response) {
                showProgressBar(false);
                MovieList movieList = response.body();

                //Set Movies Adapter
                if (movieList != null && movieList.getMovieResult().size() > 0) {
                    int curSize = movieAdapter.getItemCount();
                    mPage = movieList.getPage();
                    mTotalPages = movieList.getTotalPages();
                    MainActivity.this.movies.addAll(movieList.getMovieResult());
                    movieAdapter.notifyItemRangeInserted(curSize, MainActivity.this.movies.size() - 1);
                } else if(movies ==null ) {
                    showEmptyList(true);
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

            }
        });

    }


    private void showProgressBar(boolean value) {
        if (value)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    private void showEmptyList(boolean show) {
        if (show)
            txvEmptyList.setVisibility(View.VISIBLE);
        else
            txvEmptyList.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
