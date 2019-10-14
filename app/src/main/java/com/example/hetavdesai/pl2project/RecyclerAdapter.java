package com.example.hetavdesai.pl2project;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.hetavdesai.pl2project.CartActivity.swipeFlag;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHoder> {

    public FoodClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    List<FoodClass> list;
    Context context;
    int tableno;


    public RecyclerAdapter(List<FoodClass> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, int position) {
        mylist = list.get(position);
        final int[] flagTransition = {0};

        holder.name.setText(mylist.getName());
        holder.viewname = mylist.getName();
        holder.priceAsInt = mylist.getPrice();
        holder.viewPrice.setText(String.valueOf(mylist.getPrice()));
        holder.description.setText(mylist.getDescription());
        holder.calories.setText(mylist.getCalories());

        switch(holder.viewname) {
            case "Margherita Pizza":
                holder.dishImage.setImageResource(R.drawable.pizza01);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "Peppy Paneer Pizza":
                holder.dishImage.setImageResource(R.drawable.pizza02);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "Farmhouse Pizza":
                holder.dishImage.setImageResource(R.drawable.pizza03);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "BBQ Chicken Pizza":
                holder.dishImage.setImageResource(R.drawable.pizza04);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "Tandoori Chicken Pizza":
                holder.dishImage.setImageResource(R.drawable.pizza05);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;

            case "Cheese Burger":
                holder.dishImage.setImageResource(R.drawable.burger01);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "American Chilly Burger":
                holder.dishImage.setImageResource(R.drawable.burger02);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "Spicy Paneer Burger":
                holder.dishImage.setImageResource(R.drawable.burger03);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "Veg Tikka Burger":
                holder.dishImage.setImageResource(R.drawable.burger04);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "Spicy Meat Burger":
                holder.dishImage.setImageResource(R.drawable.burger05);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;

            case "Veg Biryani":
                holder.dishImage.setImageResource(R.drawable.biryani01);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "Paneer Biryani":
                holder.dishImage.setImageResource(R.drawable.biryani02);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "Chicken Tikka Biryani":
                holder.dishImage.setImageResource(R.drawable.biryani03);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "Chicken Dum Biryani":
                holder.dishImage.setImageResource(R.drawable.biryani04);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "Mutton Biryani":
                holder.dishImage.setImageResource(R.drawable.biryani05);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;

            case "Ice Cream":
                holder.dishImage.setImageResource(R.drawable.dessert01);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "Waffle":
                holder.dishImage.setImageResource(R.drawable.dessert02);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "Milkshake":
                holder.dishImage.setImageResource(R.drawable.dessert03);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "Chocolate pastry":
                holder.dishImage.setImageResource(R.drawable.dessert04);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "Gulab Jamun":
                holder.dishImage.setImageResource(R.drawable.dessert05);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;

            case "Mac n Cheese":
                holder.dishImage.setImageResource(R.drawable.pasta01);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "Chicken Arabiata Pasta":
                holder.dishImage.setImageResource(R.drawable.pasta02);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "Chicken Cream Cheese Pasta":
                holder.dishImage.setImageResource(R.drawable.pasta03);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "Mushroom Pesto Pasta":
                holder.dishImage.setImageResource(R.drawable.pasta04);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "Veg Alfredo Pasta":
                holder.dishImage.setImageResource(R.drawable.pasta05);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;

            case "Veg Clear Soup":
                holder.dishImage.setImageResource(R.drawable.soup01);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.pizza);
                break;

            case "Non-Veg Clear Soup":
                holder.dishImage.setImageResource(R.drawable.soup02);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.dessert);
                break;

            case "Veg Manchow Soup":
                holder.dishImage.setImageResource(R.drawable.soup03);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.burger);
                break;

            case "Non-Veg Manchow Soup":
                holder.dishImage.setImageResource(R.drawable.soup04);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.soup);
                break;

            case "Spicy Seafood Soup":
                holder.dishImage.setImageResource(R.drawable.soup05);
                holder.dishImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                holder.fullMenuCardItem.setBackgroundResource(R.drawable.biryani);
                break;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
        String email = acct.getEmail();
        Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    UserClass value = dataSnapshot1.getValue(UserClass.class);
                    tableno = value.getTableno();
                }

                final int[] flag = {0};
                databaseReference2 = FirebaseDatabase.getInstance().getReference();
                Query checkIfExists = databaseReference2.child("Cart").child(String.valueOf(tableno)).orderByChild("name").equalTo(holder.viewname);
                checkIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("ResourceType")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            if (!dataSnapshot.exists()) {
                                holder.addToCartText.setText("ADD");
//                                holder.addToCart.setCardBackgroundColor(R.attr.cardbackground);
//                                holder.addToCartText.setBackgroundColor(R.attr.cardbackground);
//                                holder.addToCartText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                                holder.addToCart.setEnabled(true);
                                flag[0] = 1;
                            } else {
                                holder.addToCartText.setText("ADDED");
//                                holder.addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                                holder.addToCart.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                                holder.addToCartText.setTextColor(ContextCompat.getColor(context, R.color.white));
                                holder.addToCart.setEnabled(false);
                                flag[0] = 1;
                            }
                        }

                        if (flag[0] == 0) {
                            holder.addToCartText.setText("ADD");
//                            holder.addToCart.setCardBackgroundColor(R.attr.cardbackground);
//                            holder.addToCartText.setBackgroundColor(R.attr.cardbackground);
//                            holder.addToCartText.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                            holder.addToCart.setEnabled(true);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //holder.dropView.setVisibility(View.GONE);

//        holder.foodItemCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (flagTransition[0] == 0) {
//                    TransitionManager.beginDelayedTransition(holder.foodItemCard);
//                    holder.dropView.setVisibility(View.VISIBLE);
//                    flagTransition[0] = 1;
//                } else {
//                    holder.dropView.setVisibility(View.GONE);
//                    flagTransition[0] = 0;
//                }
//
//            }
//        });


        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(context);
                String email = acct.getEmail();
                Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            UserClass value = dataSnapshot1.getValue(UserClass.class);
                            tableno = value.getTableno();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                databaseReference2 = FirebaseDatabase.getInstance().getReference();

                final int[] flag = {0};
                Query checkIfExists = databaseReference2.child("Cart").child(String.valueOf(tableno)).orderByChild("name").equalTo(holder.viewname);
                checkIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            if (!dataSnapshot.exists()) {
                                String key = databaseReference2.push().getKey();
                                CartClass cartClass = new CartClass(holder.viewname, holder.priceAsInt, 1, holder.priceAsInt, tableno, key, acct.getDisplayName(),0);
                                swipeFlag = false;
                                databaseReference2.child("Cart").child(String.valueOf(tableno)).child(key).setValue(cartClass);
                                holder.addToCartText.setText("ADDED");
//                                holder.addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                                holder.addToCart.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                                holder.addToCartText.setTextColor(ContextCompat.getColor(context, R.color.white));
                                holder.addToCart.setEnabled(false);
                            }
                            flag[0] = 1;
                        }

                        if (flag[0] == 0) {
                            String key = databaseReference2.push().getKey();
                            CartClass cartClass = new CartClass(holder.viewname, holder.priceAsInt, 1, holder.priceAsInt, tableno, key, acct.getDisplayName(),0);
                            swipeFlag = false;
                            databaseReference2.child("Cart").child(String.valueOf(tableno)).child(key).setValue(cartClass);
                            holder.addToCartText.setText("ADDED");
//                            holder.addToCartText.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                            holder.addToCart.setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
//                            holder.addToCartText.setTextColor(ContextCompat.getColor(context, R.color.white));
                            holder.addToCart.setEnabled(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
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
        TextView name, description, viewPrice, calories, addToCartText, viewQuantity;
        CardView addToCart;
        CardView foodItemCard;
        int priceAsInt;
        String viewname;
        ImageView dishImage;

        public MyHoder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.titleTextView);
            description = (TextView) itemView.findViewById(R.id.contentTextView);
            viewPrice = itemView.findViewById(R.id.priceTextView);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            addToCartText = itemView.findViewById(R.id.add_to_cart_text);
            //dropView = itemView.findViewById(R.id.appearLater);
            foodItemCard = (CardView) itemView.findViewById(R.id.cardView);
            calories = itemView.findViewById(R.id.caloriesTextView);
            dishImage = itemView.findViewById(R.id.dish_image);
        }
    }

}
