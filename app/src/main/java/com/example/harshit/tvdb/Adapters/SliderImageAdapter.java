package com.example.harshit.tvdb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.harshit.tvdb.Pojo.Bean_Poster;
import com.example.harshit.tvdb.R;
import com.example.harshit.tvdb.Utils.AppConstant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by harshit on 2/11/17.
 */

public class SliderImageAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<Bean_Poster> image_arraylist;

    public SliderImageAdapter(Activity activity, ArrayList<Bean_Poster> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.image);

        Bean_Poster bean_poster = image_arraylist.get(position);
        // here we are loading the image i.e coover as well as profile pic
        if (bean_poster.getFilePath() != null)
            Picasso.with(activity).load(AppConstant.IMG_PATH + bean_poster.getFilePath()).error(activity.getResources().getDrawable(R.drawable.something_went_wrong)).into(im_slider);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
