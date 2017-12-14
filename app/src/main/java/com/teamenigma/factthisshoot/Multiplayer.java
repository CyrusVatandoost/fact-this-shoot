package com.teamenigma.factthisshoot;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamenigma.factthisshoot.Game;

import java.util.ArrayList;
import java.util.Collections;

import classes.Category;
import classes.bluetooth.BluetoothConnectionService;
import classes.bluetooth.Constants;
import classes.logger.Log;

public class Multiplayer extends  classes.Game {

    private ImageView imageQuestion, imageHeart1, imageHeart2, imageHeart3, imageCheck, imageCross;
    private Button buttonA, buttonB, buttonC, buttonD;
    private ImageButton imageButtonMute;
    private TextView textViewScore, textAddedScore;
    private ProgressBar progressBarHorizontal;

    private int health = 3;

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothConnectionService mChatService = null;
    private String mConnectedDeviceName = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Declare the views.
        imageQuestion = (ImageView) findViewById(R.id.imageQuestion) ;
        imageCheck = (ImageView)findViewById(R.id.imageCheck);
        imageCross = (ImageView)findViewById(R.id.imageCross);
        textViewScore = (TextView)findViewById(R.id.textViewScore);
        textAddedScore = (TextView)findViewById(R.id.textAddedScore);
        progressBarHorizontal = (ProgressBar)findViewById(R.id.progressBarTimer);
        progressBarHorizontal.setMax(5);

        // Hide the Check and Cross graphics.
        imageCheck.setVisibility(View.INVISIBLE);
        imageCross.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        super.category = (Category)intent.getSerializableExtra("category");

        //getSupportActionBar().setTitle(category.getName());  // provide compatibility to all the versions

        imageQuestion.setImageBitmap(bitmapQuestion);

        buttonA = (Button)findViewById(R.id.btnA);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage("Hi there!");
                /*
                if(item.answer(buttonA.getText().toString())) {
                    buttonA.getBackground().setColorFilter(getResources().getColor(R.color.green), PorterDuff.Mode.SRC_ATOP);
                    correct();
                }
                else {
                    buttonA.getBackground().setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    buttonA.setEnabled(false);
                    incorrect();
                }
                */
            }
        });

        buttonB = (Button)findViewById(R.id.btnB);
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

        buttonC = (Button)findViewById(R.id.btnC);
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

        buttonD = (Button)findViewById(R.id.btnD);
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
                timer = (int) millisUntilFinished / 1000;
            }
            public void onFinish() {
                incorrect();
            }
        };

        //textAddedScore.setVisibility(View.INVISIBLE);
        //setQuestion();

        // Initialize the BluetoothChatService to perform bluetooth connections
        mChatService = new BluetoothConnectionService(super.getApplicationContext(), mHandler);




    }

    private void setGame()
    {

    }

    private void endGame() {

    }

    private void startRound() {

    }

    private void addScoreToPlayer(int score) {

    }

    private void addScoreToOpponent(int score) {

    }

    /**
     * This function gets called every time the user clicks on a correct answer.
     */
    private void correct() {
        countDownTimer.cancel();

        addScoreToPlayer(100 + (timer * 10));
        timer = 5;

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

        addScoreToPlayer(-100);
        health--;
        //updateHealth();
        timer = 5;

//        if (soundLoaded)
//            soundPool.play(soundIncorrect, volume, volume, 1, 0, 1f);

        // Display the cross mark for 250 milliseconds.
        imageCross.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                imageCross.setVisibility(View.INVISIBLE);
            }
        }, 250);

        /*
        if(checkGame(health))
            setQuestion();
        else
            endGame();
            */

    }

    /**
     * This function sets the variables for the new question.
     */
    private void setQuestion() {

        // If the Category still has Items, get one Item else end Game.
        if(category.canGet()) {
            item = category.getItem();

            bitmapQuestion = BitmapFactory.decodeResource(getResources(), item.getQuestion());

            // clear existing list, add options, and shuffle
            optionList = new ArrayList<>();
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

            imageQuestion.setImageBitmap(bitmapQuestion);
            buttonA.setText(optionList.get(0));
            buttonB.setText(optionList.get(1));
            buttonC.setText(optionList.get(2));
            buttonD.setText(optionList.get(3));

            // Re-enables all buttons that were disabled when they were incorrectly clicked on.
            buttonA.setEnabled(true);
            buttonB.setEnabled(true);
            buttonC.setEnabled(true);
            buttonD.setEnabled(true);

            //textViewScore.setText(score + "");
            progressBarHorizontal.setProgress(timer);

            countDownTimer.start();
        }
        // If there are no more items left.
        else {
            countDownTimer.cancel();
            endGame();
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
            Toast.makeText(super.getApplicationContext(), "You are not connected to a device", Toast.LENGTH_SHORT).show();
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
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothConnectionService.STATE_CONNECTED:
                            Toast.makeText(Multiplayer.this, getString(R.string.title_connected_to, mConnectedDeviceName), Toast.LENGTH_SHORT).show();
                            //String connectDeviceNamePacket = "ownDevice:"+mConnectedDeviceName;
                            //mChatService.write(connectDeviceNamePacket.getBytes(), Constants.MESSAGE_WRITE);
                            break;
                        case BluetoothConnectionService.STATE_CONNECTING:
                            Toast.makeText(Multiplayer.this, "connecting...", Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothConnectionService.STATE_LISTEN:
                        case BluetoothConnectionService.STATE_NONE:
                            Toast.makeText(Multiplayer.this, "not connected...", Toast.LENGTH_SHORT).show();
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

                        default:
                            break;
                    }
                    Toast.makeText(Multiplayer.this, writeMessage, Toast.LENGTH_LONG).show();
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
                            break;

                        case "startRound":
                            break;

                        case "opponentChoice":
                            String opponentChoice = messageContent;
                            //mGame.setOpponentAnswer(opponentChoice);
                            break;

                        case "roundWinner":
                            break;

                        case "ownDevice":
                            break;
                        default:
                            break;

                    }
                    Toast.makeText(Multiplayer.this, readMessage, Toast.LENGTH_LONG).show();
                    break;
                case Constants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
                    if (null != Multiplayer.class) {
                        Toast.makeText(getApplicationContext(), "Connected to "
                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Constants.MESSAGE_TOAST:
                    if (null != Multiplayer.class) {
                        Toast.makeText(getApplicationContext(), msg.getData().getString(Constants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

}
