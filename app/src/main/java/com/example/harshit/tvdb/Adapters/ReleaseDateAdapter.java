package com.example.harshit.tvdb.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.harshit.tvdb.Activities.MovieDetailActivity;
import com.example.harshit.tvdb.Interfaces.RecylerClickEvents;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.Pojo.Bean_Release;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.example.harshit.tvdb.Utils.AppUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Harshit on 11/11/2017.
 */

public class ReleaseDateAdapter extends RecyclerView.Adapter<ReleaseDateAdapter.MyViewHolder> {

    private ArrayList<Bean_Release> arr_release;
    private Context context;


    public ReleaseDateAdapter(ArrayList<Bean_Release> arr_release, Context context) {
        this.arr_release = arr_release;
        this.context = context;
    }

    @Override
    public ReleaseDateAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_date_row, parent, false);
        return new ReleaseDateAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReleaseDateAdapter.MyViewHolder holder, final int position) {
        // here we get the final hashmap
        final Bean_Release bean_release = arr_release.get(position);


        final Runnable r = new Runnable() {
            public void run() {
                String iso = bean_release.getIso31661();
                for (int i = 0; i < MovieDetailActivity.arr_translations.size(); i++) {
                    Bean_Translations bean_translations = MovieDetailActivity.arr_translations.get(i);
                    if (bean_translations.getIso31661().equalsIgnoreCase(iso)) {
                        holder.tv_englishName.setText(bean_translations.getEnglishName());
                        holder.tv_isoName.setText("(" + bean_translations.getName() + ")");
                        break;
                    }
                }

                ArrayList<Bean_Translations> arrayList = bean_release.getReleaseDates();
                // now work with key and value...
                String release_date = arrayList.get(0).getReleaseDate();
                String release[] = release_date.split("T");
                release_date = release[0];
                holder.tv_releaseDate.setText(AppUtil.getFormattedDate(release_date));

            }
        };

        new Handler().postDelayed(r, 0);

    }

    @Override
    public int getItemCount() {
        return arr_release.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_releaseDate, tv_englishName, tv_isoName;

        public MyViewHolder(View view) {
            super(view);
            tv_releaseDate = view.findViewById(R.id.tv_releaseDate);
            tv_englishName = view.findViewById(R.id.tv_englishName);
            tv_isoName = view.findViewById(R.id.tv_isoName);


        }
    }
}
