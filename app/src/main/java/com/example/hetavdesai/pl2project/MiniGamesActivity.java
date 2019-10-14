package com.example.hetavdesai.pl2project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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


public class MiniGamesActivity extends AppCompatActivity {


    public FirebaseAuth.AuthStateListener authStateListener;
    CircularImageView nav_image;
    TextView nav_username,nav_email;
    public BirdGameView birdGameView;
    public FlappyGameView flappyGameView;
    public int highscore1, highscore2, highscore3;
    TextView highscoreLabel2, highscoreLabel1, highscoreLabel3;
    private Button game1_btn, game2_btn, game3_btn, game4_btn;
    List<HighscoreClass> list;
    private DrawerLayout mDrawerLayout;
    private ImageButton nav_drawer;
    private FirebaseAuth auth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private FirebaseUser mFirebaseUser;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference databaseReference;
    TextView itemCount;
    List<CartClass> listNew;

    TextView highscoreName1, highscoreName2,highscoreName3;
    LinearLayout detailsRevealed1, detailsRevealed2, detailsRevealed3,gameLayout1,gameLayout2,gameLayout3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mini_games);


        detailsRevealed1 = findViewById(R.id.details_revealed_1);
        detailsRevealed1.setVisibility(View.GONE);

        detailsRevealed2 = findViewById(R.id.details_revealed_2);
        detailsRevealed2.setVisibility(View.GONE);

        detailsRevealed3 = findViewById(R.id.details_revealed_3);
        detailsRevealed3.setVisibility(View.GONE);

        gameLayout1 = findViewById(R.id.Game_1_layout);
        gameLayout2 = findViewById(R.id.Game_2_layout);
        gameLayout3 = findViewById(R.id.Game_3_layout);

        highscoreName1 = findViewById(R.id.highscore_name1);
        highscoreName2 = findViewById(R.id.highscore_name2);
        highscoreName3 = findViewById(R.id.highscore_name3);


        highscoreLabel1 = findViewById(R.id.game_high_score1);
//        SharedPreferences settingsFlappy = getSharedPreferences("HIGH_SCORE_BIRD", Context.MODE_PRIVATE);
//        highscore1 = settingsFlappy.getInt("HIGH_SCORE", 0);
//        highscoreLabel1.setText("High Score : " + highscore1);

        highscoreLabel2 = findViewById(R.id.game_high_score2);
//        SharedPreferences settingsCatch = getSharedPreferences("HIGH_SCORE_FLAPPY",Context.MODE_PRIVATE);
//        highscore2 = settingsCatch.getInt("HIGH_SCORE",0);
//        highscoreLabel2.setText("High Score : " + highscore2);

        highscoreLabel3 = findViewById(R.id.game_high_score3);
//        SharedPreferences settingsBird = getSharedPreferences("HIGH_SCORE_CATCHBALL", Context.MODE_PRIVATE);
//        highscore3 = settingsBird.getInt("HIGH_SCORE", 0);
//        highscoreLabel3.setText("High Score : " + highscore3);

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
                    startActivity(new Intent(MiniGamesActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        nav_drawer.bringToFront();

        mDrawerLayout = findViewById(R.id.drawer_layout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("High Score").child("Game 1");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HighscoreClass value = dataSnapshot1.getValue(HighscoreClass.class);
                    String name = value.getName();
                    String highscore1 = value.getHighscore();
                    highscoreLabel1.setText("Highscore : " + highscore1.toString());
                    highscoreName1.setText(value.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });

        mDatabaseReference = mFirebaseDatabase.getReference().child("High Score").child("Game 2");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HighscoreClass value = dataSnapshot1.getValue(HighscoreClass.class);
                    String name = value.getName();
                    String highscore2 = value.getHighscore();
                    highscoreLabel2.setText("Highscore : " + highscore2.toString());
                    highscoreName2.setText(value.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });

        mDatabaseReference = mFirebaseDatabase.getReference().child("High Score").child("Game 3");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    HighscoreClass value = dataSnapshot1.getValue(HighscoreClass.class);
                    String name = value.getName();
                    String highscore3 = value.getHighscore();
                    highscoreLabel3.setText("Highscore : " + highscore3.toString());
                    highscoreName3.setText(value.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Hello", "Failed to read value.", databaseError.toException());
            }
        });

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
                                finish();
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


        final View buttonCart = findViewById(R.id.btn_cart);
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MiniGamesActivity.this, CartActivity.class));
            }
        });

        game1_btn = findViewById(R.id.btn_game_one);
        game2_btn = findViewById(R.id.btn_game_two);
        game3_btn = findViewById(R.id.btn_game_three);


        game1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent1 = new Intent(getApplicationContext(), BirdGameMainActivity.class);
                startActivity(intent1);
            }
        });

        game2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent2 = new Intent("com.example.hetavdesai.pl2project.MainFlappyActivity");
                startActivity(intent2);
            }
        });

        game3_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent3 = new Intent("com.example.hetavdesai.pl2project.MainCatchBallActivity");
                startActivity(intent3);

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

        gameLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(detailsRevealed1);
                if(detailsRevealed1.getVisibility() == View.GONE)
                    detailsRevealed1.setVisibility(View.VISIBLE);
                else
                    detailsRevealed1.setVisibility(View.GONE);
            }
        });

        gameLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(detailsRevealed2);
                if(detailsRevealed2.getVisibility() == View.GONE)
                    detailsRevealed2.setVisibility(View.VISIBLE);
                else
                    detailsRevealed2.setVisibility(View.GONE);
            }
        });

        gameLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionManager.beginDelayedTransition(detailsRevealed3);
                if(detailsRevealed3.getVisibility() == View.GONE)
                    detailsRevealed3.setVisibility(View.VISIBLE);
                else
                    detailsRevealed3.setVisibility(View.GONE);
            }
        });
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