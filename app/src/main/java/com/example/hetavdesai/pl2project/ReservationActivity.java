package com.example.hetavdesai.pl2project;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.nfc.cardemulation.CardEmulation;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.flags.Flag;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class ReservationActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    public FirebaseAuth.AuthStateListener authStateListener;
    TextView nav_username,nav_email;
    CircularImageView nav_image;
    Random r = new Random();
    private DrawerLayout mDrawerLayout;
    private MenuItem item;
    private ImageButton nav_drawer;
    private GoogleSignInClient mGoogleSignInClient;
    private int foodieCount = 2;
    private String reservationId;
    private Button pickDate, pickTime, bookTable;
    private TextView dateText, timeText, foodieCountTextView;
    private TextView addFoodie, minusFoodie;
    private EditText inputName, inputEmail, inputPhone;
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String sdate, stime;
    //mobile verification
    String codeSent;
    Button send_otp,cancel_phoneNo;
    EditText enterOtp;
    Boolean bookTableFlag = false,sendotpFlag = true;
    private Boolean sendotpButtonFlag = true;
    LinearLayout otpLinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_booking);

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
        nav_drawer = findViewById(R.id.navbtn);
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
                    startActivity(new Intent(ReservationActivity.this, LoginActivity.class));
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
                        }
                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        View headerView = navigationView.getHeaderView(0);
        nav_username = headerView.findViewById(R.id.nav_username);
        nav_image = headerView.findViewById(R.id.nav_image);
        nav_email = headerView.findViewById(R.id.nav_email);

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

        pickDate = findViewById(R.id.pick_date_button);
        pickTime = findViewById(R.id.pick_time_button);
        dateText = findViewById(R.id.date_textview);
        timeText = findViewById(R.id.time_textview);
        foodieCountTextView = findViewById(R.id.foodie_count);
        addFoodie = findViewById(R.id.add_foodie_button);
        minusFoodie = findViewById(R.id.minus_foodie_button);
        bookTable = findViewById(R.id.booking_info_complete_button);
        inputName = findViewById(R.id.input_name);
        inputEmail = findViewById(R.id.input_email);
        inputPhone = findViewById(R.id.input_phone);
        send_otp = findViewById(R.id.send_otp);
        enterOtp = findViewById(R.id.enter_otp);
        cancel_phoneNo = findViewById(R.id.cancel_phone);
        otpLinearLayout = findViewById(R.id.otp_linear_layout);

        firebaseUser = auth.getCurrentUser();
        inputName.setText(acct.getDisplayName());
        inputPhone.setText(firebaseUser.getPhoneNumber());
        inputEmail.setText(acct.getEmail());

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateLabel();
            }

        };

        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog;

                datePickerDialog = new DatePickerDialog(ReservationActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar c = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                if(myCalendar.getTimeInMillis() >= c.getTimeInMillis())
                    updateTimeLabel();
                else
                    Toast.makeText(ReservationActivity.this, "Invalid Time Selection", Toast.LENGTH_SHORT).show();
            }
        };

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog;
                timePickerDialog = new TimePickerDialog(ReservationActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE), false);
                Date currentTime = Calendar.getInstance().getTime();

                timePickerDialog.show();
            }
        });

        addFoodie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodieCount < 10) { // TODO: Change max count
                    foodieCount++;
                    foodieCountTextView.setText(String.valueOf(foodieCount).concat(" Foodies"));
                }
            }
        });
        minusFoodie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodieCount != 1) {
                    foodieCount--;
                    foodieCountTextView.setText(String.valueOf(foodieCount).concat(" Foodies"));
                }
                if (foodieCount == 1) {
                    foodieCountTextView.setText(String.valueOf(foodieCount).concat(" Foodie"));
                }
            }
        });

            if(sendotpButtonFlag)
            send_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String phone,otp;
                    phone = inputPhone.getText().toString();
                    otp = enterOtp.getText().toString();
                    if(sendotpFlag) {
                            sendVerificationCode();
                    }
                    else{
                        if (TextUtils.isEmpty(otp) || otp.length() != 6) {
                            Toast.makeText(ReservationActivity.this,"Please enter valid OTP",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String code = enterOtp.getText().toString().trim();
                            verifySignInCode(code);
                        }
                    }
                }
            });

            cancel_phoneNo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inputPhone.setEnabled(true);
                    inputPhone.setText("");
                    sendotpFlag = true;
                    send_otp.setText("SEND OTP");
                    TransitionManager.beginDelayedTransition(otpLinearLayout);
                    otpLinearLayout.setVisibility(View.GONE);
                    cancel_phoneNo.setVisibility(View.INVISIBLE);

                    if(bookTableFlag){
                        send_otp.setVisibility(View.VISIBLE);
                        bookTable.setBackgroundResource(R.drawable.rounded_button_grey);
                        enterOtp.setText("");
                    }
                }
            });

            bookTable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bookTableFlag) {
                        String name, email, phone;
                        name = inputName.getText().toString();
                        email = inputEmail.getText().toString();
                        phone = inputPhone.getText().toString();
                        if (TextUtils.isEmpty(stime) || TextUtils.isEmpty(sdate)) {
                            Toast.makeText(ReservationActivity.this, "Please select date and time of booking table.", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || phone.length() != 10) {
                            Toast.makeText(ReservationActivity.this, "Please enter your contact details properly.", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            reservationId = UUID.randomUUID().toString();
                            String status = "Pending";
                            ReservationClass reservationClass = new ReservationClass(reservationId, sdate, String.valueOf(foodieCount), stime, name, email, phone, status);
                            databaseReference.child("Reservations").child(reservationId).setValue(reservationClass);
                            Toast.makeText(ReservationActivity.this, "Booking Request sent successfully.", Toast.LENGTH_SHORT).show();
                            inputPhone.setEnabled(true);
                            startActivity(new Intent(ReservationActivity.this, MyReservationsActivity.class));
                            finish();
                        }
                    }
                }
            });

    }

    private void verifySignInCode(String code){
     //   String code = enterOtp.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //here you can open new activity
                            Toast.makeText(getApplicationContext(),
                                    "Login Successfull", Toast.LENGTH_LONG).show();
                            send_otp.setVisibility(View.GONE);
                            TransitionManager.beginDelayedTransition(otpLinearLayout);
                            if(otpLinearLayout.getVisibility() == View.VISIBLE)
                                otpLinearLayout.setVisibility(View.GONE);
                            else
                                otpLinearLayout.setVisibility(View.VISIBLE);
                            bookTable.setBackgroundResource(R.drawable.rounded_button);
                            bookTableFlag = true;
//                            inputPhone.setEnabled(false);
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(),
                                        "Incorrect Verification Code ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    private void sendVerificationCode(){

        String phone = inputPhone.getText().toString().trim();

        if(phone.isEmpty()){
            inputPhone.setError("Phone number is required");
            inputPhone.requestFocus();
            return;
        }

        if(phone.length() < 10 ){
            inputPhone.setError("Please enter a valid phone");
            inputPhone.requestFocus();
            return;
        }

        inputPhone.setEnabled(false);
        cancel_phoneNo.setVisibility(View.VISIBLE);
        TransitionManager.beginDelayedTransition(otpLinearLayout);
        if(otpLinearLayout.getVisibility() == View.GONE)
            otpLinearLayout.setVisibility(View.VISIBLE);
        else
            otpLinearLayout.setVisibility(View.GONE);
        sendotpFlag = false;
        send_otp.setText("VERIFY");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }



    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                enterOtp.setText(code);
                //verifying the code
                verifySignInCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(ReservationActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
          //  mResendToken = forceResendingToken;
        }
    };

    private void updateDateLabel() {
        String myFormat = "EEE, d MMM"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateText.setText(sdf.format(myCalendar.getTime()));
        sdate = sdf.format(myCalendar.getTime());
    }


    private void updateTimeLabel() {
        String myFormat = "h:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        timeText.setText(sdf.format(myCalendar.getTime()));
        stime = sdf.format(myCalendar.getTime());
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
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
