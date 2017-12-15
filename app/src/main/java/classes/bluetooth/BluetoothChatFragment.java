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
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import classes.Item;
import classes.Multiplayer;

import com.teamenigma.factthisshoot.Game;
import com.teamenigma.factthisshoot.GameOver;
import com.teamenigma.factthisshoot.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import classes.logger.Log;

/**
 * This fragment controls Bluetooth to communicate with other devices.
 */
public class BluetoothChatFragment extends Fragment {

    private static final String TAG = "BluetoothChatFragment";

    private ImageView imageQuestion, imageHeart1, imageHeart2, imageHeart3, imageCheck, imageCross;
    private Button buttonA, buttonB, buttonC, buttonD;
    private ImageButton imageButtonMute;
    private TextView textViewOwnScore, textAddedOwnScore, textViewOpponentScore, textAddedOpponentScore;
    private ProgressBar progressBarHorizontal;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    // Layout Views
    private Button mConnectSecure;
    private Button mMakeDiscoverable;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

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
    private Multiplayer mGame = new Multiplayer();
    private Item item;
    private CountDownTimer countDownTimer = mGame.getCountDownTimer();


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
       mConnectSecure = (Button) view.findViewById(R.id.btn_connSecure);
       mMakeDiscoverable = (Button) view.findViewById(R.id.btn_makeDiscoverable);


        // Declare the views.
        imageQuestion = (ImageView) view.findViewById(R.id.imageQuestion) ;
        imageCheck = (ImageView)view.findViewById(R.id.imageCheck2);
        imageCross = (ImageView)view.findViewById(R.id.imageCross2);
        textViewOwnScore = (TextView) view.findViewById(R.id.txtScorePlayer);
        textAddedOwnScore = (TextView) view.findViewById(R.id.txtScoreChangePlayer);
        textViewOpponentScore = (TextView) view.findViewById(R.id.txtScoreOpoonent);
        textAddedOpponentScore = (TextView) view.findViewById(R.id.txtScoreChangeOpponent);
        progressBarHorizontal = (ProgressBar) view.findViewById(R.id.progressBarTimer);
        progressBarHorizontal.setMax(5);

        // Hide the Check and Cross graphics.
        imageCheck.setVisibility(View.INVISIBLE);
        imageCross.setVisibility(View.INVISIBLE);

        buttonA = (Button) view.findViewById(R.id.btnA);
        buttonB = (Button) view.findViewById(R.id.btnB);
        buttonC = (Button) view.findViewById(R.id.btnC);
        buttonD = (Button) view.findViewById(R.id.btnD);

        hideGameViews();

    }

    private void hideGameViews()
    {
        imageQuestion.setVisibility(View.INVISIBLE);
        textViewOwnScore.setVisibility(View.INVISIBLE);
        textViewOpponentScore.setVisibility(View.INVISIBLE);
        textAddedOwnScore.setVisibility(View.INVISIBLE);
        textAddedOpponentScore.setVisibility(View.INVISIBLE);
        progressBarHorizontal.setVisibility(View.INVISIBLE);

        buttonA.setVisibility(View.INVISIBLE);
        buttonB.setVisibility(View.INVISIBLE);
        buttonC.setVisibility(View.INVISIBLE);
        buttonD.setVisibility(View.INVISIBLE);


        // Hide the Check and Cross graphics.
        imageCheck.setVisibility(View.INVISIBLE);
        imageCross.setVisibility(View.INVISIBLE);
    }

    private void showGameViews()
    {
        mGame.addOpponentScore(1000);
        mGame.addOwnScore(1000);

        imageQuestion.setVisibility(View.VISIBLE);
        textViewOwnScore.setVisibility(View.VISIBLE);
        textViewOpponentScore.setVisibility(View.VISIBLE);
        progressBarHorizontal.setVisibility(View.VISIBLE);

        buttonA.setVisibility(View.VISIBLE);
        buttonB.setVisibility(View.VISIBLE);
        buttonC.setVisibility(View.VISIBLE);
        buttonD.setVisibility(View.VISIBLE);

        textViewOwnScore.setText(mGame.getOwnScore()+"");
        textViewOpponentScore.setText(mGame.getOpponentScore()+"");
        setupButtons();
        setQuestion();
    }

    private void setupButtons()
    {
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("addOwnScore:");

                if(item.answer(buttonA.getText().toString())) {
                    buttonA.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonA.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    buttonA.setEnabled(false);
                    incorrect();
                }

            }
        });

        buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonB.getText().toString())) {
                    buttonB.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonB.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    buttonB.setEnabled(false);
                    incorrect();
                }

            }
        });

        buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonC.getText().toString())) {
                    buttonC.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonC.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    buttonC.setEnabled(false);
                    incorrect();
                }
            }
        });

        buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(item.answer(buttonD.getText().toString())) {
                    buttonD.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonD.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    buttonD.setEnabled(false);
                    incorrect();
                }
            }
        });

        countDownTimer = new CountDownTimer(5000, 10) {
                    public void onTick(long millisUntilFinished) {
                        progressBarHorizontal.setProgress( (int) millisUntilFinished / 1000);
                        mGame.setTimer((int) millisUntilFinished / 1000);
                    }
                    public void onFinish()
                    {
                        //incorrect();
                    }
        };

    }

    /**
     * Set up the UI and background operations for chat.
     */
    private void setupChat() {
        Log.d(TAG, "setupChat()");

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

            //mOutEditText.setText(mOutStringBuffer);
        }
    }


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
                    Toast.makeText(getContext(), writeMessage, Toast.LENGTH_LONG).show();
                    String messagePacketWrite[] = writeMessage.split(":");
                    String messageHeaderWrite = messagePacketWrite[0];
                    //String messageContentWrite = messagePacketWrite[1];

                    switch(messageHeaderWrite)
                    {

                        case "startGame":
                            //mGame = new Game();
                            //showGameViews();
                            break;

                        case "addOwnScore":
                            addOwnScore(100);
                            addOpponentScore(-100);
                            break;

                        case "addOpponentScore":
                            addOwnScore(-100);
                            addOpponentScore(100);
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
                    Toast.makeText(getContext(), readMessage, Toast.LENGTH_LONG).show();

                    String messagePacket[] = readMessage.split(":");
                    String messageHeader = messagePacket[0];
                    //String messageContent = messagePacket[1];

                    switch(messageHeader)
                    {
                        case "startGame":
                            showGameViews();
                            break;

                        case "addOwnScore":
                            addOwnScore(-100);
                            addOpponentScore(100);
                            break;

                        case "addOpponentScore":
                            addOwnScore(100);
                            addOpponentScore(-100);
                            break;

                        default:
                            break;

                    }

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

    /**
     * This function adds points to the score.
     * It also displays how many points got added/subtracted for a second.
     * @param num
     */
    public void addOwnScore(int num) {
        mGame.addOwnScore(num);
        textAddedOwnScore.setVisibility(View.VISIBLE);

        if(num > 0)
            textAddedOwnScore.setText("+" + num);
        else if(num == 0)
            textAddedOwnScore.setText("0");
        else if(num < 0)
            textAddedOwnScore.setText("-"+num);

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                textAddedOwnScore.setVisibility(View.INVISIBLE);
            }
        }.start();

        textViewOwnScore.setText(mGame.getOwnScore()+"");
    }

    /**
     * This function adds points to the score.
     * It also displays how many points got added/subtracted for a second.
     * @param num
     */
    public void addOpponentScore(int num) {
        mGame.addOpponentScore(num);
        textAddedOpponentScore.setVisibility(View.VISIBLE);

        if(num > 0)
            textAddedOpponentScore.setText("+" + num);
        else if(num == 0)
            textAddedOpponentScore.setText("0");
        else if(num < 0)
            textAddedOpponentScore.setText("-"+num);

        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {

            }
            public void onFinish() {
                textAddedOpponentScore.setVisibility(View.INVISIBLE);
            }
        }.start();

        textViewOpponentScore.setText(mGame.getOpponentScore()+"");
    }

    /**
     * This function sets the variables for the new question.
     */
    private void setQuestion() {

        // If the Category still has Items, get one Item else end Game.
        if(mGame.getCategory().canGet()) {
            item = mGame.getItem();

            Bitmap questionBitmap = BitmapFactory.decodeResource(getResources(), item.getQuestion());

            // clear existing list, add options, and shuffle
            ArrayList<String> optionList = new ArrayList<>();
            optionList.add(item.getOption1());
            optionList.add(item.getOption2());
            optionList.add(item.getOption3());
            optionList.add(item.getAnswer());
            Collections.shuffle(optionList);

            buttonA.setBackgroundResource(android.R.drawable.btn_default);
            buttonB.setBackgroundResource(android.R.drawable.btn_default);
            buttonC.setBackgroundResource(android.R.drawable.btn_default);
            buttonD.setBackgroundResource(android.R.drawable.btn_default);

            // Clear the colors that were applied to the buttons.
            buttonA.getBackground().clearColorFilter();
            buttonB.getBackground().clearColorFilter();
            buttonC.getBackground().clearColorFilter();
            buttonD.getBackground().clearColorFilter();

            imageQuestion.setImageBitmap(questionBitmap);
            buttonA.setText(optionList.get(0));
            buttonB.setText(optionList.get(1));
            buttonC.setText(optionList.get(2));
            buttonD.setText(optionList.get(3));

            // Re-enables all buttons that were disabled when they were incorrectly clicked on.
            buttonA.setEnabled(true);
            buttonB.setEnabled(true);
            buttonC.setEnabled(true);
            buttonD.setEnabled(true);

            progressBarHorizontal.setProgress(mGame.getTimer());

            //countDownTimer.start();
        }
        // If there are no more items left.
        else {
            countDownTimer.cancel();
            endGame();
        }

    }

    /**
     * This function gets called when the game ends.
     * It brings the user to the GameOver Activity.
     */
    private void endGame() {
        //SharedPreferences prefs = this.getSharedPreferences("myPrefsKey", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = prefs.edit();

        /*
        if(mGame.getCategory().getName().equalsIgnoreCase("dogs")) {
            if(prefs.getInt("hs_dogs", 0) < score) {
                editor.putInt("hs_dogs", score);
            }
        }
        else if(mGame.getCategory().getName().equalsIgnoreCase("planets")) {
            if(prefs.getInt("hs_planets", 0) < score)
                editor.putInt("hs_planets", score);
        }
        else if(mGame.getCategory().getName().equalsIgnoreCase("flowers")) {
            if(prefs.getInt("hs_flowers", 0) < score)
                editor.putInt("hs_flowers", score);
        }
        else if(mGame.getCategory().getName().equalsIgnoreCase("sports")) {
            if(prefs.getInt("hs_sports", 0) < score)
                editor.putInt("hs_sports", score);
        }
        else if(mGame.getCategory().getName().equalsIgnoreCase("flags")) {
            if(prefs.getInt("hs_flags", 0) < score)
                editor.putInt("hs_flags", score);
        }
        editor.commit();
        */

        countDownTimer.cancel();
        Intent intent = new Intent(this.getContext(), GameOver.class);
        intent.putExtra("score", mGame.getOwnScore());
        intent.putExtra("category", mGame.getCategory().getName());
        this.getActivity().finish();
        startActivity(intent);
    }

    /**
     * This function gets called every time the user clicks on a correct answer.
     */
    private void correct() {
        countDownTimer.cancel();

        sendMessage("addOwnScore:");
        mGame.setTimer(5);

        /*
        if (soundLoaded)
            soundPool.play(soundCorrect, volume, volume, 1, 0, 1f);
        */

        // Display the check mark for 250 milliseconds.
        imageCheck.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCheck.setVisibility(View.INVISIBLE);
            }
        }, 250);

        setQuestion();
    }

    /**
     * This function gets called every time the user clicks on an incorrect answer.
     */
    private void incorrect() {
        countDownTimer.cancel();

        //sendMessage("addOpponentScore:");
        //health--;
        //updateHealth();
        mGame.setTimer(5);

        /*
        if (soundLoaded)
            soundPool.play(soundIncorrect, volume, volume, 1, 0, 1f);
        */

        // Display the cross mark for 250 milliseconds.
        imageCross.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCross.setVisibility(View.INVISIBLE);
            }
        }, 250);

        if(checkGame(mGame.getOwnScore()))
            setQuestion();
        else
            endGame();

    }

    private boolean checkGame(int score) {
        if(score > 0)
            return true;
        return false;
    }






}
