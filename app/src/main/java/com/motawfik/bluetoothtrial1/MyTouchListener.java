package com.motawfik.bluetoothtrial1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

import static com.motawfik.bluetoothtrial1.BluetoothActivity.mSocket;

public class MyTouchListener extends Activity implements View.OnTouchListener {

    private final Context context;
    private static final String TAG = "MyTouchListener";

    public MyTouchListener(Context context) {
        this.context  = context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (mSocket == null) {
            Snackbar snackbar = Snackbar.make(v, "No connected devices", Snackbar.LENGTH_LONG);
            snackbar.setAction("Connect", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, BluetoothActivity.class);
                    context.startActivity(intent);

                    Log.d(TAG, "onClick: CONNECT PLEASE");
                }
            });
            snackbar.show();
        } else {
            switch (v.getId()) {
                case R.id.rotateButton:
                    Log.d(TAG, "onTouch: Rotate");
                    sendOutput("X");
                    break;
                case R.id.forwardButton:
                    Log.d(TAG, "onTouch: Forward");
                    sendOutput("F");
                    break;
                case R.id.backwardButton:
                    Log.d(TAG, "onTouch: Backward");
                    sendOutput("B");
                    break;
                case R.id.leftButton:
                    Log.d(TAG, "onTouch: Left");
                    sendOutput("L");
                    break;
                case R.id.rightButton:
                    Log.d(TAG, "onTouch: Right");
                    sendOutput("R");
                    break;
                case R.id.speedUpButton:
                    Log.d(TAG, "onTouch: Speed Up");
                    sendOutput("I");
                    break;
                case R.id.speedDownButton:
                    Log.d(TAG, "onTouch: Speed Down");
                    sendOutput("D");
                    break;
                case R.id.lightButton:
                    Log.d(TAG, "onTouch: Flash");
                    sendOutput("E");
                    break;
                case R.id.soundButton:
                    Log.d(TAG, "onTouch: Sound");
                    sendOutput("H");
                    break;
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                Log.d(TAG, "onTouch: STOP");
                sendOutput("S");
            }

        }
        return true;
    }

    public void sendOutput(String characters) {
        if (mSocket != null) {
            try {
                mSocket.getOutputStream().write(characters.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "No Connection", Toast.LENGTH_SHORT).show();
        }
    }
}
