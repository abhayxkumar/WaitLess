package com.example.hetavdesai.pl2project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.hetavdesai.pl2project.CartActivity.tableno;
import static com.example.hetavdesai.pl2project.CartRecyclerAdapter.cart_size;


public class MyReservationsActivity extends AppCompatActivity {

    public FirebaseAuth.AuthStateListener authStateListener;
    TextView nav_username,nav_email;
    CircularImageView nav_image;
    List<ReservationClass> list, list2;
    RecyclerView recycle, recycle2;
    private DrawerLayout mDrawerLayout;
    private MenuItem item;
    private ImageButton nav_drawer,buttonCart;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference, mDatabaseReference2;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference databaseReference;
    TextView itemCount;
    List<CartClass> listNew;
    private NavigationView navigationView;
    private Menu menu;
    SwitchCompat homeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else
            setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservations);

        buttonCart = findViewById(R.id.btn_cart);
        nav_drawer = findViewById(R.id.navbtn);
        navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.setItemIconTintList(null);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            nav_drawer.setBackgroundResource(R.drawable.menu_dark);
            buttonCart.setBackgroundResource(R.drawable.cart_dark);
            menu.findItem(R.id.nav_home).setIcon(R.drawable.home_dark);
            menu.findItem(R.id.nav_game).setIcon(R.drawable.game_dark);
            menu.findItem(R.id.nav_full_menu).setIcon(R.drawable.menu_dark);
            menu.findItem(R.id.nav_book_table).setIcon(R.drawable.clock_dark);
            menu.findItem(R.id.nav_my_res).setIcon(R.drawable.reserve_dark);
            menu.findItem(R.id.nav_my_order).setIcon(R.drawable.order_dark);
            menu.findItem(R.id.nav_invoice).setIcon(R.drawable.invoice_dark);
            menu.findItem(R.id.nav_sign_out).setIcon(R.drawable.power_dark);

        }
        else {
            nav_drawer.setBackgroundResource(R.drawable.menu_light);
            buttonCart.setBackgroundResource(R.drawable.cart_light);
            menu.findItem(R.id.nav_home).setIcon(R.drawable.home_light);
            menu.findItem(R.id.nav_game).setIcon(R.drawable.game_light);
            menu.findItem(R.id.nav_full_menu).setIcon(R.drawable.menu_light);
            menu.findItem(R.id.nav_book_table).setIcon(R.drawable.clock_light);
            menu.findItem(R.id.nav_my_res).setIcon(R.drawable.reserve_light);
            menu.findItem(R.id.nav_my_order).setIcon(R.drawable.order_light);
            menu.findItem(R.id.nav_invoice).setIcon(R.drawable.invoice_light);
            menu.findItem(R.id.nav_sign_out).setIcon(R.drawable.power_light);
        }

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
                    startActivity(new Intent(MyReservationsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


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
                                Intent intent1 = new Intent("com.example.hetavdesai.pl2project.HomeActivity");
                                startActivity(intent1);
                                finish();
                                break;
                            case R.id.nav_profile:
                                Intent intent2 = new Intent("com.example.hetavdesai.pl2project.MainActivity");
                                startActivity(intent2);
                                finish();
                                break;
                            case R.id.nav_game:
                                Intent intent3 = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
                                startActivity(intent3);
                                finish();
                                break;
                            case R.id.nav_full_menu:
                                Intent intent4 = new Intent("com.example.hetavdesai.pl2project.FullMenuActivity");
                                startActivity(intent4);
                                finish();
                                break;
                            case R.id.nav_book_table:
                                Intent intent5 = new Intent("com.example.hetavdesai.pl2project.ReservationActivity");
                                startActivity(intent5);
                                finish();
                                break;
                            case R.id.nav_my_res:
                                Intent intent6 = new Intent("com.example.hetavdesai.pl2project.MyReservationsActivity");
                                startActivity(intent6);
                                finish();
                                break;
                            case R.id.nav_my_order:
                                Intent intent7 = new Intent("com.example.hetavdesai.pl2project.MyOrdersActivity");
                                startActivity(intent7);
                                finish();
                                break;
                            case R.id.nav_invoice:
                                Intent intent8 = new Intent("com.example.hetavdesai.pl2project.InvoiceActivity");
                                startActivity(intent8);
                                finish();
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

        View headerView = navigationView.getHeaderView(0);
        nav_username = (TextView)headerView.findViewById(R.id.nav_username);
        nav_image = (CircularImageView) headerView.findViewById(R.id.nav_image);
        nav_email = (TextView)headerView.findViewById(R.id.nav_email);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        nav_username.setText(acct.getDisplayName());
        nav_email.setText(acct.getEmail());
        Picasso.get().load(acct.getPhotoUrl()).into(nav_image);
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


        buttonCart = findViewById(R.id.btn_cart);
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyReservationsActivity.this, CartActivity.class));
            }
        });

        recycle = findViewById(R.id.recycle);
        recycle2 = findViewById(R.id.recycle2);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Reservations");
        mDatabaseReference2 = mFirebaseDatabase.getReference().child("Reservation Summary");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ReservationClass value = dataSnapshot1.getValue(ReservationClass.class);
                    ReservationClass fire = new ReservationClass();

                    String name = value.getName();
                    String reservationid = value.getReservationId();
                    String date = value.getDate();
                    String time = value.getTime();
                    String nop = value.getNumberOfPeople();
                    String email = value.getEmail();
                    String phone = value.getPhone();
                    String status = value.getStatus();
                    fire.setName(name);
                    fire.setDate(date);
                    fire.setEmail(email);
                    fire.setNumberOfPeople(nop + " Person");
                    fire.setPhone(phone);
                    fire.setReservationId(reservationid);
                    fire.setTime(time);
                    fire.setStatus(status);
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if (email.equals(acct.getEmail())) {
                        list.add(fire);
                    }
                }
                onActivityOpen();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        mDatabaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list2 = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    ReservationClass value = dataSnapshot1.getValue(ReservationClass.class);
                    ReservationClass fire = new ReservationClass();

                    String name = value.getName();
                    String reservationid = value.getReservationId();
                    String date = value.getDate();
                    String time = value.getTime();
                    String nop = value.getNumberOfPeople();
                    String email = value.getEmail();
                    String phone = value.getPhone();
                    String status = value.getStatus();
                    fire.setName(name);
                    fire.setDate(date);
                    fire.setEmail(email);
                    fire.setNumberOfPeople(nop);
                    fire.setPhone(phone);
                    fire.setReservationId(reservationid);
                    fire.setTime(time);
                    fire.setStatus(status);
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    if (email.equals(acct.getEmail())) {
                        list2.add(fire);
                    }
                }
                onActivityOpen2();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", error.toException());
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference();
        String email = acct.getEmail();
        Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    UserClass value = dataSnapshot1.getValue(UserClass.class);
                    tableno = value.getTableno();
                }

                Query mDatabaseReference = mFirebaseDatabase.getReference().child("Cart").child(String.valueOf(tableno)).orderByChild("username");

                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        listNew = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            CartClass value = dataSnapshot1.getValue(CartClass.class);
                            CartClass fire = new CartClass();
                            String name = value.getName();
                            int price = value.getPrice();
                            int quantity = value.getQuantity();
                            int tableno = value.getTableno();
                            int total = value.getTotal();
                            String orderid = value.getOrderid();
                            String username = value.getUsername();
                            fire.setName(name);
                            fire.setPrice(price);
                            fire.setQuantity(quantity);
                            fire.setTableno(tableno);
                            fire.setTotal(total);
                            fire.setOrderid(orderid);
                            fire.setUsername(username);
                            listNew.add(fire);
                        }

                        cart_size = listNew.size();
                        itemCount = findViewById(R.id.badge_count);
                        itemCount.setText(String.valueOf(cart_size));
                        if (cart_size == 0) {
                            buttonCart.setVisibility(View.GONE);
                        } else {
                            buttonCart.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("Hello", "Failed to read value.", error.toException());
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void onActivityOpen() {
        MyReservationRecyclerAdapter recyclerAdapter = new MyReservationRecyclerAdapter(list, MyReservationsActivity.this);
        RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(MyReservationsActivity.this);
        recycle.setLayoutManager(recycleVariable);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
    }

    public void onActivityOpen2() {
        MyReservationRecyclerAdapter recyclerAdapter2 = new MyReservationRecyclerAdapter(list2, MyReservationsActivity.this);
        RecyclerView.LayoutManager recycleVariable2 = new LinearLayoutManager(MyReservationsActivity.this);
        recycle2.setLayoutManager(recycleVariable2);
        recycle2.setItemAnimator(new DefaultItemAnimator());
        recycle2.setAdapter(recyclerAdapter2);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
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
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // ...
                    }
                });
        auth.signOut();
    }
}
