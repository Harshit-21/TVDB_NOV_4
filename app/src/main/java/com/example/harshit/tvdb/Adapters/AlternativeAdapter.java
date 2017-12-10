package com.example.harshit.tvdb.Adapters;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.harshit.tvdb.Activities.MovieDetailActivity;
import com.example.harshit.tvdb.Pojo.Bean_Release;
import com.example.harshit.tvdb.Pojo.Bean_Translations;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppUtil;

import java.util.ArrayList;

/**
 * Created by harshit on 5/12/17.
 */

public class AlternativeAdapter extends RecyclerView.Adapter<AlternativeAdapter.MyViewHolder> {

    private ArrayList<Bean_Translations> arr_alternative;
    private Context context;


    public AlternativeAdapter(ArrayList<Bean_Translations> arr_alternative, Context context) {
        this.arr_alternative = arr_alternative;
        this.context = context;
    }

    @Override
    public AlternativeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.alternative_row, parent, false);
        return new AlternativeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlternativeAdapter.MyViewHolder holder, final int position) {
        final Bean_Translations bean_translations = arr_alternative.get(position);

        holder.tv_englishTitle.setText(bean_translations.getEnglishName());
        holder.tv_alternativeTitle.setText(bean_translations.getTitle());
    }

    @Override
    public int getItemCount() {
        return arr_alternative.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_englishTitle, tv_alternativeTitle;

        public MyViewHolder(View view) {
            super(view);
            tv_englishTitle = view.findViewById(R.id.tv_englishTitle);
            tv_alternativeTitle = view.findViewById(R.id.tv_alternativeTitle);


        }
    }
}
