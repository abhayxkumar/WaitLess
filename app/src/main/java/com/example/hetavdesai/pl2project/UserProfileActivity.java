package com.example.hetavdesai.pl2project;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.hetavdesai.pl2project.CartActivity.tableno;
import static com.example.hetavdesai.pl2project.CartRecyclerAdapter.cart_size;

public class UserProfileActivity extends AppCompatActivity {

    public FirebaseAuth.AuthStateListener authStateListener;
    TextView nav_username;

    private DrawerLayout mDrawerLayout;
    private MenuItem item;
    private ImageButton nav_drawer, buttonCart;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    TextView itemCount;
    private SensorManager mSensorManager;
    private ShakeListener mSensorListener;

    List<CartClass> listNew;
    Context context;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
                    startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
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

        buttonCart = findViewById(R.id.btn_cart);

        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfileActivity.this, CartActivity.class));
            }
        });

        // Action taken when device is shaken
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeListener();
        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake() {
                String assistanceId;
                assistanceId = UUID.randomUUID().toString();
                AssistanceClass assistanceClass = new AssistanceClass(assistanceId, String.valueOf(tableno));
                databaseReference.child("Assistance").child(assistanceId).setValue(assistanceClass);
                Toast.makeText(UserProfileActivity.this, "Assistance Requested", Toast.LENGTH_SHORT).show();
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(UserProfileActivity.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
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
