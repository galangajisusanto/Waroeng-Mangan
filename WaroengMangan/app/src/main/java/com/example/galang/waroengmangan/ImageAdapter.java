package com.example.galang.waroengmangan;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galang on 11/21/17.
 */

public class ImageAdapter extends BaseAdapter {
    private Context mContext;



    public ImageAdapter(Context c /*,List<menu> menus*/) {

        mContext = c;

    }


    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);

            imageView.setLayoutParams(new GridView.LayoutParams(400, 400));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;

    }

    // references to our images

    private Integer[] mThumbIds = {
            R.drawable.masukanmenu, R.drawable.editmenu,
            R.drawable.masukandiscount, R.drawable.editdiscount,
            R.drawable.pesananmasuk,R.drawable.laporanpenjualan,



    };


}
