package com.example.dev.wizhouse;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Dev on 28/12/2015.
 */
public class Welcome extends Activity {
    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Welcome.this, android.R.layout.select_dialog_singlechoice);
    ImageButton btnCbLogout, btnCbActivate, btnCbWhoHome, btnCbDataUsage;
    ListView lstCbWhoHome;
    AlertDialog.Builder builderSingle = new AlertDialog.Builder(Welcome.this);
    String serial;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        // Retrive the current user info
        ParseUser currentUser = ParseUser.getCurrentUser();
        String strUser = currentUser.getUsername().toString();
        TextView lblCbuser = (TextView) findViewById(R.id.lblLoggedInAs);
        lblCbuser.setText("Welcome: " + strUser);
        init();

        btnCbLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                finish();
            }
        });

        btnCbActivate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, ActivateActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btnCbWhoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseObject user = new ParseObject("GpsRadar");
                checkGPS(user);
                ParseQuery<ParseObject> query = ParseQuery.getQuery("GpsRadar");
                query.whereEqualTo("serialNumber", serial);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> objects, ParseException e) {
                        for (int i = 0; i < objects.size(); i++) {
                            if (ifInRadius(objects.get(i).getDouble("laTitude"), objects.get(i).getDouble("longTitude"))) {
                                arrayAdapter.add(objects.get(i).getString("userName"));
                            }
                        }
                    }
                });

                builderSingle.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                AlertDialog.Builder builderInner = new AlertDialog.Builder(Welcome.this);
                                builderInner.setMessage(strName);
                                builderInner.setTitle("Your Selected Item is");
                                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builderInner.show();
                            }
                        });
                builderSingle.show();
            }
        });

    }

    void init() {
        btnCbLogout = (ImageButton) findViewById(R.id.btnLogout);
        btnCbActivate = (ImageButton) findViewById(R.id.btnActivate);
        btnCbDataUsage = (ImageButton) findViewById(R.id.btnDataUsage);
        btnCbWhoHome = (ImageButton) findViewById(R.id.btnWhoHome);
    }

    public void checkGPS(final ParseObject currentUser) {
        double currentLon, currentLat, oldLat, oldLon;
        //String currentCity;
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("userName", ParseUser.getCurrentUser().getUsername());
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser object, ParseException e) {
                serial = object.getString("serialNumber");
                currentUser.put("serialNumber", serial);
            }
        });
        oldLat = currentUser.getDouble("laTitude");
        oldLon = currentUser.getDouble("longTitude");
        GPSService mGPSService = new GPSService(getApplicationContext());
        mGPSService.getLocation();
        if (mGPSService.isLocationAvailable == true) {
            // Getting location co-ordinates
            currentLat = mGPSService.getLatitude();
            currentLon = mGPSService.getLongitude();
            float f = mGPSService.checkDistance(oldLat, oldLon, currentLat, currentLon);
            int i = Math.round(f) / 1000;
            //Toast.makeText(getApplicationContext(), currentCity, Toast.LENGTH_SHORT).show();
            if ((currentLat != oldLat) || (currentLon != oldLon)) {
                currentUser.put("currentlat", currentLat);
                currentUser.put("currentlon", currentLon);
                currentUser.saveInBackground();
            }
        }
        //close the gps after using it. Save user's battery power
        mGPSService.closeGPS();
    }

    boolean ifInRadius(double currentLat, double currentLon){
        GPSService mGPSService = new GPSService(getApplicationContext());
        double homeLat = ParseUser.getCurrentUser().getDouble("homeLat");
        double homeLong = ParseUser.getCurrentUser().getDouble("homeLong");
        float f = mGPSService.checkDistance(homeLat, homeLong, currentLat, currentLon);
        if (f < 50)
            return true;
        else
            return false;
    }

}
