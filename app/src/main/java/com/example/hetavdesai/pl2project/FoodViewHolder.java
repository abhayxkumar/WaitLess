package com.example.hetavdesai.pl2project;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView food_name;
    public ImageView food_image;


    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View v) {

    }
}
