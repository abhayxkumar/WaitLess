package com.example.hetavdesai.pl2project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderSummaryActivity extends AppCompatActivity {

    public FirebaseAuth.AuthStateListener authStateListener;
    TextView nav_username;
    List<OrderClass> list;
    List<CartClass> list1;
    RecyclerView recycle;
    int tableno;
    private DrawerLayout mDrawerLayout;
    private MenuItem item;
    private ImageButton nav_drawer;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, databaseReference;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_summary);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        auth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("users");
        mFirebaseUser = auth.getCurrentUser();
        nav_drawer = (ImageButton) findViewById(R.id.navbtn);
        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(AdminOrderSummaryActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        recycle = findViewById(R.id.order_recycle);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Order Copy");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    OrderClass value = dataSnapshot1.getValue(OrderClass.class);
                    OrderClass fire = new OrderClass();

                    String orderID = value.getOrderId();
                    String orderTime = value.getOrderTime();
                    String orderTotal = String.valueOf(value.getOrderTotal());
                    String tableNo = String.valueOf(value.getTableNo());
                    fire.setOrderId(orderID);
                    fire.setOrderTime(orderTime);
                    fire.setOrderTotal(Integer.parseInt(orderTotal));
                    fire.setTableNo(Integer.parseInt(tableNo));
                    list.add(fire);
                }
                onActivityOpen();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        nav_drawer.bringToFront();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        // final ImageButton nav_drawer = (ImageButton)findViewById(R.id.nav_drawer);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        switch (menuItem.getItemId()) {

                            case R.id.nav_home:
                                Intent intent1 = new Intent("com.example.hetavdesai.pl2project.AdminHomeActivity");
                                startActivity(intent1);
                                break;
                            case R.id.nav_res_summary:
                                Intent intent2 = new Intent("com.example.hetavdesai.pl2project.AdminReservationSummaryActivity");
                                startActivity(intent2);
                                break;
                            case R.id.nav_order:
                                Intent intent3 = new Intent("com.example.hetavdesai.pl2project.AdminOrderSummaryActivity");
                                startActivity(intent3);
                                break;
                            case R.id.nav_profile:
                                Intent intent4 = new Intent("com.example.hetavdesai.pl2project.MainActivity");
                                startActivity(intent4);
                                break;
                            case R.id.nav_assist:
                                Intent intent5 = new Intent("com.example.hetavdesai.pl2project.AdminAssistanceActivity");
                                startActivity(intent5);
                                break;
                            case R.id.nav_sign_out:
                                signOut();
                                break;
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

//        View headerView = navigationView.getHeaderView(0);
//        nav_username = (TextView)headerView.findViewById(R.id.nav_username);
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        nav_username.setText(acct.getDisplayName());


        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );
    }

    public void onActivityOpen() {
        OrderRecyclerAdapter recyclerAdapter = new OrderRecyclerAdapter(list, AdminOrderSummaryActivity.this);
        RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(AdminOrderSummaryActivity.this);
        recycle.setLayoutManager(recycleVariable);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                return true;
        }


        return super.onOptionsItemSelected(item);

    }

    private void signOut() {
        auth.signOut();
    }
}
