package com.app.sunshine.socratesdiaz.sunshine.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by socratesdiaz on 7/13/16.
 */
public class SunshineAuthenticatorService extends Service {

    private SunshineAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new SunshineAuthenticator(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
