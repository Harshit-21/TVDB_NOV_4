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
import com.example.harshit.tvdb.Pojo.Bean_Episode;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by harshit on 14/11/17.
 */

public class SeasonNumberAdapter extends RecyclerView.Adapter<SeasonNumberAdapter.MyViewHolder> {

    private ArrayList<Integer> season_noList;
    private Context context;
    private RecylerClickEvents recylerClickEvents;


    public SeasonNumberAdapter(ArrayList<Integer> season_noList, Context context, RecylerClickEvents recylerClickEvents) {
        this.season_noList = season_noList;
        this.context = context;
        this.recylerClickEvents = recylerClickEvents;
    }

    @Override
    public SeasonNumberAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_row, parent, false);
        return new SeasonNumberAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SeasonNumberAdapter.MyViewHolder holder, final int position) {
        holder.tv_genreName.setText(String.valueOf(season_noList.get(position)));

        holder.card_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recylerClickEvents.OnClick(0, season_noList.get(position) + "", null, "");
            }
        });


    }

    @Override
    public int getItemCount() {
        return season_noList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_genreName;
        private CardView card_genre;

        public MyViewHolder(View view) {
            super(view);
            tv_genreName = view.findViewById(R.id.tv_genreName);
            card_genre = view.findViewById(R.id.card_genre);

        }
    }

}
