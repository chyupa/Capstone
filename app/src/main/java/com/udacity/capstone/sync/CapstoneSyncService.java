package com.udacity.capstone.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by chyupa on 19-May-16.
 */
public class CapstoneSyncService extends Service {

    private final String LOG_TAG = getClass().getSimpleName();
    private static final Object sSyncAdapterLock = new Object();
    private static CapstoneSyncAdapter sCapstoneSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate - CapstoneSyncService");
        synchronized (sSyncAdapterLock) {
            if (sCapstoneSyncAdapter == null) {
                sCapstoneSyncAdapter = new CapstoneSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return sCapstoneSyncAdapter.getSyncAdapterBinder();
    }
}
