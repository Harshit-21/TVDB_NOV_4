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
import com.example.harshit.tvdb.Pojo.Bean_TvDetails;
import com.example.harshit.tvdb.Pojo.Bean_TvList;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by harshit on 1/11/17.
 */

public class TvListAdapter extends RecyclerView.Adapter<TvListAdapter.MyViewHolder> {
    private ArrayList<Bean_TvList> movieList;
    private Context context;
    private RecylerClickEvents recylerClickEvents;


    public TvListAdapter(ArrayList<Bean_TvList> movieList, Context context, RecylerClickEvents recylerClickEvents) {
        this.movieList = movieList;
        this.context = context;
        this.recylerClickEvents = recylerClickEvents;
    }

    @Override
    public TvListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_item_row, parent, false);
        return new TvListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TvListAdapter.MyViewHolder holder, int position) {
        final Bean_TvList bean_tvDetails = movieList.get(position);

        // here we are loading the image i.e coover as well as profile pic
        if (bean_tvDetails.getPosterPath() != null)
            Picasso.with(context).load(AppConstant.IMG_PATH + bean_tvDetails.getPosterPath()).error(context.getResources().getDrawable(R.drawable.something_went_wrong)).into(holder.img_title);
        if (bean_tvDetails.getBackdropPath() != null)
            Picasso.with(context).load(AppConstant.IMG_PATH + bean_tvDetails.getBackdropPath()).error(context.getResources().getDrawable(R.drawable.something_went_wrong)).into(holder.img_cover);

        // here we get the textview and we have to put all the info into it
        holder.tv_Name.setText(bean_tvDetails.getOriginalName() != null ? bean_tvDetails.getOriginalName() : "");
        holder.tv_shortDesc.setText(bean_tvDetails.getOverview() != null ? bean_tvDetails.getOverview() : "");
        holder.tv_releasedate.setText(bean_tvDetails.getFirstAirDate() != null ? bean_tvDetails.getFirstAirDate() : "");
        holder.tv_vote_average.setText(String.valueOf(bean_tvDetails.getVoteAverage() != null ? bean_tvDetails.getVoteAverage() : ""));
        holder.tv_popularity.setText(String.valueOf(bean_tvDetails.getPopularity() != null ? bean_tvDetails.getPopularity() : ""));


        holder.card_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recylerClickEvents.OnClick(0, bean_tvDetails.getId() + "", null, "");
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
            tv_Name = (TextView) view.findViewById(R.id.tv_Name);
            tv_shortDesc = (TextView) view.findViewById(R.id.tv_shortDesc);
            tv_releasedate = (TextView) view.findViewById(R.id.tv_releasedate);
            tv_vote_average = (TextView) view.findViewById(R.id.tv_vote_average);
            tv_popularity = (TextView) view.findViewById(R.id.tv_popularity);
            img_title = (ImageView) view.findViewById(R.id.img_title);
            img_cover = (ImageView) view.findViewById(R.id.img_cover);
            card_Detail = (CardView) view.findViewById(R.id.card_Detail);

        }
    }
}
