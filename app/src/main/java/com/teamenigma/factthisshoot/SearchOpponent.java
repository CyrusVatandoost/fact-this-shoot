package com.teamenigma.factthisshoot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import classes.SearchOpponentBroadcastReceiver;

public class SearchOpponent extends AppCompatActivity {

    TextView txtSearchingOpponent;
    Button btnSearchOpponentButton;

    IntentFilter mIntentFilter;
    WifiP2pManager mWifiP2pManager;
    WifiP2pManager.Channel mChannel;
    WifiP2pManager.PeerListListener mPeerListListener;

    WifiP2pManager.ActionListener mDeviceConnectListener;
    WifiP2pManager.ActionListener mStartDiscoveryListener;

    ArrayAdapter mWifiP2pArrayAdapter;
    ListView mOpponentsListView;

    SearchOpponentBroadcastReceiver mOpponentBroadcastReceiver;

    int pos;

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_opponent);

        mIntentFilter = new IntentFilter();

        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION); //Indicates whether wifi p2p is enabled or not
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION); //Indicates the available peer list has changed
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION); //Indicates the state of wifi p2p connectivity has changed
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION); //Indicates the device's config details have changed


        mWifiP2pArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, peers);

        mOpponentsListView = (ListView) findViewById(R.id.lvOpponents);
        mOpponentsListView.setAdapter(mWifiP2pArrayAdapter);


        mOpponentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                mOpponentBroadcastReceiver.connect(pos);
            }
        });


        txtSearchingOpponent = (TextView) findViewById(R.id.txtSearchingOpponent);
        txtSearchingOpponent.setVisibility(View.INVISIBLE);


        btnSearchOpponentButton = (Button) findViewById(R.id.btnSearchOpponentButton);
        btnSearchOpponentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                search(view);

            }
        });

        mWifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);

        mChannel = mWifiP2pManager.initialize(this, getMainLooper(), null);

        mOpponentBroadcastReceiver = new SearchOpponentBroadcastReceiver(mWifiP2pManager, mChannel, this);




        setupP2P();




    }

    public void play(InetAddress hostAddress, Boolean host)
    {
        //Start online play
    }

    private void setupP2P()
    {

        setupActionListeners();

        mPeerListListener = peerList -> {

            List<WifiP2pDevice> refreshedPeers = (List<WifiP2pDevice>) peerList.getDeviceList();
            if (!refreshedPeers.equals(peers))
            {
                peers.clear();
                peers.addAll(refreshedPeers);

                mWifiP2pArrayAdapter.notifyDataSetChanged();

                for(WifiP2pDevice peer : peers)
                {
                    peer.deviceName;
                }

                // If an AdapterView is backed by this data, notify it
                // of the change.  For instance, if you have a ListView of
                // available peers, trigger an update.
                ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();

                // Perform any other updates needed based on the new list of
                // peers connected to the Wi-Fi P2P network.
            }

            if (peers.size() == 0)
            {
                Log.d("WIFI P2P", "No devices found");
                return;
            }

            WifiP2pDevice opponentDevice = peers.get(0); //Gets first device on the list as the opponent
            WifiP2pConfig config = new WifiP2pConfig();//Setup configuration

            config.deviceAddress = opponentDevice.deviceAddress;

            config.wps.setup = WpsInfo.PBC;

            mWifiP2pManager.connect(mChannel, config, mDeviceConnectListener);//Connect to the opponent device

        };





    }



    private void search(View view)
    {

        mStartDiscoveryListener = new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Code for when the discovery initiation is successful goes here.
                // No services have actually been discovered yet, so this method
                // can often be left blank.  Code for peer discovery goes in the
                // onReceive method, detailed below.
                Toast.makeText(SearchOpponent.this, "Searching opponent", Toast.LENGTH_SHORT).show();
                txtSearchingOpponent.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(int reason) {
                // Code for when the discovery initiation fails goes here.
                // Alert the user that something went wrong.

                Toast.makeText(SearchOpponent.this, "Error: Code " + reason, Toast.LENGTH_SHORT).show();

            }
        };

        mWifiP2pManager.discoverPeers(mChannel, mStartDiscoveryListener);
    }

    public void displayPeers(WifiP2pDeviceList peerList)
    {
        mWifiP2pArrayAdapter.clear();

        for(WifiP2pDevice peer: peerList.getDeviceList())
        {
            mWifiP2pArrayAdapter.add(peer.deviceName + "\t" + peer.deviceAddress);
        }

    }



    private void setupActionListeners()
    {
        mDeviceConnectListener = new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() { //Called when connection to opponent device is successful.

            }

            @Override
            public void onFailure(int reasonCode) {
            //Called when connection to opponent device is unsuccessful.
            }
        };


    }



}


