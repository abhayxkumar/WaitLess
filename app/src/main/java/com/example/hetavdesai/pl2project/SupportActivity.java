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
import android.support.v7.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.UUID;

import static com.example.hetavdesai.pl2project.CartActivity.tableno;

public class SupportActivity extends AppCompatActivity {

    EditText feedbackText;
    Button submitFeedback;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private NavigationView navigationView;
    private Menu menu;
    private ImageButton nav_drawer,buttonCart;
    private SensorManager mSensorManager;
    private ShakeListener mSensorListener;
    public FirebaseAuth.AuthStateListener authStateListener;
    TextView nav_username,nav_email;
    CircularImageView nav_image;
    private DrawerLayout mDrawerLayout;
    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
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

        feedbackText = findViewById(R.id.feedback);
        submitFeedback = findViewById(R.id.submit_feedback);
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        submitFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fb = feedbackText.getText().toString();
                if(!fb.isEmpty())
                {
                    FeedbackClass feedbackClass = new FeedbackClass(fb);
                    databaseReference.child("Feedback").child(currentDateTimeString).child(acct.getDisplayName()).setValue(feedbackClass);
                    feedbackText.setText("");
                    Toast.makeText(SupportActivity.this, "Thank you for your valuable feedback!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SupportActivity.this, HomeActivity.class));
                }
            }
        });

        nav_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });

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

        nav_username.setText(acct.getDisplayName());
        nav_email.setText(acct.getEmail());
        Picasso.get().load(acct.getPhotoUrl()).into(nav_image);

        auth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(SupportActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        mDrawerLayout = findViewById(R.id.drawer_layout);
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

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeListener();
        mSensorListener.setOnShakeListener(new ShakeListener.OnShakeListener() {

            public void onShake() {
                String assistanceId;
                assistanceId = UUID.randomUUID().toString();
                AssistanceClass assistanceClass = new AssistanceClass(assistanceId, String.valueOf(tableno));
                databaseReference.child("Assistance").child(assistanceId).setValue(assistanceClass);
                Toast.makeText(SupportActivity.this, "Assistance Requested", Toast.LENGTH_SHORT).show();
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
}
