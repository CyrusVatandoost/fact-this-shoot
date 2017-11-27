package classes;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.teamenigma.factthisshoot.SearchOpponent;

import java.util.List;

/**
 * Created by Rgee on 26/11/2017.
 */

public class SearchOpponentBroadcastReceiver extends BroadcastReceiver {


    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private SearchOpponent mActivity;

    private List<WifiP2pDevice> mOpponentDevices;
    private WifiP2pDevice mOpponentDevice;
    private List<WifiP2pConfig> mConfigs;



    public SearchOpponentBroadcastReceiver(WifiP2pManager manager, WifiP2pManager.Channel channel, SearchOpponent activity) {
        super();
        this.mManager = manager;
        this.mChannel = channel;
        this.mActivity = activity;
    }



    /*
     *
     * @param context The Context in which the receiver is running.
     * @param intent  The Intent being received.
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(mActivity, "Wifi Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mActivity, "Wifi Disabled", Toast.LENGTH_SHORT).show();
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            // The peer list has changed!  We should probably do something about
            // that.

            mManager.requestPeers(mChannel, );

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            DeviceListFragment fragment = (DeviceListFragment) activity.getFragmentManager()
                    .findFragmentById(R.id.frag_list);
            fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(
                    WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));

        }





    }

    /*
    Function called when user selects an available peer to connect to
     */
    public void connect(int position)
    {
        WifiP2pConfig config = mConfigs.get(position);
        mOpponentDevice = mOpponentDevices.get(position);

        mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.i("SearchOpponent.connect", "Connect succeeded!");
            }

            @Override
            public void onFailure(int reason) {
                Log.i("SearchOpponent.connect", "Connect failed! Reason: " + reason);

            }
        });

    }



}
