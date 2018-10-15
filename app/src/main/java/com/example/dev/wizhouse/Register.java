package com.example.dev.wizhouse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by Dev on 28/12/2015.
 */
public class Register extends Activity {
    Button btnCbSU;
    EditText txtCbUserSU, txtCbPassSU, txtCbConfirm, txtCbSerial;
    String userNameCbSU, passCbSU, confirmPass;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        txtCbUserSU = (EditText) findViewById(R.id.txtRegisterEmail);
        txtCbPassSU = (EditText) findViewById(R.id.txtRegisterPassword);
        txtCbConfirm = (EditText) findViewById(R.id.txtConfirmPassword);
        txtCbSerial = (EditText)findViewById(R.id.txtSerialNumber);
        btnCbSU = (Button) findViewById(R.id.btnRegister);

        btnCbSU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameCbSU = txtCbUserSU.getText().toString().trim();
                passCbSU = txtCbPassSU.getText().toString();
                confirmPass = txtCbConfirm.getText().toString();

                if (!userNameCbSU.matches(emailPattern)) {
                    Toast.makeText(getApplicationContext(), "Invalid email address", Toast.LENGTH_LONG).show();
                }
                if (userNameCbSU.equals("") && passCbSU.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please Insert valid user name/password", Toast.LENGTH_LONG).show();
                }
                else if (!confirmPass.equals(passCbSU)) {
                    Toast.makeText(getApplicationContext(), "Your new password and confirmation password don't match", Toast.LENGTH_LONG).show();
                }
                else {
                    GPSService gpsServices = new GPSService(getApplicationContext());
                    ParseUser user = new ParseUser();
                    user.setUsername(userNameCbSU);
                    user.put("homeLat", gpsServices.getLatitude());
                    user.put("homeLong", gpsServices.getLongitude());
                    user.put("serialNumber", txtCbSerial.getText().toString());
                    user.setPassword(passCbSU);
                    user.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                Toast.makeText(getApplicationContext(), "Successful Register", Toast.LENGTH_LONG).show();
                                Intent intentObj = new Intent(Register.this, LoginActivity.class);
                                startActivity(intentObj);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Register Error", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                    }
        });
    }
}
