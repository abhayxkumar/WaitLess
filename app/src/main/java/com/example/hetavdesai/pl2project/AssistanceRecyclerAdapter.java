package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class AssistanceRecyclerAdapter extends RecyclerView.Adapter<AssistanceRecyclerAdapter.MyHoder> {

    public AssistanceClass mylist;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<AssistanceClass> list;
    Context context;


    public AssistanceRecyclerAdapter(List<AssistanceClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assistance_item, parent, false);
        MyHoder myHoder = new MyHoder(view);

        return myHoder;
    }


    @Override
    public void onBindViewHolder(final AssistanceRecyclerAdapter.MyHoder holder, final int position) {
        mylist = list.get(position);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Assistance").child(mylist.getAssistanceID());

        holder.tableNoForHolder.setText(mylist.getTableNo());

        holder.completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseReference.removeValue();
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
        TextView tableNoForHolder;
        LinearLayout completeButton, actionButtonBar, mainContent, loadingBar;
        TextView completeButtonText;
        ImageView completeImageView;

        public MyHoder(View view) {
            super(view);
            tableNoForHolder = (TextView) view.findViewById(R.id.table_no);
            completeButton = (LinearLayout) view.findViewById(R.id.complete_res_layout);
            completeButtonText = (TextView) view.findViewById(R.id.complete_textview);
            actionButtonBar = (LinearLayout) view.findViewById(R.id.action_section);
            mainContent = (LinearLayout) view.findViewById(R.id.main_content);
            loadingBar = (LinearLayout) view.findViewById(R.id.action_progress_bar);
            completeImageView = (ImageView) view.findViewById(R.id.complete_imageview);

        }
    }
}
