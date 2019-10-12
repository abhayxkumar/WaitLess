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
import static com.example.hetavdesai.pl2project.CartActivity.emptyCart;
import static com.example.hetavdesai.pl2project.CartActivity.swipeButton;
import static com.example.hetavdesai.pl2project.CartActivity.swipeFlag;
import static com.example.hetavdesai.pl2project.CartClass.gtotal;
import static com.example.hetavdesai.pl2project.InvoiceActivity.grandtotal;
import static com.example.hetavdesai.pl2project.InvoiceRecyclerAdapter.grtotal;

import java.util.List;


public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.MyHoder> {

//    public static int total = 0;
    public static int cart_size;
    public CartClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<CartClass> list;
    Context context;
    ReservationActivity ReservationActivity;
    int tableno, quantityCount;


    public CartRecyclerAdapter(List<CartClass> list, Context context) {
        this.list = list;
        this.context = context;
        cart_size = list.size();
        if (cart_size == 0)
            cartTotal.setText("0");
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, final int position) {
        mylist = list.get(position);
        quantityCount = 1;


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


        holder.cartItemName.setText(mylist.getName());
        holder.dishPrice.setText(String.valueOf(mylist.getPrice()));
        holder.dishTotal.setText(String.valueOf(mylist.getPrice() * mylist.getQuantity()));
        holder.orderid = mylist.getOrderid();
        holder.nameAsString = mylist.getName();
        holder.priceAsInt = mylist.getPrice();
        holder.quantityAsInt = mylist.getQuantity();
        holder.usernameAsString = mylist.getUsername();

        gtotal += holder.priceAsInt * holder.quantityAsInt;
        cartTotal.setText(String.valueOf(gtotal));

        holder.dishTotalAsInt = holder.priceAsInt * holder.quantityAsInt;
        holder.dishTotal.setText(String.valueOf(holder.dishTotalAsInt));
        holder.quantity.setText(String.valueOf(holder.quantityAsInt));

        holder.addquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gtotal=0;
                if (holder.quantityAsInt < 10) { // TODO: Change max count
                    holder.quantityAsInt++;
                    CartClass cartClass = new CartClass(holder.nameAsString, holder.priceAsInt, holder.quantityAsInt, holder.priceAsInt * holder.quantityAsInt, tableno, holder.orderid, holder.usernameAsString, gtotal);
                    databaseReference.child("Cart").child(String.valueOf(tableno)).child(holder.orderid).setValue(cartClass);
                    swipeFlag = false;
                }
            }
        });
        holder.minusquantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gtotal=0;

                if (holder.quantityAsInt == 1) {
//                    CartClass cartClass = new CartClass(holder.nameAsString, holder.priceAsInt, holder.quantityAsInt, holder.priceAsInt * holder.quantityAsInt, tableno, holder.orderid, holder.usernameAsString);
//                    databaseReference.child("Cart").child(String.valueOf(tableno)).child(holder.orderid).setValue(cartClass);

                    //Query checkIfExists = FirebaseDatabase.getInstance().getReference().child("Cart").child(String.valueOf(tableno)).orderByChild("name").equalTo(holder.cartItemName.toString());

                    Query checkIfExists = databaseReference.child("Cart").child(String.valueOf(tableno)).orderByChild("name").equalTo(holder.cartItemName.toString());
                    checkIfExists.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReference.child("Cart").child(String.valueOf(tableno)).child(holder.orderid).removeValue();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                if (holder.quantityAsInt > 1) {
                    holder.quantityAsInt--;
                    CartClass cartClass = new CartClass(holder.nameAsString, holder.priceAsInt, holder.quantityAsInt, holder.priceAsInt * holder.quantityAsInt, tableno, holder.orderid, holder.usernameAsString,gtotal);
                    databaseReference.child("Cart").child(String.valueOf(tableno)).child(holder.orderid).setValue(cartClass);
                    swipeFlag = false;
//                    total -= holder.priceAsInt * holder.quantityAsInt;
//                    cartTotal.setText(String.valueOf(total));
//                    holder.dishTotalAsInt = holder.priceAsInt * holder.quantityAsInt;
//                    holder.dishTotal.setText(String.valueOf(holder.dishTotalAsInt));
//                    holder.quantity.setText(String.valueOf(holder.quantityAsInt));
                }

                if (list.size() == 0) {
                    cartTotal.setText("0");
                }

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

    public class MyHoder extends RecyclerView.ViewHolder {
        TextView cartItemName, dishPrice, dishTotal, quantity, username;
        Button addquantity, minusquantity;
        String orderid, nameAsString, usernameAsString;
        int priceAsInt, quantityAsInt, dishTotalAsInt;

        public MyHoder(View view) {
            super(view);
            cartItemName = itemView.findViewById(R.id.cart_item_name);
            dishPrice = itemView.findViewById(R.id.dish_price);
            dishTotal = itemView.findViewById(R.id.dish_total);
            quantity = itemView.findViewById(R.id.quantity);
            addquantity = itemView.findViewById(R.id.add_quantity);
            minusquantity = itemView.findViewById(R.id.minus_quantity);
        }
    }

}
