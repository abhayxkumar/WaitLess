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

import static com.example.hetavdesai.pl2project.CartClass.gtotal;


public class OrderRecyclerAdapter extends RecyclerView.Adapter<OrderRecyclerAdapter.MyHoder> {

    public OrderClass mylist;
    public OrderItemClass mylist1;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference, databaseReference2;
    List<OrderClass> list;
    List<OrderItemClass> list1;
    Context context;
    FoodItemActivity foodItemActivity;
    int tableno;
    RecyclerView recycle;
    int ordertotalAsInt;


    public OrderRecyclerAdapter(List<OrderClass> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, int position) {
        mylist = list.get(position);
        final int[] flagTransition = {0};

        recycle = holder.recyclerView;

        holder.orderTotal.setText(String.valueOf(gtotal));
        holder.orderId.setText(mylist.getOrderId());
        holder.orderTime.setText(mylist.getOrderTime());
        holder.tableNo.setText("Table " + String.valueOf(mylist.getTableNo()));
        holder.tableno = String.valueOf(mylist.getTableNo());

        firebaseDatabase = FirebaseDatabase.getInstance();
        Query mDatabaseReference = firebaseDatabase.getReference().child("Order Items Copy").child(String.valueOf(holder.tableno));

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list1 = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                        OrderItemClass value = dataSnapshot2.getValue(OrderItemClass.class);
                        OrderItemClass fire = new OrderItemClass();

                        String itemName = value.getItemName();
                        String itemPrice = value.getItemPrice();
                        String itemQuantity = value.getItemQuantity();
                        String tableNo = value.getTableNo();
                        fire.setItemName(itemName);
                        fire.setItemPrice(itemPrice);
                        fire.setItemQuantity(itemQuantity);
                        fire.setTableNo(tableNo);
                        ordertotalAsInt += Integer.parseInt(itemPrice)*Integer.parseInt(itemQuantity);
                        list1.add(fire);
                    }
                }
                OrderItemRecyclerAdapter recyclerAdapter = new OrderItemRecyclerAdapter(list1, context);
                RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(context);
                holder.recyclerView.setLayoutManager(recycleVariable);
                holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
                holder.recyclerView.setAdapter(recyclerAdapter);
                holder.orderTotal.setText(String.valueOf(ordertotalAsInt));
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        holder.complete_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("Order Copy").child(holder.tableno).removeValue();
                databaseReference.child("Order Items Copy").child(holder.tableno).removeValue();
                Toast.makeText(context, holder.tableno, Toast.LENGTH_SHORT).show();
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
        TextView orderTotal, orderId, orderTime, tableNo;
        RecyclerView recyclerView;
        Button complete_order;
        String tableno;


        public MyHoder(View itemView) {
            super(itemView);

            orderTime = itemView.findViewById(R.id.order_time);
            orderId = itemView.findViewById(R.id.order_id);
            orderTotal = itemView.findViewById(R.id.order_total);
            tableNo = itemView.findViewById(R.id.table_number);
            recyclerView = itemView.findViewById(R.id.fooditem_recycle);
            complete_order = itemView.findViewById(R.id.complete_order);
        }
    }

}
