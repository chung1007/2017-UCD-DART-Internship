package com.ucdavis.dart.cardiac_arrhythmia_app;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by sam on 7/8/17.
 */
public class AcceptThread extends Thread {
    BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mmSocket;
    private BluetoothDevice mmDevice;
    private Activity context;
    private static final UUID MY_UUID = UUID.fromString("f741fa52-bd68-4f10-8529-934017a049b8");

    public AcceptThread(BluetoothDevice device, BluetoothAdapter mBluetoothAdapter, Activity context) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.context = context;
        if(device != null) {
            mmDevice = device;
            try {
                mmSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {

            }
        }
    }
    public void run() {
        mBluetoothAdapter.cancelDiscovery();
        try {
            mmSocket.connect();
        } catch (IOException connectException) {
            try {
                mmSocket.close();
            } catch (IOException closeException) {

            }
            return;
        } catch (NullPointerException NPE) {
            Log.e("No Socket", "Available");
        }
        ConnectedThread mConnectedThread = new ConnectedThread(mmSocket, context);
        mConnectedThread.start();

    }

}
