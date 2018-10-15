package com.example.dev.wizhouse;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

/**
 * Created by Dev on 28/12/2015.
 */
public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        final String appID = "rL98syudsrP6Zikf1triw0FHGAN9QYydzFELE7Wr";
        final String clientKey = "dpVB7uoWiU65Tagv80GuMp47ltHCMdPVPPOzKG5H";

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, appID, clientKey);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACLObj = new ParseACL();

        defaultACLObj.setPublicReadAccess(true);

        ParseACL.setDefaultACL(defaultACLObj, true);

    }
}
