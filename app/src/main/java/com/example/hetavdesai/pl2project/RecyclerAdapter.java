package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.support.annotation.NonNull;
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
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            if (!dataSnapshot.exists()) {
                                holder.addToCart.setImageResource(android.R.drawable.ic_input_add);
                                holder.addToCart.setEnabled(true);
                                flag[0] = 1;
                            } else {
                                holder.addToCart.setImageResource(android.R.drawable.ic_menu_add);
                                holder.addToCart.setEnabled(false);
                                flag[0] = 1;
                            }
                        }

                        if (flag[0] == 0) {
                            holder.addToCart.setImageResource(android.R.drawable.ic_input_add);
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

        holder.name.setText(mylist.getName());
        holder.viewname = mylist.getName();
        holder.priceAsInt = mylist.getPrice();
        holder.viewPrice.setText(String.valueOf(mylist.getPrice()));
        holder.description.setText(mylist.getDescription());
        holder.calories.setText(mylist.getCalories());

        holder.dropView.setVisibility(View.GONE);

        holder.foodItemCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagTransition[0] == 0) {
                    TransitionManager.beginDelayedTransition(holder.foodItemCard);
                    holder.dropView.setVisibility(View.VISIBLE);
                    flagTransition[0] = 1;
                } else {
                    holder.dropView.setVisibility(View.GONE);
                    flagTransition[0] = 0;
                }

            }
        });


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
                                holder.addToCart.setImageResource(android.R.drawable.ic_menu_add);
                                holder.addToCart.setEnabled(false);
                            }
                            flag[0] = 1;
                        }

                        if (flag[0] == 0) {
                            String key = databaseReference2.push().getKey();
                            CartClass cartClass = new CartClass(holder.viewname, holder.priceAsInt, 1, holder.priceAsInt, tableno, key, acct.getDisplayName(),0);
                            swipeFlag = false;
                            databaseReference2.child("Cart").child(String.valueOf(tableno)).child(key).setValue(cartClass);
                            holder.addToCart.setImageResource(android.R.drawable.ic_menu_add);
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
        TextView name, description, viewPrice, calories;
        ImageButton addToCart;
        LinearLayout dropView;
        CardView foodItemCard;
        int priceAsInt;
        String viewname;


        public MyHoder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.titleTextView);
            description = (TextView) itemView.findViewById(R.id.contentTextView);
            viewPrice = itemView.findViewById(R.id.priceTextView);
            addToCart = itemView.findViewById(R.id.add_to_cart);
            dropView = itemView.findViewById(R.id.appearLater);
            foodItemCard = (CardView) itemView.findViewById(R.id.cardView);
            calories = itemView.findViewById(R.id.caloriesTextView);

        }
    }

}
