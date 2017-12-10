package com.example.harshit.tvdb.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Harshit on 10/25/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MyViewHolder> {

    private ArrayList<Bean_MovieDetails> movieList;
    private Context context;
    private RecylerClickEvents recylerClickEvents;


    public MovieListAdapter(ArrayList<Bean_MovieDetails> movieList, Context context, RecylerClickEvents recylerClickEvents) {
        this.movieList = movieList;
        this.context = context;
        this.recylerClickEvents = recylerClickEvents;
    }

    @Override
    public MovieListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item_row, parent, false);
        return new MovieListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MovieListAdapter.MyViewHolder holder, int position) {
        final Bean_MovieDetails bean_movieDetails = movieList.get(position);


        // here we are loading the image i.e coover as well as profile pic
        if (bean_movieDetails.getPosterPath() != null)
            Picasso.with(context).load(AppConstant.IMG_PATH + bean_movieDetails.getPosterPath()).error(context.getResources().getDrawable(R.drawable.something_went_wrong)).into(holder.img_title);
        if (bean_movieDetails.getBackdropPath() != null)
            Picasso.with(context).load(AppConstant.IMG_PATH + bean_movieDetails.getBackdropPath()).error(context.getResources().getDrawable(R.drawable.something_went_wrong)).into(holder.img_cover);

        // here we get the textview and we have to put all the info into it
        holder.tv_Name.setText(bean_movieDetails.getOriginalTitle() != null ? bean_movieDetails.getOriginalTitle() : "");
        holder.tv_shortDesc.setText(bean_movieDetails.getOverview() != null ? bean_movieDetails.getOverview() : "");
        holder.tv_releasedate.setText(bean_movieDetails.getReleaseDate() != null ? bean_movieDetails.getReleaseDate() : "");
        holder.tv_vote_average.setText(String.valueOf(bean_movieDetails.getVoteAverage() != null ? bean_movieDetails.getVoteAverage() : ""));
        holder.tv_popularity.setText(String.valueOf(bean_movieDetails.getPopularity() != null ? bean_movieDetails.getPopularity() : ""));


        holder.card_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recylerClickEvents.OnClick(0, "", bean_movieDetails, context.getString(R.string.movie_click));
            }
        });


    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_title, img_cover;
        private TextView tv_shortDesc, tv_Name, tv_releasedate, tv_vote_average, tv_popularity;
        private CardView card_Detail;

        public MyViewHolder(View view) {
            super(view);
            tv_Name = view.findViewById(R.id.tv_Name);
            tv_shortDesc = view.findViewById(R.id.tv_shortDesc);
            tv_releasedate = view.findViewById(R.id.tv_releasedate);
            tv_vote_average = view.findViewById(R.id.tv_vote_average);
            tv_popularity = (TextView) view.findViewById(R.id.tv_popularity);
            img_title = (ImageView) view.findViewById(R.id.img_title);
            img_cover = (ImageView) view.findViewById(R.id.img_cover);
            card_Detail = (CardView) view.findViewById(R.id.card_Detail);
        }
    }

}
