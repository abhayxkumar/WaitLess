package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.hetavdesai.pl2project.CartActivity.cartTotal;

import java.util.List;


public class InvoiceItemRecyclerAdapter extends RecyclerView.Adapter<InvoiceItemRecyclerAdapter.MyHoder> {

    public OrderItemClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<OrderItemClass> list;
    Context context;
    ReservationActivity ReservationActivity;

    public InvoiceItemRecyclerAdapter(List<OrderItemClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_item, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, final int position) {
        mylist = list.get(position);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        holder.itemName.setText(mylist.getItemName());
        holder.price.setText(mylist.getItemPrice());
        holder.quantity.setText(mylist.getItemQuantity());
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

    public class MyHoder extends RecyclerView.ViewHolder {
        TextView itemName, quantity, price;
        int quantityAsInt, priceAsInt;

        public MyHoder(View view) {
            super(view);
            itemName = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            price = itemView.findViewById(R.id.item_price);
        }
    }

}
