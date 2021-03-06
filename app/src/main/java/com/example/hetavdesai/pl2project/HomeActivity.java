package com.example.hetavdesai.pl2project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static com.example.hetavdesai.pl2project.CartActivity.tableno;
import static com.example.hetavdesai.pl2project.CartRecyclerAdapter.cart_size;


public class HomeActivity extends AppCompatActivity {
    public FirebaseAuth.AuthStateListener authStateListener;
    TextView nav_username,nav_email;
    CircularImageView nav_image;
    SwitchCompat homeSwitch;
    ScannedBarcodeActivity scannedBarcodeActivity;

    private static final int REQUEST_CAMERA_PERMISSION = 201;

    private DrawerLayout mDrawerLayout;
    private MenuItem item;
    private ImageButton nav_drawer,buttonCart;
    private Button barcodeScan;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference,myRef;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    List<FoodClass> list, list2;
    TextView itemCount;
    private SensorManager mSensorManager;
    private ShakeListener mSensorListener;
    private RecyclerView homeFoodRecycler, homeFoodRecycler2;
    private NavigationView navigationView;
    private Menu menu;
    private MenuItem menuItem;

    List<CartClass> listNew;
    Context context;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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
            menu.findItem(R.id.nav_support).setIcon(R.drawable.support_dark);

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
            menu.findItem(R.id.nav_support).setIcon(R.drawable.support_light);
        }

        homeSwitch = findViewById(R.id.home_switch);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            homeSwitch.setChecked(true);
        }
        homeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            }
        });
        //scannedBarcodeActivity = new ScannedBarcodeActivity();

        if (cart_size == 0) {
            buttonCart.setVisibility(View.GONE);
        } else {
            buttonCart.setVisibility(View.VISIBLE);
        }

        //Manifest.Permissions
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.READ_SMS) +
                ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS,Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
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
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        homeFoodRecycler = findViewById(R.id.home_recycle);
        homeFoodRecycler2 = findViewById(R.id.home_recycle2);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference().child("Food");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                list = new ArrayList<>();
                list2 = new ArrayList<>();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {

                        FoodClass value = dataSnapshot2.getValue(FoodClass.class);
                        FoodClass fire = new FoodClass();
                        String name = value.getName();
                        String description = value.getDescription();
                        int price = value.getPrice();
                        String Menuid = value.getMenuid();
                        String cal = value.getCalories();
                        String popular = value.getPopular();
                        String recommended = value.getRecommended();
                        fire.setName(name);
                        fire.setDescription(description);
                        fire.setPrice(price);
                        fire.setMenuid(Menuid);
                        fire.setCalories(cal);
                        fire.setPopular(value.getPopular());
                        fire.setRecommended(value.getRecommended());
                        if (value.getPopular().equals("yes"))
                            list.add(fire);
                        if (value.getRecommended().equals("yes"))
                            list2.add(fire);
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

        nav_drawer.bringToFront();

        mDrawerLayout = findViewById(R.id.drawer_layout);
        // final ImageButton nav_drawer = (ImageButton)findViewById(R.id.nav_drawer);

        navigationView = findViewById(R.id.nav_view);
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
                                break;
                            case R.id.nav_game:
                                Intent intent3 = new Intent("com.example.hetavdesai.pl2project.MiniGamesActivity");
                                startActivity(intent3);
                                break;
                            case R.id.nav_full_menu:
                                Intent intent4 = new Intent("com.example.hetavdesai.pl2project.FullMenuActivity");
                                startActivity(intent4);
                                break;
                            case R.id.nav_book_table:
                                Intent intent5 = new Intent("com.example.hetavdesai.pl2project.ReservationActivity");
                                startActivity(intent5);
                                break;
                            case R.id.nav_my_res:
                                Intent intent6 = new Intent("com.example.hetavdesai.pl2project.MyReservationsActivity");
                                startActivity(intent6);
                                break;
                            case R.id.nav_my_order:
                                Intent intent7 = new Intent("com.example.hetavdesai.pl2project.MyOrdersActivity");
                                startActivity(intent7);
                                break;
                            case R.id.nav_invoice:
                                Intent intent8 = new Intent("com.example.hetavdesai.pl2project.InvoiceActivity");
                                startActivity(intent8);
                                break;
                            case R.id.nav_sign_out:
                                signOut();
                                break;
                            case R.id.nav_support:
                                Intent intent9 = new Intent("com.example.hetavdesai.pl2project.SupportActivity");
                                startActivity(intent9);
                                break;
                        }
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
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        barcodeScan = findViewById(R.id.btnScanBarcode);

        barcodeScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ScannedBarcodeActivity.class));
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        acct = GoogleSignIn.getLastSignedInAccount(HomeActivity.this);
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

        // Action taken when device is shaken
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeListener();
        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake() {
                String assistanceId;
                assistanceId = UUID.randomUUID().toString();
                AssistanceClass assistanceClass = new AssistanceClass(assistanceId, String.valueOf(tableno));
                databaseReference.child("Assistance").child(assistanceId).setValue(assistanceClass);
                Toast.makeText(HomeActivity.this, "Assistance Requested", Toast.LENGTH_LONG).show();
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        acct = GoogleSignIn.getLastSignedInAccount(HomeActivity.this);
        email = acct.getEmail();
        query = databaseReference.child("users").orderByChild("email").equalTo(email);
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
        HomeFoodRecyclerAdapter recyclerAdapter = new HomeFoodRecyclerAdapter(list, HomeActivity.this);
        HomeFoodRecyclerAdapter recyclerAdapter2 = new HomeFoodRecyclerAdapter(list2, HomeActivity.this);
        RecyclerView.LayoutManager recycleVariable = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView.LayoutManager recycleVariable2 = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        homeFoodRecycler.setLayoutManager(recycleVariable);
        homeFoodRecycler.setItemAnimator(new DefaultItemAnimator());
        homeFoodRecycler.setAdapter(recyclerAdapter);
        homeFoodRecycler2.setLayoutManager(recycleVariable2);
        homeFoodRecycler2.setItemAnimator(new DefaultItemAnimator());
        homeFoodRecycler2.setAdapter(recyclerAdapter2);
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

//        if (scannedBarcodeActivity.intentData.length() > 0) {
//            barcodeScan.setVisibility(View.GONE);
//        }
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

    private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            //Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }

}
