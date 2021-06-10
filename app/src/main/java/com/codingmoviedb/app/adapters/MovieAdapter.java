package com.codingmoviedb.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codingmoviedb.app.R;
import com.codingmoviedb.app.utils.Constants;
import com.codingmoviedb.app.model.Movie;
import com.codingmoviedb.app.utils.IListItemClickListener;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context context;
    private List<Movie> mDataList;
    private IListItemClickListener listItemClickListener;

    public MovieAdapter(Context ctx, IListItemClickListener listItemClickListener, List<Movie> dataList) {
        context = ctx;
        this.listItemClickListener = listItemClickListener;
        mDataList = dataList;
    }

    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivPoster;

        public TextView tvTitle;
        public TextView tvRating;
        public TextView tvOverview;
        public TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);

            ivPoster = (ImageView) itemView.findViewById(R.id.ivPoster);

            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvRating = (TextView) itemView.findViewById(R.id.tvRating);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listItemClickListener != null)
                listItemClickListener.onListItemClick(v, getPosition());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View inflatedView = inflater.inflate(R.layout.list_item_movie, parent, false);

        ViewHolder viewHolder = new ViewHolder(inflatedView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        Movie movie = mDataList.get(position);

        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvRating.setText(movie.getRating());
        viewHolder.tvOverview.setText(movie.getOverview());
        viewHolder.tvDate.setText(movie.getReleaseDate());

        RequestOptions requestOptions = new RequestOptions().centerCrop().error(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(Constants.IMAGE_BASE_PATH + movie.getPosterPath()).into(viewHolder.ivPoster);


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


}
