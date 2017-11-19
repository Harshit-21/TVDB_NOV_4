package com.example.harshit.tvdb.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_Episode;
import com.example.harshit.tvdb.Pojo.Bean_Genre;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by harshit on 14/11/17.
 */

public class EpisodeAdapter extends RecyclerView.Adapter<EpisodeAdapter.MyViewHolder> {

    private ArrayList<Bean_Episode> episodeList;
    private Context context;
    private RecylerClickEvents recylerClickEvents;


    public EpisodeAdapter(ArrayList<Bean_Episode> episodeList, Context context, RecylerClickEvents recylerClickEvents) {
        this.episodeList = episodeList;
        this.context = context;
        this.recylerClickEvents = recylerClickEvents;
    }

    @Override
    public EpisodeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.episode_row, parent, false);
        return new EpisodeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EpisodeAdapter.MyViewHolder holder, int position) {
        Bean_Episode bean_episode = episodeList.get(position);

        holder.tv_episodeName.setText(!TextUtils.isEmpty(bean_episode.getName()) ? bean_episode.getName() : "");
        holder.tv_episodeNumber.setText(!TextUtils.isEmpty(String.valueOf(bean_episode.getEpisodeNumber())) ? String.valueOf(bean_episode.getEpisodeNumber()) : "");
        holder.tv_episodeAirDate.setText(!TextUtils.isEmpty(bean_episode.getAirDate()) ? bean_episode.getAirDate() : "");
        holder.tv_EpisodeOverview.setText(!TextUtils.isEmpty(bean_episode.getOverview()) ? bean_episode.getOverview() : "");

        if (bean_episode.getStillPath() != null)
            Picasso.with(context).load(AppConstant.IMG_PATH + bean_episode.getStillPath()).error(context.getResources().getDrawable(R.drawable.something_went_wrong)).into(holder.img_episodeImg);
        else
            holder.img_episodeImg.setBackgroundResource(R.drawable.something_went_wrong);
        holder.card_episode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_episodeImg;
        private TextView tv_episodeName, tv_episodeNumber, tv_episodeAirDate, tv_EpisodeOverview;
        private CardView card_episode;

        public MyViewHolder(View view) {
            super(view);
            tv_episodeName = view.findViewById(R.id.tv_episodeName);
            tv_episodeNumber = view.findViewById(R.id.tv_episodeNumber);
            tv_episodeAirDate = view.findViewById(R.id.tv_episodeAirDate);
            tv_EpisodeOverview = view.findViewById(R.id.tv_EpisodeOverview);
            img_episodeImg = view.findViewById(R.id.img_episodeImg);
            card_episode = view.findViewById(R.id.card_episode);

        }
    }
}
