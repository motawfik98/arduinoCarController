package com.motawfik.bluetoothtrial1;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.motawfik.bluetoothtrial1.BluetoothActivity.mSocket;

public class DriveManually extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final String TAG = "DriveManually";
    public static BluetoothAdapter mBluetoothAdapter;

    static TextView bluetoothStatus;

    MyTouchListener touchListener;

    ImageButton forwardButton;
    ImageButton backwardButton;
    ImageButton leftButton;
    ImageButton rightButton;
    ImageButton speedUpButton;
    ImageButton speedDownButton;
    ImageButton flashButton;
    ImageButton soundButton;
    ImageButton rotateButton;
    ImageButton[] availableButtons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_manually);

        touchListener = new MyTouchListener(DriveManually.this);
        bluetoothStatus = (TextView) findViewById(R.id.connectedStatus);
        forwardButton = (ImageButton) findViewById(R.id.forwardButton);
        backwardButton = (ImageButton) findViewById(R.id.backwardButton);
        leftButton = (ImageButton) findViewById(R.id.leftButton);
        rightButton = (ImageButton) findViewById(R.id.rightButton);
        speedUpButton = (ImageButton) findViewById(R.id.speedUpButton);
        speedDownButton = (ImageButton) findViewById(R.id.speedDownButton);
        flashButton = (ImageButton) findViewById(R.id.lightButton);
        soundButton = (ImageButton) findViewById(R.id.soundButton);
        rotateButton = (ImageButton) findViewById(R.id.rotateButton);

        //get the default bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // check whether the bluetooth is available on the device
            Toast.makeText(this, "Device doesn\'t support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // checks that the bluetooth isn't enabled
                turnOnBluetooth();
            }
        }
        if (mSocket == null) {
            bluetoothStatus.setText("No connection");
        } else {
            if (mSocket.isConnected()) {
                bluetoothStatus.setText("Connected");
            } else {
                bluetoothStatus.setText("No connection");
            }
        }

        setOnTouchListeners();
    }



    private void turnOnBluetooth() {
        // opens a new intent (window) to ask the user to enable bluetooth
        Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(turnOn, REQUEST_ENABLE_BT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "You must enable bluetooth to continue", Toast.LENGTH_SHORT).show();
                turnOnBluetooth();
            } else {
                Toast.makeText(this, "Bluetooth enabled", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void setOnTouchListeners() {
        availableButtons = new ImageButton[]{forwardButton, backwardButton, leftButton, rightButton,
                speedUpButton, speedDownButton, rotateButton, flashButton, soundButton};

        for (ImageButton button : availableButtons) {
            button.setOnTouchListener(touchListener);
        }
    }


    public void goToBluetoothPage(View v) {
        // go to the bluetooth page
        Intent intent = new Intent(DriveManually.this, BluetoothActivity.class);
        startActivity(intent);
    }

}
