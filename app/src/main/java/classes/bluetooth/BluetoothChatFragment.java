/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package classes.bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamenigma.factthisshoot.R;

import classes.logger.Log;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private ListView mConversationView;
    private EditText mOutEditText;
    private Button mConnectSecure;
    private Button mConnectInsecure;
    private Button mMakeDiscoverable;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;
    private String mOwnDeviceName = null;

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;

    /**
     * Local Bluetooth adapter
     */
    private BluetoothAdapter mBluetoothAdapter = null;

    /**
     * Member object for the chat services
     */
    private BluetoothConnectionService mChatService = null;

    /**
     *
     * Game object that handles the game.
     */
    //private Game mGame = new Game();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //mGame = new Game();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            FragmentActivity activity = getActivity();
            Toast.makeText(activity, "Bluetooth is not available", Toast.LENGTH_LONG).show();
            activity.finish();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
            // Otherwise, setup the chat session
        } else if (mChatService == null) {
            setupChat();

            //disableButtons();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mChatService != null) {
            mChatService.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (mChatService != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (mChatService.getState() == BluetoothConnectionService.STATE_NONE) {
                // Start the Bluetooth chat services
                mChatService.start();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bluetooth_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mConversationView = (ListView) view.findViewById(R.id.in);
       mConnectSecure = (Button) view.findViewById(R.id.btn_connSecure);
       mConnectInsecure= (Button) view.findViewById(R.id.btn_connInsecure);
       mMakeDiscoverable = (Button) view.findViewById(R.id.btn_makeDiscoverable);
        //mOutEditText = (EditText) view.findViewById(R.id.edit_text_out);
    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

        // Initialize the array adapter for the conversation thread
        mConversationArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.message);

        mConversationView.setAdapter(mConversationArrayAdapter);

        // Initialize the compose field with a listener for the return key
        //mOutEditText.setOnEditorActionListener(mWriteListener);

        // Initialize the rock button with a listener that for click events
        mConnectSecure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Connect to a device in a secure fashion
                View view = getView();
                if (null != view) {
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                }
            }
        });

        mConnectInsecure.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    // Launch the DeviceListActivity to see devices and do scan
                    Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                }
            }
        });

        mMakeDiscoverable.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Send a message using content of the edit text widget
                View view = getView();
                if (null != view) {
                    // Ensure this device is discoverable by others
                    ensureDiscoverable();
                }
            }
        });

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothConnectionService(getActivity(), mHandler);

        // Initialize the buffer for outgoing messages
        mOutStringBuffer = new StringBuffer("");
    }

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    /**
     * Sends a message.
     *
     * @param message A string of text to send.
     */
    private void sendMessage(String message) {
        // Check that we're actually connected before trying anything
        if (mChatService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
            Toast.makeText(getActivity(), "You are not connected to a device", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the BluetoothChatService to write
            byte[] send = message.getBytes();
            mChatService.write(send, Constants.MESSAGE_WRITE);

            // Reset out string buffer to zero and clear the edit text field
            mOutStringBuffer.setLength(0);
            //mOutEditText.setText(mOutStringBuffer);
        }
    }

    /**
     * The action listener for the EditText widget, to listen for the return key
     */
    private TextView.OnEditorActionListener mWriteListener
            = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
            // If the action is a key-up event on the return key, send the message
            if (actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_UP) {
                String message = view.getText().toString();
                sendMessage(message);
            }
            return true;
        }
    };

    /**
     * Updates the status on the action bar.
     *
     * @param resId a string resource ID
     */
    private void setStatus(int resId) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(resId);
    }

    /**
     * Updates the status on the action bar.
     *
     * @param subTitle status
     */
    private void setStatus(CharSequence subTitle) {
        FragmentActivity activity = getActivity();
        if (null == activity) {
            return;
        }
        final ActionBar actionBar = activity.getActionBar();
        if (null == actionBar) {
            return;
        }
        actionBar.setSubtitle(subTitle);
    }

    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            FragmentActivity activity = getActivity();
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothConnectionService.STATE_CONNECTED:
                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            mConversationArrayAdapter.clear();
                            //String connectDeviceNamePacket = "ownDevice:"+mConnectedDeviceName;
                            //mChatService.write(connectDeviceNamePacket.getBytes(), Constants.MESSAGE_WRITE);
                            break;
                        case BluetoothConnectionService.STATE_CONNECTING:
                            setStatus("connecting...");
                            break;
                        case BluetoothConnectionService.STATE_LISTEN:
                        case BluetoothConnectionService.STATE_NONE:
                            setStatus("not connected");
                            break;
                    }
                    break;
                case Constants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    String messagePacketWrite[] = writeMessage.split(":");
                    String messageHeaderWrite = messagePacketWrite[0];
                    String messageContentWrite = messagePacketWrite[1];

                    switch(messageHeaderWrite)
                    {

                        case "startGame":
                            //mGame = new Game();
                            mConversationArrayAdapter.add(messageHeaderWrite + ": Game has started.");
                            break;

                        case "opponentChoice":
                            //mConversationArrayAdapter.add(mGame.getRound() + " - " + "Me: " + messageContentWrite);
                            break;

                        case "roundWinner":
                            String winnerContentPacket[] = messageContentWrite.split("%");
                            int determinant = Integer.parseInt(winnerContentPacket[0]);
                            String message = winnerContentPacket[1];

                            switch(determinant)
                            {
                                case 1://You won
                                    //mConversationArrayAdapter.add(mGame.getRound() + " - Winner:" + message);
                                   // mGame.ownEarnPoint();
                                    break;
                                case 0:
                                    //mConversationArrayAdapter.add(mGame.getRound() + " - " + "Tie! No one wins the round!");
                                    break;
                                case 2://Opponent won
                                    //mConversationArrayAdapter.add(mGame.getRound() + " - Winner:" + message);
                                    //mGame.opponentEarnPoint();
                                    break;
                            }
                            //mGame.clearAnswers();
                            //mGame.goToNextRound();
                            break;


                        case "startRound":
                            //Start a round
                            String roundContentPacket[] = messageContentWrite.split("-");
                            String currentRound = roundContentPacket[0];
                            String opponentScore = roundContentPacket[1];
                            Log.d(TAG, "Round number: " + currentRound);
                            Log.d(TAG, "Opponent score: " + opponentScore);
                           // mConversationArrayAdapter.add("You have " + mGame.getOwnScore() + " points.");
                            //mConversationArrayAdapter.add("Opponent has " + mGame.getOpponentScore() + " points.");
                            mConversationArrayAdapter.add("Round " + currentRound + " start!");
                           // mGame.clearAnswers();
                            //enableButtons();
                            break;


                        default:
                            break;
                    }
                    break;
                case Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    //String readMessage = new String(readBuf, 0, msg.arg1);
                    String readMessage = new String(readBuf);

                    String messagePacket[] = readMessage.split(":");
                    String messageHeader = messagePacket[0];
                    String messageContent = messagePacket[1];

                    switch(messageHeader)
                    {
                        case "startGame":
                            //mGame = new Game();
                            mConversationArrayAdapter.add(messageHeader + ": Game has started.");
                            startRound();
                            break;

                        case "startRound":
                            //Start a round
                            String roundContentPacket[] = messageContent.split("-");
                            String currentRound = roundContentPacket[0];
                            String opponentScore = roundContentPacket[1];
                            //Log.d(TAG, "Round number: " + currentRound);
                            //Log.d(TAG, "Opponent score: " + opponentScore);
//                            mConversationArrayAdapter.add("You have " + mGame.getOwnScore() + " points.");
//                            mConversationArrayAdapter.add("Opponent has " + mGame.getOpponentScore() + " points.");
//                            mConversationArrayAdapter.add("Round " + currentRound + " start!");
//                            mGame.clearAnswers();
                            //enableButtons();
                            break;
                        case "opponentChoice":
                            String opponentChoice = messageContent;
                            //mGame.setOpponentAnswer(opponentChoice);


                            //Display opponent's choice

//                            if(!mGame.isOwnAnswerSet()) //If you haven't set your answer yet
//                            {
//                                mConversationArrayAdapter.add(mGame.getRound() + " - " + mConnectedDeviceName + ": " + "Answer set.");
//                                mConversationArrayAdapter.add(mGame.getRound() + " - Waiting for your answer.");
//
//                            }
//                            else
//                            {
//                                mConversationArrayAdapter.add(mGame.getRound() + " - " + mConnectedDeviceName + ": " + mGame.getOpponentAnswer());
//                                getWinner();
//                            }
                            break;
                        case "roundWinner":
                            String winnerContentPacket[] = messageContent.split("%");
                            int determinant = Integer.parseInt(winnerContentPacket[0]);
                            String message = winnerContentPacket[1];

//                            switch(determinant)
//                            {
//                                case 1://To the opponent, you won. You're the opponent of the opponent.
//                                    mConversationArrayAdapter.add(mGame.getRound() + " - Winner:" + message);
//                                    mGame.opponentEarnPoint(); //You earn the point from the opponent's POV.
//                                    break;
//                                case 0:
//                                    mConversationArrayAdapter.add(mGame.getRound() + " - " + "Tie! No one wins the round!");
//                                    break;
//                                case 2://To the opponent, you lost.
//                                    mConversationArrayAdapter.add(mGame.getRound() + " - Winner:" + message);
//                                    mGame.ownEarnPoint();//Opponent earns point from opponent's POV
//                                    break;
//                            }
                            nextRound();
                            break;

                        case "ownDevice":
                            mOwnDeviceName = messageContent.trim();
                            break;
                        default:
                            break;

                    }

                    //mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE_SECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, true);
                }
                break;
            case REQUEST_CONNECT_DEVICE_INSECURE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data, false);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    setupChat();
                } else {
                    // User did not enable Bluetooth or an error occurred
                    Log.d(TAG, "BT not enabled");
                    Toast.makeText(getActivity(), "Bluetooth was not enabled. Leaving.",
                            Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
        }
    }

    /**
     * Establish connection with other device
     *
     * @param data   An {@link Intent} with {@link //DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
     * @param secure Socket Security type - Secure (true) , Insecure (false)
     */
    private void connectDevice(Intent data, boolean secure) {
        // Get the device MAC address
        String address = data.getExtras()
                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        mChatService.connect(device, secure);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.bluetooth_chat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.secure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                return true;
            }
            case R.id.insecure_connect_scan: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
                return true;
            }
            case R.id.discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }

    public void nextRound()
    {
        //mGame.clearAnswers();
        //mGame.goToNextRound();

        startRound();
    }

    public void startRound()
    {
//        int round = mGame.getRound();
//        int ownScore = mGame.getOwnScore();
//
//        String roundMessage = "startRound:"+round+"-"+ownScore;
//        Log.d(TAG, "Round Message: " + roundMessage);
//        sendMessage(roundMessage);

    }

    /*
    public void enableButtons()
    {
        mPaperButton.setClickable(true);
        mRockButton.setClickable(true);
        mScissorsButton.setClickable(true);
    }

    public void disableButtons()
    {
        mPaperButton.setClickable(false);
        mRockButton.setClickable(false);
        mScissorsButton.setClickable(false);
    }
    */

    public void answer(String message)
    {
        //disableButtons();
        String messagePacket[] = message.split(":");
        String ownAnswer = messagePacket[1];

//        mGame.setOwnAnswer(ownAnswer);
//
//        sendMessage(message);
//
//        if(!mGame.isOpponentAnswerSet()) //If opponent has not set his answer yet
//        {
//            mConversationArrayAdapter.add(mGame.getRound() + " - Waiting for opponent.");
//        }
//        else
//        {
//            mConversationArrayAdapter.add(mGame.getRound() + " - " + mConnectedDeviceName + ": " + mGame.getOpponentAnswer());
//            //getWinner();
//        }
    }

    public void getWinner()
    {
//        if(mGame.isOwnAnswerSet() && mGame.isOpponentAnswerSet())
//        {
//            Log.d(TAG, "Own answer: " + mGame.getOwnAnswer());
//            Log.d(TAG, "Opponent answer: " + mGame.getOpponentAnswer());
//            int winner = mGame.ownWinRound(mGame.getOwnAnswer().trim(), mGame.getOpponentAnswer().trim());
//            Log.d(TAG, "Determinant: " + winner);
//            Log.d(TAG, "OWN DEVICE: " + mOwnDeviceName);
//            switch(winner)
//            {
//                case 1://You win.
//                    String iWin = "roundWinner:1%" + mBluetoothAdapter.getName();
//                    sendMessage(iWin);
//                    break;
//                case 0://Tie
//                    String noWin = "roundWinner:0%Tie! No winner this round.";
//                    sendMessage(noWin);
//                    break;
//                case -1://You lose. Opponent wins.
//                    String opponentWin = "roundWinner:2%" + mConnectedDeviceName;
//                    sendMessage(opponentWin);
//                    break;
//                default:
//                    Log.e(TAG, "ERROR WITH GETTING THE WINNER");
//                    break;
//            }
//        }
//        else
//        {
//            Log.e(TAG, "ERROR WITH GETTING THE WINNER. Own answer or opponent answer not set yet");
//        }
    }

}
