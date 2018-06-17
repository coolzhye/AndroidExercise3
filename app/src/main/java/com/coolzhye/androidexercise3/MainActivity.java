package com.coolzhye.androidexercise3;

import android.Manifest.permission;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build.VERSION_CODES;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient mFusedLocationClient;

    @RequiresApi(api = VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestPermissions(new String[] { permission.ACCESS_FINE_LOCATION } , 1);

        Button buttonSearch = findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getAddress();
            }
        });
    }

    private void getAddress() {
        if ((ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                (ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                EditText editTextLocation = findViewById(R.id.editTextLocation);

                String loc = editTextLocation.getText().toString().trim();

                if (loc.isEmpty()) return;

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    List<Address> addresses = geocoder.getFromLocationName(loc, 1);
                    TextView textViewAddress = findViewById(R.id.textViewAddress);

                    if (addresses.size() > 0) {

                        textViewAddress.setText(null);
                        Address address = addresses.get(0);

                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                            textViewAddress.append(address.getAddressLine(i));
                        }
                    } else {
                        textViewAddress.setText("Not found address");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}