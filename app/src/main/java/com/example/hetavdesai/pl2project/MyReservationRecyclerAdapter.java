package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.graphics.Color;
import android.provider.ContactsContract;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class MyReservationRecyclerAdapter extends RecyclerView.Adapter<MyReservationRecyclerAdapter.MyHoder> {

    public ReservationClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<ReservationClass> list;
    Context context;
    ReservationActivity ReservationActivity;

    // public TextView viewPrice;
    int flag = 0;

    public MyReservationRecyclerAdapter(List<ReservationClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reservation_item, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }

    @Override
    public void onBindViewHolder(final MyHoder holder, final int position) {
        mylist = list.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Reservations").child(mylist.getReservationId());
        holder.name.setText(mylist.getName());
        holder.id.setText(mylist.getReservationId());
        holder.date.setText(mylist.getDate());
        holder.time.setText(mylist.getTime());
        holder.peepCount.setText(mylist.getNumberOfPeople());
        holder.status.setText(mylist.getStatus());
        if (mylist.getStatus().equals("Confirmed")) {
            holder.status.setTextColor(Color.GREEN);
        } else if (mylist.getStatus().equals("Declined")) {
            holder.status.setTextColor(Color.RED);
        }
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
        TextView id, name, peepCount, date, time, status;
        LinearLayout acceptButton, declineButton, actionButtonBar, mainContent, loadingBar;
        TextView declineButtonText, acceptButttonText;
        ImageView acceptImageView, declineImageView;

        public MyHoder(View view) {
            super(view);
            id = (TextView) view.findViewById(R.id.reservation_id_text);
            name = (TextView) view.findViewById(R.id.user_name_resact);
            peepCount = (TextView) view.findViewById(R.id.peep_count_resact);
            date = (TextView) view.findViewById(R.id.date_resact);
            time = (TextView) view.findViewById(R.id.time_resact);
            status = (TextView) view.findViewById(R.id.reservation_status);
            mainContent = (LinearLayout) view.findViewById(R.id.main_content);
            loadingBar = (LinearLayout) view.findViewById(R.id.action_progress_bar);
        }
    }

}
