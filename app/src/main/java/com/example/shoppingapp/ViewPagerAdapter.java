package com.example.shoppingapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private LayoutInflater mInflater;


    private int[] imageArray;

    ViewPagerAdapter(Context context, int[] images) {
        this.mInflater = LayoutInflater.from(context);
        imageArray = images;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.imageView.setImageResource(imageArray[position]);
    }

    @Override
    public int getItemCount() {
        return imageArray.length;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;



        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.CenterimageView);


        }
    }

}
