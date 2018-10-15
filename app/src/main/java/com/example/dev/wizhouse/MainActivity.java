package com.example.dev.wizhouse;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // determine if the current user in anonymous
        if(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser()))
        {
            Intent intentObj = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intentObj);
            finish();
        }
        else
        {
            ParseUser currentUserObj = ParseUser.getCurrentUser();
            if(currentUserObj != null)
            {
                Intent intentObj = new Intent(MainActivity.this, Welcome.class);
                startActivity(intentObj);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Please Try Again",Toast.LENGTH_LONG).show();
            }
        }
    }
}
