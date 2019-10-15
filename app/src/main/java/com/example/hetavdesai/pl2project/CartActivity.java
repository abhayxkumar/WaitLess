package com.example.hetavdesai.pl2project;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.CardView;
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
import android.widget.ImageView;
import android.widget.RemoteViews;
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
import java.util.Calendar;
import java.util.List;


public class CartActivity extends AppCompatActivity {

    public static View swipeButton;
    public static TextView cartTotal;
    public static TextView emptyCart;
    public static int tableno;
    public FirebaseAuth.AuthStateListener authStateListener;
    ImageView cartRupeeSymbol;
    TextView nav_username,nav_email;
    CircularImageView nav_image;
    TextView buttonText;
    Context context;
    List<CartClass> list;
    RecyclerView recycle;
    CartClass value1, fire1;
    String itemName, itemPrice, itemQuantity, tableNo;
    CardView scanQRButton;
    public static boolean swipeFlag;
    private DrawerLayout mDrawerLayout;
    private MenuItem item;
    private ImageButton nav_drawer;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    //int total = CartRecyclerAdapter.total;
    private DatabaseReference mDatabaseReference, databaseReference;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    public MessagingMain messagingMain;
    NotificationManagerCompat notificationManagerCompat;
    private NavigationView navigationView;
    private Menu menu;
    SwitchCompat homeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        nav_drawer = findViewById(R.id.navbtn);
        navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        navigationView.setItemIconTintList(null);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            nav_drawer.setBackgroundResource(R.drawable.menu_dark);
            menu.findItem(R.id.nav_home).setIcon(R.drawable.home_dark);
            menu.findItem(R.id.nav_game).setIcon(R.drawable.game_dark);
            menu.findItem(R.id.nav_full_menu).setIcon(R.drawable.menu_dark);
            menu.findItem(R.id.nav_book_table).setIcon(R.drawable.clock_dark);
            menu.findItem(R.id.nav_my_res).setIcon(R.drawable.reserve_dark);
            menu.findItem(R.id.nav_my_order).setIcon(R.drawable.order_dark);
            menu.findItem(R.id.nav_invoice).setIcon(R.drawable.invoice_dark);
            menu.findItem(R.id.nav_sign_out).setIcon(R.drawable.power_dark);
            menu.findItem(R.id.nav_support).setIcon(R.drawable.support_dark);

        }
        else {
            nav_drawer.setBackgroundResource(R.drawable.menu_light);
            menu.findItem(R.id.nav_home).setIcon(R.drawable.home_light);
            menu.findItem(R.id.nav_game).setIcon(R.drawable.game_light);
            menu.findItem(R.id.nav_full_menu).setIcon(R.drawable.menu_light);
            menu.findItem(R.id.nav_book_table).setIcon(R.drawable.clock_light);
            menu.findItem(R.id.nav_my_res).setIcon(R.drawable.reserve_light);
            menu.findItem(R.id.nav_my_order).setIcon(R.drawable.order_light);
            menu.findItem(R.id.nav_invoice).setIcon(R.drawable.invoice_light);
            menu.findItem(R.id.nav_sign_out).setIcon(R.drawable.power_light);
            menu.findItem(R.id.nav_support).setIcon(R.drawable.support_light);
        }
        //Notification Manager defined
        notificationManagerCompat = NotificationManagerCompat.from(this);

        cartTotal = findViewById(R.id.cart_total);
        cartRupeeSymbol = findViewById(R.id.rupee_symbol);
        buttonText = findViewById(R.id.swipe_to_order);
        scanQRButton = findViewById(R.id.scan_qr_button);

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
                    startActivity(new Intent(CartActivity.this, LoginActivity.class));
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
                            case R.id.nav_support:
                                Intent intent9 = new Intent("com.example.hetavdesai.pl2project.SupportActivity");
                                startActivity(intent9);
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

        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
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

        recycle = findViewById(R.id.recycle);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        String email = acct.getEmail();
        final Query query = databaseReference.child("users").orderByChild("email").equalTo(email);
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
                        list = new ArrayList<>();
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

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final in.shadowfax.proswipebutton.ProSwipeButton proSwipeBtn = findViewById(R.id.proSwipeButton);
        proSwipeBtn.setSwipeDistance(0.9f);

        if(!(tableno == 1001 || tableno == 1002 || tableno == 1003 || tableno == 1004)) {
            proSwipeBtn.setVisibility(View.GONE);
            scanQRButton.setVisibility(View.VISIBLE);
            proSwipeBtn.setEnabled(false);
        }
        else{
            scanQRButton.setVisibility(View.GONE);
            proSwipeBtn.setVisibility(View.VISIBLE);
        }

        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, ScannedBarcodeActivity.class));
            }
        });

        proSwipeBtn.setOnSwipeListener(new in.shadowfax.proswipebutton.ProSwipeButton.OnSwipeListener() {
            @Override
            public void onSwipeConfirm() {

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

                messagingMain = new MessagingMain();
                messagingMain.cartusers();
                cartTotal.setVisibility(View.INVISIBLE);
                cartRupeeSymbol.setVisibility(View.INVISIBLE);
                buttonText.setVisibility(View.INVISIBLE);
                // user has swiped the btn. Perform yo
                //ur async operation now
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeFlag = true;
                        Query mDatabaseReference = mFirebaseDatabase.getReference().child("Cart").child(String.valueOf(tableno));
                        mDatabaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                list = new ArrayList<>();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                    value1 = dataSnapshot1.getValue(CartClass.class);
                                    fire1 = new CartClass();

                                    itemName = value1.getName();
                                    itemQuantity = String.valueOf(value1.getQuantity());
                                    itemPrice = String.valueOf(value1.getPrice());
                                    tableNo = String.valueOf(value1.getTableno());

                                    abc();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("Hello", "Failed to read value.", error.toException());
                            }
                        });

                        databaseReference.child("Cart").child(String.valueOf(tableno)).removeValue();
                        startActivity(new Intent(CartActivity.this, FullMenuActivity.class));
                        cartTotal.setText("0");
                        sendLoginNotif();
                        proSwipeBtn.showResultIcon(true);
                    }
                }, 2000);
                swipeFlag = false;
                finish();
            }
        });


    }

    public void abc() {
        if(swipeFlag) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
            DatabaseReference databaseReference = mFirebaseDatabase.getReference();
            final String key = databaseReference.push().getKey();
            OrderClass orderClass = new OrderClass(value1.getTableno(), key, Calendar.getInstance().getTime().toString(), value1.getTotal());
            OrderItemClass orderItemClass = new OrderItemClass(itemName, itemPrice, itemQuantity, tableNo, 0, value1.getUsername());
            databaseReference.child("Order").child(String.valueOf(tableno)).setValue(orderClass);
            databaseReference.child("Order Copy").child(String.valueOf(tableno)).setValue(orderClass);
            databaseReference.child("Order Summary").child(String.valueOf(tableno)).setValue(orderClass);
            databaseReference.child("Order Items").child(String.valueOf(tableno)).child(value1.getUsername()).push().setValue(orderItemClass);
            databaseReference.child("Order Items Copy").child(String.valueOf(tableno)).child(value1.getUsername()).push().setValue(orderItemClass);
            databaseReference.child("Order Items Summary").child(String.valueOf(tableno)).child(value1.getUsername()).push().setValue(orderItemClass);
        }
    }

    public void onActivityOpen() {
        CartRecyclerAdapter recyclerAdapter = new CartRecyclerAdapter(list, CartActivity.this);
        RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(CartActivity.this);
        recycle.setLayoutManager(recycleVariable);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setAdapter(recyclerAdapter);
    }

    public void sendLoginNotif(){
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intent = new Intent(this,MyOrdersActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        RemoteViews collapsedView = new RemoteViews(getPackageName(),R.layout.notification_order_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),R.layout.notification_order_expanded);


        Notification notification = new NotificationCompat.Builder(this,MyFirebaseMessagingService.WAITLESS_ID)
                .setSmallIcon(R.drawable.waitless_tp_logo)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                .setAutoCancel(true)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setSound(defaultSoundUri)
               // .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)                  //as this just login notif hence disappears after 5 sec so no intent required
                .build();
        notificationManagerCompat.notify(1,notification);
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();
//        onActivityOpen();
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