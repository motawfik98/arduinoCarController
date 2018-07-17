package com.motawfik.bluetoothtrial1;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

import static com.motawfik.bluetoothtrial1.DriveManually.mBluetoothAdapter;

public class BTSocket {

    private Context context;

    public BTSocket(Context context) {
        this.context = context;
    }

    private static final String TAG = "BluetoothActivity";
    public static BluetoothSocket mSocket = null;
    private static UUID My_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public void createSocket(BluetoothDevice device) {
        Log.d(TAG, "connect: connecting to: " + device.getName());
        try {
            mSocket = device.createRfcommSocketToServiceRecord(My_UUID);
            mBluetoothAdapter.cancelDiscovery();
        } catch (IOException e1) {
            Log.d(TAG, "connect: socket not created");
            e1.printStackTrace();
        }
    }

    public void connectDevice(BluetoothDevice device) {
        try {
            // try to connect to chosen device
            mSocket.connect();
            Toast.makeText(context, "Connected", Toast.LENGTH_SHORT).show();
            // if success go to the main activity
            Intent back = new Intent(context, DriveManually.class);
            context.startActivity(back);
        } catch (IOException e) {
            closeSocket();
        }
    }

    private void closeSocket() {
        try {
            mSocket.close();
            mSocket = null;
            Toast.makeText(context, "Can\'t connect", Toast.LENGTH_SHORT).show();
        } catch (IOException e1) {
            Toast.makeText(context, "Socket can\'t be closed", Toast.LENGTH_SHORT).show();
            e1.printStackTrace();
        }
    }
}
