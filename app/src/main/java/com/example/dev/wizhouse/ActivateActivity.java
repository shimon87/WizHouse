package com.example.dev.wizhouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.os.AsyncTask;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;

public class ActivateActivity extends AppCompatActivity {

    Switch btnCbDoors,btnCbLR,btnCbKitchen,btnCbRoom;
    ImageButton btnCbBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);
        init();

        btnCbBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivateActivity.this,Welcome.class);
                startActivity(intent);
                finish();
            }
        });

        checkStat("kitchen");
        btnCbKitchen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Components");
                query.whereEqualTo("componentName", "kitchen");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            ParseObject status = list.get(0);
                            if(status.getBoolean("status")) {
                                status.put("status", false);
                                status.saveInBackground();
                            }
                            else {
                                status.put("status", true);
                                status.saveInBackground();
                            }
                        } else {
                            Log.d("kitchen", "Error: " + e.getMessage());
                        }
                    }
                });
            }

        });

        checkStat("bedRoom");
        btnCbRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Components");
                query.whereEqualTo("componentName", "bedRoom");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            ParseObject status = list.get(0);
                            if(status.getBoolean("status")) {
                                status.put("status", false);
                                status.saveInBackground();
                            }
                            else {
                                status.put("status", true);
                                status.saveInBackground();
                            }
                        } else {
                            Log.d("bedRoom", "Error: " + e.getMessage());
                        }
                    }
                });
            }

        });

        checkStat("livingRoom");
        btnCbLR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Components");
                query.whereEqualTo("componentName", "livingRoom");
                query.findInBackground(new FindCallback<ParseObject>() {
                    public void done(List<ParseObject> list, ParseException e) {
                        if (e == null) {
                            ParseObject status = list.get(0);
                            if(status.getBoolean("status")) {
                                status.put("status", false);
                                status.saveInBackground();
                            }
                            else {
                                status.put("status", true);
                                status.saveInBackground();
                            }
                        } else {
                            Log.d("livingRoom", "Error: " + e.getMessage());
                        }
                    }
                });
            }

        });

    }

    void init(){
        btnCbDoors = (Switch)findViewById(R.id.btnSwitchDoors);
        btnCbKitchen = (Switch)findViewById(R.id.btnSwitchKitchen);
        btnCbLR = (Switch)findViewById(R.id.btnSwitchLivingRoom);
        btnCbRoom = (Switch)findViewById(R.id.btnSwitchRoom);
        btnCbBack = (ImageButton)findViewById(R.id.btnBackButton);
    }
    void checkStat(String name){
        ParseQuery<ParseObject> q = ParseQuery.getQuery("Components");
        q.whereEqualTo("componentName", name);

        q.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    ParseObject stat = objects.get(0);
                    btnCbKitchen.setChecked(stat.getBoolean("status"));
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }
}
