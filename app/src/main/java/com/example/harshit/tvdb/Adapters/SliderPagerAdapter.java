package com.example.harshit.tvdb.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.harshit.tvdb.Interfaces.SliderClick;
import com.example.harshit.tvdb.Pojo.Bean_SliderImages;
import com.example.harshit.tvdb.R;

import java.util.ArrayList;

/**
 * Created by Harshit on 10/24/2017.
 */

public class SliderPagerAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<Bean_SliderImages> image_arraylist;
    private SliderClick sliderClick;

    public SliderPagerAdapter(Activity activity, ArrayList<Bean_SliderImages> image_arraylist,SliderClick sliderClick) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
        this.sliderClick=sliderClick;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.slidingimages_layout, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.image);
        final Bean_SliderImages bean_sliderImages = image_arraylist.get(position);
        im_slider.setBackgroundResource(bean_sliderImages.getId());

        im_slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sliderClick.onClick(bean_sliderImages.getName(),null,activity.getResources().getString(R.string.slider_click));
            }
        });



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
