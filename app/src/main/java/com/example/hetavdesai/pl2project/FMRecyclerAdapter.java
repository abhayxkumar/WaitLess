package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.List;


public class FMRecyclerAdapter extends RecyclerView.Adapter<FMRecyclerAdapter.MyHoder> {

    public static DatabaseReference myRefVariable;
    public static String foodItemHeader;
    public FoodCategoriesClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<FoodCategoriesClass> list;
    FoodItemActivity foodItemActivity = new FoodItemActivity();
    Context context;


    public FMRecyclerAdapter(List<FoodCategoriesClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public FMRecyclerAdapter.MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_menu_adapter, parent, false);
        FMRecyclerAdapter.MyHoder myHoder = new FMRecyclerAdapter.MyHoder(view);

        return myHoder;
    }


    @Override
    public void onBindViewHolder(final MyHoder holder, final int position) {
        mylist = list.get(position);

        final Intent[] intent = new Intent[1];
//        holder.catName.setText(mylist.getCatName());
        holder.switchCatID = mylist.getCatID();

        holder.foodItemHeaderInHolder = mylist.getCatName();

        switch(holder.switchCatID) {
            case "C01":
                holder.catImage.setImageResource(R.drawable.cat_pizza);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "C02":
                holder.catImage.setImageResource(R.drawable.cat_dessert);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "C03":
                holder.catImage.setImageResource(R.drawable.cat_burger);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "C04":
                holder.catImage.setImageResource(R.drawable.cat_soup);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "C05":
                holder.catImage.setImageResource(R.drawable.cat_biryani);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;

            case "C06":
                holder.catImage.setImageResource(R.drawable.cat_pasta);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pasta);
                break;
        }


        holder.fullMenuCardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (holder.switchCatID) {
                    case "C01":
                        myRefVariable = FirebaseDatabase.getInstance().getReference().child("Food").child("Pizza");
                        foodItemHeader = holder.foodItemHeaderInHolder;
                        intent[0] = new Intent("com.example.hetavdesai.pl2project.FoodItemActivity");
                        break;

                    case "C02":
                        myRefVariable = FirebaseDatabase.getInstance().getReference().child("Food").child("Dessert");
                        foodItemHeader = holder.foodItemHeaderInHolder;
                        intent[0] = new Intent("com.example.hetavdesai.pl2project.FoodItemActivity");
                        break;

                    case "C03":
                        myRefVariable = FirebaseDatabase.getInstance().getReference().child("Food").child("Burgers");
                        foodItemHeader = holder.foodItemHeaderInHolder;
                        intent[0] = new Intent("com.example.hetavdesai.pl2project.FoodItemActivity");
                        break;

                    case "C04":
                        myRefVariable = FirebaseDatabase.getInstance().getReference().child("Food").child("Soup");
                        foodItemHeader = holder.foodItemHeaderInHolder;
                        intent[0] = new Intent("com.example.hetavdesai.pl2project.FoodItemActivity");
                        break;

                    case "C05":
                        myRefVariable = FirebaseDatabase.getInstance().getReference().child("Food").child("Biryani");
                        foodItemHeader = holder.foodItemHeaderInHolder;
                        intent[0] = new Intent("com.example.hetavdesai.pl2project.FoodItemActivity");
                        break;

                    case "C06":
                        myRefVariable = FirebaseDatabase.getInstance().getReference().child("Food").child("Pasta");
                        foodItemHeader = holder.foodItemHeaderInHolder;
                        intent[0] = new Intent("com.example.hetavdesai.pl2project.FoodItemActivity");
                        break;
                }

                context.startActivity(intent[0]);
            }
        });

    }

    @Override
    public int getItemCount() {
        int arr = 0;
        try {
            if (list.size() == 0) {
                arr = 0;
            } else {
                arr = list.size();
            }
        } catch (Exception e) {
        }
        return arr;
    }

    class MyHoder extends RecyclerView.ViewHolder {
        TextView catName;
        Button openCategory;
        CardView fullMenuCardItem;
        String switchCatID;
        String foodItemHeaderInHolder;
        ImageView catImage;

        public MyHoder(View itemView) {
            super(itemView);
            //catName = (TextView) itemView.findViewById(R.id.cat_name_view);
            fullMenuCardItem = itemView.findViewById(R.id.full_menu_card);
            catImage = itemView.findViewById(R.id.cat_image_view);
            catImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
}
