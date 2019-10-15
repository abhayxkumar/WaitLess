package com.example.hetavdesai.pl2project;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.hetavdesai.pl2project.CartActivity.tableno;


public class ScannedBarcodeActivity extends AppCompatActivity {



    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference databaseReference, databaseReference1;
    String email;
    Query query;
    List<CartClass> listNew;
    TextView itemCount;


    private static final int REQUEST_CAMERA_PERMISSION = 201;
    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    Button btnAction;
    String intentData = "";
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.DarkAppTheme);
        }
        else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);
        initViews();
    }

    private void initViews() {
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);


        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (intentData.length() > 0) {


                    final GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                    String key = acct.getId();


                    mFirebaseDatabase = FirebaseDatabase.getInstance();

                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference1 = FirebaseDatabase.getInstance().getReference();
                    //acct = GoogleSignIn.getLastSignedInAccount(ScannedBarcodeActivity.this);
                    email = acct.getEmail();
                    query = databaseReference.child("users").orderByChild("email").equalTo(email);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                UserClass value = dataSnapshot1.getValue(UserClass.class);
                                tableno = value.getTableno();
                            }

                            Query mDatabaseReference = mFirebaseDatabase.getReference().child("Cart").child(acct.getId().substring(18)).orderByChild("username");

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
                                        String key = databaseReference1.push().getKey();
                                        databaseReference1.child("Cart").child(intentData).child(key).setValue(fire);
                                    }
//                                    cart_size = listNew.size();
//                                    itemCount = findViewById(R.id.badge_count);
//                                    itemCount.setText(String.valueOf(cart_size));
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



                    UserClass userClass = new UserClass(acct.getDisplayName(), null, acct.getEmail(), "customer", Integer.parseInt(intentData));
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child("users").child(key).setValue(userClass);
                    UserNameClass userNameClass = new UserNameClass(acct.getDisplayName());
                    databaseReference.child("Table").child(intentData).child(acct.getId()).setValue(userNameClass);

                    startActivity(new Intent(ScannedBarcodeActivity.this, HomeActivity.class));

                    Toast.makeText(ScannedBarcodeActivity.this, "Added To Table No. " + intentData, Toast.LENGTH_SHORT).show();
                    //databaseReference1.child("Cart").child(acct.getId().substring(18)).removeValue();
                }

            }
        });
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode Scanner Started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcodeActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcodeActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            intentData = barcodes.valueAt(0).displayValue;
                            txtBarcodeValue.setText(intentData);
                            btnAction.setText("JOIN TABLE " + intentData);

                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
