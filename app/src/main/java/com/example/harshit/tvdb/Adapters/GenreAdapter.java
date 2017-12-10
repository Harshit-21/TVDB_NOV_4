package com.example.harshit.tvdb.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_Genre;
import com.example.harshit.tvdb.R;

import java.util.ArrayList;

/**
 * Created by Harshit on 10/24/2017.
 */

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.MyViewHolder> {

    private ArrayList<Bean_Genre> genrelist;
    private Context context;
    private RecylerClickEvents recylerClickEvents;


    public GenreAdapter(ArrayList<Bean_Genre> genrelist, Context context, RecylerClickEvents recylerClickEvents) {
        this.genrelist = genrelist;
        this.context = context;
        this.recylerClickEvents = recylerClickEvents;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.genre_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Bean_Genre bean_genre = genrelist.get(position);
        holder.tv_genreName.setText(bean_genre.getName() != null ? bean_genre.getName() : "");

        holder.card_genre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recylerClickEvents.OnClick(0, String.valueOf(bean_genre.getId()), bean_genre, context.getString(R.string.genre_click));
            }
        });
    }

    @Override
    public int getItemCount() {
        return genrelist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_genreName;
        private CardView card_genre;

        public MyViewHolder(View view) {
            super(view);
            tv_genreName = (TextView) view.findViewById(R.id.tv_genreName);
            card_genre = (CardView) view.findViewById(R.id.card_genre);
            card_genre.setBackground(context.getResources().getDrawable(R.drawable.card_gradient));

        }
    }

}
