package com.udacity.capstone.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by chyupa on 19-May-16.
 */
public class CapstoneAuthenticatorService extends Service {
    // Instance field that stores the authenticator object
    private CapstoneAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        mAuthenticator = new CapstoneAuthenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
