package com.example.galang.waroengmanganuser;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Created by galang on 12/18/17.
 */

public class MyCustomPagerAdapter extends PagerAdapter{
    Context context;
    List<TampilMenus> tampilMenus;
    List<TampilDiscount> images= new ArrayList<>();

    LayoutInflater layoutInflater;


    public MyCustomPagerAdapter(Context context, List<TampilDiscount> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.item, container, false);
        final TampilDiscount mydisc = images.get(position);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewdiskon);


        Picasso.with(context)
                .load(mydisc.getMimageURL())
                .into(imageView);


        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
