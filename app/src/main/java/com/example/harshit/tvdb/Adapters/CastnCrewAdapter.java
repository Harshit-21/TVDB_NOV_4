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
import com.example.harshit.tvdb.Pojo.Bean_CastnCrew;
import com.example.harshit.tvdb.Pojo.Bean_MovieDetails;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by harshit on 3/11/17.
 */

public class CastnCrewAdapter extends RecyclerView.Adapter<CastnCrewAdapter.MyViewHolder> {

    private ArrayList<Bean_CastnCrew> castcrewlist;
    private Context context;
    private RecylerClickEvents recylerClickEvents;


    public CastnCrewAdapter(ArrayList<Bean_CastnCrew> castcrewlist, Context context, RecylerClickEvents recylerClickEvents) {
        this.castcrewlist = castcrewlist;
        this.context = context;
        this.recylerClickEvents = recylerClickEvents;
    }

    @Override
    public CastnCrewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.castncrew_row, parent, false);
        return new CastnCrewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CastnCrewAdapter.MyViewHolder holder, int position) {
        final Bean_CastnCrew bean_castnCrew = castcrewlist.get(position);

        if (bean_castnCrew.getProfilePath() != null)
            Picasso.with(context).load(AppConstant.IMG_PATH + bean_castnCrew.getProfilePath()).error(context.getResources().getDrawable(R.drawable.something_went_wrong)).into(holder.img_title);

        holder.tv_castorcrewName.setText(!TextUtils.isEmpty(bean_castnCrew.getName()) ? bean_castnCrew.getName() : "");
        holder.tv_chracter.setText(!TextUtils.isEmpty(bean_castnCrew.getCharacter()) ? bean_castnCrew.getCharacter() : "");

        // this is for the gender
        if (!TextUtils.isEmpty(String.valueOf(bean_castnCrew.getGender()))) {
            if (bean_castnCrew.getGender() == 1)
                holder.tv_gender.setText("Female");
            else holder.tv_gender.setText("Male");
        } else {
            holder.tv_gender.setVisibility(View.GONE);
        }

        // this is for the department
        if (!TextUtils.isEmpty(bean_castnCrew.getDepartment())) {
            holder.tv_department.setText(bean_castnCrew.getDepartment());
        } else {
            holder.tv_department.setVisibility(View.GONE);
        }
        // this is for the job
        if (!TextUtils.isEmpty(bean_castnCrew.getJob())) {
            holder.tv_job.setText(bean_castnCrew.getJob());
        } else {
            holder.tv_job.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return castcrewlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_title;
        private TextView tv_castorcrewName, tv_chracter, tv_gender, tv_job, tv_department;


        public MyViewHolder(View view) {
            super(view);
            img_title = view.findViewById(R.id.img_title);
            tv_castorcrewName = view.findViewById(R.id.tv_castorcrewName);
            tv_chracter = view.findViewById(R.id.tv_chracter);
            tv_gender = view.findViewById(R.id.tv_gender);
            tv_job = view.findViewById(R.id.tv_job);
            tv_department = view.findViewById(R.id.tv_department);
        }
    }

}
