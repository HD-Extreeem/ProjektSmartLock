package com.hellomicke89gmail.projektsmartlock;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by hello on 2016-05-03.
 */
public class GCMTokenRefreshListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent intent=new Intent(this, GCMRegistrationIntentService.class);
        startService(intent);
    }
}
