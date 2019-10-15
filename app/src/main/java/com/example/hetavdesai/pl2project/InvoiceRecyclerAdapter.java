package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.example.hetavdesai.pl2project.CartActivity.tableno;

import static com.example.hetavdesai.pl2project.InvoiceActivity.grandtotal;


public class InvoiceRecyclerAdapter extends RecyclerView.Adapter<InvoiceRecyclerAdapter.MyHoder> {

    public UserNameClass mylist;
    public OrderItemClass mylist1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    List<UserNameClass> list;
    List<OrderItemClass> list1;
    Context context;
    FoodItemActivity foodItemActivity;
    RecyclerView recycle;
    public static int ptotal,grtotal;


    public InvoiceRecyclerAdapter(List<UserNameClass> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoice_item, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, int position) {
        grtotal=0;
        mylist = list.get(position);
        final int[] flagTransition = {0};

        recycle = holder.recyclerView;
        holder.username.setText(mylist.getUserName());
        holder.username2.setText(mylist.getUserName()+"'s Total");
        holder.usernameAsString = mylist.getUserName();

        firebaseDatabase = FirebaseDatabase.getInstance();

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

        Query mDatabaseReference = firebaseDatabase.getReference().child("Order Items Copy").child(String.valueOf(tableno)).child(holder.usernameAsString).orderByChild("username");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ptotal=0;
                list1 = new ArrayList<>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        OrderItemClass value = dataSnapshot1.getValue(OrderItemClass.class);
                        OrderItemClass fire = new OrderItemClass();

                        String itemName = value.getItemName();
                        String itemPrice = value.getItemPrice();
                        String itemQuantity = value.getItemQuantity();
                        String tableNo = value.getTableNo();
                        String uname = value.getUserName();
                        fire.setItemName(itemName);
                        fire.setItemPrice(itemPrice);
                        fire.setItemQuantity(itemQuantity);
                        fire.setTableNo(tableNo);
                        fire.setUserName(uname);
                        ptotal += Integer.parseInt(itemPrice)*Integer.parseInt(itemQuantity);
                        grtotal += Integer.parseInt(itemPrice)*Integer.parseInt(itemQuantity);
                        list1.add(fire);

                }
                OrderItemRecyclerAdapter recyclerAdapter = new OrderItemRecyclerAdapter(list1, context);
                RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(context);
                holder.recyclerView.setLayoutManager(recycleVariable);
                holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                holder.recyclerView.setAdapter(recyclerAdapter);
                holder.usertotal.setText(String.valueOf(ptotal));
                grandtotal.setText(String.valueOf(grtotal));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });
    }

    public void onActivityOpen() {
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
        RecyclerView recyclerView;
        TextView username, username2, usertotal;
        String usernameAsString;


        public MyHoder(View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.invoice_user_name);
            username2 = itemView.findViewById(R.id.invoice_user_name2);
            usertotal = itemView.findViewById(R.id.user_total);
            recyclerView = itemView.findViewById(R.id.invoice_item_recycle);
        }
    }

}
