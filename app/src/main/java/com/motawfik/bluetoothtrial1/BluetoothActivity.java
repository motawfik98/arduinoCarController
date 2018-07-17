package com.motawfik.bluetoothtrial1;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

import static com.motawfik.bluetoothtrial1.DriveManually.mBluetoothAdapter;

public class BluetoothActivity extends AppCompatActivity {
    private static final String TAG = "BluetoothActivity";
    ArrayAdapter<String> pairedDevicesArrayAdapter;
    ArrayAdapter<String> availableDevicesArrayAdapter;
    ArrayList<BluetoothDevice> mAvailableBluetoothDevices = new ArrayList<>();
    ArrayList<BluetoothDevice> mPairedBluetoothDevices = new ArrayList<>();
    ListView pairedDevicesList;
    ListView availableDevicesList;
    TextView availableDevicesText;
    Set<BluetoothDevice> pairedDevices;
    public static BluetoothSocket mSocket = null;
    public static UUID My_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        availableDevicesText = (TextView) findViewById(R.id.availableDevices);
        availableDevicesList = (ListView) findViewById(R.id.availableDevicesList);
        availableDevicesList.setOnItemClickListener(onAvailableItemClickListener);
        pairedDevicesList = (ListView) findViewById(R.id.pairedDevicesList);
        pairedDevicesList.setOnItemClickListener(onPairedItemClickListener);
        pairedDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        availableDevicesArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        // get access to the bluetoothAdapter
        // gets the list of paired devices
        getPairedDevices();
        // gets the list of available devices
        getAvailableDevices();

    }

//    };

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    private void getPairedDevices() {
        pairedDevicesArrayAdapter.clear();
        // save the paired devices in a set to prevent duplicating
        pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {

            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                // adds the name and MAC address of each device to the ArrayAdapter
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                if (!mPairedBluetoothDevices.contains(device))
                    mPairedBluetoothDevices.add(device);
            }
            // put the ArrayAdapter to the ListView
            pairedDevicesList.setAdapter(pairedDevicesArrayAdapter);
        }
    }

    private void getAvailableDevices() {
        availableDevicesArrayAdapter.clear();
        // starts searching for available devices
        mBluetoothAdapter.startDiscovery();
        // put the ArrayAdapter to the ListView
        availableDevicesList.setAdapter(availableDevicesArrayAdapter);
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
    }

    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                mAvailableBluetoothDevices.add(device);
                //adds the name and MAC address of each device to the ArrayAdapter
                if (!mPairedBluetoothDevices.contains(device) && !mAvailableBluetoothDevices.contains(device)) {
                    mAvailableBluetoothDevices.add(device);
                    availableDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        }
    };

    public void scan(View view) {
        // clears the ArrayAdapter to prevent duplicating
        mAvailableBluetoothDevices.clear();
        getPairedDevices();
        getAvailableDevices();
        // checks is the bluetooth is already searching for devices
        if (mBluetoothAdapter.isDiscovering()) {
            // cancel the discovery to prevent app crashing
            mBluetoothAdapter.cancelDiscovery();
            // starts the discovery again
            mBluetoothAdapter.startDiscovery();
        } else {
            mBluetoothAdapter.startDiscovery();
        }
        Toast.makeText(this, "Starting discovery", Toast.LENGTH_SHORT).show();
    }


    AdapterView.OnItemClickListener onAvailableItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice selectedDevice = mAvailableBluetoothDevices.get(position);
            Log.d(TAG, "onItemClick: Trying to pair with: " + selectedDevice.getName());
            Toast.makeText(BluetoothActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
            connect(selectedDevice);
        }

    };


    AdapterView.OnItemClickListener onPairedItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            BluetoothDevice selectedDevice = mPairedBluetoothDevices.get(position);
            Log.d(TAG, "onItemClick: Trying to pair with: " + selectedDevice.getName());
            Toast.makeText(BluetoothActivity.this, "Connecting...", Toast.LENGTH_SHORT).show();
            connect(selectedDevice);

        }

    };

    public void connect(BluetoothDevice device) {
        BTSocket mSocket = new BTSocket(BluetoothActivity.this);
        mSocket.createSocket(device);
        mSocket.connectDevice(device);
    }
}
