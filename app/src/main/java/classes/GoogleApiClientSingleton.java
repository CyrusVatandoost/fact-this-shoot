package classes;

import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Cyrus on 25 Oct 2017.
 */

public class GoogleApiClientSingleton {

    private static GoogleApiClientSingleton instance = null;
    private static GoogleApiClient mGoogleApiClient = null;

    protected GoogleApiClientSingleton() {}

    public static GoogleApiClientSingleton getInstance(GoogleApiClient aGoogleApiClient) {
        if(instance == null) {
            instance = new GoogleApiClientSingleton();
            if (mGoogleApiClient == null) {
                mGoogleApiClient = aGoogleApiClient;
                Log.i("MyActivity", "GoogleApiClient created successfully.");
            }
        }
        return instance;
    }

    public GoogleApiClient getGoogleApiClient(){
        if(mGoogleApiClient == null)
            Log.e("MyActivity", "GoogleApiClient is NULL.");
        return mGoogleApiClient;
    }

}
